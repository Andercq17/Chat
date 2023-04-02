
package chat_client_server;
import java.io.*;
import java.net.*;
import java.util.*;

public class Servidor {
    public static void main(String[] args) throws IOException {
        int puerto = 1234;

        // Crear el socket del servidor y esperar a que un cliente se conecte
        ServerSocket servidor = new ServerSocket(puerto);
        System.out.println("Servidor en línea.");

        // Lista de nombres de los clientes conectados
        List<String> nombres = new ArrayList<String>();

        // Lista de sockets de los clientes conectados
        List<Socket> clientes = new ArrayList<Socket>();

        while (true) {
            Socket cliente = null;
            try {
                cliente = servidor.accept();
                System.out.println("Nuevo cliente conectado.");
            } catch (IOException e) {
                System.err.println("Error al aceptar la conexión del cliente.");
                continue;
            }

            // Atender al cliente en un hilo
            Thread hilo = new Thread(new Runnable() {
                public void run() {
                    try {
                        Buffe redReader entrada = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                        PrintWriter salida = new PrintWriter(cliente.getOutputStream(), true);

                        // Pedir el nombre del cliente
                        String nombre = null;
                        while (true) {
                            salida.println("NOMBRE");
                            nombre = entrada.readLine();
                            if (nombre == null) {
                                return;
                            }
                            synchronized (nombres) {
                                if (!nombres.contains(nombre)) {
                                    nombres.add(nombre);
                                    break;
                                }
                            }
                        }

                        // Aceptar el cliente y enviarle un mensaje de bienvenida
                        clientes.add(cliente);
                        salida.println("BIENVENIDO " + nombre);
                        for (Socket otroCliente : clientes) {
                            if (otroCliente != cliente) {
                                PrintWriter otroSalida = new PrintWriter(otroCliente.getOutputStream(), true);
                                otroSalida.println("ENLINEA " + nombre);
                            }
                        }

                        // Leer los mensajes del cliente y enviarlos a todos los demás clientes
                        while (true) {
                            String mensaje = entrada.readLine();
                            if (mensaje == null) {
                                break;
                            }
                            for (Socket otroCliente : clientes) {
                                if (otroCliente != cliente) {
                                    PrintWriter otroSalida = new PrintWriter(otroCliente.getOutputStream(), true);
                                    otroSalida.println("MENSAJE " + nombre + " " + mensaje);
                                }
                            }
                            if (mensaje.startsWith("@")) {
                                String[] partes = mensaje.split(" ", 2);
                                if (partes.length == 2) {
                                    String destinatario = partes[0].substring(1);
                                    String contenido = partes[1];
                                    for (Socket otroCliente : clientes) {
                                        String otroNombre = null;
                                        synchronized (nombres) {
                                            int indice = clientes.indexOf(otroCliente);
                                            if (indice != -1) {
                                                otroNombre = nombres.get(indice);
                                            }
                                        }
                                        if (otroNombre != null && otroNombre.equals(destinatario)) {
                                            PrintWriter otroSalida = new PrintWriter(otroCliente.getOutputStream(), true);
                                            otroSalida.println("MENSAJEPRIVADO " + nombre + " " + contenido);
                                            break;
                                        }
                                    }
                                }
                            }
                        }

                        // Eliminar al cliente de las listas y notificar a los demás clientes
                        salida.println("CHAO");
                        clientes.remove(cliente);
                        nombres.remove(nombre);
                        for (Socket otroCliente : clientes) {
                            PrintWriter otroSalida = new PrintWriter(otroCliente.getOutputStream(), true);
                            otroSalida.println("ENLINEA" + nombre);
                        }
                        cliente.close();
                    } catch (IOException e) {
                        System.err.println("Error al atender al cliente.");
                    }
                }
            });
           hilo.start();
        }
   }
}