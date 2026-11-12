
package datos.inventarios;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ControlExistenciasDAO {

    // Consultar el signo del tipo de movimiento.
    public int obtenerSigno(
            int codigoTipoMovimiento,
            Connection cn) throws SQLException {

        String sql = "SELECT SIGNO "
                   + "FROM TIPO_MOVIMIENTO "
                   + "WHERE CODIGO = ?";

        try (PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, codigoTipoMovimiento);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt("SIGNO");
                }
            }
        }

        throw new SQLException(
                "No existe el tipo de movimiento seleccionado.");
    }

    // Calcular el stock actual de un artículo.
    // numeroExcluir permite ignorar un comprobante al modificarlo.
    public int obtenerStockActual(
            int idArticulo,
            int numeroExcluir,
            Connection cn) throws SQLException {

        String sql;

        if (numeroExcluir > 0) {

            sql = "SELECT NVL(SUM(D.CANTIDAD * TM.SIGNO), 0) "
                + "AS STOCK "
                + "FROM DETALLE_INVENTARIO D "
                + "INNER JOIN CABECERA_INVENTARIO C "
                + "ON D.NUMERO_COMPROBANTE = C.NUMERO "
                + "INNER JOIN TIPO_MOVIMIENTO TM "
                + "ON C.ID_TIPO_MOVIMIENTO = TM.CODIGO "
                + "WHERE D.ID_ARTICULO = ? "
                + "AND C.NUMERO <> ?";

        } else {

            sql = "SELECT NVL(SUM(D.CANTIDAD * TM.SIGNO), 0) "
                + "AS STOCK "
                + "FROM DETALLE_INVENTARIO D "
                + "INNER JOIN CABECERA_INVENTARIO C "
                + "ON D.NUMERO_COMPROBANTE = C.NUMERO "
                + "INNER JOIN TIPO_MOVIMIENTO TM "
                + "ON C.ID_TIPO_MOVIMIENTO = TM.CODIGO "
                + "WHERE D.ID_ARTICULO = ?";
        }

        try (PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, idArticulo);

            if (numeroExcluir > 0) {
                ps.setInt(2, numeroExcluir);
            }

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt("STOCK");
                }
            }
        }

        return 0;
    }
}
