package kotlin.time;

import androidx.constraintlayout.widget.ConstraintLayout;
import kotlin.Metadata;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import okhttp3.HttpUrl;

/* JADX INFO: compiled from: Instant.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0014\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0003\u0018\u0000 !2\u00020\u0001:\u0001!B?\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u0012\u0006\u0010\u0007\u001a\u00020\u0003\u0012\u0006\u0010\b\u001a\u00020\u0003\u0012\u0006\u0010\t\u001a\u00020\u0003¢\u0006\u0004\b\n\u0010\u000bJW\u0010\u0014\u001a\u0002H\u0015\"\u0004\b\u0000\u0010\u00152\u0006\u0010\u0016\u001a\u00020\u000326\u0010\u0017\u001a2\u0012\u0013\u0012\u00110\u0019¢\u0006\f\b\u001a\u0012\b\b\u001b\u0012\u0004\b\b(\u001c\u0012\u0013\u0012\u00110\u0003¢\u0006\f\b\u001a\u0012\b\b\u001b\u0012\u0004\b\b(\u001d\u0012\u0004\u0012\u0002H\u00150\u0018H\u0086\bø\u0001\u0000¢\u0006\u0002\u0010\u001eJ\b\u0010\u001f\u001a\u00020 H\u0016R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\rR\u0011\u0010\u0005\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\rR\u0011\u0010\u0006\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\rR\u0011\u0010\u0007\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\rR\u0011\u0010\b\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\rR\u0011\u0010\t\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\r\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006\""}, d2 = {"Lkotlin/time/UnboundLocalDateTime;", HttpUrl.FRAGMENT_ENCODE_SET, "year", HttpUrl.FRAGMENT_ENCODE_SET, "month", "day", "hour", "minute", "second", "nanosecond", "<init>", "(IIIIIII)V", "getYear", "()I", "getMonth", "getDay", "getHour", "getMinute", "getSecond", "getNanosecond", "toInstant", "T", "offsetSeconds", "buildInstant", "Lkotlin/Function2;", HttpUrl.FRAGMENT_ENCODE_SET, "Lkotlin/ParameterName;", "name", "epochSeconds", "nanosecondOfSecond", "(ILkotlin/jvm/functions/Function2;)Ljava/lang/Object;", "toString", HttpUrl.FRAGMENT_ENCODE_SET, "Companion", "kotlin-stdlib"}, k = 1, mv = {2, 2, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
final class UnboundLocalDateTime {

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private final int day;
    private final int hour;
    private final int minute;
    private final int month;
    private final int nanosecond;
    private final int second;
    private final int year;

    public UnboundLocalDateTime(int year, int month, int day, int hour, int minute, int second, int nanosecond) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        this.nanosecond = nanosecond;
    }

    public final int getYear() {
        return this.year;
    }

    public final int getMonth() {
        return this.month;
    }

    public final int getDay() {
        return this.day;
    }

    public final int getHour() {
        return this.hour;
    }

    public final int getMinute() {
        return this.minute;
    }

    public final int getSecond() {
        return this.second;
    }

    public final int getNanosecond() {
        return this.nanosecond;
    }

    public final <T> T toInstant(int offsetSeconds, Function2<? super Long, ? super Integer, ? extends T> buildInstant) {
        long total;
        Intrinsics.checkNotNullParameter(buildInstant, "buildInstant");
        UnboundLocalDateTime $this$toInstant_u24lambda_u241 = this;
        long y = $this$toInstant_u24lambda_u241.getYear();
        long total2 = ((long) 365) * y;
        if (y >= 0) {
            total = total2 + (((((long) 3) + y) / ((long) 4)) - ((((long) 99) + y) / ((long) 100))) + ((((long) 399) + y) / ((long) 400));
        } else {
            total = total2 - (((y / ((long) (-4))) - (y / ((long) (-100)))) + (y / ((long) (-400))));
        }
        long total3 = total + ((long) ((($this$toInstant_u24lambda_u241.getMonth() * 367) - 362) / 12)) + ((long) ($this$toInstant_u24lambda_u241.getDay() - 1));
        if ($this$toInstant_u24lambda_u241.getMonth() > 2) {
            total3--;
            if (!InstantKt.isLeapYear($this$toInstant_u24lambda_u241.getYear())) {
                total3--;
            }
        }
        long epochDays = total3 - ((long) 719528);
        int daySeconds = ($this$toInstant_u24lambda_u241.getHour() * 3600) + ($this$toInstant_u24lambda_u241.getMinute() * 60) + $this$toInstant_u24lambda_u241.getSecond();
        long epochSeconds = ((((long) 86400) * epochDays) + ((long) daySeconds)) - ((long) offsetSeconds);
        return buildInstant.invoke(Long.valueOf(epochSeconds), Integer.valueOf(getNanosecond()));
    }

    public String toString() {
        return "UnboundLocalDateTime(" + this.year + '-' + this.month + '-' + this.day + ' ' + this.hour + ':' + this.minute + ':' + this.second + '.' + this.nanosecond + ')';
    }

    /* JADX INFO: compiled from: Instant.kt */
    @Metadata(d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002¢\u0006\u0004\b\u0002\u0010\u0003J\u000e\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007¨\u0006\b"}, d2 = {"Lkotlin/time/UnboundLocalDateTime$Companion;", HttpUrl.FRAGMENT_ENCODE_SET, "<init>", "()V", "fromInstant", "Lkotlin/time/UnboundLocalDateTime;", "instant", "Lkotlin/time/Instant;", "kotlin-stdlib"}, k = 1, mv = {2, 2, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final UnboundLocalDateTime fromInstant(Instant instant) {
            int month;
            int i;
            long doyEst;
            Intrinsics.checkNotNullParameter(instant, "instant");
            long localSecond = instant.getEpochSeconds();
            long epochDays = localSecond / 86400;
            if ((localSecond ^ 86400) < 0 && epochDays * 86400 != localSecond) {
                epochDays--;
            }
            long j = localSecond % 86400;
            int secsOfDay = (int) (j + (((((-j) | j) & (j ^ 86400)) >> 63) & 86400));
            long zeroDay = (((long) 719528) + epochDays) - ((long) 60);
            long adjust = 0;
            if (zeroDay >= 0) {
                month = 0;
                i = 400;
            } else {
                long j2 = 146097;
                long adjustCycles = ((zeroDay + 1) / j2) - 1;
                month = 0;
                i = 400;
                adjust = adjustCycles * ((long) 400);
                zeroDay += (-adjustCycles) * j2;
            }
            long j3 = i;
            long yearEst = ((j3 * zeroDay) + ((long) 591)) / ((long) 146097);
            long j4 = 365;
            long j5 = j4 * yearEst;
            long j6 = 4;
            long j7 = j5 + (yearEst / j6);
            long j8 = 100;
            long doyEst2 = zeroDay - ((j7 - (yearEst / j8)) + (yearEst / j3));
            if (doyEst2 >= 0) {
                doyEst = doyEst2;
            } else {
                yearEst--;
                doyEst = zeroDay - ((((j4 * yearEst) + (yearEst / j6)) - (yearEst / j8)) + (yearEst / j3));
            }
            int marchDoy0 = (int) doyEst;
            int marchMonth0 = ((marchDoy0 * 5) + 2) / 153;
            int month2 = ((marchMonth0 + 2) % 12) + 1;
            int month3 = marchMonth0 * 306;
            int day = (marchDoy0 - ((month3 + 5) / 10)) + 1;
            int year = (int) (((long) (marchMonth0 / 10)) + yearEst + adjust);
            int hours = secsOfDay / 3600;
            int secondWithoutHours = secsOfDay - (hours * 3600);
            int minutes = secondWithoutHours / 60;
            int second = secondWithoutHours - (minutes * 60);
            return new UnboundLocalDateTime(year, month2, day, hours, minutes, second, instant.getNanosecondsOfSecond());
        }
    }
}
