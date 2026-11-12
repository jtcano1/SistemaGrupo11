package comunicacion.cliente;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class ClienteSocket {

    public void conectar() {

        try {

            Socket socket = new Socket("localhost",5000);

            DataOutputStream salida =
                    new DataOutputStream(
                            socket.getOutputStream());

            salida.writeUTF("INVENTARIO|BUSCAR_ARTICULO|2");

            DataInputStream entrada =
                    new DataInputStream(
                            socket.getInputStream());

            String respuesta =entrada.readUTF();
            System.out.println("Respuesta servidor: "
                            + respuesta);

            socket.close();

        } catch (Exception e) {

            System.out.println("Error: " + e.getMessage());
        }
    }
}