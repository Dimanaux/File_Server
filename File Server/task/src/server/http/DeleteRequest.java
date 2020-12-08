package server.http;

import java.util.function.BiConsumer;

public class DeleteRequest implements Request {
    final ParamType paramType;
    final String value;

    public DeleteRequest(ParamType paramType, String value) {
        this.paramType = paramType;
        this.value = value;
    }

    @Override
    public void ifDelete(BiConsumer<ParamType, String> action) {
        action.accept(paramType, value);
    }

    @Override
    public String serialize() {
        return String.format("DELETE %s %s", paramType.name(), value);
    }

    static DeleteRequest parse(String data) {
        String[] tokens = data.split(" ");
        assert tokens[0].equals("DELETE");
        return new DeleteRequest(ParamType.valueOf(tokens[1]), tokens[2]);
    }
}
