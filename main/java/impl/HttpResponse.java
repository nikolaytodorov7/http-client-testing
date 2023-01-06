package impl;

import java.io.*;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class HttpResponse<T> {
    private static final Set<String> VALID_HTTP_PROTOCOLS = new HashSet<>(Set.of(
            "HTTP/0.9", "HTTP/1.0", "HTTP/1.1", "HTTP/2", "HTTP/3"));

    private Map<String, String> headers = new HashMap<>();
    private String protocol;
    private int statusCode;
    private String body;

    public HttpResponse(InputStream inputStream, FileOutputStream fileOutputStream) throws IOException {
        try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) { //todo replace with input
            extractStatusLine(bufferedReader);
            extractHeaders(bufferedReader);
            if (fileOutputStream == null)
                extractStringBody(bufferedReader);
            else
                extractFileBody(bufferedReader, fileOutputStream);
        }
    }

    private void extractStatusLine(BufferedReader bufferedReader) throws IOException {
        String line = bufferedReader.readLine();
        if (line == null)
            throw new IllegalStateException("Invalid response!");

        String[] split = line.split(" ");
        if (split.length < 2)
            throw new IllegalStateException("First line must contain protocol and status code");

        protocol = split[0];
        if (!VALID_HTTP_PROTOCOLS.contains(protocol))
            throw new IllegalStateException("Invalid protocol!");

        try {
            statusCode = Integer.parseInt(split[1]);
        } catch (NumberFormatException e) {
            throw new IllegalStateException("First part of the status code must be Integer!");
        }
    }

    private void extractFileBody(BufferedReader in, FileOutputStream fileOutputStream) {
        try {
            in.transferTo(new OutputStreamWriter(fileOutputStream));
        } catch (IOException e) {
            throw new RuntimeException("No output stream found!");
        }
    }

    private void extractStringBody(BufferedReader in) throws IOException {
        String contentLen = headers.get("Content-Length");
        if(contentLen==null)
            return;

        int contentLength = Integer.parseInt(contentLen);
        StringBuilder body = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null && contentLength > 0) {
            body.append(line).append(System.lineSeparator());
            contentLength -= line.length();
        }

        this.body = body.toString();
    }

    private void extractHeaders(BufferedReader in) throws IOException {
        String line;
        while ((line = in.readLine()) != null && !line.isBlank()) {
            String[] entry = line.split(": ");
            if (entry.length != 2)
                throw new IllegalStateException(String.format("Invalid headers '%s'", line));
             String key = entry[0];
            String value = entry[1];
            headers.put(key, value);
        }
    }

    public int statusCode() {
        return statusCode;
    }

    public Map<String, String> headers() {
        return headers;
    }

    public String body() {
        return body;
    }

    public static class BodyHandlers {
        public static BodyHandler ofString() {
            return new BodyHandler();
        }

        public static BodyHandler ofFile(Path file) {
            return new BodyHandler(file);
        }
    }
}