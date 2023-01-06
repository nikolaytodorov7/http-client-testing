package impl;

import java.io.InputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.Map;
import java.util.function.Supplier;

public class HttpRequest {
    private String method;
    private URI uri;
    private Map<String, String> headers;
    BodyPublisher bodyPublisher;

    public static HttpRequestBuilder newBuilder() {
        return new HttpRequestBuilder();
    }

    public HttpRequest(HttpRequestBuilder builder) {
        this.method = builder.method;
        this.uri = builder.uri;
        this.headers = builder.headers;
        setDefaultHeaders();
        this.bodyPublisher = builder.bodyPublisher;
    }

    private void setDefaultHeaders() {
        headers.put("Host", uri.getHost());
        headers.put("Accept-Charset", "utf-8");
        headers.put("Accept-Encoding", "gzip, deflate");
    }

    public String method() {
        return method;
    }

    public URI uri() {
        return uri;
    }

    public Map<String, String> headers() {
        return headers;
    }

    public static class BodyPublishers {
        public static BodyPublisher ofString(String body) {
            return ofString(body, Charset.defaultCharset());
        }

        public static BodyPublisher ofString(String body, Charset charset) {
            byte[] bytes = body.getBytes(charset);
            return new BodyPublisher(bytes);
        }

        public static BodyPublisher ofByteArray(byte[] buf) {
            return new BodyPublisher(buf);
        }

        public static BodyPublisher ofInputStream(Supplier<? extends InputStream> streamSupplier) {
            return new BodyPublisher(streamSupplier);
        }

        public static BodyPublisher noBody() {
            return new BodyPublisher();
        }

        public static BodyPublisher ofFile(Path path) {
            return new BodyPublisher(path);
        }
    }

    public String toString() {
        return uri + " " + method;
    }
}
