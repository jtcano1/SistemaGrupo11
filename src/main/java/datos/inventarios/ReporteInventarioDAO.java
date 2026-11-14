
package datos.inventarios;

import entidades.inventarios.SaldoArticulo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.sql.Timestamp;


public class ReporteInventarioDAO {

    public List<SaldoArticulo> listarSaldos(
        Connection cn,Date hasta)
            throws SQLException {

        List<SaldoArticulo> lista = new ArrayList<>();

        String sql =
                "SELECT "
                + "A.CODIGO, "
                + "A.NOMBRE, "
                + "A.PRECIO, "
                + "NVL(SUM(CASE "
                + "WHEN TRUNC(C.FECHA) <= TRUNC(?) "
                + "THEN D.CANTIDAD * TM.SIGNO "
                + "ELSE 0 END), 0) AS STOCK "
                + "FROM ARTICULO A "
                + "LEFT JOIN DETALLE_INVENTARIO D "
                + "ON A.CODIGO = D.ID_ARTICULO "
                + "LEFT JOIN CABECERA_INVENTARIO C "
                + "ON D.NUMERO_COMPROBANTE = C.NUMERO "
                + "LEFT JOIN TIPO_MOVIMIENTO TM "
                + "ON C.ID_TIPO_MOVIMIENTO = TM.CODIGO "
                + "GROUP BY A.CODIGO, A.NOMBRE, A.PRECIO "
                + "ORDER BY A.CODIGO";
        
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setTimestamp(1,new Timestamp(hasta.getTime()));
            
            try (ResultSet rs = ps.executeQuery()) {
            
                while (rs.next()) {

                SaldoArticulo saldo = new SaldoArticulo();

                saldo.setCodigo(rs.getInt("CODIGO"));
                saldo.setNombre(rs.getString("NOMBRE"));
                saldo.setPrecio(rs.getDouble("PRECIO"));
                saldo.setStock(rs.getInt("STOCK"));

                lista.add(saldo);
            }
        }
    }
        return lista;
}
}

