package comunicacion.servidor;

import datos.contabilidad.DAOCuenta;
import datos.contabilidad.DAOComprobante;
import datos.contabilidad.DAOReporte;
import datos.contabilidad.DAOTipoCuenta;
import datos.inventarios.ArticuloDAO;
import entidades.contabilidad.CabeceraComprobante;
import entidades.contabilidad.Cuenta;
import entidades.contabilidad.DetalleComprobante;
import entidades.inventarios.Articulo;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

                        // ============ LO DE CANO - NO SE TOCA ============
                        ProcesadorInventario procesador =
                                new ProcesadorInventario();

                        respuesta = procesador.procesar(
                                operacion, dato.toString());

                    } else if (modulo.equals("CONTABILIDAD")) {

                        // ============ MI PARTE - SOLO AGREGADA ============
                        respuesta = procesarContabilidad(operacion, dato.toString());

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

    // ============ CONTABILIDAD ============
    private static String procesarContabilidad(String operacion, String dato) {
        try {
            String[] partes = dato.split("\\|", -1);

            // --- CUENTA ---
            if (operacion.equals("LISTAR_CUENTAS")) {
                List<Cuenta> lista = new DAOCuenta().listar();
                StringBuilder sb = new StringBuilder("OK");
                for (Cuenta c : lista) {
                    sb.append(";").append(c.getCodigo()).append(",").append(c.getNombre()).append(",").append(c.getTipoCuenta() != null ? c.getTipoCuenta().getCodigo() : 0);
                }
                return sb.toString();
            }
            if (operacion.equals("BUSCAR_CUENTA")) {
                Cuenta c = new DAOCuenta().buscarPorCodigo(partes[0]);
                if (c == null) return "ERROR|NO_ENCONTRADO";
                return "OK|" + c.getCodigo() + "|" + c.getNombre() + "|" + (c.getTipoCuenta() != null ? c.getTipoCuenta().getCodigo() : 0);
            }
            if (operacion.equals("GUARDAR_CUENTA")) {
                Cuenta c = new Cuenta();
                c.setCodigo(partes[0]);
                c.setNombre(partes[1]);
                entidades.contabilidad.TipoCuenta t = new entidades.contabilidad.TipoCuenta();
                t.setCodigo(Integer.parseInt(partes[2]));
                c.setTipoCuenta(t);
                boolean ok = new DAOCuenta().insertar(c);
                return ok ? "OK|GUARDADO" : "ERROR|NO_SE_PUDO_GUARDAR";
            }
            if (operacion.equals("MODIFICAR_CUENTA")) {
                Cuenta c = new Cuenta();
                c.setCodigo(partes[0]);
                c.setNombre(partes[1]);
                entidades.contabilidad.TipoCuenta t = new entidades.contabilidad.TipoCuenta();
                t.setCodigo(Integer.parseInt(partes[2]));
                c.setTipoCuenta(t);
                boolean ok = new DAOCuenta().modificar(c);
                return ok ? "OK|MODIFICADO" : "ERROR|NO_SE_PUDO_MODIFICAR";
            }
            if (operacion.equals("ELIMINAR_CUENTA")) {
                boolean ok = new DAOCuenta().eliminar(partes[0]);
                return ok ? "OK|ELIMINADO" : "ERROR|NO_SE_PUDO_ELIMINAR";
            }
            if (operacion.equals("LISTAR_TIPO_CUENTA")) {
                List<entidades.contabilidad.TipoCuenta> lista = new DAOTipoCuenta().listar();
                StringBuilder sb = new StringBuilder("OK");
                for (entidades.contabilidad.TipoCuenta t : lista) {
                    sb.append(";").append(t.getCodigo()).append(",").append(t.getNombre());
                }
                return sb.toString();
            }

            // --- COMPROBANTE ---
            if (operacion.equals("GUARDAR_COMPROBANTE")) {
                // dato = fecha|obs|detalles
                CabeceraComprobante cab = parseComprobante(partes);
                String resultado = new negocio.contabilidad.NegocioComprobante().guardar(cab);
                if (resultado.startsWith("OK")) return "OK|GUARDADO|" + cab.getNumero();
                else return "ERROR|" + resultado;
            }
            if (operacion.equals("MODIFICAR_COMPROBANTE")) {
                // dato = numero|fecha|obs|detalles
                CabeceraComprobante cab = parseComprobanteConNumero(partes);
                String resultado = new negocio.contabilidad.NegocioComprobante().modificar(cab);
                if (resultado.startsWith("OK")) return "OK|MODIFICADO";
                else return "ERROR|" + resultado;
            }
            if (operacion.equals("BUSCAR_COMPROBANTE")) {
                CabeceraComprobante cab = new DAOComprobante().buscarPorNumero(Integer.parseInt(partes[0]));
                if (cab == null) return "ERROR|NO_ENCONTRADO";
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                StringBuilder sb = new StringBuilder("OK|" + cab.getNumero() + "|" + sdf.format(cab.getFecha()) + "|" + (cab.getObservaciones() == null ? "" : cab.getObservaciones()));
                for (DetalleComprobante d : cab.getDetalles()) {
                    sb.append(";").append(d.getCuenta().getCodigo()).append(",").append(d.getDebe()).append(",").append(d.getHaber());
                }
                return sb.toString();
            }
            if (operacion.equals("LISTAR_COMPROBANTES")) {
                List<CabeceraComprobante> listaComprobantes = new DAOComprobante().listar();
                SimpleDateFormat sdfLista = new SimpleDateFormat("yyyy-MM-dd");
                StringBuilder sbLista = new StringBuilder("OK");
                for (CabeceraComprobante cab : listaComprobantes) {
                    String fechaTexto = "";
                    if (cab.getFecha() != null) {
                        fechaTexto = sdfLista.format(cab.getFecha());
                    }
                    String obs = cab.getObservaciones() == null ? "" : cab.getObservaciones();
                    obs = obs.replace(",", " ").replace(";", " ");
                    sbLista.append(";").append(cab.getNumero()).append(",").append(fechaTexto).append(",").append(obs);
                }
                return sbLista.toString();
            }
            if (operacion.equals("ELIMINAR_COMPROBANTE")) {
                boolean ok = new DAOComprobante().eliminar(Integer.parseInt(partes[0]));
                return ok ? "OK|ELIMINADO" : "ERROR|NO_SE_PUDO_ELIMINAR";
            }

            // --- REPORTES ---
            if (operacion.equals("REPORTE_BALANCE")) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date desde = sdf.parse(partes[0]);
                Date hasta = sdf.parse(partes[1]);
                List<String[]> lista = new DAOReporte().reporteBalanceGeneral(desde, hasta);
                StringBuilder sb = new StringBuilder("OK");
                for (String[] f : lista) sb.append(";").append(f[0]).append(",").append(f[1]).append(",").append(f[2]).append(",").append(f[3]);
                return sb.toString();
            }
            if (operacion.equals("REPORTE_RESULTADOS")) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date desde = sdf.parse(partes[0]);
                Date hasta = sdf.parse(partes[1]);
                List<String[]> lista = new DAOReporte().reporteEstadoResultados(desde, hasta);
                StringBuilder sb = new StringBuilder("OK");
                for (String[] f : lista) sb.append(";").append(f[0]).append(",").append(f[1]).append(",").append(f[2]).append(",").append(f[3]).append(",").append(f[4]).append(",").append(f[5]);
                return sb.toString();
            }

            return "ERROR|OPERACION_CONTABILIDAD_NO_VALIDA";
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR|" + e.getMessage();
        }
    }

    private static CabeceraComprobante parseComprobante(String[] partes) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        CabeceraComprobante cab = new CabeceraComprobante();
        cab.setFecha(sdf.parse(partes[0]));
        cab.setObservaciones(partes[1]);
        cab.setDetalles(parseDetalles(partes[2]));
        return cab;
    }

    private static CabeceraComprobante parseComprobanteConNumero(String[] partes) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        CabeceraComprobante cab = new CabeceraComprobante();
        cab.setNumero(Integer.parseInt(partes[0]));
        cab.setFecha(sdf.parse(partes[1]));
        cab.setObservaciones(partes[2]);
        cab.setDetalles(parseDetalles(partes[3]));
        return cab;
    }

    private static List<DetalleComprobante> parseDetalles(String texto) {
        List<DetalleComprobante> lista = new ArrayList<>();
        if (texto == null || texto.isEmpty()) return lista;
        for (String item : texto.split(";")) {
            String[] d = item.split(",");
            DetalleComprobante det = new DetalleComprobante();
            Cuenta cu = new Cuenta();
            cu.setCodigo(d[0]);
            det.setCuenta(cu);
            det.setDebe(Double.parseDouble(d[1]));
            det.setHaber(Double.parseDouble(d[2]));
            lista.add(det);
        }
        return lista;
    }
}