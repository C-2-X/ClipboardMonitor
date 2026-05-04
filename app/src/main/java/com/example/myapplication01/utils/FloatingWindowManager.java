package com.example.myapplication01.utils;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.myapplication01.ClipboardProxyActivity;
import com.example.myapplication01.R;

/* JADX INFO: loaded from: classes5.dex */
public class FloatingWindowManager {
    private static final long AUTO_COLLAPSE_DELAY = 5000;
    private static FloatingWindowManager instance;
    private Runnable autoCollapseRunnable;
    private OnFloatingWindowClickListener clickListener;
    private FrameLayout container;
    private Context context;
    private View floatingView;
    private LinearLayout layoutExpanded;
    private WindowManager.LayoutParams layoutParams;
    private StorageManager storageManager;
    private TextView tvUpload;
    private View viewCollapsed;
    private WindowManager windowManager;
    private Handler handler = new Handler(Looper.getMainLooper());
    private boolean isViewAttached = false;
    private boolean isExpanded = false;
    private boolean isOnLeftEdge = true;

    public interface OnFloatingWindowClickListener {
        void onFloatingWindowClick();
    }

    public void setOnFloatingWindowClickListener(OnFloatingWindowClickListener listener) {
        this.clickListener = listener;
    }

    private FloatingWindowManager() {
    }

    public static synchronized FloatingWindowManager getInstance() {
        if (instance == null) {
            instance = new FloatingWindowManager();
        }
        return instance;
    }

    public void init(Context context) {
        if (this.context == null) {
            this.context = context.getApplicationContext();
            this.storageManager = new StorageManager(this.context);
            this.windowManager = (WindowManager) this.context.getSystemService("window");
        }
    }

