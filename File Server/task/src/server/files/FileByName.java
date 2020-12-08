package server.files;

import java.nio.file.Path;
import java.util.Optional;

public class FileByName implements FindFile {
    static Path nameRoot = FindFile.root.resolve("src")
            .resolve("server").resolve("data");

    private final Path path;

    public FileByName(String name) {
        this(nameRoot.resolve(name));
    }

    public FileByName(Path path) {
        this.path = path;
    }

    @Override
    public Optional<Path> find() {
        return Optional.of(path);
    }
}
