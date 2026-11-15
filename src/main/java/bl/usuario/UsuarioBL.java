/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bl.usuario;

/**
 *
 * @author USER
 */
import datos.usuario.UsuarioDAO;
import entidad.usuario.Usuario;
import java.util.List;

public class UsuarioBL {
    UsuarioDAO dao = new UsuarioDAO();

    public String guardar(Usuario u) {
        if (u.getNombre() == null || u.getNombre().isEmpty() ||
            u.getUsuario() == null || u.getUsuario().isEmpty() ||
            u.getClave() == null || u.getClave().isEmpty()) {
            return "Faltan datos obligatorios";
        }
        if (dao.buscarPorUsuario(u.getUsuario()) != null) {
            return "Ese usuario ya existe";
        }
        if (dao.guardar(u)) return "Guardado correctamente";
        return "Error al guardar";
    }

    public String modificar(Usuario u) {
        if (dao.modificar(u)) return "Modificado correctamente";
        return "Error al modificar";
    }

    public String eliminar(int id) {
        if (id == 1) return "No se puede eliminar al administrador";
        if (dao.eliminar(id)) return "Eliminado correctamente";
        return "Error al eliminar";
    }

    public List<Usuario> listar() { return dao.listar(); }
    public Usuario validarLogin(String usuario, String clave) { return dao.validarLogin(usuario, clave); }
}