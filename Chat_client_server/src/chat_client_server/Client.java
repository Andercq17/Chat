
package chat_client_server;
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        String host = "localhost";
        int puerto = 1234;

        Scanner scanner = new Scanner(System.in);
        System.out.print("Introduzca su nombre: ");
        String nombre = scanner.nextLine();

        Socket socket = null;
        PrintWriter salida = null;
        BufferedReader entrada = null;

        try {
            socket = new Socket(host, puerto);
            salida = new PrintWriter(socket.getOutputStream(), true);
            entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("No se puede encontrar el host: " + host);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("No se puede conectar al host: " + host);
            System.exit(1);
        }

        // Enviar el nombre del cliente al servidor
        salida.println(nombre);

        // Crear un hilo para recibir los mensajes del servidor
        Thread hilo = new Thread(new Runnable() {
            public void run() {
                String mensaje;
                try {
                    while ((mensaje = entrada.readLine()) != null) {
                        System.out.println(mensaje);
                    }
                } catch (IOException e) {
                    System.out.println("Error de entrada/salida: " + e);
                }
            }
        });
        hilo.start();

        // Bucle para leer los mensajes del usuario y enviarlos al servidor
        String mensaje;
        while (true) {
            mensaje = scanner.nextLine();
            if (mensaje.equals("/salir")) {
                break;
            } else if (mensaje.startsWith("/msg ")) {
                // Enviar un mensaje privado
                String[] partes = mensaje.split(" ", 3);
                String destinatario = partes[1];
                String contenido = partes[2];
                salida.println(mensaje);
            } else {
                // Enviar el mensaje a todos los demás clientes
                salida.println(mensaje);
            }
        }

        // Cerrar el socket y salir
        salida.close();
        entrada.close();
        socket.close();
        System.exit(0);
    }
}
