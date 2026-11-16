/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package datos.activos;

import conexion.ConexionOracle;
import entidades.activos.Activo;
import entidades.activos.TipoActivo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author USUARIO
 */
public class ActivoDAO {
    private final ConexionOracle conexionOracle = new ConexionOracle();

    public boolean insertar(Activo activo) {
        String sql = "INSERT INTO ACTIVO (NOMBRE, VALOR_COMPRA, VIDA_UTIL, CODIGO_TIPACTIVO) VALUES (?, ?, ?, ?)";
        try (Connection conn = conexionOracle.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, activo.getNombre());
            ps.setBigDecimal(2, activo.getValorCompra());
            ps.setInt(3, activo.getPeriodosDepreciacion());
            ps.setInt(4, activo.getTipoActivo().getCodigo());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al insertar Activo: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Activo activo) {
        String sql = "UPDATE ACTIVO SET NOMBRE = ?, VALOR_COMPRA = ?, VIDA_UTIL = ?, CODIGO_TIPACTIVO = ? WHERE CODIGO_ACTIVO = ?";
        try (Connection conn = conexionOracle.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, activo.getNombre());
            ps.setBigDecimal(2, activo.getValorCompra());
            ps.setInt(3, activo.getPeriodosDepreciacion());
            ps.setInt(4, activo.getTipoActivo().getCodigo());
            ps.setInt(5, activo.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar Activo: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int idActivo) {
        String sql = "DELETE FROM ACTIVO WHERE CODIGO_ACTIVO = ?";
        try (Connection conn = conexionOracle.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idActivo);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar Activo: " + e.getMessage());
            return false;
        }
    }

    public List<Activo> listarTodos() {
    List<Activo> lista = new ArrayList<>();
    String sql = "SELECT a.CODIGO_ACTIVO, a.NOMBRE, a.VALOR_COMPRA, a.VIDA_UTIL, " +
                 "t.CODIGO_TIPACTIVO, t.NOMBRE AS NOMBRE_TIPO, t.CUENTA_GASTO, t.CUENTA_DEP_ACUMULADA " +
                 "FROM ACTIVO a INNER JOIN TIPO_ACTIVO t ON a.CODIGO_TIPACTIVO = t.CODIGO_TIPACTIVO " +
                 "ORDER BY a.CODIGO_ACTIVO";

    try (Connection conn = conexionOracle.conectar();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            TipoActivo tipo = new TipoActivo();
            tipo.setCodigo(rs.getInt("CODIGO_TIPACTIVO"));
            tipo.setNombre(rs.getString("NOMBRE_TIPO"));
            tipo.setCuentaGasto(rs.getString("CUENTA_GASTO"));
            tipo.setCuentaDepAcumulada(rs.getString("CUENTA_DEP_ACUMULADA"));

            Activo activo = new Activo();
            activo.setId(rs.getInt("CODIGO_ACTIVO"));
            activo.setNombre(rs.getString("NOMBRE"));
            activo.setValorCompra(rs.getBigDecimal("VALOR_COMPRA"));
            activo.setPeriodosDepreciacion(rs.getInt("VIDA_UTIL"));
            activo.setTipoActivo(tipo);

            lista.add(activo);
        }
    } catch (SQLException e) {
        System.out.println("Error al listar Activos: " + e.getMessage());
    }
    return lista;
}

    // Reporte 2: Número de activos por tipo de activo (Ej: Muebles 3, Vehículos 4)
    public Map<String, Integer> contarActivosPorTipo() {
        Map<String, Integer> reporte = new LinkedHashMap<>();
        String sql = "SELECT t.NOMBRE, COUNT(a.CODIGO_ACTIVO) AS CANTIDAD " +
                     "FROM TIPO_ACTIVO t LEFT JOIN ACTIVO a ON t.CODIGO_TIPACTIVO = a.CODIGO_TIPACTIVO " +
                     "GROUP BY t.NOMBRE ORDER BY t.NOMBRE";
        try (Connection conn = conexionOracle.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                reporte.put(rs.getString("NOMBRE"), rs.getInt("CANTIDAD"));
            }
        } catch (SQLException e) {
            System.out.println("Error en reporte por tipo: " + e.getMessage());
        }
        return reporte;
    }

    // Reporte 1: Valores depreciados por activo en un rango de fechas
    public List<Object[]> obtenerValoresDepreciadosPorRango(Date fechaInicio, Date fechaFin) {
        List<Object[]> resultados = new ArrayList<>();
        String sql = "SELECT a.CODIGO_ACTIVO, a.NOMBRE, NVL(SUM(d.VALOR_DEPRECIACION), 0) AS TOTAL_DEPRECIADO " +
                     "FROM ACTIVO a " +
                     "INNER JOIN DETALLE_DEPRECIACION d ON a.CODIGO_ACTIVO = d.CODIGO_ACTIVO " +
                     "INNER JOIN CABECERA_DEPRECIACION c ON d.NUMERO_DEPRECIACION = c.NUMERO_DEPRECIACION " +
                     "WHERE TRUNC(c.FECHA) BETWEEN TRUNC(?) AND TRUNC(?) " +
                     "GROUP BY a.CODIGO_ACTIVO, a.NOMBRE ORDER BY a.NOMBRE";
        try (Connection conn = conexionOracle.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, fechaInicio);
            ps.setDate(2, fechaFin);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    resultados.add(new Object[]{
                        rs.getInt("CODIGO_ACTIVO"),
                        rs.getString("NOMBRE"),
                        rs.getBigDecimal("TOTAL_DEPRECIADO")
                    });
                }
            }
        } catch (SQLException e) {
            System.out.println("Error en reporte de valores depreciados: " + e.getMessage());
        }
        return resultados;
    }
     
    
}
