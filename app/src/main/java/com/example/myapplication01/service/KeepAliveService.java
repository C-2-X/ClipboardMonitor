package com.example.myapplication01.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.core.app.NotificationCompat;
import com.example.myapplication01.R;
import com.example.myapplication01.model.ClipData;
import com.example.myapplication01.utils.FloatingWindowManager;
import com.example.myapplication01.utils.NetworkManager;
import com.example.myapplication01.utils.StorageManager;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes6.dex */
public class KeepAliveService extends Service {
    private static final String CHANNEL_ID = "ClipboardMonitorChannel";
    private static final int NOTIFICATION_ID = 1;
    private FloatingWindowManager floatingWindowManager;
    private NetworkManager networkManager;
    private ScheduledExecutorService scheduler;
    private StorageManager storageManager;

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        startForeground(1, createNotification());
        this.storageManager = new StorageManager(this);
        this.networkManager = NetworkManager.getInstance();
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        this.floatingWindowManager = FloatingWindowManager.getInstance();
        this.floatingWindowManager.init(this);
        this.floatingWindowManager.showFloatingWindow();
        this.scheduler.scheduleAtFixedRate(new Runnable() { // from class: com.example.myapplication01.service.KeepAliveService$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.retryFailedUploads();
            }
        }, 1L, 1L, TimeUnit.HOURS);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void retryFailedUploads() {
        if (this.storageManager.isEnabled()) {
            List<ClipData> history = this.storageManager.getHistory();
            for (final ClipData data : history) {
                if ("PENDING".equals(data.getStatus()) || "FAILED".equals(data.getStatus())) {
                    if (data.getRetryCount() < 5) {
                        data.incrementRetryCount();
                        this.storageManager.updateClip(data);
                        this.networkManager.sendReport(this.storageManager.getUrl(), this.storageManager.getMethod(), data, new NetworkManager.NetworkCallback() { // from class: com.example.myapplication01.service.KeepAliveService.1
                            @Override // com.example.myapplication01.utils.NetworkManager.NetworkCallback
                            public void onSuccess() {
                                data.setStatus("SENT");
                                KeepAliveService.this.storageManager.updateClip(data);
                            }

                            @Override // com.example.myapplication01.utils.NetworkManager.NetworkCallback
                            public void onFailure(String error) {
                                data.setStatus("FAILED");
                                KeepAliveService.this.storageManager.updateClip(data);
                            }
                        });
                    }
                }
            }
        }
    }

    private Notification createNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID).setContentTitle("剪切板监测运行中").setContentText("正在后台监测剪切板变化").setSmallIcon(R.mipmap.ic_launcher).setPriority(-1);
        return builder.build();
    }

    private void createNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "剪切板监测服务", 2);
        NotificationManager manager = (NotificationManager) getSystemService(NotificationManager.class);
        if (manager != null) {
            manager.createNotificationChannel(channel);
        }
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (this.floatingWindowManager != null) {
            this.floatingWindowManager.showFloatingWindow();
            return 1;
        }
        return 1;
    }

    @Override // android.app.Service
    public void onDestroy() {
        if (this.scheduler != null) {
            this.scheduler.shutdown();
        }
        if (this.floatingWindowManager != null) {
            this.floatingWindowManager.removeFloatingWindow();
        }
        super.onDestroy();
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return null;
    }
}
