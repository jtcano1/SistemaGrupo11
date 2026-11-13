
package comunicacion.servidor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorSocket {

    public static void main(String[] args) {

        try (ServerSocket servidor = new ServerSocket(5000)) {

            System.out.println("Servidor iniciado en puerto 5000");
            System.out.println("Esperando conexiones...");

            // Mantener el servidor activo para atender varios clientes.
            while (true) {

                try (Socket cliente = servidor.accept()) {

                    System.out.println("Cliente conectado");

                    DataInputStream entrada =
                            new DataInputStream(cliente.getInputStream());

                    DataOutputStream salida =
                            new DataOutputStream(cliente.getOutputStream());

                    String mensaje = entrada.readUTF();

                    System.out.println("Mensaje recibido: " + mensaje);

                    String[] partes = mensaje.split("\\|");

                    if (partes.length < 2) {
                        salida.writeUTF("SOLICITUD_INVALIDA");
                        continue;
                    }

                    String modulo = partes[0];
                    String operacion = partes[1];

                    // Reconstruir los datos enviados por el cliente.
                    StringBuilder dato = new StringBuilder();

                    for (int i = 2; i < partes.length; i++) {
                        if (i > 2) {
                            dato.append("|");
                        }

                        dato.append(partes[i]);
                    }

                    String respuesta;

                    if (modulo.equals("INVENTARIO")) {

                        ProcesadorInventario procesador =
                                new ProcesadorInventario();

                        respuesta = procesador.procesar(
                                operacion, dato.toString());

                    } else {

                        respuesta = "MODULO_NO_VALIDO";
                    }

                    salida.writeUTF(respuesta);

                    System.out.println("Modulo: " + modulo);
                    System.out.println("Operacion: " + operacion);
                    System.out.println("Dato: " + dato);
                    System.out.println("Respuesta enviada: " + respuesta);

                } catch (Exception e) {

                    System.out.println(
                            "Error al atender al cliente: " + e.getMessage());
                }

                System.out.println("Esperando nueva conexión...");
            }

        } catch (Exception e) {

            System.out.println("Error en el servidor: " + e.getMessage());
        }
    }
}
