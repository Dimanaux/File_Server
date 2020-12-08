package server.http;

public class Response {
    public final boolean success;
    public final String message;

    public Response(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public String serialize() {
        return String.format("%s %s", success, message);
    }

    public static Response success(String s) {
        return new Response(true, s);
    }

    public static Response file(byte[] content) {
        return success(Request.encode(content));
    }

    public static Response fail(String message) {
        return new Response(false, message);
    }

    public static Response fail() {
        return fail("");
    }

    public static Response parse(String data) {
        String[] tokens = data.split(" ");
        String message = tokens.length > 1 ? tokens[1] : "";
        return new Response(Boolean.parseBoolean(tokens[0]), message);
    }
}
