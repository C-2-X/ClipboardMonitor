package okhttp3.internal.http;

import androidx.constraintlayout.widget.ConstraintLayout;
import java.io.EOFException;
import java.util.ArrayList;
import java.util.List;
import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import okhttp3.Challenge;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Response;
import okhttp3.internal.Util;
import okhttp3.internal.platform.Platform;
import okio.Buffer;
import okio.ByteString;

/* JADX INFO: compiled from: HttpHeaders.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(d1 = {"\u0000R\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0005\n\u0000\u001a\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007\u001a\u0018\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b*\u00020\n2\u0006\u0010\u000b\u001a\u00020\f\u001a\n\u0010\r\u001a\u00020\u0004*\u00020\u0006\u001a\u001a\u0010\u000e\u001a\u00020\u000f*\u00020\u00102\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\t0\u0012H\u0002\u001a\u000e\u0010\u0013\u001a\u0004\u0018\u00010\f*\u00020\u0010H\u0002\u001a\u000e\u0010\u0014\u001a\u0004\u0018\u00010\f*\u00020\u0010H\u0002\u001a\u001a\u0010\u0015\u001a\u00020\u000f*\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\n\u001a\f\u0010\u001a\u001a\u00020\u0004*\u00020\u0010H\u0002\u001a\u0014\u0010\u001b\u001a\u00020\u0004*\u00020\u00102\u0006\u0010\u001c\u001a\u00020\u001dH\u0002\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u001e"}, d2 = {"QUOTED_STRING_DELIMITERS", "Lokio/ByteString;", "TOKEN_DELIMITERS", "hasBody", HttpUrl.FRAGMENT_ENCODE_SET, "response", "Lokhttp3/Response;", "parseChallenges", HttpUrl.FRAGMENT_ENCODE_SET, "Lokhttp3/Challenge;", "Lokhttp3/Headers;", "headerName", HttpUrl.FRAGMENT_ENCODE_SET, "promisesBody", "readChallengeHeader", HttpUrl.FRAGMENT_ENCODE_SET, "Lokio/Buffer;", "result", HttpUrl.FRAGMENT_ENCODE_SET, "readQuotedString", "readToken", "receiveHeaders", "Lokhttp3/CookieJar;", "url", "Lokhttp3/HttpUrl;", "headers", "skipCommasAndWhitespace", "startsWith", "prefix", HttpUrl.FRAGMENT_ENCODE_SET, "okhttp"}, k = 2, mv = {1, 8, 0}, xi = ConstraintLayout.LayoutParams.Table.LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE)
public final class HttpHeaders {
    private static final ByteString QUOTED_STRING_DELIMITERS = ByteString.INSTANCE.encodeUtf8("\"\\");
    private static final ByteString TOKEN_DELIMITERS = ByteString.INSTANCE.encodeUtf8("\t ,=");

    public static final List<Challenge> parseChallenges(Headers $this$parseChallenges, String headerName) {
        Intrinsics.checkNotNullParameter($this$parseChallenges, "<this>");
        Intrinsics.checkNotNullParameter(headerName, "headerName");
        List result = new ArrayList();
        int size = $this$parseChallenges.size();
        for (int h = 0; h < size; h++) {
            if (StringsKt.equals(headerName, $this$parseChallenges.name(h), true)) {
                Buffer header = new Buffer().writeUtf8($this$parseChallenges.value(h));
                try {
                    readChallengeHeader(header, result);
                } catch (EOFException e) {
                    Platform.INSTANCE.get().log("Unable to parse challenge", 5, e);
                }
            }
        }
        return result;
    }

    /* JADX WARN: Code restructure failed: missing block: B:48:0x00c5, code lost:
    
        r11.add(new okhttp3.Challenge(r1, (java.util.Map<java.lang.String, java.lang.String>) r6));
     */
    /* JADX WARN: Removed duplicated region for block: B:28:0x008e  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x00c5 A[EDGE_INSN: B:65:0x00c5->B:48:0x00c5 BREAK  A[LOOP:1: B:23:0x007c->B:66:0x007c], SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static final void readChallengeHeader(okio.Buffer r10, java.util.List<okhttp3.Challenge> r11) throws java.io.EOFException {
        /*
            Method dump skipped, instruction units count: 207
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: okhttp3.internal.http.HttpHeaders.readChallengeHeader(okio.Buffer, java.util.List):void");
    }

    private static final boolean skipCommasAndWhitespace(Buffer $this$skipCommasAndWhitespace) throws EOFException {
        boolean commaFound = false;
        while (!$this$skipCommasAndWhitespace.exhausted()) {
            byte b = $this$skipCommasAndWhitespace.getByte(0L);
            if (b == 44) {
                $this$skipCommasAndWhitespace.readByte();
                commaFound = true;
            } else {
                boolean z = true;
                if (b != 32 && b != 9) {
                    z = false;
                }
                if (!z) {
                    break;
                }
                $this$skipCommasAndWhitespace.readByte();
            }
        }
        return commaFound;
    }

    private static final boolean startsWith(Buffer $this$startsWith, byte prefix) {
        return !$this$startsWith.exhausted() && $this$startsWith.getByte(0L) == prefix;
    }

    private static final String readQuotedString(Buffer $this$readQuotedString) throws EOFException {
        if (!($this$readQuotedString.readByte() == 34)) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        Buffer result = new Buffer();
        while (true) {
            long i = $this$readQuotedString.indexOfElement(QUOTED_STRING_DELIMITERS);
            if (i == -1) {
                return null;
            }
            if ($this$readQuotedString.getByte(i) == 34) {
                result.write($this$readQuotedString, i);
                $this$readQuotedString.readByte();
                return result.readUtf8();
            }
            if ($this$readQuotedString.size() == i + 1) {
                return null;
            }
            result.write($this$readQuotedString, i);
            $this$readQuotedString.readByte();
            result.write($this$readQuotedString, 1L);
        }
    }

    private static final String readToken(Buffer $this$readToken) {
        long tokenSize = $this$readToken.indexOfElement(TOKEN_DELIMITERS);
        if (tokenSize == -1) {
            tokenSize = $this$readToken.size();
        }
        if (tokenSize != 0) {
            return $this$readToken.readUtf8(tokenSize);
        }
        return null;
    }

    public static final void receiveHeaders(CookieJar $this$receiveHeaders, HttpUrl url, Headers headers) {
        Intrinsics.checkNotNullParameter($this$receiveHeaders, "<this>");
        Intrinsics.checkNotNullParameter(url, "url");
        Intrinsics.checkNotNullParameter(headers, "headers");
        if ($this$receiveHeaders == CookieJar.NO_COOKIES) {
            return;
        }
        List<Cookie> all = Cookie.INSTANCE.parseAll(url, headers);
        if (all.isEmpty()) {
            return;
        }
        $this$receiveHeaders.saveFromResponse(url, all);
    }

    public static final boolean promisesBody(Response $this$promisesBody) {
        Intrinsics.checkNotNullParameter($this$promisesBody, "<this>");
        if (Intrinsics.areEqual($this$promisesBody.request().method(), "HEAD")) {
            return false;
        }
        int responseCode = $this$promisesBody.code();
        return (((responseCode >= 100 && responseCode < 200) || responseCode == 204 || responseCode == 304) && Util.headersContentLength($this$promisesBody) == -1 && !StringsKt.equals("chunked", Response.header$default($this$promisesBody, "Transfer-Encoding", null, 2, null), true)) ? false : true;
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "No longer supported", replaceWith = @ReplaceWith(expression = "response.promisesBody()", imports = {}))
    public static final boolean hasBody(Response response) {
        Intrinsics.checkNotNullParameter(response, "response");
        return promisesBody(response);
    }
}
