package impl;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HttpResponseTest {

    @Test
    void statusCode() {
    }

    @Test
    void testAutomaticContentLengthSet() throws IOException, URISyntaxException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://postman-echo.com/post"))
                .headers("Content-Type", "text/plain;charset=UTF-8")
                .POST(HttpRequest.BodyPublishers.ofString("123456789"))
                .build();

        HttpClient client = new HttpClient();
        HttpResponse<String> send = client.send(request, HttpResponse.BodyHandlers.ofString());
        String body = send.body();
        String[] split = body.split("\"content-length\":\"");
        split = split[1].split("\",");
        int contentLength = 9;
        assertEquals(contentLength, Integer.valueOf(split[0]));
    }

    @Test
    void testIfDefaultHeadersAreSent() throws URISyntaxException, IOException {
        HttpRequest request3 = HttpRequest.newBuilder()
                .uri(new URI("https://postman-echo.com/post"))
                .headers("Content-Type", "text/plain;charset=UTF-8")
                .POST(HttpRequest.BodyPublishers.ofString("АABCDEFGHIKLMNOPQRSTVXYZ"))
                .build();

        HttpClient client = new HttpClient();
        HttpResponse<String> send = client.send(request3, HttpResponse.BodyHandlers.ofString());
        String strBody = send.body();
        int i = strBody.indexOf("headers\":{");
        strBody = strBody.substring(i + 11);
        i = strBody.lastIndexOf("\"},");
        strBody = strBody.substring(0, i);
        String[] split = strBody.split("\",\"");
        Map<String, String> headers = new HashMap<>();
        for (String header : split) {
            String[] kvp = header.split("\":\"");
            headers.put(kvp[0], kvp[1]);
        }

        String host = headers.get("host");
        assertEquals("postman-echo.com", host);
        String contentType = headers.get("content-type");
        assertEquals("application/json", contentType);
        String acceptCharset = headers.get("accept-charset");
        assertEquals("utf-8", acceptCharset);
        String contentLength = headers.get("content-length");
        assertEquals("25", contentLength);
    }

    @Test
    <T> void testResponseNullStatusLineException() throws IOException {
        assertThrows(IllegalStateException.class, () -> new HttpResponse<T>(InputStream.nullInputStream(), null));
    }

    @Test
    <T> void testResponseInvalidNumberOfArgumentsStatusLineException() throws IOException {
        String line = "oqkerkf 103je oaklp1 o02edjoef";
        assertThrows(IllegalStateException.class, () -> new HttpResponse<T>(new ByteArrayInputStream(line.getBytes()), null));
    }

    @Test
    <T> void testInvalidHeadersException() {
        String line = "HTTP/1.1 200\nheader1: value2\nheader1value2";
        assertThrows(IllegalStateException.class, () -> new HttpResponse<T>(new ByteArrayInputStream(line.getBytes()), null));
    }
}