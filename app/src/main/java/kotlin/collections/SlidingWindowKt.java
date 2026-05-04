package kotlin.collections;

import androidx.constraintlayout.widget.ConstraintLayout;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.RestrictedSuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequenceScope;
import kotlin.sequences.SequencesKt;
import okhttp3.HttpUrl;

/* JADX INFO: compiled from: SlidingWindow.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(d1 = {"\u0000,\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010(\n\u0002\b\u0002\u001a\u0018\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0003H\u0000\u001aD\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\b0\u00070\u0006\"\u0004\b\u0000\u0010\b*\b\u0012\u0004\u0012\u0002H\b0\u00062\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00032\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\nH\u0000\u001aH\u0010\f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\b0\u00070\r\"\u0004\b\u0000\u0010\b2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u0002H\b0\r2\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00032\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\nH\u0000¨\u0006\u000f"}, d2 = {"checkWindowSizeStep", HttpUrl.FRAGMENT_ENCODE_SET, "size", HttpUrl.FRAGMENT_ENCODE_SET, "step", "windowedSequence", "Lkotlin/sequences/Sequence;", HttpUrl.FRAGMENT_ENCODE_SET, "T", "partialWindows", HttpUrl.FRAGMENT_ENCODE_SET, "reuseBuffer", "windowedIterator", HttpUrl.FRAGMENT_ENCODE_SET, "iterator", "kotlin-stdlib"}, k = 2, mv = {2, 2, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
public final class SlidingWindowKt {
    public static final void checkWindowSizeStep(int size, int step) {
        if (!(size > 0 && step > 0)) {
            throw new IllegalArgumentException((size != step ? "Both size " + size + " and step " + step + " must be greater than zero." : "size " + size + " must be greater than zero.").toString());
        }
    }

    public static final <T> Sequence<List<T>> windowedSequence(final Sequence<? extends T> sequence, final int i, final int i2, final boolean z, final boolean z2) {
        Intrinsics.checkNotNullParameter(sequence, "<this>");
        checkWindowSizeStep(i, i2);
        return new Sequence<List<? extends T>>() { // from class: kotlin.collections.SlidingWindowKt$windowedSequence$$inlined$Sequence$1
            @Override // kotlin.sequences.Sequence
            public Iterator<List<? extends T>> iterator() {
                return SlidingWindowKt.windowedIterator(sequence.iterator(), i, i2, z, z2);
            }
        };
    }

    /* JADX INFO: Add missing generic type declarations: [T] */
    /* JADX INFO: renamed from: kotlin.collections.SlidingWindowKt$windowedIterator$1, reason: invalid class name */
    /* JADX INFO: compiled from: SlidingWindow.kt */
    @Metadata(d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010 \u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u00040\u0003H\n"}, d2 = {"<anonymous>", HttpUrl.FRAGMENT_ENCODE_SET, "T", "Lkotlin/sequences/SequenceScope;", HttpUrl.FRAGMENT_ENCODE_SET}, k = 3, mv = {2, 2, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
    @DebugMetadata(c = "kotlin.collections.SlidingWindowKt$windowedIterator$1", f = "SlidingWindow.kt", i = {0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 3, 3, 3, 3, 4, 4, 4, 4}, l = {34, 40, ConstraintLayout.LayoutParams.Table.LAYOUT_EDITOR_ABSOLUTEX, ConstraintLayout.LayoutParams.Table.LAYOUT_GONE_MARGIN_BASELINE, 58}, m = "invokeSuspend", n = {"$this$iterator", "buffer", "e", "bufferInitialCapacity", "gap", "skip", "$this$iterator", "buffer", "bufferInitialCapacity", "gap", "skip", "$this$iterator", "buffer", "e", "bufferInitialCapacity", "gap", "$this$iterator", "buffer", "bufferInitialCapacity", "gap", "$this$iterator", "buffer", "bufferInitialCapacity", "gap"}, s = {"L$0", "L$1", "L$3", "I$0", "I$1", "I$2", "L$0", "L$1", "I$0", "I$1", "I$2", "L$0", "L$1", "L$3", "I$0", "I$1", "L$0", "L$1", "I$0", "I$1", "L$0", "L$1", "I$0", "I$1"})
    static final class AnonymousClass1<T> extends RestrictedSuspendLambda implements Function2<SequenceScope<? super List<? extends T>>, Continuation<? super Unit>, Object> {
        final /* synthetic */ Iterator<T> $iterator;
        final /* synthetic */ boolean $partialWindows;
        final /* synthetic */ boolean $reuseBuffer;
        final /* synthetic */ int $size;
        final /* synthetic */ int $step;
        int I$0;
        int I$1;
        int I$2;
        private /* synthetic */ Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        AnonymousClass1(int i, int i2, Iterator<? extends T> it, boolean z, boolean z2, Continuation<? super AnonymousClass1> continuation) {
            super(2, continuation);
            this.$size = i;
            this.$step = i2;
            this.$iterator = it;
            this.$reuseBuffer = z;
            this.$partialWindows = z2;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            AnonymousClass1 anonymousClass1 = new AnonymousClass1(this.$size, this.$step, this.$iterator, this.$reuseBuffer, this.$partialWindows, continuation);
            anonymousClass1.L$0 = obj;
            return anonymousClass1;
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(SequenceScope<? super List<? extends T>> sequenceScope, Continuation<? super Unit> continuation) {
            return ((AnonymousClass1) create(sequenceScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        /* JADX WARN: Removed duplicated region for block: B:16:0x00ac  */
        /* JADX WARN: Removed duplicated region for block: B:27:0x00eb  */
        /* JADX WARN: Removed duplicated region for block: B:28:0x00ef  */
        /* JADX WARN: Removed duplicated region for block: B:38:0x0138 A[RETURN] */
        /* JADX WARN: Removed duplicated region for block: B:39:0x0139  */
        /* JADX WARN: Removed duplicated region for block: B:44:0x0153  */
        /* JADX WARN: Removed duplicated region for block: B:60:0x01b0 A[PHI: r1 r2 r4 r5 r7 r14
  0x01b0: PHI (r1v11 kotlin.sequences.SequenceScope) = (r1v8 kotlin.sequences.SequenceScope), (r1v12 kotlin.sequences.SequenceScope) binds: [B:59:0x01ae, B:69:0x01e3] A[DONT_GENERATE, DONT_INLINE]
  0x01b0: PHI (r2v17 java.lang.Object) = (r2v13 java.lang.Object), (r2v19 java.lang.Object) binds: [B:59:0x01ae, B:69:0x01e3] A[DONT_GENERATE, DONT_INLINE]
  0x01b0: PHI (r4v17 'bufferInitialCapacity' int) = (r4v13 'bufferInitialCapacity' int), (r4v18 'bufferInitialCapacity' int) binds: [B:59:0x01ae, B:69:0x01e3] A[DONT_GENERATE, DONT_INLINE]
  0x01b0: PHI (r5v16 'bufferInitialCapacity' int) = (r5v12 'bufferInitialCapacity' int), (r5v17 'bufferInitialCapacity' int) binds: [B:59:0x01ae, B:69:0x01e3] A[DONT_GENERATE, DONT_INLINE]
  0x01b0: PHI (r7v19 'buffer' kotlin.collections.RingBuffer) = (r7v10 'buffer' kotlin.collections.RingBuffer), (r7v20 'buffer' kotlin.collections.RingBuffer) binds: [B:59:0x01ae, B:69:0x01e3] A[DONT_GENERATE, DONT_INLINE]
  0x01b0: PHI (r14v10 kotlin.collections.SlidingWindowKt$windowedIterator$1<T>) = 
  (r14v7 kotlin.collections.SlidingWindowKt$windowedIterator$1<T>)
  (r14v11 kotlin.collections.SlidingWindowKt$windowedIterator$1<T>)
 binds: [B:59:0x01ae, B:69:0x01e3] A[DONT_GENERATE, DONT_INLINE]] */
        /* JADX WARN: Removed duplicated region for block: B:62:0x01b8  */
        /* JADX WARN: Removed duplicated region for block: B:70:0x01e9  */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:24:0x00df -> B:25:0x00e7). Please report as a decompilation issue!!! */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:56:0x019e -> B:57:0x01a3). Please report as a decompilation issue!!! */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:67:0x01e0 -> B:69:0x01e3). Please report as a decompilation issue!!! */
        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public final java.lang.Object invokeSuspend(java.lang.Object r14) throws java.lang.Throwable {
            /*
                Method dump skipped, instruction units count: 554
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: kotlin.collections.SlidingWindowKt.AnonymousClass1.invokeSuspend(java.lang.Object):java.lang.Object");
        }
    }

    public static final <T> Iterator<List<T>> windowedIterator(Iterator<? extends T> iterator, int size, int step, boolean partialWindows, boolean reuseBuffer) {
        Intrinsics.checkNotNullParameter(iterator, "iterator");
        return !iterator.hasNext() ? EmptyIterator.INSTANCE : SequencesKt.iterator(new AnonymousClass1(size, step, iterator, reuseBuffer, partialWindows, null));
    }
}
