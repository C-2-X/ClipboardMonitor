package com.example.myapplication01;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import okhttp3.HttpUrl;

/* JADX INFO: loaded from: classes3.dex */
public class ClipboardProxyActivity extends Activity {
    public static final String ACTION_READ_RESULT = "com.example.myapplication01.ACTION_READ_RESULT";
    public static final String EXTRA_CONTENT = "content";
    public static final String EXTRA_ERROR = "error";
    public static final String EXTRA_TYPE = "type";
    private boolean hasProcessed = false;
    private Handler handler = new Handler(Looper.getMainLooper());

    @Override // android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && !this.hasProcessed) {
            this.hasProcessed = true;
            this.handler.postDelayed(new Runnable() { // from class: com.example.myapplication01.ClipboardProxyActivity$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.readClipboardAndFinish();
                }
            }, 100L);
        }
    }

    @Override // android.app.Activity
    protected void onResume() {
        super.onResume();
        if (!this.hasProcessed) {
            this.handler.postDelayed(new Runnable() { // from class: com.example.myapplication01.ClipboardProxyActivity$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.m44x7eb33e8d();
                }
            }, 500L);
        }
    }

    /* JADX INFO: renamed from: lambda$onResume$0$com-example-myapplication01-ClipboardProxyActivity, reason: not valid java name */
    /* synthetic */ void m44x7eb33e8d() {
        if (!this.hasProcessed) {
            this.hasProcessed = true;
            readClipboardAndFinish();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void readClipboardAndFinish() {
        Handler handler;
        Runnable runnable;
        if (isFinishing()) {
            return;
        }
        try {
            try {
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService("clipboard");
                if (clipboardManager == null || !clipboardManager.hasPrimaryClip()) {
                    sendResult(null, null, "无法访问剪切板");
                } else {
                    ClipData clip = clipboardManager.getPrimaryClip();
                    if (clip == null || clip.getItemCount() <= 0) {
                        sendResult(null, null, "剪切板为空");
                    } else {
                        ClipData.Item item = clip.getItemAt(0);
                        String content = HttpUrl.FRAGMENT_ENCODE_SET;
                        String type = "text/plain";
                        if (item.getText() != null) {
                            content = item.getText().toString();
                            type = "text";
                        } else if (item.getUri() != null) {
                            content = item.getUri().toString();
                            type = "file/uri";
                        } else if (item.getIntent() != null) {
                            content = item.getIntent().toUri(0);
                            type = "intent";
                        }
                        if (content.isEmpty()) {
                            sendResult(null, null, "内容为空");
                        } else {
                            sendResult(content, type, null);
                        }
                    }
                }
                handler = this.handler;
                runnable = new Runnable() { // from class: com.example.myapplication01.ClipboardProxyActivity$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.m45x8da278ce();
                    }
                };
            } catch (Exception e) {
                e.printStackTrace();
                sendResult(null, null, "读取异常: " + e.getMessage());
                handler = this.handler;
                runnable = new Runnable() { // from class: com.example.myapplication01.ClipboardProxyActivity$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.m45x8da278ce();
                    }
                };
            }
            handler.postDelayed(runnable, 50L);
        } catch (Throwable th) {
            this.handler.postDelayed(new Runnable() { // from class: com.example.myapplication01.ClipboardProxyActivity$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.m45x8da278ce();
                }
            }, 50L);
            throw th;
        }
    }

    /* JADX INFO: renamed from: lambda$readClipboardAndFinish$1$com-example-myapplication01-ClipboardProxyActivity, reason: not valid java name */
    /* synthetic */ void m45x8da278ce() {
        finish();
        overridePendingTransition(0, 0);
    }

    private void sendResult(String content, String type, String error) {
        Intent intent = new Intent(ACTION_READ_RESULT);
        intent.setPackage(getPackageName());
        if (content != null) {
            intent.putExtra(EXTRA_CONTENT, content);
            intent.putExtra(EXTRA_TYPE, type);
        }
        if (error != null) {
            intent.putExtra(EXTRA_ERROR, error);
        }
        sendBroadcast(intent);
    }
}
