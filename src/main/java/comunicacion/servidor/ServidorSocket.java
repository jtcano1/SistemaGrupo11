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
            System.out.println("Servidor iniciado en puerto 5000 - esperando clientes...");

            // RNF CONCURRENCIA: bucle infinito + 1 hilo por cliente
            while (true) {
                Socket cliente = servidor.accept();
                System.out.println("Cliente conectado: " + cliente.getInetAddress());
                new Thread(new ManejadorCliente(cliente)).start();
            }

        } catch (Exception e) {
            System.out.println("Error servidor: " + e.getMessage());
        }
    }

    // Cada cliente corre en su propio hilo, así 2 pueden guardar a la vez
    static class ManejadorCliente implements Runnable {
        private Socket cliente;
        public ManejadorCliente(Socket cliente) { this.cliente = cliente; }

        @Override
        public void run() {
            try (DataInputStream entrada = new DataInputStream(cliente.getInputStream());
                 DataOutputStream salida = new DataOutputStream(cliente.getOutputStream())) {

                // Bucle: el mismo cliente puede mandar varios mensajes sin desconectarse
                while (true) {
                    String mensaje;
                    try {
                        mensaje = entrada.readUTF();
                    } catch (Exception ex) {
                        System.out.println("Cliente desconectado");
                        break;
                    }
                    System.out.println("Mensaje recibido: " + mensaje);
                    String respuesta = procesar(mensaje);
                    salida.writeUTF(respuesta);
                }
            } catch (Exception e) {
                System.out.println("Error cliente: " + e.getMessage());
            }
        }

        private String procesar(String mensaje) {
            try {
                String[] partes = mensaje.split("\\|", -1);
                String modulo = partes[0];
                String operacion = partes[1];

                // ============ INVENTARIO - lo de tu compañera, NO LO BORRE ============
                if (modulo.equals("INVENTARIO") && operacion.equals("BUSCAR_ARTICULO")) {
                    int codigo = Integer.parseInt(partes[2]);
                    Articulo a = new ArticuloDAO().buscar(codigo);
                    if (a != null) return "OK|" + a.getCodigo() + "|" + a.getNombre() + "|" + a.getPrecio();
                    return "ERROR|NO_ENCONTRADO";
                }

                // ============ CONTABILIDAD ============
                if (modulo.equals("CONTABILIDAD")) {

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
                        Cuenta c = new DAOCuenta().buscarPorCodigo(partes[2]);
                        if (c == null) return "ERROR|NO_ENCONTRADO";
                        return "OK|" + c.getCodigo() + "|" + c.getNombre() + "|" + (c.getTipoCuenta() != null ? c.getTipoCuenta().getCodigo() : 0);
                    }
                    if (operacion.equals("GUARDAR_CUENTA")) {
                        // GUARDAR_CUENTA|codigo|nombre|codigoTipo
                        Cuenta c = new Cuenta();
                        c.setCodigo(partes[2]);
                        c.setNombre(partes[3]);
                        entidades.contabilidad.TipoCuenta t = new entidades.contabilidad.TipoCuenta();
                        t.setCodigo(Integer.parseInt(partes[4]));
                        c.setTipoCuenta(t);
                        boolean ok = new DAOCuenta().insertar(c);
                        return ok ? "OK|GUARDADO" : "ERROR|NO_SE_PUDO_GUARDAR";
                    }
                    if (operacion.equals("MODIFICAR_CUENTA")) {
                        Cuenta c = new Cuenta();
                        c.setCodigo(partes[2]);
                        c.setNombre(partes[3]);
                        entidades.contabilidad.TipoCuenta t = new entidades.contabilidad.TipoCuenta();
                        t.setCodigo(Integer.parseInt(partes[4]));
                        c.setTipoCuenta(t);
                        boolean ok = new DAOCuenta().modificar(c);
                        return ok ? "OK|MODIFICADO" : "ERROR|NO_SE_PUDO_MODIFICAR";
                    }
                    if (operacion.equals("ELIMINAR_CUENTA")) {
                        boolean ok = new DAOCuenta().eliminar(partes[2]);
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
                    // Formato: CONTABILIDAD|GUARDAR_COMPROBANTE|yyyy-MM-dd|observaciones|codigo,debe,haber;codigo,debe,haber
                    if (operacion.equals("GUARDAR_COMPROBANTE")) {
                        CabeceraComprobante cab = parseComprobante(partes);
                        // GUARDAR_COMPROBANTE
                        String resultado = new negocio.contabilidad.NegocioComprobante().guardar(cab);
                        if (resultado.startsWith("OK")) return "OK|GUARDADO|" + cab.getNumero();
                        else return "ERROR|" + resultado;
                    }
                    if (operacion.equals("MODIFICAR_COMPROBANTE")) {
                        // MODIFICAR_COMPROBANTE|numero|fecha|obs|detalles...
                        CabeceraComprobante cab = parseComprobanteConNumero(partes);
                        // MODIFICAR_COMPROBANTE
                        String resultado = new negocio.contabilidad.NegocioComprobante().modificar(cab);
                        if (resultado.startsWith("OK")) return "OK|MODIFICADO";
                        else return "ERROR|" + resultado;
                    }
                    if (operacion.equals("BUSCAR_COMPROBANTE")) {
                        CabeceraComprobante cab = new DAOComprobante().buscarPorNumero(Integer.parseInt(partes[2]));
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
                            // Quitamos comas y ; de la observacion para que no rompa el protocolo
                            obs = obs.replace(",", " ").replace(";", " ");
                            sbLista.append(";").append(cab.getNumero()).append(",").append(fechaTexto).append(",").append(obs);
                        }
                        return sbLista.toString();
                    }
                    if (operacion.equals("ELIMINAR_COMPROBANTE")) {
                        boolean ok = new DAOComprobante().eliminar(Integer.parseInt(partes[2]));
                        return ok ? "OK|ELIMINADO" : "ERROR|NO_SE_PUDO_ELIMINAR";
                    }

                    // --- REPORTES ---
                    // REPORTE_BALANCE|yyyy-MM-dd|yyyy-MM-dd
                    if (operacion.equals("REPORTE_BALANCE")) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Date desde = sdf.parse(partes[2]);
                        Date hasta = sdf.parse(partes[3]);
                        List<String[]> lista = new DAOReporte().reporteBalanceGeneral(desde, hasta);
                        StringBuilder sb = new StringBuilder("OK");
                        for (String[] f : lista) sb.append(";").append(f[0]).append(",").append(f[1]).append(",").append(f[2]).append(",").append(f[3]);
                        return sb.toString();
                    }
                    if (operacion.equals("REPORTE_RESULTADOS")) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Date desde = sdf.parse(partes[2]);
                        Date hasta = sdf.parse(partes[3]);
                        List<String[]> lista = new DAOReporte().reporteEstadoResultados(desde, hasta);
                        StringBuilder sb = new StringBuilder("OK");
                        for (String[] f : lista) sb.append(";").append(f[0]).append(",").append(f[1]).append(",").append(f[2]).append(",").append(f[3]).append(",").append(f[4]).append(",").append(f[5]);
                        return sb.toString();
                    }

                    return "ERROR|OPERACION_CONTABILIDAD_NO_VALIDA";
                }

                return "ERROR|OPERACION_NO_VALIDA";
            } catch (Exception e) {
                e.printStackTrace();
                return "ERROR|" + e.getMessage();
            }
        }

        private CabeceraComprobante parseComprobante(String[] partes) throws Exception {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            CabeceraComprobante cab = new CabeceraComprobante();
            cab.setFecha(sdf.parse(partes[2]));
            cab.setObservaciones(partes[3]);
            cab.setDetalles(parseDetalles(partes[4]));
            return cab;
        }

        private CabeceraComprobante parseComprobanteConNumero(String[] partes) throws Exception {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            CabeceraComprobante cab = new CabeceraComprobante();
            cab.setNumero(Integer.parseInt(partes[2]));
            cab.setFecha(sdf.parse(partes[3]));
            cab.setObservaciones(partes[4]);
            cab.setDetalles(parseDetalles(partes[5]));
            return cab;
        }

        private List<DetalleComprobante> parseDetalles(String texto) {
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
}