package server.http;

import java.util.function.BiConsumer;

public class PutRequest implements Request {
    private final String path;
    private final byte[] body;

    public PutRequest(String path, byte[] body) {
        this.path = path;
        this.body = body;
    }

    @Override
    public void ifPut(BiConsumer<String, byte[]> action) {
        action.accept(path, body);
    }

    @Override
    public String serialize() {
        return String.format("PUT %s %s", path, Request.encode(body));
    }

    static PutRequest parse(String data) {
        String[] tokens = data.split(" ");
        assert tokens[0].equals("PUT");
        return new PutRequest(tokens[1], Request.decode(tokens[2]));
    }
}
