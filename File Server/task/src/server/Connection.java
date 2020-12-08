package server;

import java.io.*;
import java.net.Socket;

public class Connection implements Runnable {
    private final Socket socket;
    private final Pipe<String> pipe;
    private PrintWriter output = null;
    private BufferedReader input = null;

    private Connection(Socket socket, Pipe<String> requestPipe) {
        this.socket = socket;
        this.pipe = requestPipe;
    }

    public Connection(Socket socket) {
        this(socket, new Pipe<>());
    }

    public void send(String message) {
        output().println(message);
    }

    public void subscribe(Observer<String> observer) {
        pipe.subscribe(observer);
    }

    @Override
    public void run() {
        BufferedReader reader = input();
        try {
            String message = reader.readLine();
            if (message == null) {
                return;
            }
            pipe.send(message);
        } catch (IOException e) {
            e.printStackTrace();
            if (!socket.isClosed()) {
                throw new RuntimeException(e);
            }
        }
    }

    private BufferedReader input() {
        if (input == null) {
            try {
                input = new BufferedReader(
                        new InputStreamReader(
                                socket.getInputStream()
                        )
                );
            } catch (IOException e) {
                // if socket is closed it is ok
                if (socket.isClosed()) return new BufferedReader(Reader.nullReader());

                e.printStackTrace();
                throw new RuntimeException("Error when getting socket input stream", e);
            }
        }
        return input;
    }

    private PrintWriter output() {
        if (output == null) {
            try {
                output = new PrintWriter(
                        new OutputStreamWriter(
                                socket.getOutputStream()
                        ),
                        true
                );
            } catch (Exception e) {
                // if socket is closed it is ok
                if (socket.isClosed()) return new PrintWriter(Writer.nullWriter());

                e.printStackTrace();
                throw new RuntimeException("Error when getting socket output stream", e);
            }
        }
        return output;
    }

    public void close() {
        try {
            pipe.destroy();
            if (socket.isClosed()) return;
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
