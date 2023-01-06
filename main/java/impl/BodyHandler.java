package impl;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.file.Path;

public class BodyHandler {
    FileOutputStream fileOutputStream = null;

    public BodyHandler() {
    }

    public BodyHandler(Path path) {
        try {
            this.fileOutputStream = new FileOutputStream(path.toFile());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(String.format("File '%s' not found!", path));
        }
    }
}
