package comunicacion.servidor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import datos.inventarios.ArticuloDAO;
import entidades.inventarios.Articulo;

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
            String dato = partes[2];
            
            // Determinar qué operación debe ejecutar el servidor
            // de acuerdo al módulo y la solicitud recibida.
            if (modulo.equals("INVENTARIO")
                    && operacion.equals("BUSCAR_ARTICULO")) {
                
                int codigo = Integer.parseInt(dato);
                ArticuloDAO dao = new ArticuloDAO();
                Articulo articulo = dao.buscar(codigo);
                
                if (articulo != null) {
                    
                    String respuestaTexto =
                            articulo.getCodigo()
                            + "|"
                            + articulo.getNombre()
                            + "|"
                            + articulo.getPrecio();
                    
                    salida.writeUTF(respuestaTexto);
                
                } else {
                    
                    salida.writeUTF("NO_ENCONTRADO");
                }
            
                } else {
                        salida.writeUTF("OPERACION_NO_VALIDA");
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