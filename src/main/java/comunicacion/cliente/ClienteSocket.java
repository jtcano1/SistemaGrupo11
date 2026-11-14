package comunicacion.cliente;

import entidades.inventarios.CabeceraInventario;
import entidades.inventarios.DetalleInventario;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ClienteSocket {

    private static final String HOST = "localhost";
    private static final int PUERTO = 5000;

    private final SimpleDateFormat formato =
            new SimpleDateFormat("dd/MM/yyyy");

    // Enviar una solicitud al servidor.
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
                    "Error de comunicación: " + e.getMessage());

            return "ERROR|" + e.getMessage();
        }
    }

    // Construir los datos de los detalles.
    private String construirDetalles(
            List<DetalleInventario> detalles) {

        StringBuilder dato = new StringBuilder();

        dato.append(detalles.size());

        for (DetalleInventario detalle : detalles) {

            dato.append("|")
                .append(detalle.getIdArticulo())
                .append("|")
                .append(detalle.getCantidad())
                .append("|")
                .append(detalle.getPrecio());
        }

        return dato.toString();
    }

    // GUARDAR COMPROBANTE CON VARIOS DETALLES.
    public int guardar(
            CabeceraInventario cabecera,
            List<DetalleInventario> detalles) {

        String dato =
                formato.format(cabecera.getFecha())
                + "|"
                + cabecera.getIdTipoMovimiento()
                + "|"
                + construirDetalles(detalles);

        String respuesta =
                enviar("GUARDAR_COMPROBANTE", dato);

        if (respuesta.startsWith("GUARDADO|")) {

            return Integer.parseInt(
                    respuesta.substring("GUARDADO|".length()));
        }

        System.out.println("Respuesta al guardar: " + respuesta);

        return 0;
    }

    // BUSCAR CABECERA.
    public CabeceraInventario buscarCabecera(int numero) {

        String respuesta =
                enviar("BUSCAR_CABECERA",
                        String.valueOf(numero));

        if (respuesta.equals("NO_ENCONTRADO")
                || respuesta.startsWith("ERROR|")) {
            return null;
        }

        try {

            String[] datos = respuesta.split("\\|", 3);

            if (datos.length < 3) {
                return null;
            }

            CabeceraInventario cabecera =
                    new CabeceraInventario();

            cabecera.setNumero(Integer.parseInt(datos[0]));
            cabecera.setFecha(formato.parse(datos[1]));
            cabecera.setIdTipoMovimiento(
                    Integer.parseInt(datos[2]));

            return cabecera;

        } catch (Exception e) {

            System.out.println(
                    "Error al interpretar cabecera: "
                    + e.getMessage());

            return null;
        }
    }

    // LISTAR DETALLES DE UN COMPROBANTE.
    public List<DetalleInventario> listarDetalles(int numero) {

        List<DetalleInventario> lista = new ArrayList<>();

        String respuesta =
                enviar("LISTAR_DETALLES",
                        String.valueOf(numero));

        if (respuesta.equals("SIN_DETALLES")
                || respuesta.startsWith("ERROR|")) {
            return lista;
        }

        String[] filas = respuesta.split("\n");

        for (String fila : filas) {

            if (fila.trim().isEmpty()) {
                continue;
            }

            try {

                String[] datos = fila.split("\\|", 3);

                if (datos.length < 3) {
                    continue;
                }

                DetalleInventario detalle =
                        new DetalleInventario();

                detalle.setNumeroComprobante(numero);
                detalle.setIdArticulo(
                        Integer.parseInt(datos[0]));
                detalle.setCantidad(
                        Integer.parseInt(datos[1]));
                detalle.setPrecio(
                        Double.parseDouble(datos[2]));

                lista.add(detalle);

            } catch (Exception e) {

                System.out.println(
                        "Error al interpretar detalle: "
                        + e.getMessage());
            }
        }

        return lista;
    }

    // MODIFICAR COMPROBANTE COMPLETO.
    public boolean modificar(
            CabeceraInventario cabecera,
            List<DetalleInventario> detalles) {

        String dato =
                cabecera.getNumero()
                + "|"
                + formato.format(cabecera.getFecha())
                + "|"
                + cabecera.getIdTipoMovimiento()
                + "|"
                + construirDetalles(detalles);

        return enviar("MODIFICAR_COMPROBANTE", dato)
                .equals("MODIFICADO");
    }

    // ELIMINAR COMPROBANTE COMPLETO.
    public boolean eliminar(int numero) {

        return enviar("ELIMINAR_COMPROBANTE",
                String.valueOf(numero))
                .equals("ELIMINADO");
    }

    // Método de compatibilidad para las pruebas anteriores.
    public void conectar() {
        String respuesta = enviar("LISTAR_ARTICULOS", "");
        System.out.println("Respuesta del servidor: " + respuesta);
    }

    // =================CONTABILIDAD ============

    // Metodo generico mio: manda un mensaje tal cual
    public String enviar(String mensaje) {
        try (Socket socket = new Socket(HOST, PUERTO);
             DataOutputStream salida = new DataOutputStream(socket.getOutputStream());
             DataInputStream entrada = new DataInputStream(socket.getInputStream())) {

            salida.writeUTF(mensaje);
            String respuesta = entrada.readUTF();
            System.out.println("Respuesta servidor: " + respuesta);
            return respuesta;

        } catch (Exception e) {
            System.out.println("Error ClienteSocket: " + e.getMessage());
            return "ERROR|SIN_CONEXION";
        }
    }

    public String listarCuentas() {
        return enviar("CONTABILIDAD|LISTAR_CUENTAS");
    }
    public String buscarCuenta(String codigo) {
        return enviar("CONTABILIDAD|BUSCAR_CUENTA|" + codigo);
    }
    public String guardarCuenta(String codigo, String nombre, int tipo) {
        return enviar("CONTABILIDAD|GUARDAR_CUENTA|" + codigo + "|" + nombre + "|" + tipo);
    }
    public String modificarCuenta(String codigo, String nombre, int tipo) {
        return enviar("CONTABILIDAD|MODIFICAR_CUENTA|" + codigo + "|" + nombre + "|" + tipo);
    }
    public String eliminarCuenta(String codigo) {
        return enviar("CONTABILIDAD|ELIMINAR_CUENTA|" + codigo);
    }
    public String listarTipoCuenta() {
        return enviar("CONTABILIDAD|LISTAR_TIPO_CUENTA");
    }
    public String guardarComprobante(String fecha, String obs, String detalles) {
        return enviar("CONTABILIDAD|GUARDAR_COMPROBANTE|" + fecha + "|" + obs + "|" + detalles);
    }
    public String modificarComprobante(int numero, String fecha, String obs, String detalles) {
        return enviar("CONTABILIDAD|MODIFICAR_COMPROBANTE|" + numero + "|" + fecha + "|" + obs + "|" + detalles);
    }
    public String buscarComprobante(int numero) {
        return enviar("CONTABILIDAD|BUSCAR_COMPROBANTE|" + numero);
    }
    public String listarComprobantes() {
        return enviar("CONTABILIDAD|LISTAR_COMPROBANTES");
    }
    public String eliminarComprobante(int numero) {
        return enviar("CONTABILIDAD|ELIMINAR_COMPROBANTE|" + numero);
    }
    public String reporteBalance(String desde, String hasta) {
        return enviar("CONTABILIDAD|REPORTE_BALANCE|" + desde + "|" + hasta);
    }
    public String reporteResultados(String desde, String hasta) {
        return enviar("CONTABILIDAD|REPORTE_RESULTADOS|" + desde + "|" + hasta);
    }
}