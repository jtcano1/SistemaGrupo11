package datos.inventarios;

import conexion.ConexionOracle;
import entidades.inventarios.Articulo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ArticuloDAO {

    public boolean guardar(Articulo articulo) {

        boolean guardado = false;

        String sql = """
                     INSERT INTO ARTICULO
                     (NOMBRE, PRECIO)
                     VALUES (?, ?)
                     """;

        try {

            Connection cn = new ConexionOracle().conectar();

            PreparedStatement ps = cn.prepareStatement(sql);

            ps.setString(1, articulo.getNombre());
            ps.setDouble(2, articulo.getPrecio());

            int filas = ps.executeUpdate();

            if (filas > 0) {
                guardado = true;
            }

            ps.close();
            cn.close();

        } catch (SQLException e) {

            System.out.println("Error al guardar: "
                    + e.getMessage());

        }

        return guardado;
    }

    public boolean modificar() {
        return false;
    }

    public boolean eliminar() {
        return false;
    }

    public void buscar() {

    }

    public void listar() {

    }
}