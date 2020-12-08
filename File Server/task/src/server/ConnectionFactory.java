package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionFactory implements Runnable {
    private final ServerSocket serverSocket;
    private final Pipe<Connection> pipe;

    private ConnectionFactory(ServerSocket serverSocket, Pipe<Connection> pipe) {
        this.serverSocket = serverSocket;
        this.pipe = pipe;
    }

    public ConnectionFactory(ServerSocket serverSocket) {
        this(serverSocket, new Pipe<>());
    }

    @Override
    public void run() {
        while (canAccept()) {
            try {
                Socket socket = serverSocket.accept();
                pipe.send(new Connection(socket));
            } catch (Exception e) {
                if (serverSocket.isClosed()) return;

                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

    public void subscribe(Observer<Connection> observer) {
        pipe.subscribe(observer);
    }

    private boolean canAccept() {
        return !serverSocket.isClosed() && !Thread.interrupted();
    }

    public void close() {
        try {
            if (serverSocket.isClosed()) return;
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
