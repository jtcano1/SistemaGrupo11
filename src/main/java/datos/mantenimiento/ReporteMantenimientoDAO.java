package datos.mantenimiento;

import conexion.ConexionOracle;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReporteMantenimientoDAO {

    public List<Object[]> gastoPorActivo(Date desde, Date hasta) {
        List<Object[]> filas = new ArrayList<>();
        String sql = "SELECT a.NOMBRE, SUM(d.VALOR) "
                + "FROM DETALLE_MANTENIMIENTO d "
                + "JOIN CABECERA_MANTENIMIENTO c ON c.NUMERO = d.NUMERO_MANTENIMIENTO "
                + "JOIN ACTIVOS_M a ON a.CODIGO = d.CODIGO_ACTIVO "
                + "WHERE TRUNC(c.FECHA) BETWEEN TRUNC(?) AND TRUNC(?) "
                + "GROUP BY a.CODIGO, a.NOMBRE ORDER BY a.NOMBRE";
        try (Connection con = new ConexionOracle().conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, new java.sql.Date(desde.getTime()));
            ps.setDate(2, new java.sql.Date(hasta.getTime()));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    filas.add(new Object[]{rs.getString(1), rs.getDouble(2)});
                }
            }
        } catch (Exception e) {
            System.out.println("Error en reporte de gasto por activo: " + e.getMessage());
        }
        return filas;
    }

    public List<Object[]> gastoPorActivoYActividad(Date desde, Date hasta) {
        List<Object[]> filas = new ArrayList<>();
        String sql = "SELECT a.NOMBRE, t.NOMBRE, SUM(d.VALOR) "
                + "FROM DETALLE_MANTENIMIENTO d "
                + "JOIN CABECERA_MANTENIMIENTO c ON c.NUMERO = d.NUMERO_MANTENIMIENTO "
                + "JOIN ACTIVOS_M a ON a.CODIGO = d.CODIGO_ACTIVO "
                + "JOIN ACTIVIDAD t ON t.CODIGO = d.CODIGO_ACTIVIDAD "
                + "WHERE TRUNC(c.FECHA) BETWEEN TRUNC(?) AND TRUNC(?) "
                + "GROUP BY a.CODIGO, a.NOMBRE, t.CODIGO, t.NOMBRE "
                + "ORDER BY a.NOMBRE, t.NOMBRE";
        try (Connection con = new ConexionOracle().conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, new java.sql.Date(desde.getTime()));
            ps.setDate(2, new java.sql.Date(hasta.getTime()));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    filas.add(new Object[]{rs.getString(1), rs.getString(2), rs.getDouble(3)});
                }
            }
        } catch (Exception e) {
            System.out.println("Error en reporte cruzado: " + e.getMessage());
        }
        return filas;
    }
}
