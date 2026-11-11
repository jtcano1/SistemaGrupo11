
package datos.inventarios;

import conexion.ConexionOracle;
import entidades.inventarios.TipoMovimiento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

public class TipoMovimientoDAO {

    public boolean guardar(TipoMovimiento tipo) {

        boolean guardado = false;

        String sql =
                "INSERT INTO TIPO_MOVIMIENTO "
                + "(NOMBRE, SIGNO) "
                + "VALUES (?, ?)";

        try {

            Connection cn =
                    new ConexionOracle().conectar();

            PreparedStatement ps =
                    cn.prepareStatement(sql);

            ps.setString(1, tipo.getNombre());
            ps.setInt(2, tipo.getSigno());

            guardado =
                    ps.executeUpdate() > 0;

            ps.close();
            cn.close();

        } catch (SQLException e) {

            System.out.println(
                    "Error guardar: "
                    + e.getMessage());
        }

        return guardado;
    }

    public boolean modificar(
            TipoMovimiento tipo) {

        boolean actualizado = false;

        String sql =
                "UPDATE TIPO_MOVIMIENTO "
                + "SET NOMBRE=?, SIGNO=? "
                + "WHERE CODIGO=?";

        try {

            Connection cn =
                    new ConexionOracle().conectar();

            PreparedStatement ps =
                    cn.prepareStatement(sql);

            ps.setString(1,
                    tipo.getNombre());

            ps.setInt(2,
                    tipo.getSigno());

            ps.setInt(3,
                    tipo.getCodigo());

            actualizado =
                    ps.executeUpdate() > 0;

            ps.close();
            cn.close();

        } catch (SQLException e) {

            System.out.println(
                    "Error modificar: "
                    + e.getMessage());
        }

        return actualizado;
    }

    public boolean eliminar(int codigo) {

        boolean eliminado = false;

        String sql =
                "DELETE FROM TIPO_MOVIMIENTO "
                + "WHERE CODIGO=?";

        try {

            Connection cn =
                    new ConexionOracle().conectar();

            PreparedStatement ps =
                    cn.prepareStatement(sql);

            ps.setInt(1, codigo);

            eliminado =
                    ps.executeUpdate() > 0;

            ps.close();
            cn.close();

        } catch (SQLException e) {

            System.out.println(
                    "Error eliminar: "
                    + e.getMessage());
        }

        return eliminado;
    }

    public TipoMovimiento buscar(
            int codigo) {

        TipoMovimiento tipo = null;

        String sql =
                "SELECT * "
                + "FROM TIPO_MOVIMIENTO "
                + "WHERE CODIGO=?";

        try {

            Connection cn =
                    new ConexionOracle().conectar();

            PreparedStatement ps =
                    cn.prepareStatement(sql);

            ps.setInt(1, codigo);

            ResultSet rs =
                    ps.executeQuery();

            if (rs.next()) {

                tipo = new TipoMovimiento();

                tipo.setCodigo(
                        rs.getInt("CODIGO"));

                tipo.setNombre(
                        rs.getString("NOMBRE"));

                tipo.setSigno(
                        rs.getInt("SIGNO"));
            }

            rs.close();
            ps.close();
            cn.close();

        } catch (SQLException e) {

            System.out.println(
                    "Error buscar: "
                    + e.getMessage());
        }

        return tipo;
    }

    public List<TipoMovimiento> listar() {

        List<TipoMovimiento> lista =
                new ArrayList<>();

        String sql =
                "SELECT * "
                + "FROM TIPO_MOVIMIENTO "
                + "ORDER BY CODIGO";

        try {

            Connection cn =
                    new ConexionOracle().conectar();

            PreparedStatement ps =
                    cn.prepareStatement(sql);

            ResultSet rs =
                    ps.executeQuery();

            while (rs.next()) {

                TipoMovimiento tipo =
                        new TipoMovimiento();

                tipo.setCodigo(
                        rs.getInt("CODIGO"));

                tipo.setNombre(
                        rs.getString("NOMBRE"));

                tipo.setSigno(
                        rs.getInt("SIGNO"));

                lista.add(tipo);
            }

            rs.close();
            ps.close();
            cn.close();

        } catch (SQLException e) {

            System.out.println(
                    "Error listar: "
                    + e.getMessage());
        }

        return lista;
    }
}