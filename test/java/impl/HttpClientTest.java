package httpclient;

import jdk.jfr.Description;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

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
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://postman-echo.com/post"))
                .GET()
                .build();

        Socket socket = Mockito.mock(Socket.class);
        ByteArrayInputStream inputStream = new ByteArrayInputStream("HTTP/1.1 200\nheader1: value2\nheader2: value2".getBytes());
        when(socket.getInputStream()).thenReturn(inputStream);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        when(socket.getOutputStream()).thenReturn(outputStream);

        HttpClient client = Mockito.spy(HttpClient.class);
        when(client.createSocket(request)).thenReturn(socket);

        HttpResponse<Object> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertNotNull(httpResponse);
    }

    @Test
    @Description("Tests if client sends content-length header")
    void sentContentLengthHeader() throws URISyntaxException, IOException {
        String str = "aokerctg hktqeraomw";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://postman-echo.com/post"))
                .POST(HttpRequest.BodyPublishers.ofString(str))
                .build();

        Socket socket = Mockito.mock(Socket.class);
        ByteArrayInputStream inputStream = new ByteArrayInputStream("HTTP/1.1 200\nheader1: value2\nheader2: value2".getBytes());
        when(socket.getInputStream()).thenReturn(inputStream);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        when(socket.getOutputStream()).thenReturn(outputStream);

        HttpClient client = Mockito.spy(HttpClient.class);
        when(client.createSocket(request)).thenReturn(socket);
        client.send(request, HttpResponse.BodyHandlers.ofString());

        String[] split = outputStream.toString().split("Content-length: ");
        split = split[1].split("\r\n");
        String contentLength = split[0];
        assertEquals(String.valueOf(str.getBytes().length), contentLength);
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

        Socket socket = Mockito.mock(Socket.class);
        ByteArrayInputStream inputStream = new ByteArrayInputStream("HTTP/1.1 200\nheader1: value2\nheader2: value2".getBytes());
        when(socket.getInputStream()).thenReturn(inputStream);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        when(socket.getOutputStream()).thenReturn(outputStream);

        HttpClient client = Mockito.spy(HttpClient.class);
        when(client.createSocket(request)).thenReturn(socket);

        client.send(request, HttpResponse.BodyHandlers.ofString());

        String[] split = outputStream.toString().split("Accept-charset: ");
        split = split[1].split("\n");
        String sentCharset = split[0];
        assertEquals(charset, sentCharset);
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

    private static void contentLengthTest(HttpRequest request, String length) throws IOException {
        String contentLength = request.headers().get("Content-length");
        assertNull(contentLength);

        Socket socket = Mockito.mock(Socket.class);
        ByteArrayInputStream inputStream = new ByteArrayInputStream("HTTP/1.1 200\nheader1: value2\nheader2: value2".getBytes());
        when(socket.getInputStream()).thenReturn(inputStream);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        when(socket.getOutputStream()).thenReturn(outputStream);

        HttpClient client = Mockito.spy(HttpClient.class);
        when(client.createSocket(request)).thenReturn(socket);

        client.send(request, HttpResponse.BodyHandlers.ofString());

        String[] split = outputStream.toString().split("Content-length: ");
        if (split.length > 1) {
            split = split[1].split("\r\n");
            if (split.length > 1)
                contentLength = split[0];
        }

        assertEquals(length, contentLength);
    }
}