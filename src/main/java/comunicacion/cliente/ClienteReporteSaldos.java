
package comunicacion.cliente;

import entidades.inventarios.SaldoArticulo;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ClienteReporteSaldos {

    private static final String HOST = "localhost";
    private static final int PUERTO = 5000;

    // Consultar saldos de artículos mediante sockets.
    public List<SaldoArticulo> listarSaldos(Date hasta) {

        List<SaldoArticulo> lista = new ArrayList<>();

        if (hasta == null) {
            return lista;
        }

        SimpleDateFormat formato =
                new SimpleDateFormat("dd/MM/yyyy");

        String fecha = formato.format(hasta);

        try (Socket socket = new Socket(HOST, PUERTO);
             DataOutputStream salida =
                     new DataOutputStream(socket.getOutputStream());
             DataInputStream entrada =
                     new DataInputStream(socket.getInputStream())) {

            salida.writeUTF(
                    "INVENTARIO|LISTAR_SALDOS|" + fecha);

            String respuesta = entrada.readUTF();

            if (respuesta.startsWith("ERROR|")) {
                System.out.println(respuesta);
                return lista;
            }

            if (respuesta.isEmpty()) {
                return lista;
            }

            String[] filas = respuesta.split("\n");

            for (String fila : filas) {

                if (fila.trim().isEmpty()) {
                    continue;
                }

                String[] datos = fila.split("\\|", 4);

                if (datos.length < 4) {
                    continue;
                }

                SaldoArticulo saldo = new SaldoArticulo();

                saldo.setCodigo(Integer.parseInt(datos[0]));
                saldo.setNombre(datos[1]);
                saldo.setPrecio(Double.parseDouble(datos[2]));
                saldo.setStock(Integer.parseInt(datos[3]));

                lista.add(saldo);
            }

        } catch (Exception e) {

            System.out.println(
                    "Error al consultar saldos: "
                    + e.getMessage());
        }

        return lista;
    }
}
