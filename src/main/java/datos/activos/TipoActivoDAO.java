/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package datos.activos;

import conexion.ConexionOracle;
import entidades.activos.TipoActivo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author USUARIO
 */
public class TipoActivoDAO {
    private final ConexionOracle conexionOracle = new ConexionOracle();

    public boolean insertar(TipoActivo tipo) {
        String sql = "INSERT INTO TIPO_ACTIVO (NOMBRE, DESCRIPCION, CUENTA_GASTO, CUENTA_DEP_ACUMULADA) VALUES (?, ?, ?, ?)";
        try (Connection conn = conexionOracle.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tipo.getNombre());
            ps.setString(2, tipo.getDescripcion());
            ps.setString(3, tipo.getCuentaGasto());
            ps.setString(4, tipo.getCuentaDepAcumulada());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al insertar TipoActivo: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(TipoActivo tipo) {
        String sql = "UPDATE TIPO_ACTIVO SET NOMBRE = ? WHERE CODIGO_TIPACTIVO = ?";
        try (Connection conn = conexionOracle.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tipo.getNombre());
            ps.setInt(2, tipo.getCodigo()); // Asegúrate de que tu getter en TipoActivo se llame getCodigo() o getId()
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar TipoActivo: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int codigo) {
        String sql = "DELETE FROM TIPO_ACTIVO WHERE CODIGO_TIPACTIVO = ?";
        try (Connection conn = conexionOracle.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, codigo);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar TipoActivo: " + e.getMessage());
            return false;
        }
    }

    public List<TipoActivo> listarTodos() {
        List<TipoActivo> lista = new ArrayList<>();
        String sql = "SELECT CODIGO_TIPACTIVO, NOMBRE, DESCRIPCION, CUENTA_GASTO, CUENTA_DEP_ACUMULADA FROM TIPO_ACTIVO ORDER BY CODIGO_TIPACTIVO";
        try (Connection conn = conexionOracle.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                TipoActivo tipo = new TipoActivo();
                tipo.setCodigo(rs.getInt("CODIGO_TIPACTIVO"));
                tipo.setNombre(rs.getString("NOMBRE"));
                tipo.setDescripcion(rs.getString("DESCRIPCION"));
                tipo.setCuentaGasto(rs.getString("CUENTA_GASTO"));
                tipo.setCuentaDepAcumulada(rs.getString("CUENTA_DEP_ACUMULADA"));
                lista.add(tipo);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar TipoActivo: " + e.getMessage());
        }
        return lista;
    }
}
