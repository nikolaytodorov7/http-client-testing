package impl;

import java.io.*;
import java.nio.file.Path;
import java.util.function.Supplier;

public class BodyPublisher {
    private InputStream inputStream;

    public BodyPublisher() {
        inputStream = InputStream.nullInputStream();
    }

    public BodyPublisher(byte[] body) {
        this.inputStream = new ByteArrayInputStream(body);
    }

    public BodyPublisher(Supplier<? extends InputStream> streamSupplier) {
        this.inputStream = streamSupplier.get();
    }

    public BodyPublisher(Path path) {
        try {
            File file = path.toFile();
            if (!file.exists() || !file.isFile()) {
                inputStream = InputStream.nullInputStream();
                return;
            }

            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(String.format("Invalid path '%s' provided!", path));
        }
    }

    public InputStream getBody() {
        return inputStream;
    }
}
