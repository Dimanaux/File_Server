package client;

import server.Connection;

import java.net.InetAddress;
import java.net.Socket;

public class Main {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket(InetAddress.getByName("127.0.0.1"), server.Main.PORT);
        Connection connection = new Connection(socket);
        Client client = new Client(connection);
        client.run();
    }
}
