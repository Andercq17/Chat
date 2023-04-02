
package chat_client_server;
import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    public static void main(String[] args) throws IOException{
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter server IP: ");
        String serverIp = scanner.nextLine();

        Socket socket = new Socket(serverIp, 1234);
        System.out.println("Connected to server");

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        String username;
        do {
            System.out.print("Enter your username: ");
            username = scanner.nextLine();
            out.println(username);
        } while (in.readLine().startsWith("Username taken"));

        System.out.println("Welcome, " + username + "!");

        Thread inputThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String inputLine;
                    while ((inputLine = scanner.nextLine()) != null) {
                        out.println(inputLine);
                    }
                } catch (Exception e) {
                    System.out.println("Error reading input from user");
                }
            }
        });
        inputThread.start();

        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            System.out.println(inputLine);
        }

        socket.close();
        scanner.close();
    }
}
