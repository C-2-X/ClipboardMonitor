package com.example.myapplication01.service;

import android.R;
import android.accessibilityservice.AccessibilityService;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.provider.Settings;
import android.view.accessibility.AccessibilityEvent;
import com.example.myapplication01.ClipboardProxyActivity;
import com.example.myapplication01.model.ClipData;
import com.example.myapplication01.utils.FloatingWindowManager;
import com.example.myapplication01.utils.NetworkManager;
import com.example.myapplication01.utils.StorageManager;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import okhttp3.HttpUrl;

/* JADX INFO: loaded from: classes6.dex */
public class ClipboardMonitorService extends AccessibilityService {
    private ClipboardManager.OnPrimaryClipChangedListener clipListener;
    private ClipboardManager clipboardManager;
    private FloatingWindowManager floatingWindowManager;
    private String lastClipContent = HttpUrl.FRAGMENT_ENCODE_SET;
    private NetworkManager networkManager;
    private ScheduledExecutorService poller;
    private ClipboardResultReceiver resultReceiver;
    private StorageManager storageManager;

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        this.storageManager = new StorageManager(this);
        this.networkManager = NetworkManager.getInstance();
        this.clipboardManager = (ClipboardManager) getSystemService("clipboard");
        this.floatingWindowManager = FloatingWindowManager.getInstance();
        this.resultReceiver = new ClipboardResultReceiver();
        IntentFilter filter = new IntentFilter(ClipboardProxyActivity.ACTION_READ_RESULT);
        registerReceiver(this.resultReceiver, filter, 4);
        this.floatingWindowManager.setOnFloatingWindowClickListener(new FloatingWindowManager.OnFloatingWindowClickListener() { // from class: com.example.myapplication01.service.ClipboardMonitorService.1
            @Override // com.example.myapplication01.utils.FloatingWindowManager.OnFloatingWindowClickListener
            public void onFloatingWindowClick() {
                ClipboardMonitorService.this.floatingWindowManager.launchClipboardProxy();
            }
        });
        this.poller = Executors.newSingleThreadScheduledExecutor();
        this.poller.scheduleAtFixedRate(new Runnable() { // from class: com.example.myapplication01.service.ClipboardMonitorService$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.m67xf9023e82();
            }
        }, 3L, 3L, TimeUnit.SECONDS);
    }

    /* JADX INFO: renamed from: lambda$onCreate$0$com-example-myapplication01-service-ClipboardMonitorService, reason: not valid java name */
    /* synthetic */ void m67xf9023e82() {
        processClipboard(false);
    }

    private class ClipboardResultReceiver extends BroadcastReceiver {
        private ClipboardResultReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (ClipboardProxyActivity.ACTION_READ_RESULT.equals(intent.getAction())) {
                String error = intent.getStringExtra(ClipboardProxyActivity.EXTRA_ERROR);
                if (error != null) {
                    ClipboardMonitorService.this.floatingWindowManager.updateStatus(error, R.color.holo_red_dark);
                    ClipboardMonitorService.this.floatingWindowManager.delayedCollapseView();
                    return;
                }
                String content = intent.getStringExtra(ClipboardProxyActivity.EXTRA_CONTENT);
                String type = intent.getStringExtra(ClipboardProxyActivity.EXTRA_TYPE);
                if (content == null || content.isEmpty()) {
                    ClipboardMonitorService.this.floatingWindowManager.updateStatus("内容为空", R.color.darker_gray);
                    ClipboardMonitorService.this.floatingWindowManager.delayedCollapseView();
                } else {
                    ClipboardMonitorService.this.processManualContent(content, type);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processManualContent(final String content, final String type) {
        if (content.equals(this.lastClipContent)) {
            this.floatingWindowManager.updateStatus("内容重复", R.color.darker_gray);
            this.floatingWindowManager.delayedCollapseView();
        } else {
            this.lastClipContent = content;
            new Thread(new Runnable() { // from class: com.example.myapplication01.service.ClipboardMonitorService$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.m69x1ecb29ac(content, type);
                }
            }).start();
        }
    }

    /* JADX INFO: renamed from: lambda$processManualContent$1$com-example-myapplication01-service-ClipboardMonitorService, reason: not valid java name */
    /* synthetic */ void m69x1ecb29ac(String content, String type) {
        this.floatingWindowManager.updateStatus("上传中...", com.example.myapplication01.R.color.purple_500);
        String deviceId = Settings.Secure.getString(getContentResolver(), "android_id");
        final ClipData data = new ClipData(deviceId, System.currentTimeMillis(), content, type);
        this.storageManager.saveClip(data);
        String url = this.storageManager.getUrl();
        if (url != null && !url.isEmpty()) {
            this.networkManager.sendReport(url, this.storageManager.getMethod(), data, new NetworkManager.NetworkCallback() { // from class: com.example.myapplication01.service.ClipboardMonitorService.2
                @Override // com.example.myapplication01.utils.NetworkManager.NetworkCallback
                public void onSuccess() {
                    data.setStatus("SENT");
                    ClipboardMonitorService.this.storageManager.updateClip(data);
                    ClipboardMonitorService.this.floatingWindowManager.updateStatus("上传成功", R.color.holo_green_dark);
                    ClipboardMonitorService.this.floatingWindowManager.delayedCollapseView();
                }

                @Override // com.example.myapplication01.utils.NetworkManager.NetworkCallback
                public void onFailure(String error) {
                    data.setStatus("FAILED");
                    ClipboardMonitorService.this.storageManager.updateClip(data);
                    ClipboardMonitorService.this.floatingWindowManager.updateStatus("上传失败", R.color.holo_red_dark);
                    ClipboardMonitorService.this.floatingWindowManager.delayedCollapseView();
                }
            });
            return;
        }
        data.setStatus("FAILED");
        this.storageManager.updateClip(data);
        this.floatingWindowManager.updateStatus("配置缺失", R.color.holo_red_dark);
        this.floatingWindowManager.delayedCollapseView();
    }

    @Override // android.accessibilityservice.AccessibilityService
    protected void onServiceConnected() {
        super.onServiceConnected();
        this.clipListener = new ClipboardManager.OnPrimaryClipChangedListener() { // from class: com.example.myapplication01.service.ClipboardMonitorService.3
            @Override // android.content.ClipboardManager.OnPrimaryClipChangedListener
            public void onPrimaryClipChanged() {
                ClipboardMonitorService.this.processClipboard(false);
            }
        };
        if (this.clipboardManager != null) {
            this.clipboardManager.addPrimaryClipChangedListener(this.clipListener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void processClipboard(boolean isManualTrigger) {
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (this.storageManager.isEnabled()) {
            if (this.clipboardManager == null) {
                return;
            }
            if (this.clipboardManager.hasPrimaryClip()) {
                android.content.ClipData clip = this.clipboardManager.getPrimaryClip();
                if (clip != null && clip.getItemCount() != 0) {
                    ClipData.Item item = clip.getItemAt(0);
                    String contentStr = HttpUrl.FRAGMENT_ENCODE_SET;
                    String typeStr = "text/plain";
                    if (item.getText() != null) {
                        contentStr = item.getText().toString();
                        typeStr = "text";
                    } else if (item.getUri() != null) {
                        contentStr = item.getUri().toString();
                        typeStr = "file/uri";
                    } else if (item.getIntent() != null) {
                        contentStr = item.getIntent().toUri(0);
                        typeStr = "intent";
                    }
                    if (contentStr.isEmpty()) {
                        return;
                    }
                    if (contentStr.equals(this.lastClipContent)) {
                        return;
                    }
                    this.lastClipContent = contentStr;
                    final String content = contentStr;
                    final String type = typeStr;
                    new Thread(new Runnable() { // from class: com.example.myapplication01.service.ClipboardMonitorService$$ExternalSyntheticLambda2
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.m68x1174a1b0(content, type);
                        }
                    }).start();
                }
            }
        }
    }

    /* JADX INFO: renamed from: lambda$processClipboard$2$com-example-myapplication01-service-ClipboardMonitorService, reason: not valid java name */
    /* synthetic */ void m68x1174a1b0(String content, String type) {
        String deviceId = Settings.Secure.getString(getContentResolver(), "android_id");
        final com.example.myapplication01.model.ClipData data = new com.example.myapplication01.model.ClipData(deviceId, System.currentTimeMillis(), content, type);
        this.storageManager.saveClip(data);
        String url = this.storageManager.getUrl();
        if (url != null && !url.isEmpty()) {
            this.networkManager.sendReport(url, this.storageManager.getMethod(), data, new NetworkManager.NetworkCallback() { // from class: com.example.myapplication01.service.ClipboardMonitorService.4
                @Override // com.example.myapplication01.utils.NetworkManager.NetworkCallback
                public void onSuccess() {
                    data.setStatus("SENT");
                    ClipboardMonitorService.this.storageManager.updateClip(data);
                }

                @Override // com.example.myapplication01.utils.NetworkManager.NetworkCallback
                public void onFailure(String error) {
                    data.setStatus("FAILED");
                    ClipboardMonitorService.this.storageManager.updateClip(data);
                }
            });
        } else {
            data.setStatus("FAILED");
            this.storageManager.updateClip(data);
        }
    }

    @Override // android.accessibilityservice.AccessibilityService
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getEventType() == 8192 || event.getEventType() == 2048) {
            processClipboard(false);
        }
    }

    @Override // android.accessibilityservice.AccessibilityService
    public void onInterrupt() {
        if (this.clipboardManager != null && this.clipListener != null) {
            this.clipboardManager.removePrimaryClipChangedListener(this.clipListener);
        }
    }

    @Override // android.app.Service
    public void onDestroy() {
        if (this.resultReceiver != null) {
            unregisterReceiver(this.resultReceiver);
        }
        if (this.clipboardManager != null && this.clipListener != null) {
            this.clipboardManager.removePrimaryClipChangedListener(this.clipListener);
        }
        if (this.poller != null) {
            this.poller.shutdown();
        }
        super.onDestroy();
    }
}
