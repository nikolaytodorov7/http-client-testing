package httpclient;

import jdk.jfr.Description;
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
    @Description("Tests if response throws exception after receiving null status line")
    <T> void responseNullStatusLineException() {
        assertThrows(IllegalStateException.class, () -> new HttpResponse<T>(InputStream.nullInputStream(), null));
    }

    @Test
    @Description("Tests if response throws correct exception after receiving invalid status line")
    <T> void responseStatusLineInvalidArgsException() throws IOException {
        String line = "oqkerkf 103je oaklp1 o02edjoef";
        assertThrows(IllegalStateException.class, () -> new HttpResponse<T>(new ByteArrayInputStream(line.getBytes()), null));
    }

    @Test
    @Description("Tests if response throws correct exception after receiving invalid headers")
    <T> void invalidHeadersException() {
        String line = "HTTP/1.1 200\nheader1: value2\nheader1value2";
        assertThrows(IllegalStateException.class, () -> new HttpResponse<T>(new ByteArrayInputStream(line.getBytes()), null));
    }

    @Test
    @Description("Tests if we get back headers sent as body")
    void testHeadersInBody() throws URISyntaxException, IOException {
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
    @Description("Tests if throws exception after sending body before headers")
    <T> void sendBodyBeforeHeadersException() {
        String line = "HTTP/1.1 200\nbody......\n\nheader1: value2\nheader1value2";
        assertThrows(IllegalStateException.class, () -> new HttpResponse<T>(new ByteArrayInputStream(line.getBytes()), null));
    }
}