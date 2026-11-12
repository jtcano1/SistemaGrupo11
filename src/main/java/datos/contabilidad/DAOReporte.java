/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package datos.contabilidad;

/**
 *
 * @author USER
 */
import conexion.ConexionOracle;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DAOReporte {

    public List<String[]> reporteBalanceGeneral(Date desde, Date hasta) {
        List<String[]> lista = new ArrayList<>();
        String sql = "SELECT d.CODIGO_CUENTA, cu.NOMBRE, t.NOMBRE, SUM(d.CANTIDAD_DEBE - d.CANTIDAD_HABER) AS SALDO " +
                     "FROM DETALLE_COMPROBANTE d " +
                     "JOIN CUENTA cu ON cu.CODIGO = d.CODIGO_CUENTA " +
                     "JOIN TIPO_CUENTA t ON t.CODIGO = cu.CODIGO_TIPO_CUENTA " +
                     "JOIN CABECERA_COMPROBANTE cab ON cab.NUMERO = d.NUMERO_COMPROBANTE " +
                     "WHERE cab.FECHA BETWEEN ? AND ? " +
                     "AND (d.CODIGO_CUENTA LIKE '1%' OR d.CODIGO_CUENTA LIKE '2%' OR d.CODIGO_CUENTA LIKE '3%') " +
                     "GROUP BY d.CODIGO_CUENTA, cu.NOMBRE, t.NOMBRE " +
                     "ORDER BY d.CODIGO_CUENTA";
        try (Connection con = new ConexionOracle().conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, new java.sql.Date(desde.getTime()));
            ps.setDate(2, new java.sql.Date(hasta.getTime()));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new String[]{ rs.getString(1), rs.getString(2), rs.getString(3), String.valueOf(rs.getDouble(4)) });
                }
            }
        } catch (Exception e) {
            System.out.println("Error Balance General: " + e.getMessage());
        }
        return lista;
    }

    public List<String[]> reporteEstadoResultados(Date desde, Date hasta) {
        List<String[]> lista = new ArrayList<>();
        String sql = "SELECT d.CODIGO_CUENTA, cu.NOMBRE, t.NOMBRE, SUM(d.CANTIDAD_DEBE) AS TOTAL_DEBE, SUM(d.CANTIDAD_HABER) AS TOTAL_HABER, SUM(d.CANTIDAD_HABER - d.CANTIDAD_DEBE) AS SALDO " +
                     "FROM DETALLE_COMPROBANTE d " +
                     "JOIN CUENTA cu ON cu.CODIGO = d.CODIGO_CUENTA " +
                     "JOIN TIPO_CUENTA t ON t.CODIGO = cu.CODIGO_TIPO_CUENTA " +
                     "JOIN CABECERA_COMPROBANTE cab ON cab.NUMERO = d.NUMERO_COMPROBANTE " +
                     "WHERE cab.FECHA BETWEEN ? AND ? " +
                     "AND (d.CODIGO_CUENTA LIKE '4%' OR d.CODIGO_CUENTA LIKE '5%') " +
                     "GROUP BY d.CODIGO_CUENTA, cu.NOMBRE, t.NOMBRE " +
                     "ORDER BY d.CODIGO_CUENTA";
        try (Connection con = new ConexionOracle().conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, new java.sql.Date(desde.getTime()));
            ps.setDate(2, new java.sql.Date(hasta.getTime()));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new String[]{ rs.getString(1), rs.getString(2), rs.getString(3), String.valueOf(rs.getDouble(4)), String.valueOf(rs.getDouble(5)), String.valueOf(rs.getDouble(6)) });
                }
            }
        } catch (Exception e) {
            System.out.println("Error Estado Resultados: " + e.getMessage());
        }
        return lista;
    }
}