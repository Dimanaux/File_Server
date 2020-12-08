package server.http;

import java.util.Base64;
import java.util.function.BiConsumer;

/**
 * PSEUDO HTTP SPECIFICATION
 * It is a text based protocol.
 * Each request must have 1 line and it should be terminated with newline.
 *
 * Supported methods:
 * PUT: create a file
 *   PUT path content - content can be encoded with base64
 * GET: download a file
 *   GET BY_NAME path
 *   GET BY_ID id
 * DELETE: delete a file
 *   DELETE BY_NAME path
 *   DELETE BY_ID id
 * EXIT: close connection (no options)
 */
public interface Request {
    String serialize();

    default void ifGet(BiConsumer<ParamType, String> action) {}
    default void ifPut(BiConsumer<String, byte[]> action) {}
    default void ifExit(Runnable runnable) {}
    default void ifDelete(BiConsumer<ParamType, String> action) {}

    static String encode(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    static byte[] decode(String string) {
        return Base64.getDecoder().decode(string);
    }

    static Request parse(String data) {
        switch (data.split(" ")[0]) {
            case "PUT": return PutRequest.parse(data);
            case "GET": return GetRequest.parse(data);
            case "EXIT": return ExitRequest.parse(data);
            case "DELETE": return DeleteRequest.parse(data);
            default: throw new RuntimeException("INVALID REQUEST METHOD: " + data);
        }
    }
}
