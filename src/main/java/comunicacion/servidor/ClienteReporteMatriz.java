
package comunicacion.cliente;

import entidades.inventarios.ColumnaMovimiento;
import entidades.inventarios.MovimientoMatriz;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ClienteReporteMatriz {

    private static final String HOST = "localhost";
    private static final int PUERTO = 5000;

    // Consultar los tipos de movimiento mediante sockets.
    public List<ColumnaMovimiento> listarTipos() {

        List<ColumnaMovimiento> lista = new ArrayList<>();

        try (Socket socket = new Socket(HOST, PUERTO);
             DataOutputStream salida =
                     new DataOutputStream(socket.getOutputStream());
             DataInputStream entrada =
                     new DataInputStream(socket.getInputStream())) {

            salida.writeUTF("INVENTARIO|LISTAR_TIPOS_MATRIZ");

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

                String[] datos = fila.split("\\|", 2);

                ColumnaMovimiento tipo = new ColumnaMovimiento();

                tipo.setCodigo(Integer.parseInt(datos[0]));
                tipo.setNombre(datos[1]);

                lista.add(tipo);
            }

        } catch (Exception e) {

            System.out.println(
                    "Error al consultar tipos de movimiento: "
                    + e.getMessage());
        }

        return lista;
    }

    // Consultar movimientos entre dos fechas mediante sockets.
    public List<MovimientoMatriz> listarMovimientos(
            Date desde, Date hasta) {

        List<MovimientoMatriz> lista = new ArrayList<>();

        if (desde == null || hasta == null || desde.after(hasta)) {
            return lista;
        }

        SimpleDateFormat formato =
                new SimpleDateFormat("dd/MM/yyyy");

        String fechas =
                formato.format(desde) + "|"
                + formato.format(hasta);

        try (Socket socket = new Socket(HOST, PUERTO);
             DataOutputStream salida =
                     new DataOutputStream(socket.getOutputStream());
             DataInputStream entrada =
                     new DataInputStream(socket.getInputStream())) {

            salida.writeUTF(
                    "INVENTARIO|LISTAR_MOVIMIENTOS_MATRIZ|" + fechas);

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

                String[] datos = fila.split("\\|", 5);

                if (datos.length < 5) {
                    continue;
                }

                MovimientoMatriz movimiento =
                        new MovimientoMatriz();

                movimiento.setCodigoArticulo(
                        Integer.parseInt(datos[0]));

                movimiento.setArticulo(datos[1]);

                movimiento.setCodigoTipoMovimiento(
                        Integer.parseInt(datos[2]));

                movimiento.setTipoMovimiento(datos[3]);

                movimiento.setCantidad(
                        Integer.parseInt(datos[4]));

                lista.add(movimiento);
            }

        } catch (Exception e) {

            System.out.println(
                    "Error al consultar movimientos: "
                    + e.getMessage());
        }

        return lista;
    }
}
