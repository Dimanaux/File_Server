package server.http;

import java.util.function.BiConsumer;

public class GetRequest implements Request {
    final ParamType paramType;
    final String value;

    public GetRequest(ParamType paramType, String value) {
        this.paramType = paramType;
        this.value = value;
    }

    @Override
    public void ifGet(BiConsumer<ParamType, String> action) {
        action.accept(paramType, value);
    }

    @Override
    public String serialize() {
        return String.format("GET %s %s", paramType.name(), value);
    }

    static GetRequest parse(String data) {
        String[] tokens = data.split(" ");
        assert tokens[0].equals("GET");
        return new GetRequest(ParamType.valueOf(tokens[1]), tokens[2]);
    }
}
