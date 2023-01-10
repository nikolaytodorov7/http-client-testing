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
    <T> void responseStatusLineInvalidArgsException() {
        String line = "oqkerkf 103je oaklp1 o02edjoef";
        ByteArrayInputStream input = new ByteArrayInputStream(line.getBytes());
        assertThrows(IllegalStateException.class, () -> new HttpResponse<T>(input, null));
    }

    @Test
    @Description("Tests if response throws correct exception after receiving invalid headers")
    <T> void invalidHeadersException() {
        String line = "HTTP/1.1 200\nheader1: value2\nheader1value2";
        ByteArrayInputStream input = new ByteArrayInputStream(line.getBytes());
        assertThrows(IllegalStateException.class, () -> new HttpResponse<T>(input, null));
    }

    @Test
    @Description("Tests if throws exception after sending body before headers")
    <T> void sendBodyBeforeHeadersException() {
        String line = "HTTP/1.1 200\nbody......\n\nheader1: value2\nheader1value2";
        ByteArrayInputStream input = new ByteArrayInputStream(line.getBytes());
        assertThrows(IllegalStateException.class, () -> new HttpResponse<T>(input, null));
    }
}