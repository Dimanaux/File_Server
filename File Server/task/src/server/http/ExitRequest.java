package server.http;

public class ExitRequest implements Request {
    public static ExitRequest instance = new ExitRequest();

    private ExitRequest() {}

    @Override
    public void ifExit(Runnable runnable) {
        runnable.run();
    }

    @Override
    public String serialize() {
        return "EXIT";
    }

    static ExitRequest parse(String data) {
        assert data.equals("EXIT");
        return instance;
    }
}
