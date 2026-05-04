package com.example.myapplication01;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.core.motion.utils.TypedValues;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.location.LocationRequestCompat;
import com.example.myapplication01.service.KeepAliveService;
import com.example.myapplication01.utils.StorageManager;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import java.util.List;

/* JADX INFO: loaded from: classes3.dex */
public class MainActivity extends AppCompatActivity {
    private Button btnHistory;
    private Button btnSave;
    private TextInputEditText etUrl;
    private RadioButton rbGet;
    private RadioButton rbPost;
    private RadioGroup rgMethod;
    private StorageManager storageManager;
    private SwitchMaterial switchFloatingWindow;
    private SwitchMaterial switchMonitor;
    private TextView tvStatus;

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.storageManager = new StorageManager(this);
        initViews();
        loadConfig();
        checkPermissions();
    }

    private void initViews() {
        this.switchMonitor = (SwitchMaterial) findViewById(R.id.switch_monitor);
        this.switchFloatingWindow = (SwitchMaterial) findViewById(R.id.switch_floating_window);
        this.etUrl = (TextInputEditText) findViewById(R.id.et_url);
        this.rgMethod = (RadioGroup) findViewById(R.id.rg_method);
        this.rbPost = (RadioButton) findViewById(R.id.rb_post);
        this.rbGet = (RadioButton) findViewById(R.id.rb_get);
        this.btnSave = (Button) findViewById(R.id.btn_save);
        this.btnHistory = (Button) findViewById(R.id.btn_history);
        this.tvStatus = (TextView) findViewById(R.id.tv_status);
        this.btnSave.setOnClickListener(new View.OnClickListener() { // from class: com.example.myapplication01.MainActivity$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                this.f$0.m61lambda$initViews$0$comexamplemyapplication01MainActivity(view);
            }
        });
        this.btnHistory.setOnClickListener(new View.OnClickListener() { // from class: com.example.myapplication01.MainActivity$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                this.f$0.m62lambda$initViews$1$comexamplemyapplication01MainActivity(view);
            }
        });
        this.switchMonitor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.example.myapplication01.MainActivity$$ExternalSyntheticLambda3
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                this.f$0.m63lambda$initViews$2$comexamplemyapplication01MainActivity(compoundButton, z);
            }
        });
        this.switchFloatingWindow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.example.myapplication01.MainActivity$$ExternalSyntheticLambda4
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                this.f$0.m64lambda$initViews$3$comexamplemyapplication01MainActivity(compoundButton, z);
            }
        });
    }

    /* JADX INFO: renamed from: lambda$initViews$0$com-example-myapplication01-MainActivity, reason: not valid java name */
    /* synthetic */ void m61lambda$initViews$0$comexamplemyapplication01MainActivity(View v) {
        saveConfig();
    }

    /* JADX INFO: renamed from: lambda$initViews$1$com-example-myapplication01-MainActivity, reason: not valid java name */
    /* synthetic */ void m62lambda$initViews$1$comexamplemyapplication01MainActivity(View v) {
        Intent intent = new Intent(this, (Class<?>) HistoryActivity.class);
        startActivity(intent);
    }

    /* JADX INFO: renamed from: lambda$initViews$2$com-example-myapplication01-MainActivity, reason: not valid java name */
    /* synthetic */ void m63lambda$initViews$2$comexamplemyapplication01MainActivity(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            if (checkAccessibilityPermission()) {
                startMonitoring();
                return;
            } else {
                this.switchMonitor.setChecked(false);
                showAccessibilityDialog();
                return;
            }
        }
        stopMonitoring();
    }

    /* JADX INFO: renamed from: lambda$initViews$3$com-example-myapplication01-MainActivity, reason: not valid java name */
    /* synthetic */ void m64lambda$initViews$3$comexamplemyapplication01MainActivity(CompoundButton buttonView, boolean isChecked) {
        if (!isChecked) {
            this.storageManager.setFloatingWindowEnabled(false);
            if (this.storageManager.isEnabled()) {
                startMonitoring();
                return;
            }
            return;
        }
        if (!checkOverlayPermission()) {
            this.switchFloatingWindow.setChecked(false);
            requestOverlayPermission();
        } else {
            this.storageManager.setFloatingWindowEnabled(true);
            if (this.storageManager.isEnabled()) {
                startMonitoring();
            }
        }
    }

    private void loadConfig() {
        this.etUrl.setText(this.storageManager.getUrl());
        String method = this.storageManager.getMethod();
        if ("GET".equals(method)) {
            this.rbGet.setChecked(true);
        } else {
            this.rbPost.setChecked(true);
        }
        boolean enabled = this.storageManager.isEnabled();
        this.switchMonitor.setChecked(enabled);
        this.switchFloatingWindow.setChecked(this.storageManager.isFloatingWindowEnabled());
        updateStatus(enabled);
    }

    private void saveConfig() {
        String url = this.etUrl.getText().toString().trim();
        String method = this.rbPost.isChecked() ? "POST" : "GET";
        boolean enabled = this.switchMonitor.isChecked();
        boolean floatEnabled = this.switchFloatingWindow.isChecked();
        this.storageManager.saveConfig(url, method, enabled);
        this.storageManager.setFloatingWindowEnabled(floatEnabled);
        Toast.makeText(this, "配置已保存", 0).show();
        if (enabled) {
            startMonitoring();
        } else {
            stopMonitoring();
        }
    }

    private boolean checkOverlayPermission() {
        return Settings.canDrawOverlays(this);
    }

    private void requestOverlayPermission() {
        new AlertDialog.Builder(this).setTitle("需要悬浮窗权限").setMessage("请开启悬浮窗权限以显示运行状态。").setPositiveButton("去设置", new DialogInterface.OnClickListener() { // from class: com.example.myapplication01.MainActivity$$ExternalSyntheticLambda5
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                this.f$0.m65x33c1b3c6(dialogInterface, i);
            }
        }).setNegativeButton("取消", (DialogInterface.OnClickListener) null).show();
    }

    /* JADX INFO: renamed from: lambda$requestOverlayPermission$4$com-example-myapplication01-MainActivity, reason: not valid java name */
    /* synthetic */ void m65x33c1b3c6(DialogInterface dialog, int which) {
        Intent intent = new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION", Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, LocationRequestCompat.QUALITY_BALANCED_POWER_ACCURACY);
    }

    private void startMonitoring() {
        if (!checkAccessibilityPermission()) {
            showAccessibilityDialog();
            return;
        }
        Intent serviceIntent = new Intent(this, (Class<?>) KeepAliveService.class);
        startForegroundService(serviceIntent);
        updateStatus(true);
    }

    private void stopMonitoring() {
        Intent serviceIntent = new Intent(this, (Class<?>) KeepAliveService.class);
        stopService(serviceIntent);
        updateStatus(false);
    }

    private void updateStatus(boolean running) {
        TextView textView = this.tvStatus;
        if (running) {
            textView.setText("状态: 运行中");
            this.tvStatus.setTextColor(ContextCompat.getColor(this, R.color.teal_700));
        } else {
            textView.setText("状态: 已停止");
            this.tvStatus.setTextColor(ContextCompat.getColor(this, android.R.color.darker_gray));
        }
    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= 33 && ContextCompat.checkSelfPermission(this, "android.permission.POST_NOTIFICATIONS") != 0) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.POST_NOTIFICATIONS"}, TypedValues.TYPE_TARGET);
        }
        PowerManager pm = (PowerManager) getSystemService("power");
        if (!pm.isIgnoringBatteryOptimizations(getPackageName())) {
            new AlertDialog.Builder(this).setTitle("忽略电池优化").setMessage("为确保剪切板监测在后台运行，请允许应用忽略电池优化。").setPositiveButton("设置", new DialogInterface.OnClickListener() { // from class: com.example.myapplication01.MainActivity$$ExternalSyntheticLambda6
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    this.f$0.m60x1072fd79(dialogInterface, i);
                }
            }).setNegativeButton("取消", (DialogInterface.OnClickListener) null).show();
        }
    }

    /* JADX INFO: renamed from: lambda$checkPermissions$5$com-example-myapplication01-MainActivity, reason: not valid java name */
    /* synthetic */ void m60x1072fd79(DialogInterface dialog, int which) {
        Intent intent = new Intent("android.settings.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS");
        intent.setData(Uri.parse("package:" + getPackageName()));
        try {
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkAccessibilityPermission() {
        AccessibilityManager am = (AccessibilityManager) getSystemService("accessibility");
        List<AccessibilityServiceInfo> enabledServices = am.getEnabledAccessibilityServiceList(16);
        for (AccessibilityServiceInfo info : enabledServices) {
            if (info.getId().contains(getPackageName())) {
                return true;
            }
        }
        return false;
    }

    private void showAccessibilityDialog() {
        new AlertDialog.Builder(this).setTitle("需要无障碍权限").setMessage("此应用需要无障碍权限以在后台监测剪切板变化，请在设置中开启。").setPositiveButton("去设置", new DialogInterface.OnClickListener() { // from class: com.example.myapplication01.MainActivity$$ExternalSyntheticLambda0
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                this.f$0.m66xe0ae435(dialogInterface, i);
            }
        }).setNegativeButton("取消", (DialogInterface.OnClickListener) null).show();
    }

    /* JADX INFO: renamed from: lambda$showAccessibilityDialog$6$com-example-myapplication01-MainActivity, reason: not valid java name */
    /* synthetic */ void m66xe0ae435(DialogInterface dialog, int which) {
        Intent intent = new Intent("android.settings.ACCESSIBILITY_SETTINGS");
        startActivity(intent);
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        if (this.storageManager.isEnabled() && checkAccessibilityPermission()) {
            updateStatus(true);
        } else if (this.storageManager.isEnabled() && !checkAccessibilityPermission()) {
            updateStatus(false);
        }
    }
}
