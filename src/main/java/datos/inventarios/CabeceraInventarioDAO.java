
package datos.inventarios;

import entidades.inventarios.CabeceraInventario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CabeceraInventarioDAO {

    // INSERTAR CABECERA Y RECUPERAR EL NÚMERO GENERADO
    public int guardar(CabeceraInventario cabecera, Connection cn)
            throws SQLException {

        String sql = "INSERT INTO CABECERA_INVENTARIO "
                   + "(FECHA, ID_TIPO_MOVIMIENTO) VALUES (?, ?)";

        try (PreparedStatement ps = cn.prepareStatement(
                sql, new String[]{"NUMERO"})) {

            ps.setDate(1, new java.sql.Date(
                    cabecera.getFecha().getTime()));

            ps.setInt(2, cabecera.getIdTipoMovimiento());

            int filas = ps.executeUpdate();

            if (filas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        }

        return 0;
    }

    // BUSCAR CABECERA POR NÚMERO
    public CabeceraInventario buscarPorNumero(
            int numero, Connection cn) throws SQLException {

        String sql = "SELECT NUMERO, FECHA, ID_TIPO_MOVIMIENTO "
                   + "FROM CABECERA_INVENTARIO WHERE NUMERO = ?";

        try (PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, numero);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    CabeceraInventario cabecera =
                            new CabeceraInventario();

                    cabecera.setNumero(rs.getInt("NUMERO"));
                    cabecera.setFecha(rs.getDate("FECHA"));
                    cabecera.setIdTipoMovimiento(
                            rs.getInt("ID_TIPO_MOVIMIENTO"));

                    return cabecera;
                }
            }
        }

        return null;
    }

    // ACTUALIZAR CABECERA
    public boolean actualizar(
            CabeceraInventario cabecera, Connection cn)
            throws SQLException {

        String sql = "UPDATE CABECERA_INVENTARIO "
                   + "SET FECHA = ?, ID_TIPO_MOVIMIENTO = ? "
                   + "WHERE NUMERO = ?";

        try (PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setDate(1, new java.sql.Date(
                    cabecera.getFecha().getTime()));

            ps.setInt(2, cabecera.getIdTipoMovimiento());
            ps.setInt(3, cabecera.getNumero());

            return ps.executeUpdate() > 0;
        }
    }

    // ELIMINAR CABECERA
    public boolean eliminar(int numero, Connection cn)
            throws SQLException {

        String sql = "DELETE FROM CABECERA_INVENTARIO "
                   + "WHERE NUMERO = ?";

        try (PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, numero);

            return ps.executeUpdate() > 0;
        }
    }
}
