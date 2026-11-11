package datos.inventarios;

import conexion.ConexionOracle;
import entidades.inventarios.DetalleInventario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DetalleInventarioDAO {

    public boolean guardar(DetalleInventario detalle) {

        boolean guardado = false;

        String sql =
                "INSERT INTO DETALLE_INVENTARIO "
                + "(NUMERO_COMPROBANTE, "
                + "ID_ARTICULO, "
                + "CANTIDAD, "
                + "PRECIO) "
                + "VALUES (?, ?, ?, ?)";

        try {

            Connection cn = new ConexionOracle().conectar();

            PreparedStatement ps = cn.prepareStatement(sql);

            ps.setInt(1,detalle.getNumeroComprobante());
            ps.setInt(2,detalle.getIdArticulo());
            ps.setInt(3,detalle.getCantidad());
            ps.setDouble(4,detalle.getPrecio());

            guardado = ps.executeUpdate() > 0;

            ps.close();
            cn.close();

        } catch (SQLException e) {
            System.out.println("Error guardar detalle: "+ e.getMessage());
        }

        return guardado;
    }
}