import impl.HttpClient;
import impl.HttpRequest;
import impl.HttpResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws Exception {
//        test1();
//        test2();
//        test3();
//        test4();

        myTest();
    }

    private static void myTest() throws URISyntaxException, IOException {
        HttpRequest request3 = HttpRequest.newBuilder()
                .uri(new URI("https://postman-echo.com/post"))
                .headers("Content-Type", "text/plain;charset=UTF-8")
                .POST(HttpRequest.BodyPublishers.ofString("Awerfigjo3h1ti4ggk"))
                .build();

        HttpClient client = new HttpClient();
        HttpResponse<String> send = client.send(request3, HttpResponse.BodyHandlers.ofString());
        System.out.println(send.body());
    }

    private static void test1() throws URISyntaxException, IOException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://postman-echo.com/get"))
                .GET()
                .build();

        HttpClient client = HttpClient.newBuilder().build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private static void test2() throws URISyntaxException, IOException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://postman-echo.com/get"))
                .headers("key1", "value1", "key2", "value2")
                .GET()
                .build();

        HttpClient client = HttpClient.newBuilder().build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(new URI("https://postman-echo.com/get"))
                .header("key1", "value1")
                .header("key2", "value2")
                .GET()
                .build();

        client.send(request2, HttpResponse.BodyHandlers.ofString());
    }

    private static void test3() throws URISyntaxException, IOException {
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(new URI("https://postman-echo.com/post"))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(new URI("https://postman-echo.com/post"))
                .headers("Content-Type", "text/plain;charset=UTF-8")
                .POST(HttpRequest.BodyPublishers.ofString("Sample request body"))
                .build();

        byte[] sampleData1 = "Sample request body".getBytes();
        HttpRequest request3 = HttpRequest.newBuilder()
                .uri(new URI("https://postman-echo.com/post"))
                .headers("Content-Type", "text/plain;charset=UTF-8")
                .POST(HttpRequest.BodyPublishers.ofByteArray(sampleData1))
                .build();

        byte[] sampleData2 = "Sample request body".getBytes();
        HttpRequest request4 = HttpRequest.newBuilder()
                .uri(new URI("https://postman-echo.com/post"))
                .headers("Content-Type", "text/plain;charset=UTF-8")
                .POST(HttpRequest.BodyPublishers
                        .ofInputStream(() -> new ByteArrayInputStream(sampleData2)))
                .build();

        HttpRequest request5 = HttpRequest.newBuilder()
                .uri(new URI("https://postman-echo.com/post"))
                .headers("Content-Type", "text/plain;charset=UTF-8")
                .POST(HttpRequest.BodyPublishers.ofFile(
                        Paths.get("src/test/resources/sample.txt")))
                .build();
    }

    private static void test4() throws URISyntaxException, IOException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://postman-echo.com/get"))
                .headers("key1", "value1", "key2", "value2")
                .GET()
                .build();
        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}