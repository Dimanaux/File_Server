package client;

import server.Connection;
import server.files.FileOnDisk;
import server.files.FindFile;
import server.files.NewFile;
import server.http.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;

public class Client implements Runnable {
    static Path root = FindFile.root.resolve("src")
            .resolve("client").resolve("data");

    private final Connection connection;
    Scanner scanner = new Scanner(System.in);

    public Client(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void run() {
        System.out.print("Enter action (1 - get the file, 2 - save the file, 3 - delete the file): ");
        String selection = scanner.nextLine();
        if (selection.equals("exit")) {
            sendRequest(ExitRequest.instance);
        } else if (selection.equals("1")) {
            System.out.print("Do you want to get the file by name or by id (1 - name, 2 - id): ");
            String option = scanner.nextLine();
            ParamType paramType = option.equals("1") ? ParamType.BY_NAME : ParamType.BY_ID;
            System.out.printf("Enter %s: ", paramType);
            String paramValue = scanner.nextLine();
            connection.subscribe(this::downloadFile);
            sendRequest(new GetRequest(paramType, paramValue));
        } else if (selection.equals("2")) {
            System.out.print("Enter name of the file: ");
            String localFilename = scanner.nextLine();
            FileOnDisk fileOnDisk = new FileOnDisk(root.resolve(localFilename));
            System.out.println("Enter name of the file to be saved on server:");
            String remotePath = scanner.nextLine();
            connection.subscribe(this::uploadFile);
            sendRequest(new PutRequest(remotePath, fileOnDisk.content().orElseThrow()));
        } else {
            System.out.print("Do you want to delete the file by name or by id (1 - name, 2 - id): ");
            String option = scanner.nextLine();
            ParamType paramType = option.equals("1") ? ParamType.BY_NAME : ParamType.BY_ID;
            System.out.printf("Enter %s: ", paramType);
            String paramValue = scanner.nextLine();
            connection.subscribe(this::deleteFile);
            sendRequest(new DeleteRequest(paramType, paramValue));
        }
        connection.run();
    }

    private void uploadFile(String data) {
        Response response = Response.parse(data);
        if (response.success) {
            System.out.println("Response says that file is saved! ID = " + response.message);
        }
    }

    private void downloadFile(String data) {
        Response response = Response.parse(data);
        if (response.success) {
            System.out.print("The file was downloaded! Specify a name for it: ");
            String location = scanner.nextLine();
            try {
                new NewFile(
                        root.resolve(location),
                        Request.decode(response.message)
                ).save();
                System.out.println("File saved on the hard drive!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("The response says that this file is not found!");
        }
    }

    private void deleteFile(String data) {
        Response response = Response.parse(data);
        if (response.success) {
            System.out.println("The response says that this file was deleted successfully!");
        } else {
            System.out.println("The response says that this file is not found!");
        }
    }

    private void sendRequest(Request request) {
        connection.send(request.serialize());
        System.out.println("The request was sent.");
    }
}
