package httpclient;

import jdk.jfr.Description;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HttpRequestTest {

    @Test
    @Description("Tests if builder is not null after creation.")
    void testNewBuilderNotNull() {
        HttpRequestBuilder builder = HttpRequest.newBuilder();
        assertNotNull(builder);
    }

    @Test
    @Description("Tests if get method is set from builder to request.")
    void testGetMethod() {
        HttpRequestBuilder builder = Mockito.mock(HttpRequestBuilder.class);
        builder.method = "GET";
        HttpRequest request = new HttpRequest(builder);
        assertEquals("GET", request.method());
    }

    @Test
    @Description("Tests if uri is set from builder to request.")
    void getUri() throws URISyntaxException {
        HttpRequestBuilder builder = Mockito.mock(HttpRequestBuilder.class);
        builder.uri = new URI("https://postman-echo.com/get");
        builder.headers = new HashMap<>();
        HttpRequest request = new HttpRequest(builder);
        assertEquals(new URI("https://postman-echo.com/get"), request.uri());
    }

    @Test
    @Description("Tests if headers are set from builder to request.")
        //todo @BEFOREALL builder
    void getHeaders() {
        Map<String, String> headers = Map.of("key", "value");
        HttpRequestBuilder builder = Mockito.mock(HttpRequestBuilder.class);
        builder.headers = new HashMap<>(headers);
        HttpRequest request = new HttpRequest(builder);
        assertEquals(headers, request.headers());
    }

    @Test
    @Description("Tests if correct bodyPublisher is set.")
    void setBodyPublisherOfString() {
        HttpRequestBuilder builder = Mockito.mock(HttpRequestBuilder.class);
        BodyPublisher stringPublisher = HttpRequest.BodyPublishers.ofString("body");
        builder.bodyPublisher = stringPublisher;
        HttpRequest request = new HttpRequest(builder);
        assertEquals(stringPublisher, request.bodyPublisher);
    }

    @Test
    @Description("Tests if correct bodyPublisher is set.")
    void setBodyPublisherOfStringWithCharset() {
        HttpRequestBuilder builder = Mockito.mock(HttpRequestBuilder.class);
        BodyPublisher stringPublisher = HttpRequest.BodyPublishers.ofString("body", StandardCharsets.UTF_8);
        builder.bodyPublisher = stringPublisher;
        HttpRequest request = new HttpRequest(builder);
        assertEquals(stringPublisher, request.bodyPublisher);
    }

    @Test
    @Description("Tests if correct bodyPublisher is set.")
    void setBodyPublisherOfByteArray() {
        HttpRequestBuilder builder = Mockito.mock(HttpRequestBuilder.class);
        BodyPublisher bytePublisher = HttpRequest.BodyPublishers.ofByteArray(new byte[]{3, 124, 12, 31, 125});
        builder.bodyPublisher = bytePublisher;
        HttpRequest request = new HttpRequest(builder);
        assertEquals(bytePublisher, request.bodyPublisher);
    }

    @Test
    @Description("Tests if correct bodyPublisher is set.")
    void setBodyPublisherOfInputStream() {
        HttpRequestBuilder builder = Mockito.mock(HttpRequestBuilder.class);
        BodyPublisher inputStreamPublisher = HttpRequest.BodyPublishers.ofInputStream(() -> new ByteArrayInputStream(new byte[]{1, 3}));
        builder.bodyPublisher = inputStreamPublisher;
        HttpRequest request = new HttpRequest(builder);
        assertEquals(inputStreamPublisher, request.bodyPublisher);
    }

    @Test
    @Description("Tests if correct bodyPublisher is set.")
    void setNoBodyPublisher() {
        HttpRequestBuilder builder = Mockito.mock(HttpRequestBuilder.class);
        BodyPublisher noBodyPublisher = HttpRequest.BodyPublishers.noBody();
        builder.bodyPublisher = noBodyPublisher;
        HttpRequest request = new HttpRequest(builder);
        assertEquals(noBodyPublisher, request.bodyPublisher);
    }

    @Test
    @Description("Tests if request generates default headers")
    void generateDefaultHeaders() throws URISyntaxException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://postman-echo.com/post"))
                .GET()
                .build();

        String host = request.headers().get("Host");
        String acceptEncoding = request.headers().get("Accept-encoding");
        String acceptCharset = request.headers().get("Accept-charset");
        assertAll(
                () -> assertEquals("postman-echo.com", host),
                () -> assertEquals("utf-8", acceptCharset),
                () -> assertEquals("gzip, deflate", acceptEncoding));
    }
}