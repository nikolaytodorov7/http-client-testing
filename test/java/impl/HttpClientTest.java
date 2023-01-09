package httpclient;

import jdk.jfr.Description;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;

class HttpClientTest {

    @Test
    @Description("Test if we receive not null builder")
    void newBuilderNotNull() {
        HttpClientBuilder builder = HttpClient.newBuilder();
        assertNotNull(builder);
    }

    @Test
    @Description("Test if we receive not null response")
    void sendReturnResponse() throws URISyntaxException, IOException {
        HttpClient client = new HttpClient();
        HttpResponse<Object> send = client.send(HttpRequest.newBuilder().uri(new URI("https://postman-echo.com/post")).GET().build(), HttpResponse.BodyHandlers.ofString());
        assertNotNull(send);
    }
}