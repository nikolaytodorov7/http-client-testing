package httpclient;

import jdk.jfr.Description;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
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

    @Test
    @Description("Tests if requests sends default headers")
    void sentDefaultHeaders() throws URISyntaxException, IOException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://postman-echo.com/post"))
                .POST(HttpRequest.BodyPublishers.ofString("АABCDEFGHIKLMNOPQRSTVXYZ"))
                .build();

        HttpClient client = new HttpClient();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        String host = request.headers().get("Host");
        String acceptEncoding = request.headers().get("Accept-encoding");
        String acceptCharset = request.headers().get("Accept-charset");
        String contentLength = request.headers().get("Content-length");
        assertAll(
                () -> assertEquals("postman-echo.com", host),
                () -> assertEquals("utf-8", acceptCharset),
                () -> assertEquals("gzip, deflate", acceptEncoding),
                () -> assertEquals("25", contentLength));
    }

    @Test
    @Description("Tests if client sends correct header after modifying one of default headers")
    void sendCorrectHeaderAfterModifying() throws URISyntaxException, IOException {
        String charset = "utf-16";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://postman-echo.com/post"))
                .POST(HttpRequest.BodyPublishers.ofString("АABCDEFGHIKLMNOPQRSTVXYZ"))
                .headers("Accept-charset", charset)
                .build();

        HttpClient client = new HttpClient();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        String acceptCharset = request.headers().get("Accept-charset");
        assertEquals(charset, acceptCharset);
    }

    @Test
    @Description("Tests if client sets automatically content-length after giving string body")
    void automaticContentLengthSetStringBody() throws IOException, URISyntaxException {
        String body = "123456789wolqeptj4t2h098io4p3";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://postman-echo.com/post"))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        contentLengthTest(request, String.valueOf(body.getBytes().length));
    }

    @Test
    @Description("Tests if client does not set content-length after giving no body")
    void automaticContentLengthSetNoBody() throws IOException, URISyntaxException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://postman-echo.com/post"))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        contentLengthTest(request, null);
    }

    @Test
    @Description("Tests if client sets automatically content-length after giving byte array body")
    void automaticContentLengthSetByteBody() throws IOException, URISyntaxException {
        byte[] bytes = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://postman-echo.com/post"))
                .POST(HttpRequest.BodyPublishers.ofByteArray(bytes))
                .build();

        contentLengthTest(request, String.valueOf(bytes.length));
    }

    @Test
    @Description("Tests if client sets automatically content-length after giving input stream body")
    void automaticContentLengthSetInputStreamBody() throws IOException, URISyntaxException {
        byte[] bytes = "testing".getBytes();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://postman-echo.com/post"))
                .POST(HttpRequest.BodyPublishers.ofInputStream(() -> byteArrayInputStream))
                .build();

        contentLengthTest(request, String.valueOf(bytes.length));
    }

    private static void contentLengthTest(HttpRequest request, String length) throws IOException {
        String contentLength = request.headers().get("Content-length");
        assertNull(contentLength);

        HttpClient client = new HttpClient();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        contentLength = request.headers().get("Content-length");
        assertEquals(contentLength, length);
    }
}