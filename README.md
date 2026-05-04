# 剪切板监测 (Clipboard Monitor)

一款 Android 平台的剪切板监测应用，能够实时监控系统剪切板变化，自动将内容上报到指定服务器，并提供完整的历史记录管理功能。

[English](README_en.md) | 简体中文

---

## ✨ 功能特性

### 核心功能

- **实时监控** - 利用 Android 无障碍服务（AccessibilityService）实时监测系统剪切板变化
- **自动上报** - 剪切板内容变化时自动上传到配置的服务器，支持 POST 和 GET 两种请求方式
- **历史记录** - 本地保存所有剪切板记录，支持查看、搜索、筛选和导出
- **重试机制** - 发送失败自动重试，最多重试 5 次，确保数据可靠送达
- **悬浮窗** - 可拖拽的悬浮窗组件，随时查看监测状态并手动触发上传
- **开机自启** - 支持设备启动后自动开始监测
- **数据导出** - 支持将历史记录导出为 JSON 或 CSV 格式

### 辅助功能

- [x] 多条件筛选（关键词/状态/时间范围）
- [x] 自定义日期范围筛选
- [x] 单条记录重新发送
- [x] 长按删除记录
- [x] 一键复制内容到剪切板
- [x] 电池优化白名单适配
- [x] Android 13+ 通知权限适配

---

## 📱 截图预览

> （请在此处添加应用截图）

| 主界面 | 历史记录 | 悬浮窗 |
|:------:|:-------:|:------:|
| ![主界面]() | ![历史记录]() | ![悬浮窗]() |

---

## 🛠 技术栈

| 分类 | 技术 |
|------|------|
| **平台** | Android (API 29+) |
| **语言** | Java 8 |
| **网络** | OkHttp 3.x |
| **JSON** | Gson |
| **UI** | Material Design Components |
| **架构** | AndroidX + 分层架构 |
| **构建** | Gradle 8.10.1 |

---

## 📂 项目结构

```
api_App/
├── app/
│   ├── src/main/
│   │   ├── java/com/example/myapplication01/
│   │   │   ├── MainActivity.java          # 主配置界面
│   │   │   ├── HistoryActivity.java       # 历史记录管理
│   │   │   ├── ClipboardProxyActivity.java # 剪切板读取代理
│   │   │   ├── model/
│   │   │   │   └── ClipData.java          # 数据模型
│   │   │   ├── service/
│   │   │   │   ├── ClipboardMonitorService.java  # 无障碍监测服务
│   │   │   │   ├── KeepAliveService.java         # 前台保活服务
│   │   │   │   └── BootReceiver.java             # 开机广播接收器
│   │   │   └── utils/
│   │   │       ├── NetworkManager.java      # 网络请求管理
│   │   │       ├── StorageManager.java      # 本地存储管理
│   │   │       └── FloatingWindowManager.java # 悬浮窗管理
│   │   ├── res/
│   │   │   ├── layout/                     # 布局文件
│   │   │   ├── values/                     # 字符串资源
│   │   │   └── xml/                        # 配置文件
│   │   └── AndroidManifest.xml
│   └── build.gradle
├── build.gradle
└── settings.gradle
```

---

## 🚀 快速开始

### 环境要求

- Android Studio Hedgehog (2023.1.1) 或更高版本
- JDK 17
- Android SDK 34
- Android 设备或模拟器（API 29+）

### 安装步骤

1. **克隆项目**
   ```bash
   git clone https://github.com/your-username/clipboard-monitor.git
   cd clipboard-monitor
   ```

2. **用 Android Studio 打开**
   - File → Open → 选择项目根目录

3. **同步 Gradle**
   - Android Studio 会自动提示同步，点击 "Sync Now"

4. **运行应用**
   - 选择目标设备，点击 Run (Shift + F10)

### 构建 APK

```bash
./gradlew assembleDebug
```

APK 文件输出位置：`app/build/outputs/apk/debug/app-debug.apk`

---

## 📖 使用说明

### 首次配置

