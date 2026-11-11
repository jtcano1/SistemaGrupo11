package datos.inventarios;

import conexion.ConexionOracle;
import entidades.inventarios.CabeceraInventario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;

public class CabeceraInventarioDAO {

    public boolean guardar(CabeceraInventario cabecera) {

        boolean guardado = false;
        String sql =
                "INSERT INTO CABECERA_INVENTARIO "
                + "(FECHA, ID_TIPO_MOVIMIENTO) "
                + "VALUES (?, ?)";

        try {
            Connection cn = new ConexionOracle().conectar();
            PreparedStatement ps = cn.prepareStatement(sql);

            ps.setDate(1, new Date(cabecera.getFecha().getTime()));  
            ps.setInt(2,cabecera.getIdTipoMovimiento());

            guardado = ps.executeUpdate() > 0;

            ps.close();
            cn.close();

        } catch (SQLException e) {

            System.out.println("Error guardar cabecera: "+ e.getMessage());
        }
        return guardado;
    }
}