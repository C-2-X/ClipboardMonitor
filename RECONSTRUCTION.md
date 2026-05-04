# ClipboardMonitor - 项目重构文档

> 本文档旨在提供完整、精确的技术规范，使 AI 编程工具能够在不依赖原始源代码的情况下独立重构整个项目。

---

## 1. 项目概述

### 1.1 项目目的

ClipboardMonitor 是一款 Android 平台的剪切板监测应用，核心目标是在后台持续监控系统剪切板变化，将捕获的内容自动上报到用户配置的远程服务器。该应用解决了跨设备剪切板同步的需求——用户在一台设备上复制的内容，可以通过此应用自动发送到服务器，供其他设备获取。

### 1.2 核心功能

| 功能 | 优先级 | 描述 |
|------|:------:|------|
| 剪切板实时监测 | P0 | 通过无障碍服务实时监听系统剪切板变化 |
| 自动上报 | P0 | 检测到新内容后自动发送到配置的服务器 |
| 后台保活 | P0 | 前台服务 + 开机自启确保持续运行 |
| 历史记录管理 | P1 | 本地持久化存储，支持查看/搜索/筛选/删除 |
| 悬浮窗 | P1 | 可拖拽悬浮窗，显示状态并支持手动触发上传 |
| 失败重试 | P1 | 发送失败的记录自动重试，最多5次 |
| 数据导出 | P2 | 支持导出为 JSON/CSV 格式 |

### 1.3 目标用户

- 需要跨设备同步剪切板内容的用户
- 需要监控和记录剪切板历史的企业/开发者
- 需要将移动端剪切板内容自动同步到 PC 的用户

### 1.4 业务需求

1. **实时性**：剪切板内容变化后应在 3 秒内完成上报
2. **可靠性**：网络异常时本地保存，恢复后自动重试
3. **持久性**：应用被系统杀死后能自动恢复运行
4. **轻量性**：后台运行时资源占用最小化
5. **隐私性**：所有数据仅存储在本地和用户配置的服务器

---

## 2. 系统架构

### 2.1 架构图

```
┌─────────────────────────────────────────────────────────────────────┐
│                        Android System                               │
│                                                                     │
│  ┌──────────────┐    ┌──────────────────┐    ┌──────────────────┐  │
│  │  Clipboard   │    │ Accessibility    │    │  Boot            │  │
│  │  Manager     │    │ Service Manager  │    │  Completed       │  │
│  └──────┬───────┘    └────────┬─────────┘    └────────┬─────────┘  │
│         │                     │                       │             │
└─────────┼─────────────────────┼───────────────────────┼─────────────┘
          │                     │                       │
          ▼                     ▼                       ▼
┌─────────────────────────────────────────────────────────────────────┐
│                        Application Layer                            │
│                                                                     │
│  ┌─────────────────────────────────────────────────────────────┐   │
│  │                    ClipboardMonitorService                   │   │
│  │  ┌─────────────────┐  ┌─────────────────┐  ┌────────────┐ │   │
│  │  │ ClipChanged     │  │ Polling Timer   │  │ Broadcast  │ │   │
│  │  │ Listener        │  │ (3s interval)   │  │ Receiver   │ │   │
│  │  └────────┬────────┘  └────────┬────────┘  └─────┬──────┘ │   │
│  │           │                    │                  │         │   │
│  │           └────────────┬───────┘                  │         │   │
│  │                        ▼                          │         │   │
│  │              ┌──────────────────┐                 │         │   │
│  │              │ processClipboard │◄────────────────┘         │   │
│  │              └────────┬─────────┘                           │   │
│  └───────────────────────┼─────────────────────────────────────┘   │
│                          │                                          │
│  ┌───────────────────────┼─────────────────────────────────────┐   │
│  │                KeepAliveService                              │   │
│  │  ┌──────────────┐  ┌──────────────────┐  ┌──────────────┐ │   │
│  │  │ Foreground   │  │ Retry Scheduler  │  │ Floating     │ │   │
│  │  │ Notification │  │ (1h interval)    │  │ Window Mgr   │ │   │
│  │  └──────────────┘  └──────────────────┘  └──────────────┘ │   │
│  └─────────────────────────────────────────────────────────────┘   │
│                                                                     │
│  ┌──────────────────┐  ┌──────────────────┐  ┌──────────────────┐  │
│  │  MainActivity    │  │ HistoryActivity  │  │ ClipboardProxy   │  │
│  │  (Configuration) │  │ (Records Mgmt)   │  │ Activity         │  │
│  └──────────────────┘  └──────────────────┘  └──────────────────┘  │
│                                                                     │
├─────────────────────────────────────────────────────────────────────┤
│                        Data Layer                                   │
│                                                                     │
│  ┌──────────────────┐  ┌──────────────────┐  ┌──────────────────┐  │
│  │ StorageManager   │  │ NetworkManager   │  │ FloatingWindow   │  │
│  │ (SP + JSON File) │  │ (OkHttp)         │  │ Manager          │  │
│  └──────────────────┘  └──────────────────┘  └──────────────────┘  │
│                                                                     │
├─────────────────────────────────────────────────────────────────────┤
│                        Model Layer                                  │
│                                                                     │
│  ┌──────────────────┐                                               │
│  │ ClipData         │                                               │
│  │ (Data Model)     │                                               │
│  └──────────────────┘                                               │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
                          │
                          ▼
              ┌──────────────────────┐
              │   Remote Server      │
              │   (User Configured)  │
              └──────────────────────┘
```

### 2.2 组件关系

```
MainActivity ──启动──▶ KeepAliveService ──初始化──▶ FloatingWindowManager
     │                       │
     │                       ├──定时──▶ retryFailedUploads()
     │                       │                    │
     │                       │                    ▼
     │                       │            NetworkManager.sendReport()
     │                       │                    │
     │                       │                    ▼
     │                       │            Remote Server
     │                       │
     │                       └──系统绑定──▶ ClipboardMonitorService
     │                                          │
     │                                          ├──监听──▶ ClipboardManager
     │                                          ├──轮询──▶ processClipboard()
     │                                          └──广播──▶ ClipboardProxyActivity
     │
     └──跳转──▶ HistoryActivity ──读取──▶ StorageManager
```

### 2.3 交互流程

#### 剪切板监测主流程

