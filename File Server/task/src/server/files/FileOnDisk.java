package server.files;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public class FileOnDisk {
    private final Path path;

    public FileOnDisk(Path path) {
        this.path = path;
    }

    public Optional<byte[]> content() {
        try {
            return Optional.of(Files.readAllBytes(path));
        } catch (Exception e) {
            System.out.println("Couldn't find " + path + " file.");
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public boolean delete() {
        try {
            Files.delete(path);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
