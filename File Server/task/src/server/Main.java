package server;

import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static final int PORT = 3000;

    private static final ExecutorService executorService = Executors.newFixedThreadPool(4);

    public static void main(String[] args) throws Exception {
        Server server = new Server(
                new ConnectionFactory(new ServerSocket(PORT)),
                executorService
        );
        executorService.submit(server);

        terminate();
        server.close();
    }

    public static void terminate() {
        try {
            executorService.awaitTermination(12, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            // ok
        }
    }
}
