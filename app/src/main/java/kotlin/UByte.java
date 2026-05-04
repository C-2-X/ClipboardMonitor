package kotlin;

import androidx.constraintlayout.widget.ConstraintLayout;
import kotlin.jvm.JvmInline;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.UIntRange;
import kotlin.ranges.URangesKt;
import okhttp3.HttpUrl;

/* JADX INFO: compiled from: UByte.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(d1 = {"\u0000n\n\u0002\u0018\u0002\n\u0002\u0010\u000f\n\u0000\n\u0002\u0010\u0005\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b-\n\u0002\u0018\u0002\n\u0002\b\u000f\n\u0002\u0010\n\n\u0002\b\u0006\n\u0002\u0010\t\n\u0002\b\u000b\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\u0010\u0000\n\u0002\b\u0003\b\u0087@\u0018\u0000 s2\b\u0012\u0004\u0012\u00020\u00000\u0001:\u0001sB\u0011\b\u0001\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0004\b\u0004\u0010\u0005J\u0018\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u0000H\u0097\n¢\u0006\u0004\b\u000b\u0010\fJ\u0018\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\rH\u0087\n¢\u0006\u0004\b\u000e\u0010\u000fJ\u0018\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u0010H\u0087\n¢\u0006\u0004\b\u0011\u0010\u0012J\u0018\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u0013H\u0087\n¢\u0006\u0004\b\u0014\u0010\u0015J\u0018\u0010\u0016\u001a\u00020\u00102\u0006\u0010\n\u001a\u00020\u0000H\u0087\n¢\u0006\u0004\b\u0017\u0010\fJ\u0018\u0010\u0016\u001a\u00020\u00102\u0006\u0010\n\u001a\u00020\rH\u0087\n¢\u0006\u0004\b\u0018\u0010\u000fJ\u0018\u0010\u0016\u001a\u00020\u00102\u0006\u0010\n\u001a\u00020\u0010H\u0087\n¢\u0006\u0004\b\u0019\u0010\u0012J\u0018\u0010\u0016\u001a\u00020\u00132\u0006\u0010\n\u001a\u00020\u0013H\u0087\n¢\u0006\u0004\b\u001a\u0010\u001bJ\u0018\u0010\u001c\u001a\u00020\u00102\u0006\u0010\n\u001a\u00020\u0000H\u0087\n¢\u0006\u0004\b\u001d\u0010\fJ\u0018\u0010\u001c\u001a\u00020\u00102\u0006\u0010\n\u001a\u00020\rH\u0087\n¢\u0006\u0004\b\u001e\u0010\u000fJ\u0018\u0010\u001c\u001a\u00020\u00102\u0006\u0010\n\u001a\u00020\u0010H\u0087\n¢\u0006\u0004\b\u001f\u0010\u0012J\u0018\u0010\u001c\u001a\u00020\u00132\u0006\u0010\n\u001a\u00020\u0013H\u0087\n¢\u0006\u0004\b \u0010\u001bJ\u0018\u0010!\u001a\u00020\u00102\u0006\u0010\n\u001a\u00020\u0000H\u0087\n¢\u0006\u0004\b\"\u0010\fJ\u0018\u0010!\u001a\u00020\u00102\u0006\u0010\n\u001a\u00020\rH\u0087\n¢\u0006\u0004\b#\u0010\u000fJ\u0018\u0010!\u001a\u00020\u00102\u0006\u0010\n\u001a\u00020\u0010H\u0087\n¢\u0006\u0004\b$\u0010\u0012J\u0018\u0010!\u001a\u00020\u00132\u0006\u0010\n\u001a\u00020\u0013H\u0087\n¢\u0006\u0004\b%\u0010\u001bJ\u0018\u0010&\u001a\u00020\u00102\u0006\u0010\n\u001a\u00020\u0000H\u0087\n¢\u0006\u0004\b'\u0010\fJ\u0018\u0010&\u001a\u00020\u00102\u0006\u0010\n\u001a\u00020\rH\u0087\n¢\u0006\u0004\b(\u0010\u000fJ\u0018\u0010&\u001a\u00020\u00102\u0006\u0010\n\u001a\u00020\u0010H\u0087\n¢\u0006\u0004\b)\u0010\u0012J\u0018\u0010&\u001a\u00020\u00132\u0006\u0010\n\u001a\u00020\u0013H\u0087\n¢\u0006\u0004\b*\u0010\u001bJ\u0018\u0010+\u001a\u00020\u00102\u0006\u0010\n\u001a\u00020\u0000H\u0087\n¢\u0006\u0004\b,\u0010\fJ\u0018\u0010+\u001a\u00020\u00102\u0006\u0010\n\u001a\u00020\rH\u0087\n¢\u0006\u0004\b-\u0010\u000fJ\u0018\u0010+\u001a\u00020\u00102\u0006\u0010\n\u001a\u00020\u0010H\u0087\n¢\u0006\u0004\b.\u0010\u0012J\u0018\u0010+\u001a\u00020\u00132\u0006\u0010\n\u001a\u00020\u0013H\u0087\n¢\u0006\u0004\b/\u0010\u001bJ\u0018\u00100\u001a\u00020\u00102\u0006\u0010\n\u001a\u00020\u0000H\u0087\b¢\u0006\u0004\b1\u0010\fJ\u0018\u00100\u001a\u00020\u00102\u0006\u0010\n\u001a\u00020\rH\u0087\b¢\u0006\u0004\b2\u0010\u000fJ\u0018\u00100\u001a\u00020\u00102\u0006\u0010\n\u001a\u00020\u0010H\u0087\b¢\u0006\u0004\b3\u0010\u0012J\u0018\u00100\u001a\u00020\u00132\u0006\u0010\n\u001a\u00020\u0013H\u0087\b¢\u0006\u0004\b4\u0010\u001bJ\u0018\u00105\u001a\u00020\u00002\u0006\u0010\n\u001a\u00020\u0000H\u0087\b¢\u0006\u0004\b6\u00107J\u0018\u00105\u001a\u00020\r2\u0006\u0010\n\u001a\u00020\rH\u0087\b¢\u0006\u0004\b8\u00109J\u0018\u00105\u001a\u00020\u00102\u0006\u0010\n\u001a\u00020\u0010H\u0087\b¢\u0006\u0004\b:\u0010\u0012J\u0018\u00105\u001a\u00020\u00132\u0006\u0010\n\u001a\u00020\u0013H\u0087\b¢\u0006\u0004\b;\u0010\u001bJ\u0010\u0010<\u001a\u00020\u0000H\u0087\n¢\u0006\u0004\b=\u0010\u0005J\u0010\u0010>\u001a\u00020\u0000H\u0087\n¢\u0006\u0004\b?\u0010\u0005J\u0018\u0010@\u001a\u00020A2\u0006\u0010\n\u001a\u00020\u0000H\u0087\n¢\u0006\u0004\bB\u0010CJ\u0018\u0010D\u001a\u00020A2\u0006\u0010\n\u001a\u00020\u0000H\u0087\n¢\u0006\u0004\bE\u0010CJ\u0018\u0010F\u001a\u00020\u00002\u0006\u0010\n\u001a\u00020\u0000H\u0087\f¢\u0006\u0004\bG\u00107J\u0018\u0010H\u001a\u00020\u00002\u0006\u0010\n\u001a\u00020\u0000H\u0087\f¢\u0006\u0004\bI\u00107J\u0018\u0010J\u001a\u00020\u00002\u0006\u0010\n\u001a\u00020\u0000H\u0087\f¢\u0006\u0004\bK\u00107J\u0010\u0010L\u001a\u00020\u0000H\u0087\b¢\u0006\u0004\bM\u0010\u0005J\u0010\u0010N\u001a\u00020\u0003H\u0087\b¢\u0006\u0004\bO\u0010\u0005J\u0010\u0010P\u001a\u00020QH\u0087\b¢\u0006\u0004\bR\u0010SJ\u0010\u0010T\u001a\u00020\tH\u0087\b¢\u0006\u0004\bU\u0010VJ\u0010\u0010W\u001a\u00020XH\u0087\b¢\u0006\u0004\bY\u0010ZJ\u0010\u0010[\u001a\u00020\u0000H\u0087\b¢\u0006\u0004\b\\\u0010\u0005J\u0010\u0010]\u001a\u00020\rH\u0087\b¢\u0006\u0004\b^\u0010SJ\u0010\u0010_\u001a\u00020\u0010H\u0087\b¢\u0006\u0004\b`\u0010VJ\u0010\u0010a\u001a\u00020\u0013H\u0087\b¢\u0006\u0004\bb\u0010ZJ\u0010\u0010c\u001a\u00020dH\u0087\b¢\u0006\u0004\be\u0010fJ\u0010\u0010g\u001a\u00020hH\u0087\b¢\u0006\u0004\bi\u0010jJ\u000f\u0010k\u001a\u00020lH\u0016¢\u0006\u0004\bm\u0010nJ\u0013\u0010o\u001a\u00020p2\b\u0010\n\u001a\u0004\u0018\u00010qHÖ\u0003J\t\u0010r\u001a\u00020\tHÖ\u0001R\u0016\u0010\u0002\u001a\u00020\u00038\u0000X\u0081\u0004¢\u0006\b\n\u0000\u0012\u0004\b\u0006\u0010\u0007\u0088\u0001\u0002\u0092\u0001\u00020\u0003¨\u0006t"}, d2 = {"Lkotlin/UByte;", HttpUrl.FRAGMENT_ENCODE_SET, "data", HttpUrl.FRAGMENT_ENCODE_SET, "constructor-impl", "(B)B", "getData$annotations", "()V", "compareTo", HttpUrl.FRAGMENT_ENCODE_SET, "other", "compareTo-7apg3OU", "(BB)I", "Lkotlin/UShort;", "compareTo-xj2QHRw", "(BS)I", "Lkotlin/UInt;", "compareTo-WZ4Q5Ns", "(BI)I", "Lkotlin/ULong;", "compareTo-VKZWuLQ", "(BJ)I", "plus", "plus-7apg3OU", "plus-xj2QHRw", "plus-WZ4Q5Ns", "plus-VKZWuLQ", "(BJ)J", "minus", "minus-7apg3OU", "minus-xj2QHRw", "minus-WZ4Q5Ns", "minus-VKZWuLQ", "times", "times-7apg3OU", "times-xj2QHRw", "times-WZ4Q5Ns", "times-VKZWuLQ", "div", "div-7apg3OU", "div-xj2QHRw", "div-WZ4Q5Ns", "div-VKZWuLQ", "rem", "rem-7apg3OU", "rem-xj2QHRw", "rem-WZ4Q5Ns", "rem-VKZWuLQ", "floorDiv", "floorDiv-7apg3OU", "floorDiv-xj2QHRw", "floorDiv-WZ4Q5Ns", "floorDiv-VKZWuLQ", "mod", "mod-7apg3OU", "(BB)B", "mod-xj2QHRw", "(BS)S", "mod-WZ4Q5Ns", "mod-VKZWuLQ", "inc", "inc-w2LRezQ", "dec", "dec-w2LRezQ", "rangeTo", "Lkotlin/ranges/UIntRange;", "rangeTo-7apg3OU", "(BB)Lkotlin/ranges/UIntRange;", "rangeUntil", "rangeUntil-7apg3OU", "and", "and-7apg3OU", "or", "or-7apg3OU", "xor", "xor-7apg3OU", "inv", "inv-w2LRezQ", "toByte", "toByte-impl", "toShort", HttpUrl.FRAGMENT_ENCODE_SET, "toShort-impl", "(B)S", "toInt", "toInt-impl", "(B)I", "toLong", HttpUrl.FRAGMENT_ENCODE_SET, "toLong-impl", "(B)J", "toUByte", "toUByte-w2LRezQ", "toUShort", "toUShort-Mh2AYeg", "toUInt", "toUInt-pVg5ArA", "toULong", "toULong-s-VKNKU", "toFloat", HttpUrl.FRAGMENT_ENCODE_SET, "toFloat-impl", "(B)F", "toDouble", HttpUrl.FRAGMENT_ENCODE_SET, "toDouble-impl", "(B)D", "toString", HttpUrl.FRAGMENT_ENCODE_SET, "toString-impl", "(B)Ljava/lang/String;", "equals", HttpUrl.FRAGMENT_ENCODE_SET, HttpUrl.FRAGMENT_ENCODE_SET, "hashCode", "Companion", "kotlin-stdlib"}, k = 1, mv = {2, 2, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
@JvmInline
public final class UByte implements Comparable<UByte> {
    public static final byte MAX_VALUE = -1;
    public static final byte MIN_VALUE = 0;
    public static final int SIZE_BITS = 8;
    public static final int SIZE_BYTES = 1;
    private final byte data;

    /* JADX INFO: renamed from: box-impl, reason: not valid java name */
    public static final /* synthetic */ UByte m182boximpl(byte b) {
        return new UByte(b);
    }

    /* JADX INFO: renamed from: constructor-impl, reason: not valid java name */
    public static byte m188constructorimpl(byte b) {
        return b;
    }

    /* JADX INFO: renamed from: equals-impl, reason: not valid java name */
    public static boolean m194equalsimpl(byte b, Object obj) {
        return (obj instanceof UByte) && b == ((UByte) obj).getData();
    }

    /* JADX INFO: renamed from: equals-impl0, reason: not valid java name */
    public static final boolean m195equalsimpl0(byte b, byte b2) {
        return b == b2;
    }

    public static /* synthetic */ void getData$annotations() {
    }

    /* JADX INFO: renamed from: hashCode-impl, reason: not valid java name */
    public static int m200hashCodeimpl(byte b) {
        return Byte.hashCode(b);
    }

    public boolean equals(Object other) {
        return m194equalsimpl(this.data, other);
    }

    public int hashCode() {
        return m200hashCodeimpl(this.data);
    }

    /* JADX INFO: renamed from: unbox-impl, reason: not valid java name and from getter */
    public final /* synthetic */ byte getData() {
        return this.data;
    }

    @Override // java.lang.Comparable
    public /* bridge */ /* synthetic */ int compareTo(UByte uByte) {
        return Intrinsics.compare(getData() & MAX_VALUE, uByte.getData() & MAX_VALUE);
    }

    private /* synthetic */ UByte(byte data) {
        this.data = data;
    }

    /* JADX INFO: renamed from: compareTo-7apg3OU, reason: not valid java name */
    private int m183compareTo7apg3OU(byte other) {
        return Intrinsics.compare(getData() & MAX_VALUE, other & MAX_VALUE);
    }

    /* JADX INFO: renamed from: compareTo-7apg3OU, reason: not valid java name */
    private static int m184compareTo7apg3OU(byte arg0, byte other) {
        return Intrinsics.compare(arg0 & MAX_VALUE, other & MAX_VALUE);
    }

    /* JADX INFO: renamed from: compareTo-xj2QHRw, reason: not valid java name */
    private static final int m187compareToxj2QHRw(byte arg0, short other) {
        return Intrinsics.compare(arg0 & MAX_VALUE, 65535 & other);
    }

    /* JADX INFO: renamed from: compareTo-WZ4Q5Ns, reason: not valid java name */
    private static final int m186compareToWZ4Q5Ns(byte arg0, int other) {
        return Integer.compareUnsigned(UInt.m265constructorimpl(arg0 & MAX_VALUE), other);
    }

    /* JADX INFO: renamed from: compareTo-VKZWuLQ, reason: not valid java name */
    private static final int m185compareToVKZWuLQ(byte arg0, long other) {
        return Long.compareUnsigned(ULong.m344constructorimpl(((long) arg0) & 255), other);
    }

    /* JADX INFO: renamed from: plus-7apg3OU, reason: not valid java name */
    private static final int m212plus7apg3OU(byte arg0, byte other) {
        return UInt.m265constructorimpl(UInt.m265constructorimpl(arg0 & MAX_VALUE) + UInt.m265constructorimpl(other & MAX_VALUE));
    }

    /* JADX INFO: renamed from: plus-xj2QHRw, reason: not valid java name */
    private static final int m215plusxj2QHRw(byte arg0, short other) {
        return UInt.m265constructorimpl(UInt.m265constructorimpl(arg0 & MAX_VALUE) + UInt.m265constructorimpl(65535 & other));
    }

    /* JADX INFO: renamed from: plus-WZ4Q5Ns, reason: not valid java name */
    private static final int m214plusWZ4Q5Ns(byte arg0, int other) {
        return UInt.m265constructorimpl(UInt.m265constructorimpl(arg0 & MAX_VALUE) + other);
    }

    /* JADX INFO: renamed from: plus-VKZWuLQ, reason: not valid java name */
    private static final long m213plusVKZWuLQ(byte arg0, long other) {
        return ULong.m344constructorimpl(ULong.m344constructorimpl(((long) arg0) & 255) + other);
    }

    /* JADX INFO: renamed from: minus-7apg3OU, reason: not valid java name */
    private static final int m203minus7apg3OU(byte arg0, byte other) {
        return UInt.m265constructorimpl(UInt.m265constructorimpl(arg0 & MAX_VALUE) - UInt.m265constructorimpl(other & MAX_VALUE));
    }

    /* JADX INFO: renamed from: minus-xj2QHRw, reason: not valid java name */
    private static final int m206minusxj2QHRw(byte arg0, short other) {
        return UInt.m265constructorimpl(UInt.m265constructorimpl(arg0 & MAX_VALUE) - UInt.m265constructorimpl(65535 & other));
    }

    /* JADX INFO: renamed from: minus-WZ4Q5Ns, reason: not valid java name */
    private static final int m205minusWZ4Q5Ns(byte arg0, int other) {
        return UInt.m265constructorimpl(UInt.m265constructorimpl(arg0 & MAX_VALUE) - other);
    }

    /* JADX INFO: renamed from: minus-VKZWuLQ, reason: not valid java name */
    private static final long m204minusVKZWuLQ(byte arg0, long other) {
        return ULong.m344constructorimpl(ULong.m344constructorimpl(((long) arg0) & 255) - other);
    }

    /* JADX INFO: renamed from: times-7apg3OU, reason: not valid java name */
    private static final int m222times7apg3OU(byte arg0, byte other) {
        return UInt.m265constructorimpl(UInt.m265constructorimpl(arg0 & MAX_VALUE) * UInt.m265constructorimpl(other & MAX_VALUE));
    }

    /* JADX INFO: renamed from: times-xj2QHRw, reason: not valid java name */
    private static final int m225timesxj2QHRw(byte arg0, short other) {
        return UInt.m265constructorimpl(UInt.m265constructorimpl(arg0 & MAX_VALUE) * UInt.m265constructorimpl(65535 & other));
    }

    /* JADX INFO: renamed from: times-WZ4Q5Ns, reason: not valid java name */
    private static final int m224timesWZ4Q5Ns(byte arg0, int other) {
        return UInt.m265constructorimpl(UInt.m265constructorimpl(arg0 & MAX_VALUE) * other);
    }

    /* JADX INFO: renamed from: times-VKZWuLQ, reason: not valid java name */
    private static final long m223timesVKZWuLQ(byte arg0, long other) {
        return ULong.m344constructorimpl(ULong.m344constructorimpl(((long) arg0) & 255) * other);
    }

    /* JADX INFO: renamed from: div-7apg3OU, reason: not valid java name */
    private static final int m190div7apg3OU(byte arg0, byte other) {
        return Integer.divideUnsigned(UInt.m265constructorimpl(arg0 & MAX_VALUE), UInt.m265constructorimpl(other & MAX_VALUE));
    }

    /* JADX INFO: renamed from: div-xj2QHRw, reason: not valid java name */
    private static final int m193divxj2QHRw(byte arg0, short other) {
        return Integer.divideUnsigned(UInt.m265constructorimpl(arg0 & MAX_VALUE), UInt.m265constructorimpl(65535 & other));
    }

    /* JADX INFO: renamed from: div-WZ4Q5Ns, reason: not valid java name */
    private static final int m192divWZ4Q5Ns(byte arg0, int other) {
        return Integer.divideUnsigned(UInt.m265constructorimpl(arg0 & MAX_VALUE), other);
    }

    /* JADX INFO: renamed from: div-VKZWuLQ, reason: not valid java name */
    private static final long m191divVKZWuLQ(byte arg0, long other) {
        return Long.divideUnsigned(ULong.m344constructorimpl(((long) arg0) & 255), other);
    }

    /* JADX INFO: renamed from: rem-7apg3OU, reason: not valid java name */
    private static final int m218rem7apg3OU(byte arg0, byte other) {
        return Integer.remainderUnsigned(UInt.m265constructorimpl(arg0 & MAX_VALUE), UInt.m265constructorimpl(other & MAX_VALUE));
    }

    /* JADX INFO: renamed from: rem-xj2QHRw, reason: not valid java name */
    private static final int m221remxj2QHRw(byte arg0, short other) {
        return Integer.remainderUnsigned(UInt.m265constructorimpl(arg0 & MAX_VALUE), UInt.m265constructorimpl(65535 & other));
    }

    /* JADX INFO: renamed from: rem-WZ4Q5Ns, reason: not valid java name */
    private static final int m220remWZ4Q5Ns(byte arg0, int other) {
        return Integer.remainderUnsigned(UInt.m265constructorimpl(arg0 & MAX_VALUE), other);
    }

    /* JADX INFO: renamed from: rem-VKZWuLQ, reason: not valid java name */
    private static final long m219remVKZWuLQ(byte arg0, long other) {
        return Long.remainderUnsigned(ULong.m344constructorimpl(((long) arg0) & 255), other);
    }

    /* JADX INFO: renamed from: floorDiv-7apg3OU, reason: not valid java name */
    private static final int m196floorDiv7apg3OU(byte arg0, byte other) {
        return Integer.divideUnsigned(UInt.m265constructorimpl(arg0 & MAX_VALUE), UInt.m265constructorimpl(other & MAX_VALUE));
    }

    /* JADX INFO: renamed from: floorDiv-xj2QHRw, reason: not valid java name */
    private static final int m199floorDivxj2QHRw(byte arg0, short other) {
        return Integer.divideUnsigned(UInt.m265constructorimpl(arg0 & MAX_VALUE), UInt.m265constructorimpl(65535 & other));
    }

    /* JADX INFO: renamed from: floorDiv-WZ4Q5Ns, reason: not valid java name */
    private static final int m198floorDivWZ4Q5Ns(byte arg0, int other) {
        return Integer.divideUnsigned(UInt.m265constructorimpl(arg0 & MAX_VALUE), other);
    }

    /* JADX INFO: renamed from: floorDiv-VKZWuLQ, reason: not valid java name */
    private static final long m197floorDivVKZWuLQ(byte arg0, long other) {
        return Long.divideUnsigned(ULong.m344constructorimpl(((long) arg0) & 255), other);
    }

    /* JADX INFO: renamed from: mod-7apg3OU, reason: not valid java name */
    private static final byte m207mod7apg3OU(byte arg0, byte other) {
        return m188constructorimpl((byte) Integer.remainderUnsigned(UInt.m265constructorimpl(arg0 & MAX_VALUE), UInt.m265constructorimpl(other & MAX_VALUE)));
    }

    /* JADX INFO: renamed from: mod-xj2QHRw, reason: not valid java name */
    private static final short m210modxj2QHRw(byte arg0, short other) {
        return UShort.m451constructorimpl((short) Integer.remainderUnsigned(UInt.m265constructorimpl(arg0 & MAX_VALUE), UInt.m265constructorimpl(65535 & other)));
    }

    /* JADX INFO: renamed from: mod-WZ4Q5Ns, reason: not valid java name */
    private static final int m209modWZ4Q5Ns(byte arg0, int other) {
        return Integer.remainderUnsigned(UInt.m265constructorimpl(arg0 & MAX_VALUE), other);
    }

    /* JADX INFO: renamed from: mod-VKZWuLQ, reason: not valid java name */
    private static final long m208modVKZWuLQ(byte arg0, long other) {
        return Long.remainderUnsigned(ULong.m344constructorimpl(((long) arg0) & 255), other);
    }

    /* JADX INFO: renamed from: inc-w2LRezQ, reason: not valid java name */
    private static final byte m201incw2LRezQ(byte arg0) {
        return m188constructorimpl((byte) (arg0 + 1));
    }

    /* JADX INFO: renamed from: dec-w2LRezQ, reason: not valid java name */
    private static final byte m189decw2LRezQ(byte arg0) {
        return m188constructorimpl((byte) (arg0 - 1));
    }

    /* JADX INFO: renamed from: rangeTo-7apg3OU, reason: not valid java name */
    private static final UIntRange m216rangeTo7apg3OU(byte arg0, byte other) {
        return new UIntRange(UInt.m265constructorimpl(arg0 & MAX_VALUE), UInt.m265constructorimpl(other & MAX_VALUE), null);
    }

    /* JADX INFO: renamed from: rangeUntil-7apg3OU, reason: not valid java name */
    private static final UIntRange m217rangeUntil7apg3OU(byte arg0, byte other) {
        return URangesKt.m1443untilJ1ME1BU(UInt.m265constructorimpl(arg0 & MAX_VALUE), UInt.m265constructorimpl(other & MAX_VALUE));
    }

    /* JADX INFO: renamed from: and-7apg3OU, reason: not valid java name */
    private static final byte m181and7apg3OU(byte arg0, byte other) {
        return m188constructorimpl((byte) (arg0 & other));
    }

    /* JADX INFO: renamed from: or-7apg3OU, reason: not valid java name */
    private static final byte m211or7apg3OU(byte arg0, byte other) {
        return m188constructorimpl((byte) (arg0 | other));
    }

    /* JADX INFO: renamed from: xor-7apg3OU, reason: not valid java name */
    private static final byte m237xor7apg3OU(byte arg0, byte other) {
        return m188constructorimpl((byte) (arg0 ^ other));
    }

    /* JADX INFO: renamed from: inv-w2LRezQ, reason: not valid java name */
    private static final byte m202invw2LRezQ(byte arg0) {
        return m188constructorimpl((byte) (~arg0));
    }

    /* JADX INFO: renamed from: toByte-impl, reason: not valid java name */
    private static final byte m226toByteimpl(byte arg0) {
        return arg0;
    }

    /* JADX INFO: renamed from: toShort-impl, reason: not valid java name */
    private static final short m231toShortimpl(byte arg0) {
        return (short) (arg0 & 255);
    }

    /* JADX INFO: renamed from: toInt-impl, reason: not valid java name */
    private static final int m229toIntimpl(byte arg0) {
        return arg0 & MAX_VALUE;
    }

    /* JADX INFO: renamed from: toLong-impl, reason: not valid java name */
    private static final long m230toLongimpl(byte arg0) {
        return ((long) arg0) & 255;
    }

    /* JADX INFO: renamed from: toUByte-w2LRezQ, reason: not valid java name */
    private static final byte m233toUBytew2LRezQ(byte arg0) {
        return arg0;
    }

    /* JADX INFO: renamed from: toUShort-Mh2AYeg, reason: not valid java name */
    private static final short m236toUShortMh2AYeg(byte arg0) {
        return UShort.m451constructorimpl((short) (arg0 & 255));
    }

    /* JADX INFO: renamed from: toUInt-pVg5ArA, reason: not valid java name */
    private static final int m234toUIntpVg5ArA(byte arg0) {
        return UInt.m265constructorimpl(arg0 & MAX_VALUE);
    }

    /* JADX INFO: renamed from: toULong-s-VKNKU, reason: not valid java name */
    private static final long m235toULongsVKNKU(byte arg0) {
        return ULong.m344constructorimpl(((long) arg0) & 255);
    }

    /* JADX INFO: renamed from: toFloat-impl, reason: not valid java name */
    private static final float m228toFloatimpl(byte arg0) {
        return (float) UnsignedKt.uintToDouble(arg0 & MAX_VALUE);
    }

    /* JADX INFO: renamed from: toDouble-impl, reason: not valid java name */
    private static final double m227toDoubleimpl(byte arg0) {
        return UnsignedKt.uintToDouble(arg0 & MAX_VALUE);
    }

    /* JADX INFO: renamed from: toString-impl, reason: not valid java name */
    public static String m232toStringimpl(byte arg0) {
        return String.valueOf(arg0 & MAX_VALUE);
    }

    public String toString() {
        return m232toStringimpl(this.data);
    }
}
