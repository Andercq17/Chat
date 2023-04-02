
package chat_client_server;
import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) throws IOException{
        String hostName = "localhost";
        int portNumber = 1234;

        Socket socket = new Socket(hostName, portNumber);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String userInput;
        while ((userInput = stdIn.readLine()) != null) {
            out.println(userInput);
            System.out.println("Server said: " + in.readLine());
        }
    }
}
