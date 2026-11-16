package datos.mantenimiento;

import conexion.ConexionOracle;
import entidades.mantenimiento.Activo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ActivoDAO {

    public boolean guardar(Activo activo) {
        String sql = "INSERT INTO ACTIVOS_M (CODIGO, NOMBRE, FECHA_COMPRA) "
                + "VALUES (SEQ_ACTIVOS_M.NEXTVAL, ?, ?)";
        try (Connection con = new ConexionOracle().conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, activo.getNombre());
            ps.setDate(2, new java.sql.Date(activo.getFechaCompra().getTime()));
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error al guardar activo: " + e.getMessage());
            return false;
        }
    }

    public boolean modificar(Activo activo) {
        String sql = "UPDATE ACTIVOS_M SET NOMBRE = ?, FECHA_COMPRA = ? WHERE CODIGO = ?";
        try (Connection con = new ConexionOracle().conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, activo.getNombre());
            ps.setDate(2, new java.sql.Date(activo.getFechaCompra().getTime()));
            ps.setInt(3, activo.getCodigo());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error al modificar activo: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int codigo) {
        String sql = "DELETE FROM ACTIVOS_M WHERE CODIGO = ?";
        try (Connection con = new ConexionOracle().conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, codigo);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error al eliminar activo: " + e.getMessage());
            return false;
        }
    }

    public Activo buscar(int codigo) {
        String sql = "SELECT CODIGO, NOMBRE, FECHA_COMPRA FROM ACTIVOS_M WHERE CODIGO = ?";
        try (Connection con = new ConexionOracle().conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Activo(rs.getInt("CODIGO"), rs.getString("NOMBRE"),
                            rs.getDate("FECHA_COMPRA"));
                }
            }
        } catch (Exception e) {
            System.out.println("Error al buscar activo: " + e.getMessage());
        }
        return null;
    }

    public List<Activo> listar(String filtro) {
        List<Activo> lista = new ArrayList<>();
        String sql = "SELECT CODIGO, NOMBRE, FECHA_COMPRA FROM ACTIVOS_M "
                + "WHERE UPPER(NOMBRE) LIKE UPPER(?) ORDER BY CODIGO";
        try (Connection con = new ConexionOracle().conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + (filtro == null ? "" : filtro.trim()) + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Activo(rs.getInt("CODIGO"), rs.getString("NOMBRE"),
                            rs.getDate("FECHA_COMPRA")));
                }
            }
        } catch (Exception e) {
            System.out.println("Error al listar activos: " + e.getMessage());
        }
        return lista;
    }
}
