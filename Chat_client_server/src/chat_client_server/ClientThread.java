
package chat_client_server;
import java.io.*;
import java.net.*;
public class ClientThread implements Runnable {
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;
    private String username;

    public ClientThread(Socket socket) throws IOException {
        this.clientSocket = socket;
        this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.out = new PrintWriter(clientSocket.getOutputStream(), true);
    }
    public void run(){
        try {
            out.println("Enter your username:");
            this.username = in.readLine();
            out.println("Welcome, " + this.username + "!");

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Received message: " + inputLine);
                if (inputLine.startsWith("/private")) {
                    String[] parts = inputLine.split(" ", 3);
                    if (parts.length == 3) {
                        String recipientName = parts[1];
                        String message = parts[2];
                        Server.sendPrivateMessage(message, this, recipientName);
                    } else {
                        sendMessage("Usage: /private <username> <message>");
                    }
                } else {
                    Server.broadcast(inputLine, this);
                }
            }
        } catch (IOException e) {
            System.out.println("Error handling client");
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.out.println("Could not close client socket");
            }
            System.out.println("Client disconnected");
        }
    }
    public void sendMessage(String message) {
        out.println(message);
    }

    public String getUsername() {
        return this.username;
    }
}
