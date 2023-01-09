package httpclient;

import jdk.jfr.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HttpRequestBuilderTest {
    private HttpRequestBuilder builder = null;

    @BeforeEach
    void setUp() {
        builder = HttpRequest.newBuilder();
    }

    @Test
    @Description("Tests if we get correct exception after building without URI")
    void exceptionBuildWithoutURI() {
        assertThrows(IllegalStateException.class, builder::build);
    }

    @Test
    @Description("Tests if we get correct exception after building without method")
    void testExceptionBuildWithoutMethod() throws URISyntaxException {
        builder = builder.uri(new URI("https://postman-echo.com/get"));
        assertThrows(IllegalStateException.class, builder::build);
    }

    @Test
    @Description("Tests if build does not return null")
    void testBuildNotNull() throws URISyntaxException {
        builder = HttpRequest.newBuilder().uri(new URI("https://postman-echo.com/get")).GET();
        HttpRequest request = builder.build();
        assertNotNull(request);
    }

    @Test
    @Description("Tests uri set")
    void testSetUri() throws URISyntaxException {
        URI uri = new URI("https://postman-echo.com/get");
        builder = builder.uri(uri);
        URI builderUri = builder.uri;
        assertEquals(uri, builderUri);
    }

    @Test
    @Description("Tests if after we set null uri exception is generated")
    void testNullURIException() {
        assertThrows(IllegalArgumentException.class, () -> builder.uri(null));
    }

    @Test
    @Description("Tests set method GET")
    void testSetGetMethod() {
        builder = builder.GET();
        String getMethod = "GET";
        String builderMethod = builder.method;
        assertEquals(getMethod, builderMethod);
    }

    @Test
    @Description("Tests set method POST")
    void testSetPostMethod() {
        builder = builder.POST(null);
        String postMethod = "POST";
        String builderMethod = builder.method;
        assertEquals(postMethod, builderMethod);
    }

    @Test
    @Description("Tests set method PUT")
    void testSetPutMethod() {
        builder = builder.PUT(null);
        String putMethod = "PUT";
        String builderMethod = builder.method;
        assertEquals(putMethod, builderMethod);
    }

    @Test
    @Description("Tests set method DELETE")
    void testSetDeleteMethod() {
        builder = builder.DELETE();
        String deleteMethod = "DELETE";
        String builderMethod = builder.method;
        assertEquals(deleteMethod, builderMethod);
    }

    @Test
    @Description("Tests set header")
    void testSetHeaderMethod() {
        builder = builder.header("key", "value");
        Map<String, String> expectedHeaders = new HashMap<>(Map.of("key", "value"));
        assertEquals(expectedHeaders, builder.headers);
    }

    @Test
    @Description("Tests set headers")
    void testSetHeadersMethod() {
        builder = builder.headers("key", "value", "key2", "value2");
        Map<String, String> expectedHeaders = new HashMap<>(Map.of("key", "value", "key2", "value2"));
        assertEquals(expectedHeaders, builder.headers);
    }

    @Test
    @Description("Tests set headers exception after giving odd number of headers")
    void testExceptionOddHeaders() {
        assertThrows(IllegalArgumentException.class, () -> builder.headers("key", "value", "key2"));
    }

    @Test
    @Description("Tests set headers after giving null headers")
    void testExceptionNullHeaders() {
        assertThrows(IllegalArgumentException.class, () -> builder.headers(null));
    }

    @Test
    @Description("Tests set headers after giving null value")
    void testExceptionHeadersOfNullValues() {
        assertThrows(IllegalArgumentException.class, () -> builder.headers("key", null));
    }
}