package androidx.transition;

import android.view.ViewGroup;
import java.lang.reflect.Method;

/* JADX INFO: loaded from: classes.dex */
class ViewGroupUtils {
    private static Method sGetChildDrawingOrderMethod;
    private static boolean sGetChildDrawingOrderMethodFetched;
    private static boolean sTryHiddenSuppressLayout = true;

    static ViewGroupOverlayImpl getOverlay(ViewGroup group) {
        return new ViewGroupOverlayApi18(group);
    }

    static void suppressLayout(ViewGroup group, boolean suppress) {
        group.suppressLayout(suppress);
    }

    private static void hiddenSuppressLayout(ViewGroup group, boolean suppress) {
        if (sTryHiddenSuppressLayout) {
            try {
                group.suppressLayout(suppress);
            } catch (NoSuchMethodError e) {
                sTryHiddenSuppressLayout = false;
            }
        }
    }

    static int getChildDrawingOrder(ViewGroup viewGroup, int i) {
        return viewGroup.getChildDrawingOrder(i);
    }

    private ViewGroupUtils() {
    }
}
