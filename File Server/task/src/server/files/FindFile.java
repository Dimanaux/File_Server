package server.files;

import java.nio.file.Path;
import java.util.Optional;

public interface FindFile {
    Path root = Path.of(
            System.getProperty("user.dir")
//            , "File Server", "task"
    );

    Optional<Path> find();
}
