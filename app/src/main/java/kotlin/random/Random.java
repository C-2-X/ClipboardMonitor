package kotlin.random;

import androidx.constraintlayout.core.motion.utils.TypedValues;
import androidx.constraintlayout.widget.ConstraintLayout;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import kotlin.Metadata;
import kotlin.internal.PlatformImplementationsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import okhttp3.HttpUrl;

/* JADX INFO: compiled from: Random.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0006\b'\u0018\u0000 \u00182\u00020\u0001:\u0001\u0018B\u0007¢\u0006\u0004\b\u0002\u0010\u0003J\u0010\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0005H&J\b\u0010\u0007\u001a\u00020\u0005H\u0016J\u0010\u0010\u0007\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u0005H\u0016J\u0018\u0010\u0007\u001a\u00020\u00052\u0006\u0010\t\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u0005H\u0016J\b\u0010\n\u001a\u00020\u000bH\u0016J\u0010\u0010\n\u001a\u00020\u000b2\u0006\u0010\b\u001a\u00020\u000bH\u0016J\u0018\u0010\n\u001a\u00020\u000b2\u0006\u0010\t\u001a\u00020\u000b2\u0006\u0010\b\u001a\u00020\u000bH\u0016J\b\u0010\f\u001a\u00020\rH\u0016J\b\u0010\u000e\u001a\u00020\u000fH\u0016J\u0010\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\b\u001a\u00020\u000fH\u0016J\u0018\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\t\u001a\u00020\u000f2\u0006\u0010\b\u001a\u00020\u000fH\u0016J\b\u0010\u0010\u001a\u00020\u0011H\u0016J$\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00132\b\b\u0002\u0010\u0015\u001a\u00020\u00052\b\b\u0002\u0010\u0016\u001a\u00020\u0005H\u0016J\u0010\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0013H\u0016J\u0010\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0017\u001a\u00020\u0005H\u0016¨\u0006\u0019"}, d2 = {"Lkotlin/random/Random;", HttpUrl.FRAGMENT_ENCODE_SET, "<init>", "()V", "nextBits", HttpUrl.FRAGMENT_ENCODE_SET, "bitCount", "nextInt", "until", TypedValues.TransitionType.S_FROM, "nextLong", HttpUrl.FRAGMENT_ENCODE_SET, "nextBoolean", HttpUrl.FRAGMENT_ENCODE_SET, "nextDouble", HttpUrl.FRAGMENT_ENCODE_SET, "nextFloat", HttpUrl.FRAGMENT_ENCODE_SET, "nextBytes", HttpUrl.FRAGMENT_ENCODE_SET, "array", "fromIndex", "toIndex", "size", "Default", "kotlin-stdlib"}, k = 1, mv = {2, 2, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
public abstract class Random {

    /* JADX INFO: renamed from: Default, reason: from kotlin metadata */
    public static final Companion INSTANCE = new Companion(null);
    private static final Random defaultRandom = PlatformImplementationsKt.IMPLEMENTATIONS.defaultPlatformRandom();

    public abstract int nextBits(int bitCount);

    public int nextInt() {
        return nextBits(32);
    }

    public int nextInt(int until) {
        return nextInt(0, until);
    }

    public int nextInt(int from, int until) {
        int bits;
        int v;
        int bitCount;
        int rnd;
        boolean z;
        RandomKt.checkRangeBounds(from, until);
        int n = until - from;
        if (n <= 0 && n != Integer.MIN_VALUE) {
            do {
                rnd = nextInt();
                z = false;
                if (from <= rnd && rnd < until) {
                    z = true;
                }
            } while (!z);
            return rnd;
        }
        if (((-n) & n) == n) {
            int bitCount2 = RandomKt.fastLog2(n);
            bitCount = nextBits(bitCount2);
        } else {
            do {
                bits = nextInt() >>> 1;
                v = bits % n;
            } while ((bits - v) + (n - 1) < 0);
            bitCount = v;
        }
        return from + bitCount;
    }

    public long nextLong() {
        return (((long) nextInt()) << 32) + ((long) nextInt());
    }

    public long nextLong(long until) {
        return nextLong(0L, until);
    }

    public long nextLong(long from, long until) {
        long rnd;
        boolean z;
        long bits;
        long v;
        RandomKt.checkRangeBounds(from, until);
        long n = until - from;
        if (n <= 0) {
            do {
                rnd = nextLong();
                z = false;
                if (from <= rnd && rnd < until) {
                    z = true;
                }
            } while (!z);
            return rnd;
        }
        if (((-n) & n) == n) {
            int nLow = (int) n;
            int nHigh = (int) (n >>> 32);
            if (nLow != 0) {
                int bitCount = RandomKt.fastLog2(nLow);
                v = 4294967295L & ((long) nextBits(bitCount));
            } else if (nHigh == 1) {
                v = 4294967295L & ((long) nextInt());
            } else {
                int bitCount2 = RandomKt.fastLog2(nHigh);
                v = (4294967295L & ((long) nextInt())) + (((long) nextBits(bitCount2)) << 32);
            }
        } else {
            do {
                bits = nextLong() >>> 1;
                v = bits % n;
            } while ((bits - v) + (n - 1) < 0);
        }
        return from + v;
    }

    public boolean nextBoolean() {
        return nextBits(1) != 0;
    }

    public double nextDouble() {
        return PlatformRandomKt.doubleFromParts(nextBits(26), nextBits(27));
    }

    public double nextDouble(double until) {
        return nextDouble(0.0d, until);
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x003b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public double nextDouble(double r10, double r12) {
        /*
            r9 = this;
            kotlin.random.RandomKt.checkRangeBounds(r10, r12)
            double r0 = r12 - r10
            boolean r2 = java.lang.Double.isInfinite(r0)
            if (r2 == 0) goto L3b
            double r2 = java.lang.Math.abs(r10)
            r4 = 9218868437227405311(0x7fefffffffffffff, double:1.7976931348623157E308)
            int r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            r3 = 1
            r6 = 0
            if (r2 > 0) goto L1c
            r2 = r3
            goto L1d
        L1c:
            r2 = r6
        L1d:
            if (r2 == 0) goto L3b
            double r7 = java.lang.Math.abs(r12)
            int r2 = (r7 > r4 ? 1 : (r7 == r4 ? 0 : -1))
            if (r2 > 0) goto L28
            goto L29
        L28:
            r3 = r6
        L29:
            if (r3 == 0) goto L3b
            double r2 = r9.nextDouble()
            r4 = 2
            double r4 = (double) r4
            double r6 = r12 / r4
            double r4 = r10 / r4
            double r6 = r6 - r4
            double r2 = r2 * r6
            double r4 = r10 + r2
            double r4 = r4 + r2
            goto L42
        L3b:
            double r2 = r9.nextDouble()
            double r2 = r2 * r0
            double r4 = r10 + r2
        L42:
            int r2 = (r4 > r12 ? 1 : (r4 == r12 ? 0 : -1))
            if (r2 < 0) goto L4e
            r2 = -4503599627370496(0xfff0000000000000, double:-Infinity)
            double r2 = java.lang.Math.nextAfter(r12, r2)
            goto L4f
        L4e:
            r2 = r4
        L4f:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlin.random.Random.nextDouble(double, double):double");
    }

    public float nextFloat() {
        return nextBits(24) / 1.6777216E7f;
    }

    public static /* synthetic */ byte[] nextBytes$default(Random random, byte[] bArr, int i, int i2, int i3, Object obj) {
        if (obj != null) {
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: nextBytes");
        }
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = bArr.length;
        }
        return random.nextBytes(bArr, i, i2);
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x001d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public byte[] nextBytes(byte[] r9, int r10, int r11) {
        /*
            Method dump skipped, instruction units count: 208
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlin.random.Random.nextBytes(byte[], int, int):byte[]");
    }

    public byte[] nextBytes(byte[] array) {
        Intrinsics.checkNotNullParameter(array, "array");
        return nextBytes(array, 0, array.length);
    }

    public byte[] nextBytes(int size) {
        return nextBytes(new byte[size]);
    }

    /* JADX INFO: renamed from: kotlin.random.Random$Default, reason: from kotlin metadata */
    /* JADX INFO: compiled from: Random.kt */
    @Metadata(d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0006\b\u0086\u0003\u0018\u00002\u00020\u00012\u00060\u0002j\u0002`\u0003:\u0001#B\t\b\u0002¢\u0006\u0004\b\u0004\u0010\u0005J\b\u0010\u0007\u001a\u00020\bH\u0002J\u0019\u0010\t\u001a\u00020\n2\n\u0010\u000b\u001a\u00060\fj\u0002`\rH\u0002¢\u0006\u0002\u0010\u000eJ\u0010\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0010H\u0016J\b\u0010\u0012\u001a\u00020\u0010H\u0016J\u0010\u0010\u0012\u001a\u00020\u00102\u0006\u0010\u0013\u001a\u00020\u0010H\u0016J\u0018\u0010\u0012\u001a\u00020\u00102\u0006\u0010\u0014\u001a\u00020\u00102\u0006\u0010\u0013\u001a\u00020\u0010H\u0016J\b\u0010\u0015\u001a\u00020\u0016H\u0016J\u0010\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0013\u001a\u00020\u0016H\u0016J\u0018\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0014\u001a\u00020\u00162\u0006\u0010\u0013\u001a\u00020\u0016H\u0016J\b\u0010\u0017\u001a\u00020\u0018H\u0016J\b\u0010\u0019\u001a\u00020\u001aH\u0016J\u0010\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u0013\u001a\u00020\u001aH\u0016J\u0018\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u0014\u001a\u00020\u001a2\u0006\u0010\u0013\u001a\u00020\u001aH\u0016J\b\u0010\u001b\u001a\u00020\u001cH\u0016J\u0010\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u001eH\u0016J\u0010\u0010\u001d\u001a\u00020\u001e2\u0006\u0010 \u001a\u00020\u0010H\u0016J \u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u001e2\u0006\u0010!\u001a\u00020\u00102\u0006\u0010\"\u001a\u00020\u0010H\u0016R\u000e\u0010\u0006\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006$"}, d2 = {"Lkotlin/random/Random$Default;", "Lkotlin/random/Random;", "Ljava/io/Serializable;", "Lkotlin/io/Serializable;", "<init>", "()V", "defaultRandom", "writeReplace", HttpUrl.FRAGMENT_ENCODE_SET, "readObject", HttpUrl.FRAGMENT_ENCODE_SET, "input", "Ljava/io/ObjectInputStream;", "Lkotlin/internal/ReadObjectParameterType;", "(Ljava/io/ObjectInputStream;)V", "nextBits", HttpUrl.FRAGMENT_ENCODE_SET, "bitCount", "nextInt", "until", TypedValues.TransitionType.S_FROM, "nextLong", HttpUrl.FRAGMENT_ENCODE_SET, "nextBoolean", HttpUrl.FRAGMENT_ENCODE_SET, "nextDouble", HttpUrl.FRAGMENT_ENCODE_SET, "nextFloat", HttpUrl.FRAGMENT_ENCODE_SET, "nextBytes", HttpUrl.FRAGMENT_ENCODE_SET, "array", "size", "fromIndex", "toIndex", "Serialized", "kotlin-stdlib"}, k = 1, mv = {2, 2, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
    public static final class Companion extends Random implements Serializable {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        /* JADX INFO: renamed from: kotlin.random.Random$Default$Serialized */
        /* JADX INFO: compiled from: Random.kt */
        @Metadata(d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0000\n\u0000\bÂ\u0002\u0018\u00002\u00060\u0001j\u0002`\u0002B\t\b\u0002¢\u0006\u0004\b\u0003\u0010\u0004J\b\u0010\u0007\u001a\u00020\bH\u0002R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082T¢\u0006\u0002\n\u0000¨\u0006\t"}, d2 = {"Lkotlin/random/Random$Default$Serialized;", "Ljava/io/Serializable;", "Lkotlin/io/Serializable;", "<init>", "()V", "serialVersionUID", HttpUrl.FRAGMENT_ENCODE_SET, "readResolve", HttpUrl.FRAGMENT_ENCODE_SET, "kotlin-stdlib"}, k = 1, mv = {2, 2, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
        private static final class Serialized implements Serializable {
            public static final Serialized INSTANCE = new Serialized();
            private static final long serialVersionUID = 0;

            private Serialized() {
            }

            private final Object readResolve() {
                return Random.INSTANCE;
            }
        }

        private final Object writeReplace() {
            return Serialized.INSTANCE;
        }

        private final void readObject(ObjectInputStream input) throws InvalidObjectException {
            throw new InvalidObjectException("Deserialization is supported via proxy only");
        }

        @Override // kotlin.random.Random
        public int nextBits(int bitCount) {
            return Random.defaultRandom.nextBits(bitCount);
        }

        @Override // kotlin.random.Random
        public int nextInt() {
            return Random.defaultRandom.nextInt();
        }

        @Override // kotlin.random.Random
        public int nextInt(int until) {
            return Random.defaultRandom.nextInt(until);
        }

        @Override // kotlin.random.Random
        public int nextInt(int from, int until) {
            return Random.defaultRandom.nextInt(from, until);
        }

        @Override // kotlin.random.Random
        public long nextLong() {
            return Random.defaultRandom.nextLong();
        }

        @Override // kotlin.random.Random
        public long nextLong(long until) {
            return Random.defaultRandom.nextLong(until);
        }

        @Override // kotlin.random.Random
        public long nextLong(long from, long until) {
            return Random.defaultRandom.nextLong(from, until);
        }

        @Override // kotlin.random.Random
        public boolean nextBoolean() {
            return Random.defaultRandom.nextBoolean();
        }

        @Override // kotlin.random.Random
        public double nextDouble() {
            return Random.defaultRandom.nextDouble();
        }

        @Override // kotlin.random.Random
        public double nextDouble(double until) {
            return Random.defaultRandom.nextDouble(until);
        }

        @Override // kotlin.random.Random
        public double nextDouble(double from, double until) {
            return Random.defaultRandom.nextDouble(from, until);
        }

        @Override // kotlin.random.Random
        public float nextFloat() {
            return Random.defaultRandom.nextFloat();
        }

        @Override // kotlin.random.Random
        public byte[] nextBytes(byte[] array) {
            Intrinsics.checkNotNullParameter(array, "array");
            return Random.defaultRandom.nextBytes(array);
        }

        @Override // kotlin.random.Random
        public byte[] nextBytes(int size) {
            return Random.defaultRandom.nextBytes(size);
        }

        @Override // kotlin.random.Random
        public byte[] nextBytes(byte[] array, int fromIndex, int toIndex) {
            Intrinsics.checkNotNullParameter(array, "array");
            return Random.defaultRandom.nextBytes(array, fromIndex, toIndex);
        }
    }
}