1. 安装并打开应用
2. 授予必要权限：
   - **无障碍权限** - 必须开启才能监测剪切板
   - **悬浮窗权限** - 可选，开启后显示悬浮窗
   - **通知权限** (Android 13+) - 可选，用于前台服务通知
   - **电池优化** - 建议允许，避免后台被系统杀死

3. 输入服务器地址（URL）
4. 选择请求方式（POST / GET）
5. 点击「保存配置」
6. 开启「剪切板监测」开关

### 悬浮窗使用

- 拖拽悬浮窗可调整位置
- 点击展开查看详细信息
- 再次点击「点击上传」区域手动触发上传
- 5 秒后自动折叠

### 历史记录

- **搜索**：输入关键词搜索内容
- **筛选**：按状态（待发送/已发送/发送失败）或时间范围筛选
- **重新发送**：点击失败记录的重试按钮
- **删除**：长按记录然后确认删除
- **复制**：点击记录复制内容到剪切板
- **导出**：菜单 → 导出为 JSON/CSV

---

## ⚙️ 配置说明

### 服务器接收格式

应用会以 JSON 格式发送数据，示例：

```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "deviceId": "android_a1b2c3d4e5f6",
  "timestamp": 1699000000000,
  "clipContent": "要分享的内容",
  "clipType": "text",
  "status": "PENDING",
  "retryCount": 0
}
```

### 请求方式

| 方式 | 说明 |
|------|------|
| **POST** | JSON 数据放在请求体中发送 |
| **GET** | JSON 数据经过 URL 编码后作为 `data` 参数发送 |

---

## 🔒 权限说明

| 权限 | 必须 | 说明 |
|------|:----:|------|
| INTERNET | ✅ | 网络请求 |
| ACCESS_NETWORK_STATE | - | 检查网络状态 |
| RECEIVE_BOOT_COMPLETED | - | 开机自启 |
| FOREGROUND_SERVICE | ✅ | 保持后台运行 |
| POST_NOTIFICATIONS | - | Android 13+ 通知 |
| WAKE_LOCK | - | 防止休眠 |
| REQUEST_IGNORE_BATTERY_OPTIMIZATIONS | - | 电池优化白名单 |
| SYSTEM_ALERT_WINDOW | - | 悬浮窗功能 |
| BIND_ACCESSIBILITY_SERVICE | ✅ | 无障碍服务 |

---

## ⚠️ 注意事项

1. **无障碍权限** 是必须开启的核心权限，关闭后监测功能将停止
2. 建议将应用加入电池优化白名单，以确保持续后台运行
3. Android 10+ 系统的剪切板隐私限制可能导致部分应用内容无法读取
4. 应用使用明文 HTTP 流量，如有安全需求请配置 HTTPS 服务器
5. 历史记录最多保存 1000 条，超出后自动清理旧数据

---

## 🔧 常见问题

### Q: 为什么无法读取某些应用的内容？
A: 部分应用（如银行类、密码管理器）有额外的剪切板保护，应用无法读取其内容。

### Q: 为什么监测不生效？
A: 请检查：
1. 是否已开启无障碍权限
2. 是否已开启「剪切板监测」开关
3. 是否被系统省电策略限制

### Q: 如何彻底关闭应用？
A: 需要先关闭「剪切板监测」开关，然后卸载应用。

---

## 📄 开源协议

本项目基于 [MIT License](LICENSE) 开源。

---

## 🙏 致谢

- [OkHttp](https://github.com/square/okhttp) - 网络请求库
- [Gson](https://github.com/google/gson) - JSON 处理库
- [Material Components](https://github.com/material-components/material-components-android) - Material Design UI 组件

---

## 📬 联系作者

如有问题或建议，请提交 [Issue](https://github.com/your-username/clipboard-monitor/issues)。

---

<p align="center">
  <a href="https://github.com/your-username/clipboard-monitor">GitHub</a> ·
  <a href="https://github.com/your-username/clipboard-monitor/releases">Releases</a>
</p>
