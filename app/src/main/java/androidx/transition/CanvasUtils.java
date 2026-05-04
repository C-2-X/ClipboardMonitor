package androidx.transition;

import android.graphics.Canvas;
import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes.dex */
class CanvasUtils {
    private static Method sInorderBarrierMethod;
    private static boolean sOrderMethodsFetched;
    private static Method sReorderBarrierMethod;

    static void enableZ(Canvas canvas, boolean enable) {
        if (enable) {
            canvas.enableZ();
        } else {
            canvas.disableZ();
        }
    }

    private CanvasUtils() {
    }
}
