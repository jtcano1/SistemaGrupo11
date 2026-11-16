package datos.mantenimiento;

import conexion.ConexionOracle;
import entidades.mantenimiento.Actividad;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ActividadDAO {

    public boolean guardar(Actividad actividad) {
        String sql = "INSERT INTO ACTIVIDAD (CODIGO, NOMBRE) "
                + "VALUES (SEQ_ACTIVIDAD.NEXTVAL, ?)";
        try (Connection con = new ConexionOracle().conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, actividad.getNombre());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error al guardar actividad: " + e.getMessage());
            return false;
        }
    }

    public boolean modificar(Actividad actividad) {
        String sql = "UPDATE ACTIVIDAD SET NOMBRE = ? WHERE CODIGO = ?";
        try (Connection con = new ConexionOracle().conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, actividad.getNombre());
            ps.setInt(2, actividad.getCodigo());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error al modificar actividad: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int codigo) {
        String sql = "DELETE FROM ACTIVIDAD WHERE CODIGO = ?";
        try (Connection con = new ConexionOracle().conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, codigo);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error al eliminar actividad: " + e.getMessage());
            return false;
        }
    }

    public Actividad buscar(int codigo) {
        String sql = "SELECT CODIGO, NOMBRE FROM ACTIVIDAD WHERE CODIGO = ?";
        try (Connection con = new ConexionOracle().conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Actividad(rs.getInt("CODIGO"), rs.getString("NOMBRE"));
                }
            }
        } catch (Exception e) {
            System.out.println("Error al buscar actividad: " + e.getMessage());
        }
        return null;
    }

    public List<Actividad> listar(String filtro) {
        List<Actividad> lista = new ArrayList<>();
        String sql = "SELECT CODIGO, NOMBRE FROM ACTIVIDAD "
                + "WHERE UPPER(NOMBRE) LIKE UPPER(?) ORDER BY CODIGO";
        try (Connection con = new ConexionOracle().conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + (filtro == null ? "" : filtro.trim()) + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Actividad(rs.getInt("CODIGO"), rs.getString("NOMBRE")));
                }
            }
        } catch (Exception e) {
            System.out.println("Error al listar actividades: " + e.getMessage());
        }
        return lista;
    }
}
