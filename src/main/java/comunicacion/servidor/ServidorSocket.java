package comunicacion.servidor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import datos.inventarios.ArticuloDAO;
import entidades.inventarios.Articulo;
import comunicacion.servidor.ProcesadorInventario;


public class ServidorSocket {

    public static void main(String[] args) {

        try {

            ServerSocket servidor =
                    new ServerSocket(5000);

            System.out.println(
                    "Servidor iniciado en puerto 5000");

            Socket cliente =
                    servidor.accept();

            System.out.println(
                    "Cliente conectado");

            DataInputStream entrada =
                    new DataInputStream(
                            cliente.getInputStream());
            DataOutputStream salida =
                    new DataOutputStream(
                            cliente.getOutputStream());

            String mensaje = entrada.readUTF();
            
            System.out.println("Mensaje recibido: "
                            + mensaje);
            
            // Separar la información enviada por el cliente.
            String[] partes = mensaje.split("\\|");
            String modulo = partes[0];
            String operacion = partes[1];
            // Reconstruir toda la información enviada
            // después del módulo y la operación.
            String dato = "";
            
            for (int i = 2; i < partes.length; i++) {
                if (i > 2) {
                    dato += "|";
                }
                
                dato += partes[i];
            }
            
        // Delegar las operaciones del módulo Inventario
        // al procesador correspondiente.
        if (modulo.equals("INVENTARIO")) {
            ProcesadorInventario procesador =
                    new ProcesadorInventario();
            
            String respuesta =
                    procesador.procesar(
                    operacion,dato);
            
            salida.writeUTF(respuesta);
        
        } else {
            
            salida.writeUTF("MODULO_NO_VALIDO");
        }
            
            System.out.println("Modulo: " + modulo);
            System.out.println("Operacion: " + operacion);
            System.out.println("Dato: " + dato);


            cliente.close();
            servidor.close();

        } catch (Exception e) {

            System.out.println(
                    "Error: "
                            + e.getMessage());
        }
    }
}