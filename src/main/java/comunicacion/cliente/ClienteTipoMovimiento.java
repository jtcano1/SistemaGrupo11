
package comunicacion.cliente;

import entidades.inventarios.TipoMovimiento;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClienteTipoMovimiento {

    private static final String HOST = "localhost";
    private static final int PUERTO = 5000;

    // Enviar solicitud al servidor y recibir respuesta.
    private String enviar(String operacion, String dato) {

        try (Socket socket = new Socket(HOST, PUERTO);
             DataOutputStream salida =
                     new DataOutputStream(socket.getOutputStream());
             DataInputStream entrada =
                     new DataInputStream(socket.getInputStream())) {

            String mensaje = "INVENTARIO|" + operacion;

            if (dato != null && !dato.isEmpty()) {
                mensaje += "|" + dato;
            }

            salida.writeUTF(mensaje);

            return entrada.readUTF();

        } catch (Exception e) {

            System.out.println(
                    "Error de comunicación con el servidor: "
                    + e.getMessage());

            return "ERROR|" + e.getMessage();
        }
    }

    // Listar todos los tipos de movimiento.
    public List<TipoMovimiento> listar() {

        List<TipoMovimiento> lista = new ArrayList<>();

        String respuesta =
                enviar("LISTAR_TIPOS_MOVIMIENTO", "");

        if (respuesta.startsWith("ERROR|")
                || respuesta.isEmpty()) {
            return lista;
        }

        String[] filas = respuesta.split("\n");

        for (String fila : filas) {

            if (fila.trim().isEmpty()) {
                continue;
            }

            String[] datos = fila.split("\\|", 3);

            if (datos.length < 3) {
                continue;
            }

            TipoMovimiento tipo = new TipoMovimiento();

            tipo.setCodigo(Integer.parseInt(datos[0]));
            tipo.setNombre(datos[1]);
            tipo.setSigno(Integer.parseInt(datos[2]));

            lista.add(tipo);
        }

        return lista;
    }

    // Buscar tipo de movimiento por código.
    public TipoMovimiento buscar(int codigo) {

        String respuesta =
                enviar("BUSCAR_TIPO_MOVIMIENTO",
                        String.valueOf(codigo));

        if (respuesta.equals("NO_ENCONTRADO")
                || respuesta.startsWith("ERROR|")) {
            return null;
        }

        String[] datos = respuesta.split("\\|", 3);

        if (datos.length < 3) {
            return null;
        }

        TipoMovimiento tipo = new TipoMovimiento();

        tipo.setCodigo(Integer.parseInt(datos[0]));
        tipo.setNombre(datos[1]);
        tipo.setSigno(Integer.parseInt(datos[2]));

        return tipo;
    }

    // Guardar un nuevo tipo de movimiento.
    public boolean guardar(TipoMovimiento tipo) {

        String dato = tipo.getNombre()
                + "|"
                + tipo.getSigno();

        return enviar("GUARDAR_TIPO_MOVIMIENTO", dato)
                .equals("GUARDADO");
    }

    // Modificar un tipo de movimiento existente.
    public boolean modificar(TipoMovimiento tipo) {

        String dato = tipo.getCodigo()
                + "|"
                + tipo.getNombre()
                + "|"
                + tipo.getSigno();

        return enviar("MODIFICAR_TIPO_MOVIMIENTO", dato)
                .equals("MODIFICADO");
    }

    // Eliminar tipo de movimiento por código.
    public boolean eliminar(int codigo) {

        return enviar("ELIMINAR_TIPO_MOVIMIENTO",
                String.valueOf(codigo))
                .equals("ELIMINADO");
    }
}
