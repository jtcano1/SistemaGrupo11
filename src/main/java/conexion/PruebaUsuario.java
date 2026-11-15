/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package conexion;

/**
 *
 * @author USER
 */
import bl.usuario.UsuarioBL;
import entidad.usuario.Usuario;

public class PruebaUsuario {
    public static void main(String[] args) {
        UsuarioBL bl = new UsuarioBL();

        // 1. Probar Login correcto
        Usuario u = bl.validarLogin("admin", "1234");
        System.out.println(u != null ? "LOGIN OK: " + u.getNombre() : "LOGIN FALLO");

        // 2. Probar clave incorrecta
        Usuario u2 = bl.validarLogin("admin", "mal");
        System.out.println(u2 == null ? "Clave incorrecta detectada OK" : "ERROR");

        // 3. Probar Guardar
        Usuario nuevo = new Usuario(0, "Caty Prueba", "caty", "123", "USUARIO", "ACTIVO");
        System.out.println(bl.guardar(nuevo));

        // 4. Listar
        for (Usuario x : bl.listar()) {
            System.out.println(x.getId_usuario() + " - " + x.getUsuario());
        }
    }
}
