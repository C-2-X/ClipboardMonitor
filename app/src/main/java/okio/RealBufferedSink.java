package okio;

import androidx.constraintlayout.core.motion.utils.TypedValues;
import androidx.constraintlayout.widget.ConstraintLayout;
import java.io.EOFException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import okhttp3.HttpUrl;

/* JADX INFO: renamed from: okio.RealBufferedSink, reason: from toString */
/* JADX INFO: compiled from: RealBufferedSink.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(d1 = {"\u0000f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0012\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0011\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\b\u0010\u0005\u001a\u00020\u0006H\u0016J\b\u0010\u000e\u001a\u00020\u000fH\u0016J\b\u0010\u0010\u001a\u00020\u0001H\u0016J\b\u0010\u0011\u001a\u00020\u0001H\u0016J\b\u0010\u0012\u001a\u00020\u000fH\u0016J\b\u0010\u0013\u001a\u00020\rH\u0016J\b\u0010\u0014\u001a\u00020\u0015H\u0016J\b\u0010\u0016\u001a\u00020\u0017H\u0016J\b\u0010\u0018\u001a\u00020\u0019H\u0016J\u0010\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001dH\u0016J\u0010\u0010\u001a\u001a\u00020\u00012\u0006\u0010\u001c\u001a\u00020\u001eH\u0016J \u0010\u001a\u001a\u00020\u00012\u0006\u0010\u001c\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u001b2\u0006\u0010 \u001a\u00020\u001bH\u0016J\u0018\u0010\u001a\u001a\u00020\u000f2\u0006\u0010\u001c\u001a\u00020\u00062\u0006\u0010 \u001a\u00020!H\u0016J\u0010\u0010\u001a\u001a\u00020\u00012\u0006\u0010\"\u001a\u00020#H\u0016J \u0010\u001a\u001a\u00020\u00012\u0006\u0010\"\u001a\u00020#2\u0006\u0010\u001f\u001a\u00020\u001b2\u0006\u0010 \u001a\u00020\u001bH\u0016J\u0018\u0010\u001a\u001a\u00020\u00012\u0006\u0010\u001c\u001a\u00020$2\u0006\u0010 \u001a\u00020!H\u0016J\u0010\u0010%\u001a\u00020!2\u0006\u0010\u001c\u001a\u00020$H\u0016J\u0010\u0010&\u001a\u00020\u00012\u0006\u0010'\u001a\u00020\u001bH\u0016J\u0010\u0010(\u001a\u00020\u00012\u0006\u0010)\u001a\u00020!H\u0016J\u0010\u0010*\u001a\u00020\u00012\u0006\u0010)\u001a\u00020!H\u0016J\u0010\u0010+\u001a\u00020\u00012\u0006\u0010,\u001a\u00020\u001bH\u0016J\u0010\u0010-\u001a\u00020\u00012\u0006\u0010,\u001a\u00020\u001bH\u0016J\u0010\u0010.\u001a\u00020\u00012\u0006\u0010)\u001a\u00020!H\u0016J\u0010\u0010/\u001a\u00020\u00012\u0006\u0010)\u001a\u00020!H\u0016J\u0010\u00100\u001a\u00020\u00012\u0006\u00101\u001a\u00020\u001bH\u0016J\u0010\u00102\u001a\u00020\u00012\u0006\u00101\u001a\u00020\u001bH\u0016J\u0018\u00103\u001a\u00020\u00012\u0006\u00104\u001a\u00020\u00192\u0006\u00105\u001a\u000206H\u0016J(\u00103\u001a\u00020\u00012\u0006\u00104\u001a\u00020\u00192\u0006\u00107\u001a\u00020\u001b2\u0006\u00108\u001a\u00020\u001b2\u0006\u00105\u001a\u000206H\u0016J\u0010\u00109\u001a\u00020\u00012\u0006\u00104\u001a\u00020\u0019H\u0016J \u00109\u001a\u00020\u00012\u0006\u00104\u001a\u00020\u00192\u0006\u00107\u001a\u00020\u001b2\u0006\u00108\u001a\u00020\u001bH\u0016J\u0010\u0010:\u001a\u00020\u00012\u0006\u0010;\u001a\u00020\u001bH\u0016R\u001b\u0010\u0005\u001a\u00020\u00068Ö\u0002X\u0096\u0004¢\u0006\f\u0012\u0004\b\u0007\u0010\b\u001a\u0004\b\t\u0010\nR\u0010\u0010\u000b\u001a\u00020\u00068\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R\u0012\u0010\f\u001a\u00020\r8\u0006@\u0006X\u0087\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004¢\u0006\u0002\n\u0000¨\u0006<"}, d2 = {"Lokio/RealBufferedSink;", "Lokio/BufferedSink;", "sink", "Lokio/Sink;", "(Lokio/Sink;)V", "buffer", "Lokio/Buffer;", "getBuffer$annotations", "()V", "getBuffer", "()Lokio/Buffer;", "bufferField", "closed", HttpUrl.FRAGMENT_ENCODE_SET, "close", HttpUrl.FRAGMENT_ENCODE_SET, "emit", "emitCompleteSegments", "flush", "isOpen", "outputStream", "Ljava/io/OutputStream;", "timeout", "Lokio/Timeout;", "toString", HttpUrl.FRAGMENT_ENCODE_SET, "write", HttpUrl.FRAGMENT_ENCODE_SET, "source", "Ljava/nio/ByteBuffer;", HttpUrl.FRAGMENT_ENCODE_SET, TypedValues.CycleType.S_WAVE_OFFSET, "byteCount", HttpUrl.FRAGMENT_ENCODE_SET, "byteString", "Lokio/ByteString;", "Lokio/Source;", "writeAll", "writeByte", "b", "writeDecimalLong", "v", "writeHexadecimalUnsignedLong", "writeInt", "i", "writeIntLe", "writeLong", "writeLongLe", "writeShort", "s", "writeShortLe", "writeString", TypedValues.Custom.S_STRING, "charset", "Ljava/nio/charset/Charset;", "beginIndex", "endIndex", "writeUtf8", "writeUtf8CodePoint", "codePoint", "okio"}, k = 1, mv = {1, 9, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
public final class buffer implements BufferedSink {
    public final Buffer bufferField;
    public boolean closed;
    public final Sink sink;

    public static /* synthetic */ void getBuffer$annotations() {
    }

    public buffer(Sink sink) {
        Intrinsics.checkNotNullParameter(sink, "sink");
        this.sink = sink;
        this.bufferField = new Buffer();
    }

    @Override // okio.BufferedSink
    public Buffer getBuffer() {
        return this.bufferField;
    }

    @Override // okio.BufferedSink
    /* JADX INFO: renamed from: buffer, reason: from getter */
    public Buffer getBufferField() {
        return this.bufferField;
    }

    @Override // okio.Sink
    public void write(Buffer source, long byteCount) throws IOException {
        Intrinsics.checkNotNullParameter(source, "source");
        if (this.closed) {
            throw new IllegalStateException("closed".toString());
        }
        this.bufferField.write(source, byteCount);
        emitCompleteSegments();
    }

    @Override // okio.BufferedSink
    public BufferedSink write(ByteString byteString) {
        Intrinsics.checkNotNullParameter(byteString, "byteString");
        if (this.closed) {
            throw new IllegalStateException("closed".toString());
        }
        this.bufferField.write(byteString);
        return emitCompleteSegments();
    }

    @Override // okio.BufferedSink
    public BufferedSink write(ByteString byteString, int offset, int byteCount) {
        Intrinsics.checkNotNullParameter(byteString, "byteString");
        if (this.closed) {
            throw new IllegalStateException("closed".toString());
        }
        this.bufferField.write(byteString, offset, byteCount);
        return emitCompleteSegments();
    }

    @Override // okio.BufferedSink
    public BufferedSink writeUtf8(String string) {
        Intrinsics.checkNotNullParameter(string, "string");
        if (this.closed) {
            throw new IllegalStateException("closed".toString());
        }
        this.bufferField.writeUtf8(string);
        return emitCompleteSegments();
    }

    @Override // okio.BufferedSink
    public BufferedSink writeUtf8(String string, int beginIndex, int endIndex) {
        Intrinsics.checkNotNullParameter(string, "string");
        if (this.closed) {
            throw new IllegalStateException("closed".toString());
        }
        this.bufferField.writeUtf8(string, beginIndex, endIndex);
        return emitCompleteSegments();
    }

    @Override // okio.BufferedSink
    public BufferedSink writeUtf8CodePoint(int codePoint) {
        if (this.closed) {
            throw new IllegalStateException("closed".toString());
        }
        this.bufferField.writeUtf8CodePoint(codePoint);
        return emitCompleteSegments();
    }

    @Override // okio.BufferedSink
    public BufferedSink writeString(String string, Charset charset) {
        Intrinsics.checkNotNullParameter(string, "string");
        Intrinsics.checkNotNullParameter(charset, "charset");
        if (this.closed) {
            throw new IllegalStateException("closed".toString());
        }
        this.bufferField.writeString(string, charset);
        return emitCompleteSegments();
    }

    @Override // okio.BufferedSink
    public BufferedSink writeString(String string, int beginIndex, int endIndex, Charset charset) {
        Intrinsics.checkNotNullParameter(string, "string");
        Intrinsics.checkNotNullParameter(charset, "charset");
        if (this.closed) {
            throw new IllegalStateException("closed".toString());
        }
        this.bufferField.writeString(string, beginIndex, endIndex, charset);
        return emitCompleteSegments();
    }

    @Override // okio.BufferedSink
    public BufferedSink write(byte[] source) {
        Intrinsics.checkNotNullParameter(source, "source");
        if (this.closed) {
            throw new IllegalStateException("closed".toString());
        }
        this.bufferField.write(source);
        return emitCompleteSegments();
    }

    @Override // okio.BufferedSink
    public BufferedSink write(byte[] source, int offset, int byteCount) {
        Intrinsics.checkNotNullParameter(source, "source");
        if (this.closed) {
            throw new IllegalStateException("closed".toString());
        }
        this.bufferField.write(source, offset, byteCount);
        return emitCompleteSegments();
    }

    @Override // java.nio.channels.WritableByteChannel
    public int write(ByteBuffer source) throws IOException {
        Intrinsics.checkNotNullParameter(source, "source");
        if (this.closed) {
            throw new IllegalStateException("closed".toString());
        }
        int result = this.bufferField.write(source);
        emitCompleteSegments();
        return result;
    }

    @Override // okio.BufferedSink
    public long writeAll(Source source) throws IOException {
        Intrinsics.checkNotNullParameter(source, "source");
        long totalBytesRead$iv = 0;
        while (true) {
            long readCount$iv = source.read(this.bufferField, 8192L);
            if (readCount$iv == -1) {
                return totalBytesRead$iv;
            }
            totalBytesRead$iv += readCount$iv;
            emitCompleteSegments();
        }
    }

    @Override // okio.BufferedSink
    public BufferedSink write(Source source, long byteCount) throws IOException {
        Intrinsics.checkNotNullParameter(source, "source");
        long byteCount$iv = byteCount;
        while (byteCount$iv > 0) {
            long read$iv = source.read(this.bufferField, byteCount$iv);
            if (read$iv == -1) {
                throw new EOFException();
            }
            byteCount$iv -= read$iv;
            emitCompleteSegments();
        }
        return this;
    }

    @Override // okio.BufferedSink
    public BufferedSink writeByte(int b) {
        if (this.closed) {
            throw new IllegalStateException("closed".toString());
        }
        this.bufferField.writeByte(b);
        return emitCompleteSegments();
    }

    @Override // okio.BufferedSink
    public BufferedSink writeShort(int s) {
        if (this.closed) {
            throw new IllegalStateException("closed".toString());
        }
        this.bufferField.writeShort(s);
        return emitCompleteSegments();
    }

    @Override // okio.BufferedSink
    public BufferedSink writeShortLe(int s) {
        if (this.closed) {
            throw new IllegalStateException("closed".toString());
        }
        this.bufferField.writeShortLe(s);
        return emitCompleteSegments();
    }

    @Override // okio.BufferedSink
    public BufferedSink writeInt(int i) {
        if (this.closed) {
            throw new IllegalStateException("closed".toString());
        }
        this.bufferField.writeInt(i);
        return emitCompleteSegments();
    }

    @Override // okio.BufferedSink
    public BufferedSink writeIntLe(int i) {
        if (this.closed) {
            throw new IllegalStateException("closed".toString());
        }
        this.bufferField.writeIntLe(i);
        return emitCompleteSegments();
    }

    @Override // okio.BufferedSink
    public BufferedSink writeLong(long v) {
        if (this.closed) {
            throw new IllegalStateException("closed".toString());
        }
        this.bufferField.writeLong(v);
        return emitCompleteSegments();
    }

    @Override // okio.BufferedSink
    public BufferedSink writeLongLe(long v) {
        if (this.closed) {
            throw new IllegalStateException("closed".toString());
        }
        this.bufferField.writeLongLe(v);
        return emitCompleteSegments();
    }

    @Override // okio.BufferedSink
    public BufferedSink writeDecimalLong(long v) {
        if (this.closed) {
            throw new IllegalStateException("closed".toString());
        }
        this.bufferField.writeDecimalLong(v);
        return emitCompleteSegments();
    }

    @Override // okio.BufferedSink
    public BufferedSink writeHexadecimalUnsignedLong(long v) {
        if (this.closed) {
            throw new IllegalStateException("closed".toString());
        }
        this.bufferField.writeHexadecimalUnsignedLong(v);
        return emitCompleteSegments();
    }

    @Override // okio.BufferedSink
    public BufferedSink emitCompleteSegments() throws IOException {
        if (this.closed) {
            throw new IllegalStateException("closed".toString());
        }
        long byteCount$iv = this.bufferField.completeSegmentByteCount();
        if (byteCount$iv > 0) {
            this.sink.write(this.bufferField, byteCount$iv);
        }
        return this;
    }

    @Override // okio.BufferedSink
    public BufferedSink emit() throws IOException {
        if (this.closed) {
            throw new IllegalStateException("closed".toString());
        }
        long byteCount$iv = this.bufferField.size();
        if (byteCount$iv > 0) {
            this.sink.write(this.bufferField, byteCount$iv);
        }
        return this;
    }

    @Override // okio.BufferedSink
    public OutputStream outputStream() {
        return new OutputStream() { // from class: okio.RealBufferedSink.outputStream.1
            @Override // java.io.OutputStream
            public void write(int b) throws IOException {
                if (buffer.this.closed) {
                    throw new IOException("closed");
                }
                buffer this_$iv = buffer.this;
                int $i$f$getBuffer = (byte) b;
                this_$iv.bufferField.writeByte($i$f$getBuffer);
                buffer.this.emitCompleteSegments();
            }

            @Override // java.io.OutputStream
            public void write(byte[] data, int offset, int byteCount) throws IOException {
                Intrinsics.checkNotNullParameter(data, "data");
                if (buffer.this.closed) {
                    throw new IOException("closed");
                }
                buffer this_$iv = buffer.this;
                this_$iv.bufferField.write(data, offset, byteCount);
                buffer.this.emitCompleteSegments();
            }

            @Override // java.io.OutputStream, java.io.Flushable
            public void flush() throws IOException {
                if (!buffer.this.closed) {
                    buffer.this.flush();
                }
            }

            @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
            public void close() throws Throwable {
                buffer.this.close();
            }

            public String toString() {
                return buffer.this + ".outputStream()";
            }
        };
    }

    @Override // okio.BufferedSink, okio.Sink, java.io.Flushable
    public void flush() throws IOException {
        if (this.closed) {
            throw new IllegalStateException("closed".toString());
        }
        if (this.bufferField.size() > 0) {
            this.sink.write(this.bufferField, this.bufferField.size());
        }
        this.sink.flush();
    }

    @Override // java.nio.channels.Channel
    public boolean isOpen() {
        return !this.closed;
    }

    @Override // okio.Sink, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws Throwable {
        if (this.closed) {
            return;
        }
        Throwable thrown$iv = null;
        try {
            if (this.bufferField.size() > 0) {
                this.sink.write(this.bufferField, this.bufferField.size());
            }
        } catch (Throwable e$iv) {
            thrown$iv = e$iv;
        }
        try {
            this.sink.close();
        } catch (Throwable e$iv2) {
            if (thrown$iv == null) {
                thrown$iv = e$iv2;
            }
        }
        this.closed = true;
        if (thrown$iv == null) {
        } else {
            throw thrown$iv;
        }
    }

    @Override // okio.Sink
    /* JADX INFO: renamed from: timeout */
    public Timeout getTimeout() {
        return this.sink.getTimeout();
    }

    public String toString() {
        return "buffer(" + this.sink + ')';
    }
}
