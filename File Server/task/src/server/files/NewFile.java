package server.files;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class NewFile {
    private final Path path;
    private final byte[] content;

    public NewFile(Path path, byte[] content) {
        this.path = path;
        this.content = content;
    }

    public NewFile(String name, byte[] content) {
        this(new FileByName(name).find().orElseThrow(), content);
    }

    public void save() throws IOException {
        if (!Files.exists(path)) {
            Files.createDirectories(path.getParent());
            Files.createFile(path);
        }
        Files.write(path, content);
    }
}