```
[系统剪切板变化]
       │
       ▼
ClipboardMonitorService.onPrimaryClipChanged()
       │
       ▼
processClipboard(isManualTrigger=false)
       │
       ├── 检查 monitoring_enabled 配置 ──(禁用)──▶ return
       │
       ├── 检查 clipboardManager.hasPrimaryClip() ──(无)──▶ return
       │
       ├── 读取 ClipData.Item
       │       ├── item.getText() → contentStr, typeStr="text"
       │       ├── item.getUri()  → contentStr, typeStr="file/uri"
       │       └── item.getIntent() → contentStr, typeStr="intent"
       │
       ├── 检查 contentStr.isEmpty() ──(空)──▶ return
       │
       ├── 检查 contentStr == lastClipContent ──(重复)──▶ return
       │
       ├── 更新 lastClipContent = contentStr
       │
       ├── new Thread → 创建 ClipData 对象
       │       │
       │       ├── storageManager.saveClip(data)
       │       │
       │       ├── 检查 URL 配置 ──(空)──▶ data.setStatus("FAILED")
       │       │
       │       └── networkManager.sendReport(url, method, data, callback)
       │               │
       │               ├── onSuccess → data.setStatus("SENT")
       │               │              storageManager.updateClip(data)
       │               │
       │               └── onFailure → data.setStatus("FAILED")
       │                              storageManager.updateClip(data)
       │
       ▼
   [完成]
```

#### 悬浮窗手动上传流程

```
[用户点击悬浮窗]
       │
       ▼
FloatingWindowManager.onFloatingWindowClick()
       │
       ▼
ClipboardMonitorService.launchClipboardProxy()
       │
       ▼
ClipboardProxyActivity.onCreate()
       │
       ├── onWindowFocusChanged(hasFocus=true)
       │       │
       │       └── handler.postDelayed(100ms) → readClipboardAndFinish()
       │
       ▼
readClipboardAndFinish()
       │
       ├── 获取 ClipboardManager
       │
       ├── 读取 primaryClip
       │       ├── (null/empty) → sendResult(error="无法访问剪切板")
       │       └── (有内容) → 解析 content + type
       │               ├── (空内容) → sendResult(error="内容为空")
       │               └── (有内容) → sendResult(content, type)
       │
       ├── sendResult() → sendBroadcast(ACTION_READ_RESULT)
       │
       └── handler.postDelayed(50ms) → finish()
               │
               ▼
ClipboardMonitorService.ClipboardResultReceiver.onReceive()
       │
       ├── (有error) → floatingWindowManager.updateStatus(error)
       │
       └── (有content) → processManualContent(content, type)
               │
               ├── (重复内容) → updateStatus("内容重复")
               │
               └── (新内容) → 更新 lastClipContent
                       │
                       ├── storageManager.saveClip(data)
                       ├── updateStatus("上传中...")
                       └── networkManager.sendReport(...)
                               ├── onSuccess → updateStatus("上传成功")
                               └── onFailure → updateStatus("上传失败")
```

---

## 3. 技术栈

### 3.1 开发环境

| 工具 | 版本 | 用途 |
|------|------|------|
| Android Studio | Hedgehog (2023.1.1)+ | IDE |
| JDK | 17 | Java 编译 |
| Android Gradle Plugin | 8.10.1 | 构建系统 |
| Gradle | 8.x | 构建工具 |

### 3.2 运行环境

| 项目 | 值 |
|------|-----|
| compileSdkVersion | 34 |
| minSdkVersion | 29 (Android 10) |
| targetSdkVersion | 34 (Android 14) |
| applicationId | com.example.myapplication01 |

### 3.3 编程语言

| 语言 | 版本 | 兼容级别 |
|------|------|---------|
| Java | 8 | sourceCompatibility: VERSION_1_8, targetCompatibility: VERSION_1_8 |

### 3.4 依赖库

| 库 | 版本范围 | 用途 |
|----|---------|------|
| androidx.appcompat:appcompat | 1.x | 兼容性支持 |
| com.google.android.material:material | 1.x | Material Design 组件 |
| androidx.constraintlayout:constraintlayout | 2.x | 约束布局 |
| androidx.recyclerview:recyclerview | 1.x | 列表视图 |
| androidx.cardview:cardview | 1.x | 卡片视图 |
| com.google.code.gson:gson | 2.x | JSON 序列化/反序列化 |
| com.squareup.okhttp3:okhttp | 4.x | HTTP 网络请求 |
| androidx.core:core-ktx | 1.x | AndroidX 核心扩展 |
| androidx.lifecycle:lifecycle-runtime | 2.x | 生命周期管理 |
| androidx.startup:startup-runtime | 1.x | 组件初始化 |

### 3.5 关键工具类

| 工具 | 用途 |
|------|------|
| SharedPreferences | 轻量级键值对存储 |
| Gson | JSON 文件读写 |
| OkHttpClient | 异步 HTTP 请求 |
| ScheduledExecutorService | 定时任务调度 |
| Handler + Looper | 线程间通信 |
| WindowManager | 悬浮窗管理 |
| ClipboardManager | 剪切板访问 |
| AccessibilityService | 后台事件监听 |

---

## 4. 数据模型

### 4.1 ClipData 实体

```java
package com.example.myapplication01.model;

public class ClipData {
    private String id;            // UUID.randomUUID().toString()
    private String device_id;     // Settings.Secure.getString(resolver, "android_id")
    private long timestamp;       // System.currentTimeMillis()
    private String clip_content;  // 剪切板文本内容
    private String clip_type;     // 内容类型: "text" | "file/uri" | "intent"
    private String status;        // 发送状态: "PENDING" | "SENT" | "FAILED"
    private int retry_count;      // 重试次数, 最大5
}
```

### 4.2 字段详细说明

| 字段 | Java类型 | JSON键名 | 默认值 | 约束 | 说明 |
|------|---------|----------|--------|------|------|
| id | String | id | UUID随机 | 唯一, 不可变 | 记录主键 |
| device_id | String | device_id | - | 非空 | Android 设备ID |
| timestamp | long | timestamp | - | > 0 | 毫秒级时间戳 |
| clip_content | String | clip_content | - | 非空 | 剪切板内容原文 |
| clip_type | String | clip_type | "text/plain" | 枚举值 | 内容类型 |
| status | String | status | "PENDING" | 枚举值 | 发送状态 |
| retry_count | int | retry_count | 0 | 0-5 | 已重试次数 |

### 4.3 枚举值定义

**clip_type:**
| 值 | 触发条件 |
|----|---------|
| "text" | ClipData.Item.getText() != null |
| "file/uri" | ClipData.Item.getUri() != null |
| "intent" | ClipData.Item.getIntent() != null |
| "text/plain" | 默认值（无法识别类型时） |

