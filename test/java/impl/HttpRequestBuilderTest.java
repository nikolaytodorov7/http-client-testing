package impl;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HttpRequestBuilderTest {

    @Test
    void testExceptionBuildWithoutURI() {
        HttpRequestBuilder builder = HttpRequest.newBuilder().GET();
        assertThrows(IllegalStateException.class, builder::build);
    }

    @Test
    void testExceptionBuildWithoutMethod() throws URISyntaxException {
        HttpRequestBuilder builder = HttpRequest.newBuilder().uri(new URI("https://postman-echo.com/get"));
        assertThrows(IllegalStateException.class, builder::build);
    }

    @Test
    void testBuildNotNull() throws URISyntaxException {
        HttpRequestBuilder builder = HttpRequest.newBuilder().uri(new URI("https://postman-echo.com/get")).GET();
        HttpRequest request = builder.build();
        assertNotNull(request);
    }

    @Test
    void testSetUri() throws URISyntaxException {
        URI uri = new URI("https://postman-echo.com/get");
        HttpRequestBuilder builder = HttpRequest.newBuilder().uri(uri);
        URI builderUri = builder.uri;
        assertEquals(uri, builderUri);
    }

    @Test
    void testNullURIException() {
        HttpRequestBuilder builder = HttpRequest.newBuilder();
        assertThrows(IllegalArgumentException.class, () -> builder.uri(null));
    }

    @Test
    void testSetGetMethod() {
        HttpRequestBuilder builder = HttpRequest.newBuilder().GET();
        String getMethod = "GET";
        String builderMethod = builder.method;
        assertEquals(getMethod, builderMethod);
    }

    @Test
    void testSetPostMethod() {
        HttpRequestBuilder builder = HttpRequest.newBuilder().POST(null);
        String postMethod = "POST";
        String builderMethod = builder.method;
        assertEquals(postMethod, builderMethod);
    }

    @Test
    void testSetPutMethod() {
        HttpRequestBuilder builder = HttpRequest.newBuilder().PUT(null);
        String putMethod = "PUT";
        String builderMethod = builder.method;
        assertEquals(putMethod, builderMethod);
    }

    @Test
    void testSetDeleteMethod() {
        HttpRequestBuilder builder = HttpRequest.newBuilder().DELETE();
        String deleteMethod = "DELETE";
        String builderMethod = builder.method;
        assertEquals(deleteMethod, builderMethod);
    }

    @Test
    void testSetHeaderMethod() {
        HttpRequestBuilder builder = HttpRequest.newBuilder().header("key", "value");
        Map<String, String> expectedHeaders = new HashMap<>(Map.of("key", "value"));
        assertEquals(expectedHeaders, builder.headers);
    }

    @Test
    void testSetHeadersMethod() {
        HttpRequestBuilder builder = HttpRequest.newBuilder().headers("key", "value", "key2", "value2");
        Map<String, String> expectedHeaders = new HashMap<>(Map.of("key", "value", "key2", "value2"));
        assertEquals(expectedHeaders, builder.headers);
    }

    @Test
    void testExceptionNotEvenHeaders() {
        HttpRequestBuilder builder = HttpRequest.newBuilder();
        assertThrows(IllegalArgumentException.class, () -> builder.headers("key", "value", "key2"));
    }

    @Test
    void testExceptionNullHeaders() {
        HttpRequestBuilder builder = HttpRequest.newBuilder();
        assertThrows(IllegalArgumentException.class, () -> builder.headers(null));
    }

    @Test
    void testExceptionHeadersOfNullValues() {
        HttpRequestBuilder builder = HttpRequest.newBuilder();
        assertThrows(IllegalArgumentException.class, () -> builder.headers("key", null));
    }
}