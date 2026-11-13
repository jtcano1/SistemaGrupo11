
package comunicacion.cliente;

import entidades.inventarios.Articulo;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClienteArticulo {

    private static final String HOST = "localhost";
    private static final int PUERTO = 5000;

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

    public List<Articulo> listar() {

        List<Articulo> lista = new ArrayList<>();

        String respuesta = enviar("LISTAR_ARTICULOS", "");

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

            Articulo articulo = new Articulo();

            articulo.setCodigo(Integer.parseInt(datos[0]));
            articulo.setNombre(datos[1]);
            articulo.setPrecio(Double.parseDouble(datos[2]));

            lista.add(articulo);
        }

        return lista;
    }

    public Articulo buscar(int codigo) {

        String respuesta =
                enviar("BUSCAR_ARTICULO",
                        String.valueOf(codigo));

        if (respuesta.equals("NO_ENCONTRADO")
                || respuesta.startsWith("ERROR|")) {
            return null;
        }

        String[] datos = respuesta.split("\\|", 3);

        if (datos.length < 3) {
            return null;
        }

        Articulo articulo = new Articulo();

        articulo.setCodigo(Integer.parseInt(datos[0]));
        articulo.setNombre(datos[1]);
        articulo.setPrecio(Double.parseDouble(datos[2]));

        return articulo;
    }

    public boolean guardar(Articulo articulo) {

        String dato = articulo.getNombre()
                + "|"
                + Double.toString(articulo.getPrecio());

        return enviar("GUARDAR_ARTICULO", dato)
                .equals("GUARDADO");
    }

    public boolean modificar(Articulo articulo) {

        String dato = articulo.getCodigo()
                + "|"
                + articulo.getNombre()
                + "|"
                + Double.toString(articulo.getPrecio());

        return enviar("MODIFICAR_ARTICULO", dato)
                .equals("MODIFICADO");
    }

    public boolean eliminar(int codigo) {

        return enviar("ELIMINAR_ARTICULO",
                String.valueOf(codigo))
                .equals("ELIMINADO");
    }
}
