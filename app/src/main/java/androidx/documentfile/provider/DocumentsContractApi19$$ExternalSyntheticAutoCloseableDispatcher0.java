package androidx.documentfile.provider;

import android.content.res.TypedArray;
import java.util.concurrent.ExecutorService;

/* JADX INFO: compiled from: D8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public final /* synthetic */ class DocumentsContractApi19$$ExternalSyntheticAutoCloseableDispatcher0 {
    public static /* synthetic */ void m(Object obj) throws Exception {
        if (obj instanceof AutoCloseable) {
            ((AutoCloseable) obj).close();
            return;
        }
        if (obj instanceof ExecutorService) {
            DocumentsContractApi19$$ExternalSyntheticAutoCloseableForwarder1.m((ExecutorService) obj);
        } else if (obj instanceof TypedArray) {
            ((TypedArray) obj).recycle();
        } else {
            DocumentsContractApi19$$ExternalSyntheticThrowIAE2.m(obj);
        }
    }
}
