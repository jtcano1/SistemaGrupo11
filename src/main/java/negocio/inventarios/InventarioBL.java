
package negocio.inventarios;

import conexion.ConexionOracle;

import datos.inventarios.CabeceraInventarioDAO;
import datos.inventarios.DetalleInventarioDAO;
import datos.inventarios.ControlExistenciasDAO;

import entidades.inventarios.CabeceraInventario;
import entidades.inventarios.DetalleInventario;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class InventarioBL {

    private final CabeceraInventarioDAO cabeceraDAO;
    private final DetalleInventarioDAO detalleDAO;
    private final ControlExistenciasDAO existenciasDAO;

    public InventarioBL() {

        cabeceraDAO = new CabeceraInventarioDAO();
        detalleDAO = new DetalleInventarioDAO();
        existenciasDAO = new ControlExistenciasDAO();
    }

    // VALIDAR DATOS DEL COMPROBANTE.
    private boolean validar(
            CabeceraInventario cabecera,
            List<DetalleInventario> detalles) {

        if (cabecera == null
                || cabecera.getFecha() == null
                || cabecera.getIdTipoMovimiento() <= 0
                || detalles == null
                || detalles.isEmpty()) {

            return false;
        }

        for (DetalleInventario detalle : detalles) {

            if (detalle == null
                    || detalle.getIdArticulo() <= 0
                    || detalle.getCantidad() <= 0
                    || detalle.getPrecio() < 0) {

                return false;
            }
        }

        return true;
    }

    // VALIDAR EXISTENCIAS ANTES DE REGISTRAR UNA SALIDA.
    private boolean validarExistencias(
            CabeceraInventario cabecera,
            List<DetalleInventario> detalles,
            int numeroExcluir,
            Connection cn) throws SQLException {

        int signo = existenciasDAO.obtenerSigno(
                cabecera.getIdTipoMovimiento(), cn);

        // Las entradas no necesitan validar stock disponible.
        if (signo >= 0) {
            return true;
        }

        // Agrupar las cantidades si un artículo aparece varias veces.
        Map<Integer, Integer> cantidadesPorArticulo =
                new HashMap<>();

        for (DetalleInventario detalle : detalles) {

            int codigo = detalle.getIdArticulo();

            cantidadesPorArticulo.put(
                    codigo,
                    cantidadesPorArticulo.getOrDefault(codigo, 0)
                    + detalle.getCantidad()
            );
        }

        // Comprobar el stock de cada artículo.
        for (Map.Entry<Integer, Integer> entrada
                : cantidadesPorArticulo.entrySet()) {

            int codigoArticulo = entrada.getKey();
            int cantidadSolicitada = entrada.getValue();

            int stockActual = existenciasDAO.obtenerStockActual(
                    codigoArticulo,
                    numeroExcluir,
                    cn);

            if (cantidadSolicitada > stockActual) {

                System.out.println(
                        "VENTA RECHAZADA. Artículo: "
                        + codigoArticulo
                        + ". Stock disponible: "
                        + stockActual
                        + ". Cantidad solicitada: "
                        + cantidadSolicitada);

                return false;
            }
        }

        return true;
    }

    // GUARDAR COMPROBANTE.
    public int guardar(
            CabeceraInventario cabecera,
            List<DetalleInventario> detalles) {

        if (!validar(cabecera, detalles)) {
            return 0;
        }

        try (Connection cn = new ConexionOracle().conectar()) {

            if (cn == null) {
                System.out.println("No existe conexión con Oracle.");
                return 0;
            }

            cn.setAutoCommit(false);

            try {

                // VALIDAR STOCK ANTES DE INSERTAR.
                if (!validarExistencias(
                        cabecera, detalles, 0, cn)) {

                    cn.rollback();
                    return 0;
                }

                int numero = cabeceraDAO.guardar(cabecera, cn);

                if (numero <= 0) {
                    throw new SQLException(
                            "No se generó el número del comprobante.");
                }

                for (DetalleInventario detalle : detalles) {

                    detalle.setNumeroComprobante(numero);

                    if (!detalleDAO.guardar(detalle, cn)) {
                        throw new SQLException(
                                "No se pudo guardar un detalle.");
                    }
                }

                cn.commit();
                return numero;

            } catch (SQLException e) {

                cn.rollback();

                System.out.println(
                        "Error al guardar: " + e.getMessage());

                return 0;
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error de conexión: " + e.getMessage());

            return 0;
        }
    }

    // BUSCAR CABECERA.
    public CabeceraInventario buscarCabecera(int numero) {

        try (Connection cn = new ConexionOracle().conectar()) {

            if (cn == null) {
                return null;
            }

            return cabeceraDAO.buscarPorNumero(numero, cn);

        } catch (SQLException e) {

            System.out.println(
                    "Error al buscar cabecera: " + e.getMessage());

            return null;
        }
    }

    // LISTAR DETALLES.
    public List<DetalleInventario> listarDetalles(int numero) {

        try (Connection cn = new ConexionOracle().conectar()) {

            if (cn == null) {
                return null;
            }

            return detalleDAO.listarPorComprobante(numero, cn);

        } catch (SQLException e) {

            System.out.println(
                    "Error al listar detalles: " + e.getMessage());

            return null;
        }
    }

    // MODIFICAR COMPROBANTE COMPLETO.
    public boolean modificar(
            CabeceraInventario cabecera,
            List<DetalleInventario> detalles) {

        if (cabecera == null
                || cabecera.getNumero() <= 0
                || !validar(cabecera, detalles)) {

            return false;
        }

        try (Connection cn = new ConexionOracle().conectar()) {

            if (cn == null) {
                return false;
            }

            cn.setAutoCommit(false);

            try {

                // VALIDAR STOCK EXCLUYENDO EL COMPROBANTE ACTUAL.
                if (!validarExistencias(
                        cabecera,
                        detalles,
                        cabecera.getNumero(),
                        cn)) {

                    cn.rollback();
                    return false;
                }

                boolean actualizado =
                        cabeceraDAO.actualizar(cabecera, cn);

                if (!actualizado) {
                    throw new SQLException(
                            "No se encontró la cabecera.");
                }

                // Eliminar los detalles anteriores.
                detalleDAO.eliminarPorComprobante(
                        cabecera.getNumero(), cn);

                // Registrar nuevamente los detalles.
                for (DetalleInventario detalle : detalles) {

                    detalle.setNumeroComprobante(
                            cabecera.getNumero());

                    if (!detalleDAO.guardar(detalle, cn)) {
                        throw new SQLException(
                                "No se pudo actualizar un detalle.");
                    }
                }

                cn.commit();
                return true;

            } catch (SQLException e) {

                cn.rollback();

                System.out.println(
                        "Error al modificar: " + e.getMessage());

                return false;
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error de conexión: " + e.getMessage());

            return false;
        }
    }

    // ELIMINAR COMPROBANTE COMPLETO.
    public boolean eliminar(int numero) {

        if (numero <= 0) {
            return false;
        }

        try (Connection cn = new ConexionOracle().conectar()) {

            if (cn == null) {
                return false;
            }

            cn.setAutoCommit(false);

            try {

                // Primero eliminar los detalles.
                detalleDAO.eliminarPorComprobante(numero, cn);

                // Después eliminar la cabecera.
                boolean eliminado =
                        cabeceraDAO.eliminar(numero, cn);

                if (!eliminado) {
                    throw new SQLException(
                            "No se encontró el comprobante.");
                }

                cn.commit();
                return true;

            } catch (SQLException e) {

                cn.rollback();

                System.out.println(
                        "Error al eliminar: " + e.getMessage());

                return false;
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error de conexión: " + e.getMessage());

            return false;
        }
    }
}
