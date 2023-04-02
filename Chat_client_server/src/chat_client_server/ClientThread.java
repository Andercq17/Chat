
package chat_client_server;
import java.io.*;
import java.net.*;
public class ClientThread implements Runnable {
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;

    public ClientThread(Socket socket) throws IOException {
        this.clientSocket = socket;
        this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.out = new PrintWriter(clientSocket.getOutputStream(), true);
    }
    public void run(){
        try {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Received message: " + inputLine);
                out.println("You said: " + inputLine);
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
}
