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
import java.util.List;

public class DAOReporte {

    // REPORTE 1: BALANCE GENERAL - solo cuentas 1, 2, 3
    // Devuelve: codigo cuenta, nombre, tipo, saldo
    // Saldo = SUM(DEBE - HABER)
    public List<String[]> reporteBalanceGeneral() {
        List<String[]> lista = new ArrayList<>();
        String sql = "SELECT d.CODIGO_CUENTA, cu.NOMBRE, t.NOMBRE, SUM(d.CANTIDAD_DEBE - d.CANTIDAD_HABER) AS SALDO " +
                     "FROM DETALLE_COMPROBANTE d " +
                     "JOIN CUENTA cu ON cu.CODIGO = d.CODIGO_CUENTA " +
                     "JOIN TIPO_CUENTA t ON t.CODIGO = cu.CODIGO_TIPO_CUENTA " +
                     "WHERE d.CODIGO_CUENTA LIKE '1%' OR d.CODIGO_CUENTA LIKE '2%' OR d.CODIGO_CUENTA LIKE '3%' " +
                     "GROUP BY d.CODIGO_CUENTA, cu.NOMBRE, t.NOMBRE " +
                     "ORDER BY d.CODIGO_CUENTA";
        try (Connection con = new ConexionOracle().conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new String[]{
                    rs.getString(1), // codigo ej: 1.1.1
                    rs.getString(2), // nombre cuenta
                    rs.getString(3), // tipo: Activo / Pasivo / Capital
                    String.valueOf(rs.getDouble(4)) // saldo
                });
            }
        } catch (Exception e) {
            System.out.println("Error Balance General: " + e.getMessage());
        }
        return lista;
    }

    // REPORTE 2: ESTADO DE RESULTADOS - solo cuentas 4 y 5
    // Devuelve: codigo, nombre, tipo, total debe, total haber, saldo
    // Utilidad = Total Ingresos - Total Egresos, eso lo calculamos en Negocio / Pantalla
    public List<String[]> reporteEstadoResultados() {
        List<String[]> lista = new ArrayList<>();
        String sql = "SELECT d.CODIGO_CUENTA, cu.NOMBRE, t.NOMBRE, SUM(d.CANTIDAD_DEBE) AS TOTAL_DEBE, SUM(d.CANTIDAD_HABER) AS TOTAL_HABER, SUM(d.CANTIDAD_HABER - d.CANTIDAD_DEBE) AS SALDO " +
                     "FROM DETALLE_COMPROBANTE d " +
                     "JOIN CUENTA cu ON cu.CODIGO = d.CODIGO_CUENTA " +
                     "JOIN TIPO_CUENTA t ON t.CODIGO = cu.CODIGO_TIPO_CUENTA " +
                     "WHERE d.CODIGO_CUENTA LIKE '4%' OR d.CODIGO_CUENTA LIKE '5%' " +
                     "GROUP BY d.CODIGO_CUENTA, cu.NOMBRE, t.NOMBRE " +
                     "ORDER BY d.CODIGO_CUENTA";
        try (Connection con = new ConexionOracle().conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new String[]{
                    rs.getString(1),
                    rs.getString(2),
                    rs.getString(3),
                    String.valueOf(rs.getDouble(4)),
                    String.valueOf(rs.getDouble(5)),
                    String.valueOf(rs.getDouble(6))
                });
            }
        } catch (Exception e) {
            System.out.println("Error Estado Resultados: " + e.getMessage());
        }
        return lista;
    }
}