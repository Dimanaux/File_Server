package server.files;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public class FileById implements FindFile {
    static Path idRoot = FindFile.root.resolve("src")
            .resolve("server").resolve("data").resolve("ids");

    private final Path path;

    public FileById(Path path) {
        this.path = path;
    }

    public FileById(String id) {
        this(idRoot.resolve(id));
    }

    public Path getPath() {
        return path;
    }

    @Override
    public Optional<Path> find() {
        try {
            return Optional.ofNullable(Files.readSymbolicLink(path));
        } catch (IOException e) {
//            e.printStackTrace();
            return Optional.empty();
        }
    }
}
