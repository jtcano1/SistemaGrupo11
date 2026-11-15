/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package datos.usuario;

/**
 *
 * @author USER
 */
import conexion.ConexionOracle;
import entidad.usuario.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    // Método ayudante para no repetir la conexión en cada método
    private Connection getCon() {
        return new ConexionOracle().conectar();
    }

    public boolean guardar(Usuario u) {
        String sql = "INSERT INTO USUARIO VALUES (seq_usuario.NEXTVAL, ?, ?, ?, ?, ?)";
        try (Connection con = getCon();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getUsuario());
            ps.setString(3, u.getClave());
            ps.setString(4, u.getRol());
            ps.setString(5, u.getEstado());
            return ps.executeUpdate() > 0;
        } catch (Exception e) { System.out.println(e); return false; }
    }

    public boolean modificar(Usuario u) {
        String sql = "UPDATE USUARIO SET nombre=?, usuario=?, clave=?, rol=?, estado=? WHERE id_usuario=?";
        try (Connection con = getCon();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, u.getNombre());
            ps.setString(2, u.getUsuario());
            ps.setString(3, u.getClave());
            ps.setString(4, u.getRol());
            ps.setString(5, u.getEstado());
            ps.setInt(6, u.getId_usuario());
            return ps.executeUpdate() > 0;
        } catch (Exception e) { System.out.println(e); return false; }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM USUARIO WHERE id_usuario=?";
        try (Connection con = getCon();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { System.out.println(e); return false; }
    }

    public Usuario buscarPorUsuario(String usuario) {
        String sql = "SELECT * FROM USUARIO WHERE usuario=?";
        try (Connection con = getCon();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, usuario);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Usuario(rs.getInt("id_usuario"), rs.getString("nombre"),
                    rs.getString("usuario"), rs.getString("clave"),
                    rs.getString("rol"), rs.getString("estado"));
            }
        } catch (Exception e) { System.out.println(e); }
        return null;
    }

    public List<Usuario> listar() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM USUARIO ORDER BY id_usuario";
        try (Connection con = getCon();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Usuario(rs.getInt("id_usuario"), rs.getString("nombre"),
                    rs.getString("usuario"), rs.getString("clave"),
                    rs.getString("rol"), rs.getString("estado")));
            }
        } catch (Exception e) { System.out.println(e); }
        return lista;
    }

    public Usuario validarLogin(String usuario, String clave) {
        Usuario u = buscarPorUsuario(usuario);
        if (u != null && u.getClave().equals(clave) && u.getEstado().equals("ACTIVO")) {
            return u;
        }
        return null; // clave incorrecta o usuario inactivo
    }
}
