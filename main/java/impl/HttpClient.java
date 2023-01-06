package impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URI;
import java.util.Map;

public class HttpClient {
    private final static int DEFAULT_HTTP_PORT = 80;

    public HttpClient() {
    }

    public HttpClient(HttpClientBuilder builder) {
    }

    public static HttpClientBuilder newBuilder() {
        return new HttpClientBuilder();
    }

    public <T> HttpResponse<T> send(HttpRequest request, BodyHandler bodyHandler) throws IOException {
        URI uri = request.uri();
        InetAddress address = InetAddress.getByName(uri.getHost());
        Socket socket = new Socket(address, DEFAULT_HTTP_PORT);
        OutputStream outputStream = socket.getOutputStream();
        sendHeaders(request, outputStream);
        sendBody(request, outputStream);

        HttpResponse<T> httpResponse = new HttpResponse<>(socket.getInputStream(), bodyHandler.fileOutputStream);
        socket.close();

        return httpResponse;
    }

    private static void sendHeaders(HttpRequest request, OutputStream outputStream) {
        URI uri = request.uri();
        PrintWriter out = new PrintWriter(outputStream, true);
        String requestLine = String.format("%s %s HTTP/1.1", request.method(), uri.getPath());
        out.println(requestLine);
        out.println(String.format("Host: %s", uri.getHost()));
        int available = 0;
        try {
            available = request.bodyPublisher.getBody().available();
        } catch (IOException ignored) {
        }

        if (available > 0)
            out.println(String.format("Content-length: %s", available));

        for (Map.Entry<String, String> entry : request.headers().entrySet()) {
            String headerLine = String.format("%s: %s\n", entry.getKey(), entry.getValue());
            out.println(headerLine);
        }

        out.println();
        out.flush();
    }

    private static void sendBody(HttpRequest request, OutputStream outputStream) throws IOException {
        BodyPublisher bodyPublisher = request.bodyPublisher;
        InputStream bodyInputStream = bodyPublisher.getBody();
        bodyInputStream.transferTo(outputStream);
        outputStream.flush();
    }
}
