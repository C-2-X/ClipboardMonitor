package com.example.myapplication01.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.example.myapplication01.utils.StorageManager;

/* JADX INFO: loaded from: classes6.dex */
public class BootReceiver extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            StorageManager storageManager = new StorageManager(context);
            if (storageManager.isEnabled()) {
                Intent serviceIntent = new Intent(context, (Class<?>) KeepAliveService.class);
                context.startForegroundService(serviceIntent);
            }
        }
    }
}
