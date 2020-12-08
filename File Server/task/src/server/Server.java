package server;

import server.http.Request;

import java.util.concurrent.ExecutorService;

public class Server implements Observer<Connection>, Runnable {
    private final ConnectionFactory connectionFactory;
    private final ExecutorService executorService;

    public Server(ConnectionFactory connectionFactory, ExecutorService executorService) {
        this.connectionFactory = connectionFactory;
        this.executorService = executorService;
        connectionFactory.subscribe(this);
    }

    @Override
    public void onNext(Connection connection) {
        connection.subscribe(new Controller(connection));
        executorService.submit(connection);
        connection.subscribe(this::closeOnExit);
    }

    private void closeOnExit(String data) {
        Request.parse(data).ifExit(this::close);
    }

    @Override
    public void run() {
        connectionFactory.run();
    }

    public void close() {
        connectionFactory.close();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }
}
