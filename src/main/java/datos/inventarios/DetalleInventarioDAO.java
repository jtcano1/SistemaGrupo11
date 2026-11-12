
package datos.inventarios;

import entidades.inventarios.DetalleInventario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DetalleInventarioDAO {

    // INSERTAR DETALLE
    public boolean guardar(
            DetalleInventario detalle, Connection cn)
            throws SQLException {

        String sql = "INSERT INTO DETALLE_INVENTARIO "
                   + "(NUMERO_COMPROBANTE, ID_ARTICULO, "
                   + "CANTIDAD, PRECIO) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, detalle.getNumeroComprobante());
            ps.setInt(2, detalle.getIdArticulo());
            ps.setInt(3, detalle.getCantidad());
            ps.setDouble(4, detalle.getPrecio());

            return ps.executeUpdate() > 0;
        }
    }

    // LISTAR DETALLES DE UN COMPROBANTE
    public List<DetalleInventario> listarPorComprobante(
            int numero, Connection cn) throws SQLException {

        List<DetalleInventario> lista = new ArrayList<>();

        String sql = "SELECT ID_DETALLE, NUMERO_COMPROBANTE, "
                   + "ID_ARTICULO, CANTIDAD, PRECIO "
                   + "FROM DETALLE_INVENTARIO "
                   + "WHERE NUMERO_COMPROBANTE = ? "
                   + "ORDER BY ID_DETALLE";

        try (PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, numero);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    DetalleInventario detalle =
                            new DetalleInventario();

                    detalle.setIdDetalle(
                            rs.getInt("ID_DETALLE"));

                    detalle.setNumeroComprobante(
                            rs.getInt("NUMERO_COMPROBANTE"));

                    detalle.setIdArticulo(
                            rs.getInt("ID_ARTICULO"));

                    detalle.setCantidad(
                            rs.getInt("CANTIDAD"));

                    detalle.setPrecio(
                            rs.getDouble("PRECIO"));

                    lista.add(detalle);
                }
            }
        }

        return lista;
    }

    // ELIMINAR TODOS LOS DETALLES DE UN COMPROBANTE
    public boolean eliminarPorComprobante(
            int numero, Connection cn) throws SQLException {

        String sql = "DELETE FROM DETALLE_INVENTARIO "
                   + "WHERE NUMERO_COMPROBANTE = ?";

        try (PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, numero);

            ps.executeUpdate();

            return true;
        }
    }
}
