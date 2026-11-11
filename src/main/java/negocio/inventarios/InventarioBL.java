
package negocio.inventarios;

import conexion.ConexionOracle;
import datos.inventarios.CabeceraInventarioDAO;
import datos.inventarios.DetalleInventarioDAO;
import entidades.inventarios.CabeceraInventario;
import entidades.inventarios.DetalleInventario;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class InventarioBL {

    private final CabeceraInventarioDAO cabeceraDAO;
    private final DetalleInventarioDAO detalleDAO;

    public InventarioBL() {
        cabeceraDAO = new CabeceraInventarioDAO();
        detalleDAO = new DetalleInventarioDAO();
    }

    // VALIDAR DATOS DEL COMPROBANTE
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

    // GUARDAR COMPROBANTE
    public int guardar(
            CabeceraInventario cabecera,
            List<DetalleInventario> detalles) {

        if (!validar(cabecera, detalles)) {
            return 0;
        }

        try (Connection cn = new ConexionOracle().conectar()) {
            cn.setAutoCommit(false);

            try {
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

    // BUSCAR CABECERA
    public CabeceraInventario buscarCabecera(int numero) {

        try (Connection cn = new ConexionOracle().conectar()) {
            return cabeceraDAO.buscarPorNumero(numero, cn);
        } catch (SQLException e) {
            System.out.println(
                    "Error al buscar cabecera: " + e.getMessage());
            return null;
        }
    }

    // LISTAR DETALLES
    public List<DetalleInventario> listarDetalles(int numero) {

        try (Connection cn = new ConexionOracle().conectar()) {
            return detalleDAO.listarPorComprobante(numero, cn);

        } catch (SQLException e) {
            System.out.println(
                    "Error al listar detalles: " + e.getMessage());
            return null;
        }
    }

    // MODIFICAR COMPROBANTE COMPLETO
    public boolean modificar(
            CabeceraInventario cabecera,
            List<DetalleInventario> detalles) {

        if (cabecera == null
                || cabecera.getNumero() <= 0
                || !validar(cabecera, detalles)) {
            return false;
        }

        try (Connection cn = new ConexionOracle().conectar()) {
            cn.setAutoCommit(false);

            try {
                boolean actualizado =
                        cabeceraDAO.actualizar(cabecera, cn);

                if (!actualizado) {
                    throw new SQLException(
                            "No se encontró la cabecera.");
                }

                // Reemplazar los detalles anteriores
                detalleDAO.eliminarPorComprobante(
                        cabecera.getNumero(), cn);

                // Guardar los detalles actualizados
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
                System.out.println("Error al modificar: " + e.getMessage());
                return false;
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error de conexión: " + e.getMessage());
            return false;
        }
    }

    // ELIMINAR COMPROBANTE COMPLETO
    public boolean eliminar(int numero) {

        if (numero <= 0) {
            return false;
        }

        try (Connection cn = new ConexionOracle().conectar()) {
            cn.setAutoCommit(false);

            try {
                // Primero eliminar los detalles
                detalleDAO.eliminarPorComprobante(numero, cn);

                // Después eliminar la cabecera
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
