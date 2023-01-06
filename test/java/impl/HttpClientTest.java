package impl;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;

class HttpClientTest {

    @Test
    void testNewBuilderNotNull() {
        HttpClientBuilder builder = HttpClient.newBuilder();
        assertNotNull(builder);
    }
}