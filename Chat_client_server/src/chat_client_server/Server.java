
package chat_client_server;
import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static List<ClientThread> clients = new ArrayList<>();
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1234);
        System.out.println("Server started");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected");

            ClientThread client = new ClientThread(clientSocket);
            clients.add(client);

            Thread thread = new Thread(client);
            thread.start();
        }    
    }
     public static void sendPrivateMessage(String message, ClientThread sender, String recipientName) {
        for (ClientThread client : clients) {
            if (client.getUsername().equals(recipientName)) {
                client.sendMessage(sender.getUsername() + " (private): " + message);
                sender.sendMessage("You (private) -> " + recipientName + ": " + message);
                return;
            }
        }
        sender.sendMessage("User " + recipientName + " not found.");
    }

    public static void broadcast(String message, ClientThread sender) {
        for (ClientThread client : clients) {
            if (client != sender) {
                client.sendMessage(sender.getUsername() + ": " + message);
            }
        }
    }
}