**status:**
| 值 | 含义 | 颜色 |
|----|------|------|
| "PENDING" | 待发送 | 橙色 (#FFA500) |
| "SENT" | 已发送 | 绿色 (#008000 等价) |
| "FAILED" | 发送失败 | 红色 (#FF0000 等价) |

### 4.4 数据存储方案

#### SharedPreferences 配置存储

文件名: `monitor_config`
模式: `MODE_PRIVATE` (0)

| Key | 类型 | 默认值 | 说明 |
|-----|------|--------|------|
| target_url | String | "" | 服务器地址 |
| request_method | String | "POST" | HTTP 请求方式 |
| monitoring_enabled | boolean | false | 监测开关 |
| float_window_enabled | boolean | false | 悬浮窗开关 |
| float_x | int | 0 | 悬浮窗X坐标 |
| float_y | int | 200 | 悬浮窗Y坐标 |

#### JSON 文件历史记录存储

文件名: `clipboard_history.json`
位置: `context.getFilesDir()` (内部存储)
格式: `ArrayList<ClipData>` 的 JSON 序列化
最大条数: 1000
排序: 新记录插入到列表头部 (index 0)

### 4.5 数据流图

```
[剪切板变化]
     │
     ▼
ClipData 对象创建 (status=PENDING)
     │
     ├──▶ StorageManager.saveClip() ──▶ clipboard_history.json
     │
     ├──▶ NetworkManager.sendReport() ──▶ Remote Server
     │         │
     │         ├── onSuccess ──▶ status=SENT ──▶ StorageManager.updateClip()
     │         └── onFailure ──▶ status=FAILED ──▶ StorageManager.updateClip()
     │
     ▼
[KeepAliveService 定时重试]
     │
     ├── 查询 status=PENDING 或 FAILED 且 retry_count<5 的记录
     ├── retry_count++
     └── NetworkManager.sendReport() ──▶ 同上处理
```

---

## 5. API 规范

### 5.1 请求格式

应用作为 **HTTP 客户端**，向用户配置的服务器发送数据。服务器端需要自行实现接收逻辑。

#### POST 请求

```
POST {target_url}
Content-Type: application/json

{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "device_id": "a1b2c3d4e5f6g7h8",
  "timestamp": 1699000000000,
  "clip_content": "Hello World",
  "clip_type": "text",
  "status": "PENDING",
  "retry_count": 0
}
```

#### GET 请求

```
GET {target_url}?data={URL_ENCODED_JSON}
```

其中 JSON 数据经过 `URLEncoder.encode(json, "UTF-8")` 编码后作为 `data` 参数附加到 URL。

如果 URL 已包含 `?`，则使用 `&` 连接；否则使用 `?` 连接。

### 5.2 请求头

| Header | 值 |
|--------|-----|
| Content-Type | application/json |

### 5.3 响应处理

| HTTP 状态码 | 处理方式 |
|------------|---------|
| 200-299 | 调用 `callback.onSuccess()`，状态更新为 SENT |
| 其他状态码 | 调用 `callback.onFailure("HTTP " + code)`，状态更新为 FAILED |
| 网络异常 | 调用 `callback.onFailure(e.getMessage())`，状态更新为 FAILED |

### 5.4 请求特性

- **异步执行**: OkHttp 的 `enqueue()` 方法，不阻塞主线程
- **无认证**: 当前版本不包含任何认证机制
- **无加密**: 支持 HTTP 明文传输 (`usesCleartextTraffic=true`)
- **无超时配置**: 使用 OkHttp 默认超时设置

### 5.5 服务器端实现参考

服务器端需要实现一个接收 JSON 数据的 HTTP 端点：

```
POST /api/clipboard
Content-Type: application/json

接收上述 JSON 格式数据，返回 2xx 状态码表示成功。
```

---

## 6. UI/UX 设计

### 6.1 界面清单

| 界面 | Activity | 主题 | 启动模式 |
|------|---------|------|---------|
| 主配置界面 | MainActivity | Theme.MyApplication01 | standard |
| 历史记录 | HistoryActivity | Theme.MyApplication01 | standard |
| 剪切板代理 | ClipboardProxyActivity | Theme.Translucent.NoTitleBar | singleInstance |

### 6.2 MainActivity 布局规范

**布局类型**: ConstraintLayout
**内边距**: 16dp

```
┌─────────────────────────────────────┐
│         剪切板监测配置               │  ← tv_title (24sp, bold, 居中)
│                                     │
│  [○] 开启监测                       │  ← switch_monitor (18sp)
│  [○] 显示悬浮窗                     │  ← switch_floating_window (18sp)
│                                     │
│  ┌─────────────────────────────┐   │
│  │ 服务器地址 (http/https)      │   │  ← til_url (TextInputLayout)
│  │ http://192.168.1.1:8080/api │   │  ← et_url (TextInputEditText, inputType=textUri)
│  └─────────────────────────────┘   │
│                                     │
│  请求方式:                          │  ← tv_method
│  (●) POST  (○) GET                 │  ← rg_method (RadioGroup, horizontal)
│                                     │
│  ┌─────────────────────────────┐   │
│  │         保存配置              │   │  ← btn_save
│  └─────────────────────────────┘   │
│  ┌─────────────────────────────┐   │
│  │        查看历史记录           │   │  ← btn_history
│  └─────────────────────────────┘   │
│                                     │
│        状态: 已停止                  │  ← tv_status (居中)
│                                     │
│  注意: 请在提示时授予无障碍和通知权限 │  ← tv_instructions (12sp, #666666)
└─────────────────────────────────────┘
```

**控件 ID 映射**:

| ID | 控件类型 | 用途 |
|----|---------|------|
| tv_title | TextView | 标题 |
| switch_monitor | SwitchMaterial | 监测开关 |
| switch_floating_window | SwitchMaterial | 悬浮窗开关 |
| til_url | TextInputLayout | URL 输入容器 |
| et_url | TextInputEditText | URL 输入框 |
| tv_method | TextView | 请求方式标签 |
| rg_method | RadioGroup | 请求方式选择组 |
| rb_post | RadioButton | POST 选项 (默认选中) |
| rb_get | RadioButton | GET 选项 |
| btn_save | Button | 保存配置按钮 |
| btn_history | Button | 查看历史按钮 |
| tv_status | TextView | 运行状态显示 |
| tv_instructions | TextView | 权限提示说明 |

### 6.3 HistoryActivity 布局规范

**布局类型**: LinearLayout (vertical)

```
┌─────────────────────────────────────┐
│ ┌─────────────────────────────────┐ │
│ │  筛选区域 (默认隐藏)             │ │  ← layout_filter (visibility=gone)
│ │  ┌───────────────────────────┐ │ │
│ │  │ 搜索内容                   │ │ │  ← et_search (TextInputEditText)
│ │  └───────────────────────────┘ │ │
│ │  ┌──────────┐ ┌──────────────┐│ │
│ │  │状态 ▼    │ │时间范围 ▼    ││ │  ← spinner_status / spinner_time_range
│ │  └──────────┘ └──────────────┘│ │
│ │  ┌───────────────────────────┐ │ │
│ │  │ 2024-01-01 至 2024-12-31  │ │ │  ← tv_date_range (自定义日期时显示)
│ │  └───────────────────────────┘ │ │
│ │  ┌──────────┐ ┌──────────────┐│ │
│ │  │ 应用筛选  │ │    清除      ││ │  ← btn_apply_filter / btn_clear_filter
│ │  └──────────┘ └──────────────┘│ │
│ └─────────────────────────────────┘ │
│                                     │
│  共 15 条记录                       │  ← tv_record_count (14sp, #666666)
│                                     │
│  ┌─────────────────────────────┐   │
│  │ Hello World                  │   │  ← tv_content (16sp, bold, maxLines=3)
│  │                              │   │
│  │ 2024-01-15 10:30:00  已发送  │   │  ← tv_timestamp (12sp, #888888) + tv_status
│  │                    [重新发送] │   │  ← btn_resend (仅FAILED时显示)
│  └─────────────────────────────┘   │
│                                     │
│  ┌─────────────────────────────┐   │
│  │ 另一条记录...                 │   │
│  │ ...                          │   │
│  └─────────────────────────────┘   │
│                                     │
│  (空时显示) 暂无历史记录             │  ← tv_empty (居中, visibility=gone)
└─────────────────────────────────────┘
```

**菜单项** (menu/menu_history.xml):

| ID | 图标 | 标题 | 显示条件 |
|----|------|------|---------|
| action_toggle_filter | ic_menu_search | 筛选 | ifRoom |
| action_export_json | - | 导出为JSON | 子菜单 |
| action_export_csv | - | 导出为CSV | 子菜单 |

**筛选状态选项**:

| 位置 | 显示文本 | 筛选值 |
|------|---------|--------|
| 0 | 全部 | "" |
| 1 | 待发送 | "PENDING" |
| 2 | 已发送 | "SENT" |
| 3 | 发送失败 | "FAILED" |

**时间范围选项**:

| 位置 | 显示文本 | 时间范围 |
|------|---------|---------|
| 0 | 全部时间 | 0 ~ MAX |
| 1 | 今日 | 今日0:00 ~ 现在 |
| 2 | 昨日 | 昨日0:00 ~ 昨日23:59 |
| 3 | 近7天 | 7天前 ~ 现在 |
| 4 | 近30天 | 30天前 ~ 现在 |
| 5 | 自定义 | 用户选择 |

### 6.4 悬浮窗布局规范

**布局类型**: FrameLayout

```
折叠状态:                    展开状态:
┌──┐                        ┌──────────────────┐
│  │                        │  点击上传          │
│  │ 12dp × 80dp            │  (14sp, bold, 白色)│
│  │                        └──────────────────┘
│  │                        80dp 高, wrap_content 宽
└──┘                        内边距: paddingStart=16dp, paddingEnd=16dp
bg_floating_collapsed.xml    bg_floating_expanded.xml
```

**悬浮窗行为规范**:

| 行为 | 规则 |
|------|------|
| 初始位置 | gravity=TOP|START, x=存储值, y=存储值 |
| 窗口类型 | TYPE_APPLICATION_OVERLAY (2038) |
| 窗口标志 | FLAG_NOT_FOCUSABLE (40) |
| 自动折叠 | 展开后 5 秒自动折叠 |
| 贴边动画 | ValueAnimator, 200ms, 松手后自动贴到左/右边缘 |
| 拖拽阈值 | 15px |
| 点击逻辑 | 折叠→展开; 展开→触发上传 |
| 位置保存 | 拖拽结束后保存坐标到 SharedPreferences |

### 6.5 ClipboardProxyActivity 规范

| 属性 | 值 |
|------|-----|
| 主题 | Theme.Translucent.NoTitleBar |
| taskAffinity | "" (空字符串) |
| excludeFromRecents | true |
| launchMode | singleInstance |
| 行为 | 获取焦点后延迟100ms读取剪切板，读取后延迟50ms自动finish |

### 6.6 颜色方案

| 用途 | 颜色值 | 说明 |
|------|--------|------|
| 状态-运行中 | teal_700 | 绿色系 |
| 状态-已停止 | android:darker_gray | 灰色 |
| 状态-待发送 | #FFA500 | 橙色 |
| 状态-已发送 | #FF008000 等价 | 绿色 |
| 状态-发送失败 | #FF660000 等价 (SupportMenu.CATEGORY_MASK) | 红色 |
| 上传中 | purple_500 | 紫色 |
| 上传成功 | holo_green_dark | 绿色 |
| 上传失败 | holo_red_dark | 红色 |
| 辅助文字 | #666666 | 灰色 |
| 时间戳 | #888888 | 浅灰色 |
| 悬浮窗文字 | #FFFFFF | 白色 |

---

## 7. 业务逻辑

### 7.1 剪切板监测逻辑

**ClipboardMonitorService** 是核心服务，继承 `AccessibilityService`。

#### 7.1.1 初始化 (onCreate)

```
1. 创建 StorageManager 实例
2. 获取 NetworkManager 单例
3. 获取 ClipboardManager 系统服务
4. 获取 FloatingWindowManager 单例
5. 创建 ClipboardResultReceiver 广播接收器
6. 注册广播: IntentFilter(ACTION_READ_RESULT), flag=RECEIVER_NOT_EXPORTED
7. 设置悬浮窗点击监听器 → launchClipboardProxy()
8. 创建 ScheduledExecutorService (单线程)
9. 启动定时轮询: scheduleAtFixedRate(processClipboard, 3s, 3s)
```

#### 7.1.2 连接时 (onServiceConnected)

```
1. 创建 OnPrimaryClipChangedListener
2. clipboardManager.addPrimaryClipChangedListener(listener)
```

#### 7.1.3 处理剪切板 (processClipboard)

```
synchronized processClipboard(isManualTrigger: boolean):
    try:
        if NOT storageManager.isEnabled(): return
        if clipboardManager == null: return
        if NOT clipboardManager.hasPrimaryClip(): return

        clip = clipboardManager.getPrimaryClip()
        if clip == null OR clip.getItemCount() == 0: return

        item = clip.getItemAt(0)
        contentStr = ""
        typeStr = "text/plain"

        if item.getText() != null:
            contentStr = item.getText().toString()
            typeStr = "text"
        else if item.getUri() != null:
            contentStr = item.getUri().toString()
            typeStr = "file/uri"
        else if item.getIntent() != null:
            contentStr = item.getIntent().toUri(0)
            typeStr = "intent"

        if contentStr.isEmpty(): return
        if contentStr.equals(lastClipContent): return

        lastClipContent = contentStr

        // 在新线程中执行网络操作
        new Thread(() -> {
            deviceId = Settings.Secure.getString(getContentResolver(), "android_id")
            data = new ClipData(deviceId, System.currentTimeMillis(), contentStr, typeStr)
            storageManager.saveClip(data)

            url = storageManager.getUrl()
            if url != null AND !url.isEmpty():
                networkManager.sendReport(url, storageManager.getMethod(), data, callback)
            else:
                data.setStatus("FAILED")
                storageManager.updateClip(data)
        }).start()
    catch Exception e:
        e.printStackTrace()
```

#### 7.1.4 无障碍事件处理 (onAccessibilityEvent)

```
eventType == TYPE_WINDOW_STATE_CHANGED (8192) OR
eventType == TYPE_WINDOW_CONTENT_CHANGED (2048):
    → processClipboard(false)
```

#### 7.1.5 手动上传处理 (processManualContent)

```
processManualContent(content, type):
    if content.equals(lastClipContent):
        floatingWindowManager.updateStatus("内容重复", darker_gray)
        floatingWindowManager.delayedCollapseView()
        return

    lastClipContent = content

    new Thread(() -> {
        floatingWindowManager.updateStatus("上传中...", purple_500)
        deviceId = Settings.Secure.getString(getContentResolver(), "android_id")
        data = new ClipData(deviceId, System.currentTimeMillis(), content, type)
        storageManager.saveClip(data)

        url = storageManager.getUrl()
        if url != null AND !url.isEmpty():
            networkManager.sendReport(url, method, data, callback)
                onSuccess:
                    data.setStatus("SENT")
                    storageManager.updateClip(data)
                    floatingWindowManager.updateStatus("上传成功", holo_green_dark)
                    floatingWindowManager.delayedCollapseView()
                onFailure:
                    data.setStatus("FAILED")
                    storageManager.updateClip(data)
                    floatingWindowManager.updateStatus("上传失败", holo_red_dark)
                    floatingWindowManager.delayedCollapseView()
        else:
            data.setStatus("FAILED")
            storageManager.updateClip(data)
            floatingWindowManager.updateStatus("配置缺失", holo_red_dark)
            floatingWindowManager.delayedCollapseView()
    }).start()
```

### 7.2 保活服务逻辑 (KeepAliveService)

#### 7.2.1 初始化 (onCreate)

```
1. createNotificationChannel()
   - channelId = "ClipboardMonitorChannel"
   - channelName = "剪切板监测服务"
   - importance = IMPORTANCE_LOW (2)

2. startForeground(1, createNotification())
   - 通知标题: "剪切板监测运行中"
   - 通知内容: "正在后台监测剪切板变化"
   - 通知图标: ic_launcher
   - 优先级: PRIORITY_LOW (-1)

3. 创建 StorageManager 实例
4. 获取 NetworkManager 单例
5. 创建 ScheduledExecutorService (单线程)
6. 获取 FloatingWindowManager 单例
7. floatingWindowManager.init(this)
8. floatingWindowManager.showFloatingWindow()
9. 启动重试定时器: scheduleAtFixedRate(retryFailedUploads, 1h, 1h)
```

#### 7.2.2 失败重试逻辑 (retryFailedUploads)

```
retryFailedUploads():
    if NOT storageManager.isEnabled(): return

    history = storageManager.getHistory()
    for data in history:
        if data.getStatus() == "PENDING" OR data.getStatus() == "FAILED":
            if data.getRetryCount() < 5:
                data.incrementRetryCount()
                storageManager.updateClip(data)
                networkManager.sendReport(url, method, data, callback)
                    onSuccess: data.setStatus("SENT"); storageManager.updateClip(data)
                    onFailure: data.setStatus("FAILED"); storageManager.updateClip(data)
```

#### 7.2.3 onStartCommand

```
返回 START_STICKY (1)
如果 floatingWindowManager != null: showFloatingWindow()
```

### 7.3 网络请求逻辑 (NetworkManager)

```
sendReport(url, method, data, callback):
    json = gson.toJson(data)
    body = RequestBody.create(json, MediaType.parse("application/json; charset=utf-8"))
    builder = new Request.Builder().url(url).addHeader("Content-Type", "application/json")

    if method == "POST":
        builder.post(body)
    else if method == "GET":
        encodedJson = URLEncoder.encode(json, "UTF-8")
        if url.contains("?"):
            newUrl = url + "&data=" + encodedJson
        else:
            newUrl = url + "?data=" + encodedJson
        builder.url(newUrl)
        builder.get()
    else:
        builder.method(method, body)

    client.newCall(builder.build()).enqueue(callback)
        onFailure: callback.onFailure(e.getMessage())
        onResponse:
            if response.isSuccessful(): callback.onSuccess()
            else: callback.onFailure("HTTP " + response.code())
            response.close()
```

### 7.4 存储逻辑 (StorageManager)

#### 7.4.1 保存记录 (saveClip)

```
saveClip(data):
    history = getHistory()
    history.add(0, data)              // 插入到头部
    if history.size() > 1000:
        history = history.subList(0, 1000)   // 截断
    writeHistory(history)
```

#### 7.4.2 更新记录 (updateClip)

```
updateClip(updatedData):
    history = getHistory()
    for i in range(history.size()):
        if history.get(i).getId().equals(updatedData.getId()):
            history.set(i, updatedData)
            break
    writeHistory(history)
```

#### 7.4.3 综合筛选 (filter)

```
filter(keyword, status, startTime, endTime):
    return getHistory().stream().filter(data -> {
        if keyword != null AND !keyword.trim().isEmpty():
            if data.getClipContent() == null OR
               !data.getClipContent().toLowerCase().contains(keyword.toLowerCase()):
                return false

        if status != null AND !status.isEmpty():
            if !status.equals(data.getStatus()): return false

        if data.getTimestamp() < startTime OR data.getTimestamp() > endTime:
            return false

        return true
    }).collect(Collectors.toList())
```

#### 7.4.4 导出功能

**JSON 导出**:
```
exportToJson(): return gson.toJson(getHistory())
```

**CSV 导出**:
```
exportToCsv():
    csv = "ID,Device ID,Timestamp,Content,Type,Status,Retry Count\n"
    for data in history:
        csv += escapeCsv(data.getId()) + ","
        csv += escapeCsv(data.getDeviceId()) + ","
        csv += sdf.format(Date(data.getTimestamp())) + ","
        csv += escapeCsv(data.getClipContent()) + ","
        csv += escapeCsv(data.getClipType()) + ","
        csv += escapeCsv(data.getStatus()) + ","
        csv += data.getRetryCount() + "\n"
    return csv

escapeCsv(value):
    if value == null: return "\"\""
    if value contains "," OR "\n" OR "\"":
        return "\"" + value.replace("\"", "\"\"") + "\""
    return value
```

**文件保存**:
```
saveExportFile(content, fileName):
    downloadsDir = Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS)
    file = new File(downloadsDir, fileName)
    FileWriter.write(content)
    return file
```

### 7.5 悬浮窗逻辑 (FloatingWindowManager)

#### 7.5.1 显示悬浮窗

```
showFloatingWindow():
    if storageManager.isFloatingWindowEnabled() AND
       Settings.canDrawOverlays(context) AND
       NOT isViewAttached:
        createFloatingView()
        windowManager.addView(floatingView, layoutParams)
        isViewAttached = true
        snapToEdge(animate=false)
```

#### 7.5.2 触摸事件处理

```
onTouch(v, event):
    ACTION_DOWN:
        initialX = layoutParams.x
        initialY = layoutParams.y
        initialTouchX = event.getRawX()
        initialTouchY = event.getRawY()
        isDragging = false

    ACTION_MOVE:
        dx = event.getRawX() - initialTouchX
        dy = event.getRawY() - initialTouchY
        if |dx| > 15 OR |dy| > 15: isDragging = true
        if isDragging:
            layoutParams.x = initialX + dx
            layoutParams.y = initialY + dy
            windowManager.updateViewLayout(floatingView, layoutParams)

    ACTION_UP:
        if NOT isDragging:
            if isExpanded:
                cancelAutoCollapse()
                updateStatus("读取中...", teal_200)
                clickListener.onFloatingWindowClick()   // 触发上传
            else:
                expandView()
        else:
            storageManager.saveFloatingWindowPosition(layoutParams.x, layoutParams.y)
            snapToEdge(animate=true)
```

#### 7.5.3 贴边逻辑

```
snapToEdge(animate):
    screenWidth = displayMetrics.widthPixels
    currentX = layoutParams.x

    if currentX + floatingView.width/2 < screenWidth/2:
        targetX = 0              // 贴左
        isOnLeftEdge = true
    else:
        targetX = screenWidth - viewWidth   // 贴右
        isOnLeftEdge = false

    if animate:
        ValueAnimator.ofInt(currentX, targetX)
            .setDuration(200)
            .addUpdateListener { layoutParams.x = animatedValue; updateViewLayout() }
            .start()
    else:
        layoutParams.x = targetX
        windowManager.updateViewLayout(floatingView, layoutParams)

    storageManager.saveFloatingWindowPosition(targetX, layoutParams.y)
```

### 7.6 权限检查逻辑 (MainActivity)

#### 7.6.1 无障碍权限检查

```
checkAccessibilityPermission():
    am = getSystemService(ACCESSIBILITY_SERVICE)
    enabledServices = am.getEnabledAccessibilityServiceList(FEEDBACK_GENERIC)
    for info in enabledServices:
        if info.getId().contains(getPackageName()): return true
    return false
```

#### 7.6.2 悬浮窗权限检查

```
checkOverlayPermission(): return Settings.canDrawOverlays(this)
```

#### 7.6.3 通知权限检查 (Android 13+)

```
if Build.VERSION.SDK_INT >= 33 AND
   ContextCompat.checkSelfPermission(this, POST_NOTIFICATIONS) != GRANTED:
    ActivityCompat.requestPermissions(this, [POST_NOTIFICATIONS], requestCode)
```

#### 7.6.4 电池优化检查

```
pm = getSystemService(POWER_SERVICE)
if NOT pm.isIgnoringBatteryOptimizations(getPackageName()):
    显示对话框引导用户到 REQUEST_IGNORE_BATTERY_OPTIMIZATIONS 设置
```

### 7.7 开机自启逻辑 (BootReceiver)

```
onReceive(context, intent):
    if intent.getAction() == BOOT_COMPLETED:
        storageManager = new StorageManager(context)
        if storageManager.isEnabled():
            intent = new Intent(context, KeepAliveService.class)
            context.startForegroundService(intent)
```

---

## 8. 配置要求

### 8.1 AndroidManifest.xml 完整配置

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.example.myapplication01">

    <!-- 权限声明 -->
    <uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
    <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED"/>
    <uses-permission android:name="android.permission.FOREGROUND_SERVICE"/>
    <uses-permission android:name="android.permission.POST_NOTIFICATIONS"/>
    <uses-permission android:name="android.permission.WAKE_LOCK"/>
    <uses-permission android:name="android.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS"/>
    <uses-permission android:name="android.permission.FOREGROUND_SERVICE_DATA_SYNC"/>
    <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW"/>

    <!-- 自定义签名权限 -->
    <permission
        android:name="com.example.myapplication01.DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION"
        android:protectionLevel="signature"/>
    <uses-permission android:name="com.example.myapplication01.DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION"/>

    <application
        android:theme="@style/Theme.MyApplication01"
        android:label="@string/app_name"
        android:icon="@mipmap/ic_launcher"
        android:allowBackup="true"
        android:supportsRtl="true"
        android:extractNativeLibs="false"
        android:fullBackupContent="@xml/backup_rules"
        android:usesCleartextTraffic="true"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:dataExtractionRules="@xml/data_extraction_rules">

        <!-- 主界面 (启动入口) -->
        <activity
            android:name=".MainActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN"/>
                <category android:name="android.intent.category.LAUNCHER"/>
            </intent-filter>
        </activity>

        <!-- 历史记录界面 -->
        <activity
            android:label="历史记录"
            android:name=".HistoryActivity"
            android:exported="false"/>

        <!-- 剪切板代理Activity (透明, 用于读取剪切板) -->
        <activity
            android:theme="@android:style/Theme.Translucent.NoTitleBar"
            android:name=".ClipboardProxyActivity"
            android:exported="false"
            android:taskAffinity=""
            android:excludeFromRecents="true"
            android:launchMode="singleInstance"/>

        <!-- 前台保活服务 -->
        <service
            android:name=".service.KeepAliveService"
            android:enabled="true"
            android:exported="false"
            android:foregroundServiceType="dataSync"/>

        <!-- 无障碍监测服务 -->
        <service
            android:name=".service.ClipboardMonitorService"
            android:permission="android.permission.BIND_ACCESSIBILITY_SERVICE"
            android:exported="true">
            <intent-filter>
                <action android:name="android.accessibilityservice.AccessibilityService"/>
            </intent-filter>
            <meta-data
                android:name="android.accessibilityservice"
                android:resource="@xml/accessibility_service_config"/>
        </service>

        <!-- 开机广播接收器 -->
        <receiver
            android:name=".service.BootReceiver"
            android:enabled="true"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.BOOT_COMPLETED"/>
            </intent-filter>
        </receiver>

    </application>
</manifest>
```

### 8.2 无障碍服务配置 (res/xml/accessibility_service_config.xml)

```xml
<?xml version="1.0" encoding="utf-8"?>
<accessibility-service
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:description="@string/accessibility_desc"
    android:accessibilityEventTypes="typeAllMask"
    android:accessibilityFeedbackType="feedbackGeneric"
    android:notificationTimeout="100"
    android:accessibilityFlags="flagDefault"
    android:canRetrieveWindowContent="true"/>
```

### 8.3 build.gradle 配置

**项目级 build.gradle**:
```groovy
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:8.10.1'
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

task clean(type: Delete) {
    delete rootProject.buildDir
}
```

**应用级 build.gradle**:
```groovy
plugins {
    id 'com.android.application'
}

android {
    namespace "com.example.myapplication01"
    compileSdkVersion 34

    defaultConfig {
        applicationId 'com.example.myapplication01'
        minSdkVersion 29
        targetSdkVersion 34
        versionCode 1
        versionName "1.0"
        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }

    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }

    lintOptions {
        abortOnError false
    }

    buildFeatures {
        buildConfig = false
    }
}

dependencies {
    // AndroidX
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
    implementation 'androidx.recyclerview:recyclerview:1.3.2'
    implementation 'androidx.cardview:cardview:1.0.0'
    implementation 'androidx.core:core:1.12.0'

    // Material Design
    implementation 'com.google.android.material:material:1.11.0'

    // Network
    implementation 'com.squareup.okhttp3:okhttp:4.12.0'

    // JSON
    implementation 'com.google.code.gson:gson:2.10.1'
}
```

**settings.gradle**:
```groovy
rootProject.name = '剪切板监测'
include 'app'
```

### 8.4 字符串资源 (res/values/strings.xml 关键项)

```xml
<string name="app_name">剪切板监测</string>
<string name="accessibility_desc">后台监测剪切板变化并自动上报。</string>
```

---

## 9. 测试要求

### 9.1 功能测试用例

| 编号 | 测试场景 | 前置条件 | 操作步骤 | 预期结果 |
|------|---------|---------|---------|---------|
| TC-01 | 开启监测 | 已授予无障碍权限 | 切换 switch_monitor 为 ON | 服务启动，状态显示"运行中" |
| TC-02 | 关闭监测 | 监测运行中 | 切换 switch_monitor 为 OFF | 服务停止，状态显示"已停止" |
| TC-03 | 无障碍权限缺失 | 未授予无障碍权限 | 切换 switch_monitor 为 ON | 弹出引导对话框，开关回弹为 OFF |
| TC-04 | 剪切板自动检测 | 监测运行中 | 在其他应用复制文本 | 3秒内自动上报，历史记录新增一条 |
| TC-05 | 重复内容过滤 | 监测运行中 | 连续复制相同内容 | 仅上报一次，后续忽略 |
| TC-06 | POST 请求发送 | 配置了 URL，方法为 POST | 复制内容 | 发送 POST 请求，body 为 JSON |
| TC-07 | GET 请求发送 | 配置了 URL，方法为 GET | 复制内容 | 发送 GET 请求，data 参数为编码后 JSON |
| TC-08 | 服务器不可达 | 配置了无效 URL | 复制内容 | 状态变为 FAILED，记录保存 |
| TC-09 | 悬浮窗显示 | 已授予悬浮窗权限 | 开启悬浮窗开关 | 悬浮窗出现在屏幕边缘 |
| TC-10 | 悬浮窗拖拽 | 悬浮窗已显示 | 拖拽悬浮窗 | 位置跟随手指，松手后贴边 |
| TC-11 | 悬浮窗手动上传 | 悬浮窗展开状态 | 点击悬浮窗 | 读取剪切板并上传 |
| TC-12 | 悬浮窗自动折叠 | 悬浮窗展开状态 | 等待5秒 | 自动折叠为竖条 |
| TC-13 | 历史记录查看 | 有历史数据 | 点击"查看历史记录" | 显示所有记录列表 |
| TC-14 | 关键词搜索 | 有历史数据 | 输入关键词，点击应用筛选 | 仅显示匹配的记录 |
| TC-15 | 状态筛选 | 有不同状态记录 | 选择"发送失败" | 仅显示 FAILED 记录 |
| TC-16 | 时间范围筛选 | 有历史数据 | 选择"近7天" | 仅显示7天内的记录 |
| TC-17 | 重新发送 | 有 FAILED 记录 | 点击重新发送按钮 | 重新发送请求，更新状态 |
| TC-18 | 删除记录 | 有历史数据 | 长按记录，确认删除 | 记录从列表和存储中移除 |
| TC-19 | 复制内容 | 有历史数据 | 点击记录 | 内容复制到剪切板，Toast 提示 |
| TC-20 | 导出 JSON | 有历史数据 | 菜单→导出为JSON | 文件保存到 Downloads 目录 |
| TC-21 | 导出 CSV | 有历史数据 | 菜单→导出为CSV | 文件保存到 Downloads 目录 |
| TC-22 | 开机自启 | 已开启监测 | 重启设备 | 设备启动后自动开始监测 |
| TC-23 | 失败重试 | 有 FAILED 记录 | 等待1小时 | 自动重试发送，retry_count+1 |
| TC-24 | 最大重试次数 | retry_count=5 | 等待重试 | 不再重试该记录 |
| TC-25 | 配置持久化 | 已保存配置 | 杀掉应用重新打开 | 配置恢复，开关状态正确 |

### 9.2 边界条件测试

| 编号 | 场景 | 预期行为 |
|------|------|---------|
| BT-01 | 剪切板内容为空 | 不上报，不创建记录 |
| BT-02 | URL 为空 | 记录状态设为 FAILED |
| BT-03 | 历史记录达到 1000 条 | 新记录插入头部，旧记录被截断 |
| BT-04 | 网络中断时复制内容 | 记录保存为 FAILED，等待重试 |
| BT-05 | 同时复制文本和 URI | 优先读取 text 内容 |
| BT-06 | 应用被系统强制停止 | 开机自启后恢复运行 |
| BT-07 | 悬浮窗权限被撤销 | 悬浮窗不显示，不崩溃 |

### 9.3 性能指标

| 指标 | 目标值 |
|------|--------|
| 剪切板检测延迟 | < 3 秒 |
| 网络请求超时 | OkHttp 默认 (10s 连接, 10s 读取) |
| 内存占用 (后台) | < 50MB |
| CPU 占用 (空闲) | < 1% |
| 本地存储上限 | 1000 条记录 |
| 重试间隔 | 1 小时 |

---

## 10. 实施路线图

### Phase 1: 基础框架 (第1-2天)

**目标**: 搭建项目骨架，实现基本 UI

| 任务 | 依赖 | 交付物 |
|------|------|--------|
| 创建 Android 项目 | 无 | 项目结构、build.gradle |
| 实现 ClipData 模型 | 无 | ClipData.java |
| 实现 StorageManager | ClipData | StorageManager.java (SP + JSON) |
| 实现 MainActivity 布局 | 无 | activity_main.xml |
| 实现 MainActivity 逻辑 | StorageManager | MainActivity.java (配置保存/加载) |

**里程碑**: 应用可启动，配置可保存和恢复

### Phase 2: 核心服务 (第3-4天)

**目标**: 实现剪切板监测和上报

| 任务 | 依赖 | 交付物 |
|------|------|--------|
| 实现 NetworkManager | ClipData | NetworkManager.java |
| 实现 ClipboardMonitorService | NetworkManager, StorageManager | ClipboardMonitorService.java |
| 实现 KeepAliveService | NetworkManager, StorageManager | KeepAliveService.java |
| 实现 BootReceiver | KeepAliveService | BootReceiver.java |
| 配置 AndroidManifest | 所有 Service | 权限声明、服务注册 |

**里程碑**: 剪切板变化可自动上报到服务器

### Phase 3: 悬浮窗功能 (第5-6天)

**目标**: 实现悬浮窗交互

| 任务 | 依赖 | 交付物 |
|------|------|--------|
| 实现 FloatingWindowManager | StorageManager | FloatingWindowManager.java |
| 实现悬浮窗布局 | 无 | window_floating.xml |
| 实现 ClipboardProxyActivity | 无 | ClipboardProxyActivity.java |
| 集成悬浮窗到 KeepAliveService | FloatingWindowManager | 悬浮窗显示/隐藏 |
| 集成手动上传流程 | ClipboardProxyActivity, ClipboardMonitorService | 点击上传功能 |

**里程碑**: 悬浮窗可拖拽、可手动触发上传

### Phase 4: 历史记录 (第7-8天)

**目标**: 实现完整的记录管理

| 任务 | 依赖 | 交付物 |
|------|------|--------|
| 实现 HistoryActivity 布局 | 无 | activity_history.xml, item_history.xml |
| 实现 HistoryAdapter | ClipData | RecyclerView 适配器 |
| 实现筛选功能 | StorageManager | 关键词/状态/时间筛选 |
| 实现导出功能 | StorageManager | JSON/CSV 导出 |
| 实现重发/删除/复制 | NetworkManager, StorageManager | 单条记录操作 |

**里程碑**: 历史记录可查看、搜索、筛选、导出

### Phase 5: 权限与保活 (第9-10天)

**目标**: 完善权限处理和后台保活

| 任务 | 依赖 | 交付物 |
|------|------|--------|
| 实现权限检查流程 | MainActivity | 无障碍/悬浮窗/通知/电池优化 |
| 实现开机自启 | BootReceiver | BOOT_COMPLETED 处理 |
| 实现失败重试 | KeepAliveService | 定时重试逻辑 |
| 实现去重逻辑 | ClipboardMonitorService | lastClipContent 比对 |

**里程碑**: 应用可在后台稳定运行，权限处理完善

### Phase 6: 测试与优化 (第11-12天)

**目标**: 全面测试和性能优化

| 任务 | 依赖 | 交付物 |
|------|------|--------|
| 执行功能测试用例 | 所有功能 | 测试报告 |
| 执行边界条件测试 | 所有功能 | 边界测试报告 |
| 性能优化 | 测试结果 | 优化后的代码 |
| UI 细节调整 | 测试结果 | 最终 UI |
| 文档完善 | 所有功能 | README, 重构文档 |

**里程碑**: 应用通过所有测试，可发布

---

## 附录 A: 关键常量汇总

| 常量 | 值 | 所在类 |
|------|-----|--------|
| MAX_HISTORY_SIZE | 1000 | StorageManager |
| PREF_NAME | "monitor_config" | StorageManager |
| HISTORY_FILE | "clipboard_history.json" | StorageManager |
| KEY_URL | "target_url" | StorageManager |
| KEY_METHOD | "request_method" | StorageManager |
| KEY_ENABLED | "monitoring_enabled" | StorageManager |
| KEY_FLOAT_WINDOW_ENABLED | "float_window_enabled" | StorageManager |
| KEY_FLOAT_X | "float_x" | StorageManager |
| KEY_FLOAT_Y | "float_y" | StorageManager |
| CHANNEL_ID | "ClipboardMonitorChannel" | KeepAliveService |
| NOTIFICATION_ID | 1 | KeepAliveService |
| ACTION_READ_RESULT | "com.example.myapplication01.ACTION_READ_RESULT" | ClipboardProxyActivity |
| EXTRA_CONTENT | "content" | ClipboardProxyActivity |
| EXTRA_TYPE | "type" | ClipboardProxyActivity |
| EXTRA_ERROR | "error" | ClipboardProxyActivity |
| AUTO_COLLAPSE_DELAY | 5000 (ms) | FloatingWindowManager |
| POLL_INTERVAL | 3 (s) | ClipboardMonitorService |
| RETRY_INTERVAL | 1 (h) | KeepAliveService |
| MAX_RETRY_COUNT | 5 | KeepAliveService |
| DRAG_THRESHOLD | 15 (px) | FloatingWindowManager |
| SNAP_ANIMATION_DURATION | 200 (ms) | FloatingWindowManager |

## 附录 B: 广播 Action 汇总

| Action | 发送方 | 接收方 | 携带数据 |
|--------|--------|--------|---------|
| ACTION_READ_RESULT | ClipboardProxyActivity | ClipboardMonitorService | content, type, error |
| BOOT_COMPLETED | Android System | BootReceiver | 无 |

## 附录 C: 单例类汇总

| 类 | 获取方式 |
|----|---------|
| NetworkManager | `NetworkManager.getInstance()` |
| FloatingWindowManager | `FloatingWindowManager.getInstance()` |

## 附录 D: 线程模型

| 线程 | 用途 | 创建方式 |
|------|------|---------|
| 主线程 | UI 更新 | Android 主线程 |
| 网络请求线程 | HTTP 请求 | OkHttp 内部线程池 |
| 数据处理线程 | 保存/上报数据 | `new Thread().start()` |
| 定时轮询线程 | 剪切板轮询 | `Executors.newSingleThreadScheduledExecutor()` |
| 重试调度线程 | 失败重试 | `Executors.newSingleThreadScheduledExecutor()` |