    public void showFloatingWindow() {
        if (this.storageManager.isFloatingWindowEnabled() && Settings.canDrawOverlays(this.context) && !this.isViewAttached) {
            createFloatingView();
            try {
                this.windowManager.addView(this.floatingView, this.layoutParams);
                this.isViewAttached = true;
                snapToEdge(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void removeFloatingWindow() {
        if (this.isViewAttached && this.floatingView != null) {
            try {
                cancelAutoCollapse();
                this.windowManager.removeView(this.floatingView);
                this.isViewAttached = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void launchClipboardProxy() {
        if (this.context == null) {
            return;
        }
        Intent intent = new Intent(this.context, (Class<?>) ClipboardProxyActivity.class);
        intent.addFlags(268435456);
        this.context.startActivity(intent);
    }

    private void startAutoCollapseTimer() {
        cancelAutoCollapse();
        this.autoCollapseRunnable = new Runnable() { // from class: com.example.myapplication01.utils.FloatingWindowManager.1
            @Override // java.lang.Runnable
            public void run() {
                if (FloatingWindowManager.this.isExpanded) {
                    FloatingWindowManager.this.collapseView();
                }
            }
        };
        this.handler.postDelayed(this.autoCollapseRunnable, AUTO_COLLAPSE_DELAY);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelAutoCollapse() {
        if (this.autoCollapseRunnable != null) {
            this.handler.removeCallbacks(this.autoCollapseRunnable);
            this.autoCollapseRunnable = null;
        }
    }

    public void delayedCollapseView() {
        cancelAutoCollapse();
        this.autoCollapseRunnable = new Runnable() { // from class: com.example.myapplication01.utils.FloatingWindowManager.2
            @Override // java.lang.Runnable
            public void run() {
                if (FloatingWindowManager.this.isExpanded) {
                    FloatingWindowManager.this.collapseView();
                }
            }
        };
        this.handler.postDelayed(this.autoCollapseRunnable, AUTO_COLLAPSE_DELAY);
    }

    private void createFloatingView() {
        if (this.floatingView == null) {
            this.floatingView = LayoutInflater.from(this.context).inflate(R.layout.window_floating, (ViewGroup) null);
            this.container = (FrameLayout) this.floatingView.findViewById(R.id.floating_container);
            this.viewCollapsed = this.floatingView.findViewById(R.id.view_collapsed);
            this.layoutExpanded = (LinearLayout) this.floatingView.findViewById(R.id.layout_expanded);
            this.tvUpload = (TextView) this.floatingView.findViewById(R.id.tv_upload);
            this.layoutParams = new WindowManager.LayoutParams(-2, -2, 2038, 40, -3);
            this.layoutParams.gravity = 8388659;
            this.layoutParams.x = this.storageManager.getFloatingWindowX();
            this.layoutParams.y = this.storageManager.getFloatingWindowY();
            this.floatingView.setOnTouchListener(new View.OnTouchListener() { // from class: com.example.myapplication01.utils.FloatingWindowManager.3
                private static final int DRAG_THRESHOLD = 15;
                private float initialTouchX;
                private float initialTouchY;
                private int initialX;
                private int initialY;
                private boolean isDragging = false;

                @Override // android.view.View.OnTouchListener
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case 0:
                            this.initialX = FloatingWindowManager.this.layoutParams.x;
                            this.initialY = FloatingWindowManager.this.layoutParams.y;
                            this.initialTouchX = event.getRawX();
                            this.initialTouchY = event.getRawY();
                            this.isDragging = false;
                            return true;
                        case 1:
                            boolean z = this.isDragging;
                            FloatingWindowManager floatingWindowManager = FloatingWindowManager.this;
                            if (!z) {
                                boolean z2 = floatingWindowManager.isExpanded;
                                FloatingWindowManager floatingWindowManager2 = FloatingWindowManager.this;
                                if (z2) {
                                    floatingWindowManager2.cancelAutoCollapse();
                                    FloatingWindowManager.this.updateStatus("读取中...", R.color.teal_200);
                                    if (FloatingWindowManager.this.clickListener != null) {
                                        FloatingWindowManager.this.clickListener.onFloatingWindowClick();
                                    }
                                } else {
                                    floatingWindowManager2.expandView();
                                }
                            } else {
                                floatingWindowManager.storageManager.saveFloatingWindowPosition(FloatingWindowManager.this.layoutParams.x, FloatingWindowManager.this.layoutParams.y);
                                FloatingWindowManager.this.snapToEdge(true);
                            }
                            return true;
                        case 2:
                            float dx = event.getRawX() - this.initialTouchX;
                            float dy = event.getRawY() - this.initialTouchY;
                            if (Math.abs(dx) > 15.0f || Math.abs(dy) > 15.0f) {
                                this.isDragging = true;
                            }
                            if (this.isDragging) {
                                FloatingWindowManager.this.layoutParams.x = this.initialX + ((int) dx);
                                FloatingWindowManager.this.layoutParams.y = this.initialY + ((int) dy);
                                FloatingWindowManager.this.windowManager.updateViewLayout(FloatingWindowManager.this.floatingView, FloatingWindowManager.this.layoutParams);
                            }
                            return true;
                        default:
                            return false;
                    }
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void expandView() {
        if (this.isViewAttached) {
            this.isExpanded = true;
            this.viewCollapsed.setVisibility(8);
            this.layoutExpanded.setVisibility(0);
            updateStatus("点击上传", R.color.teal_200);
            updateBackgroundCorners();
            startAutoCollapseTimer();
        }
    }

    public void collapseView() {
        if (this.isViewAttached) {
            cancelAutoCollapse();
            this.isExpanded = false;
            this.layoutExpanded.setVisibility(8);
            this.viewCollapsed.setVisibility(0);
            snapToEdge(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void snapToEdge(boolean animate) {
        int targetX;
        if (!this.isViewAttached || this.floatingView == null) {
            return;
        }
        int screenWidth = this.context.getResources().getDisplayMetrics().widthPixels;
        int currentX = this.layoutParams.x;
        if ((this.floatingView.getWidth() / 2) + currentX < screenWidth / 2) {
            targetX = 0;
            this.isOnLeftEdge = true;
        } else {
            int targetX2 = getCurrentViewWidth();
            targetX = screenWidth - targetX2;
            this.isOnLeftEdge = false;
        }
        if (animate) {
            ValueAnimator animator = ValueAnimator.ofInt(currentX, targetX);
            animator.setDuration(200L);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.example.myapplication01.utils.FloatingWindowManager$$ExternalSyntheticLambda0
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    this.f$0.m70x30631b8b(valueAnimator);
                }
            });
            animator.start();
        } else {
            this.layoutParams.x = targetX;
            try {
                this.windowManager.updateViewLayout(this.floatingView, this.layoutParams);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        updateBackgroundCorners();
        this.storageManager.saveFloatingWindowPosition(targetX, this.layoutParams.y);
    }

    /* JADX INFO: renamed from: lambda$snapToEdge$0$com-example-myapplication01-utils-FloatingWindowManager, reason: not valid java name */
    /* synthetic */ void m70x30631b8b(ValueAnimator animation) {
        if (this.isViewAttached) {
            this.layoutParams.x = ((Integer) animation.getAnimatedValue()).intValue();
            try {
                this.windowManager.updateViewLayout(this.floatingView, this.layoutParams);
            } catch (Exception e) {
            }
        }
    }

    private int getCurrentViewWidth() {
        if (this.isExpanded && this.layoutExpanded != null && this.layoutExpanded.getVisibility() == 0) {
            if (this.layoutExpanded.getWidth() > 0) {
                return this.layoutExpanded.getWidth();
            }
            return 120;
        }
        if (this.viewCollapsed == null || this.viewCollapsed.getWidth() <= 0) {
            return 12;
        }
        return this.viewCollapsed.getWidth();
    }

    private void updateBackgroundCorners() {
    }

    public void updateStatus(final String status, int colorResId) {
        if (!this.isViewAttached || this.tvUpload == null) {
            return;
        }
        this.handler.post(new Runnable() { // from class: com.example.myapplication01.utils.FloatingWindowManager$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.m71x65e05ae5(status);
            }
        });
    }

    /* JADX INFO: renamed from: lambda$updateStatus$1$com-example-myapplication01-utils-FloatingWindowManager, reason: not valid java name */
    /* synthetic */ void m71x65e05ae5(String status) {
        this.tvUpload.setText(status);
    }

    public boolean isExpanded() {
        return this.isExpanded;
    }
}
