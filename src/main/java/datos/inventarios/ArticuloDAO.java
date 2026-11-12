package datos.inventarios;

import conexion.ConexionOracle;
import entidades.inventarios.Articulo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    public boolean modificar(Articulo articulo) {

        boolean actualizado = false;
        String sql =
                "UPDATE ARTICULO "
                + "SET NOMBRE=?, PRECIO=? "
                + "WHERE CODIGO=?";

        try {
            Connection cn = new ConexionOracle().conectar();
            PreparedStatement ps = cn.prepareStatement(sql);
            ps.setString(1, articulo.getNombre());
            ps.setDouble(2, articulo.getPrecio());
            ps.setInt(3, articulo.getCodigo());

            actualizado = ps.executeUpdate() > 0;

            ps.close();
            cn.close();

        } catch (SQLException e) {

            System.out.println("Error al modificar: "
                    + e.getMessage());

        }
        return actualizado;
    }

    public boolean eliminar(int codigo) {

        boolean eliminado = false;
        String sql = "DELETE FROM ARTICULO WHERE CODIGO=?";

        try {
            Connection cn = new ConexionOracle().conectar();
            PreparedStatement ps = cn.prepareStatement(sql);

            ps.setInt(1, codigo);
            eliminado = ps.executeUpdate() > 0;
            ps.close();
            cn.close();

        } catch (SQLException e) {
            System.out.println("Error al eliminar: "
                    + e.getMessage());
        }

        return eliminado;
    }

    public Articulo buscar(int codigo) {

        Articulo articulo = null;
        String sql = "SELECT * FROM ARTICULO WHERE CODIGO=?";
        
        try {
            Connection cn = new ConexionOracle().conectar();
            PreparedStatement ps = cn.prepareStatement(sql);
            ps.setInt(1, codigo);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                articulo = new Articulo();
                articulo.setCodigo(rs.getInt("CODIGO"));
                articulo.setNombre(rs.getString("NOMBRE"));
                articulo.setPrecio(rs.getDouble("PRECIO"));
            }

            rs.close();
            ps.close();
            cn.close();

        } catch (SQLException e) {

            System.out.println("Error al buscar: "
                    + e.getMessage());
        }
        return articulo;
    }
    
    public List<Articulo> listar() {
        List<Articulo> lista = new ArrayList<>();
        String sql = "SELECT * FROM ARTICULO ORDER BY CODIGO";

    try {
        Connection cn = new ConexionOracle().conectar();
        PreparedStatement ps = cn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Articulo articulo = new Articulo();
            articulo.setCodigo(
                    rs.getInt("CODIGO"));
            articulo.setNombre(
                    rs.getString("NOMBRE"));
            articulo.setPrecio(
                    rs.getDouble("PRECIO"));
            lista.add(articulo);
        }
        rs.close();
        ps.close();
        cn.close();

    } catch (SQLException e) {
        System.out.println(
                "Error al listar: "
                + e.getMessage());
    }
    return lista;
}
}