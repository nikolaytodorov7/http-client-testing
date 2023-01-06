package impl;

public class HttpClientBuilder {
    public HttpClient build() {
        return new HttpClient(this);
    }
}
