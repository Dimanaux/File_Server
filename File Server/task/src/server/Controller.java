package server;

import server.files.*;
import server.http.ParamType;
import server.http.Request;
import server.http.Response;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

public class Controller implements Observer<String> {
    private final Connection connection;

    public Controller(Connection connection) {
        this.connection = connection;
    }

    public Response process(Request request) {
        final Response[] response = new Response[1];
        request.ifExit(() -> response[0] = terminate());
        request.ifDelete((p, v) -> response[0] = deleteFile(p, v));
        request.ifGet((p, v) -> response[0] = readFile(p, v));
        request.ifPut((p, v) -> response[0] = createFile(p, v));
        return response[0];
    }

    private Response terminate() {
        return Response.success("");
    }

    private Response deleteFile(ParamType paramType, String s) {
        Optional<FileOnDisk> file = findFile(paramType, s);
        return file.map(FileOnDisk::delete)
                .filter(x -> x)
                .map(_x -> Response.success(""))
                .orElse(Response.fail());
    }

    private Response readFile(ParamType paramType, String s) {
        return findFile(paramType, s)
                .flatMap(FileOnDisk::content)
                .map(Response::file).orElse(Response.fail());
    }

    private Response createFile(String name, byte[] content) {
        if (name.isBlank()) {
            name = UUID.randomUUID().toString();
        }
        NewFile newFile = new NewFile(name, content);
        try {
            newFile.save();
            String id = String.valueOf(new Random().nextInt(100000));
            Path idPath = new FileById(id).getPath();
            Files.createDirectories(idPath.getParent());
            Files.createSymbolicLink(
                    idPath, new FileByName(name).find().orElseThrow()
            );
            return Response.success(id);
        } catch (IOException e) {
            e.printStackTrace();
            return Response.fail("");
        }
    }

    private Optional<FileOnDisk> findFile(ParamType paramType, String value) {
        FindFile findFile = paramType == ParamType.BY_ID ? new FileById(value) : new FileByName(value);
        Optional<Path> path = findFile.find();
        return path.map(FileOnDisk::new);
    }

    @Override
    public void onNext(String data) {
        Request request = Request.parse(data);
        Response response = process(request);
        connection.send(response.serialize());
    }
}
