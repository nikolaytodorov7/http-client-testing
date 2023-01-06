package impl;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class HttpRequestTest {

    @Test
    void testNewBuilderNotNull() {
        HttpRequestBuilder builder = HttpRequest.newBuilder();
        assertNotNull(builder);
    }

    @Test
    void testGetMethod() {
        HttpRequestBuilder builder = Mockito.mock(HttpRequestBuilder.class);
        builder.method = "POST";
        HttpRequest request = new HttpRequest(builder);
        assertEquals("POST", request.method());
    }

    @Test
    void testGetUri() throws URISyntaxException {
        HttpRequestBuilder builder = Mockito.mock(HttpRequestBuilder.class);
        builder.uri = new URI("https://postman-echo.com/get");
        HttpRequest request = new HttpRequest(builder);
        assertEquals(new URI("https://postman-echo.com/get"), request.uri());
    }

    @Test
    void testGetHeaders() {
        HttpRequestBuilder builder = Mockito.mock(HttpRequestBuilder.class);
        builder.headers = new HashMap<>(Map.of("key", "value"));
        HttpRequest request = new HttpRequest(builder);
        assertEquals(Map.of("key", "value"), request.headers());
    }

    @Test
    void testBodyPublisherOfString() {
        HttpRequestBuilder builder = Mockito.mock(HttpRequestBuilder.class);
        BodyPublisher stringPublisher = HttpRequest.BodyPublishers.ofString("body");
        builder.bodyPublisher = stringPublisher;
        HttpRequest request = new HttpRequest(builder);
        assertEquals(stringPublisher, request.bodyPublisher);
    }

    @Test
    void testBodyPublisherOfStringWithCharset() {
        HttpRequestBuilder builder = Mockito.mock(HttpRequestBuilder.class);
        BodyPublisher stringPublisher = HttpRequest.BodyPublishers.ofString("body", StandardCharsets.UTF_8);
        builder.bodyPublisher = stringPublisher;
        HttpRequest request = new HttpRequest(builder);
        assertEquals(stringPublisher, request.bodyPublisher);
    }

    @Test
    void testBodyPublisherOfByteArray() {
        HttpRequestBuilder builder = Mockito.mock(HttpRequestBuilder.class);
        BodyPublisher bytePublisher = HttpRequest.BodyPublishers.ofByteArray(new byte[]{3, 124, 12, 31, 125});
        builder.bodyPublisher = bytePublisher;
        HttpRequest request = new HttpRequest(builder);
        assertEquals(bytePublisher, request.bodyPublisher);
    }

    @Test
    void testBodyPublisherOfInputStream() {
        HttpRequestBuilder builder = Mockito.mock(HttpRequestBuilder.class);
        BodyPublisher inputStreamPublisher = HttpRequest.BodyPublishers.ofInputStream(() -> new ByteArrayInputStream(new byte[]{1, 3}));
        builder.bodyPublisher = inputStreamPublisher;
        HttpRequest request = new HttpRequest(builder);
        assertEquals(inputStreamPublisher, request.bodyPublisher);
    }

    @Test
    void testNoBodyPublisher() {
        HttpRequestBuilder builder = Mockito.mock(HttpRequestBuilder.class);
        BodyPublisher noBodyPublisher = HttpRequest.BodyPublishers.noBody();
        builder.bodyPublisher = noBodyPublisher;
        HttpRequest request = new HttpRequest(builder);
        assertEquals(noBodyPublisher, request.bodyPublisher);
    }


}