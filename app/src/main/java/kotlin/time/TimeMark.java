package kotlin.time;

import androidx.constraintlayout.core.motion.utils.TypedValues;
import androidx.constraintlayout.widget.ConstraintLayout;
import kotlin.Metadata;
import okhttp3.HttpUrl;

/* JADX INFO: compiled from: TimeSource.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0002\bg\u0018\u00002\u00020\u0001J\u000f\u0010\u0002\u001a\u00020\u0003H&¢\u0006\u0004\b\u0004\u0010\u0005J\u0018\u0010\u0006\u001a\u00020\u00002\u0006\u0010\u0007\u001a\u00020\u0003H\u0096\u0002¢\u0006\u0004\b\b\u0010\tJ\u0018\u0010\n\u001a\u00020\u00002\u0006\u0010\u0007\u001a\u00020\u0003H\u0096\u0002¢\u0006\u0004\b\u000b\u0010\tJ\b\u0010\f\u001a\u00020\rH\u0016J\b\u0010\u000e\u001a\u00020\rH\u0016¨\u0006\u000f"}, d2 = {"Lkotlin/time/TimeMark;", HttpUrl.FRAGMENT_ENCODE_SET, "elapsedNow", "Lkotlin/time/Duration;", "elapsedNow-UwyO8pc", "()J", "plus", TypedValues.TransitionType.S_DURATION, "plus-LRDsOJo", "(J)Lkotlin/time/TimeMark;", "minus", "minus-LRDsOJo", "hasPassedNow", HttpUrl.FRAGMENT_ENCODE_SET, "hasNotPassedNow", "kotlin-stdlib"}, k = 1, mv = {2, 2, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
public interface TimeMark {
    /* JADX INFO: renamed from: elapsedNow-UwyO8pc */
    long mo1499elapsedNowUwyO8pc();

    boolean hasNotPassedNow();

    boolean hasPassedNow();

    /* JADX INFO: renamed from: minus-LRDsOJo */
    TimeMark mo1500minusLRDsOJo(long duration);

    /* JADX INFO: renamed from: plus-LRDsOJo */
    TimeMark mo1502plusLRDsOJo(long duration);

    /* JADX INFO: compiled from: TimeSource.kt */
    @Metadata(k = 3, mv = {2, 2, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
    public static final class DefaultImpls {
        /* JADX INFO: renamed from: plus-LRDsOJo, reason: not valid java name */
        public static TimeMark m1620plusLRDsOJo(TimeMark $this, long duration) {
            return new AdjustedTimeMark($this, duration, null);
        }

        /* JADX INFO: renamed from: minus-LRDsOJo, reason: not valid java name */
        public static TimeMark m1619minusLRDsOJo(TimeMark $this, long duration) {
            return $this.mo1502plusLRDsOJo(Duration.m1553unaryMinusUwyO8pc(duration));
        }

        public static boolean hasPassedNow(TimeMark $this) {
            return !Duration.m1535isNegativeimpl($this.mo1499elapsedNowUwyO8pc());
        }

        public static boolean hasNotPassedNow(TimeMark $this) {
            return Duration.m1535isNegativeimpl($this.mo1499elapsedNowUwyO8pc());
        }
    }
}
