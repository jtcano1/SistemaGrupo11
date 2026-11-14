package datos.inventarios;

import entidades.inventarios.ColumnaMovimiento;
import entidades.inventarios.MovimientoMatriz;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ReporteMatrizDAO {

    // OBTENER LOS TIPOS DE MOVIMIENTO
    public List<ColumnaMovimiento> listarTipos(Connection cn)
            throws SQLException {

        List<ColumnaMovimiento> lista = new ArrayList<>();

        String sql =
                "SELECT CODIGO, NOMBRE "
                + "FROM TIPO_MOVIMIENTO "
                + "ORDER BY CODIGO";

        try (PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                ColumnaMovimiento tipo =
                        new ColumnaMovimiento();

                tipo.setCodigo(rs.getInt("CODIGO"));
                tipo.setNombre(rs.getString("NOMBRE"));

                lista.add(tipo);
            }
        }

        return lista;
    }

    // OBTENER MOVIMIENTOS DENTRO DEL RANGO DE FECHAS
    public List<MovimientoMatriz> listarMovimientos(
            Connection cn,
            Date desde,
            Date hasta) throws SQLException {

        List<MovimientoMatriz> lista = new ArrayList<>();
        
        
String sql =
        "SELECT "
        + "A.CODIGO AS CODIGO_ARTICULO, "
        + "A.NOMBRE AS ARTICULO, "
        + "TM.CODIGO AS CODIGO_TIPO, "
        + "TM.NOMBRE AS TIPO_MOVIMIENTO, "
        + "NVL(M.CANTIDAD, 0) AS CANTIDAD "
        + "FROM ARTICULO A "
        + "LEFT JOIN ( "
        + "    SELECT "
        + "    D.ID_ARTICULO, "
        + "    C.ID_TIPO_MOVIMIENTO, "
        + "    SUM(D.CANTIDAD) AS CANTIDAD "
        + "    FROM DETALLE_INVENTARIO D "
        + "    INNER JOIN CABECERA_INVENTARIO C "
        + "    ON D.NUMERO_COMPROBANTE = C.NUMERO "
        + "    WHERE C.FECHA >= ? "
        + "    AND C.FECHA < ? "
        + "    GROUP BY D.ID_ARTICULO, C.ID_TIPO_MOVIMIENTO "
        + ") M "
        + "ON A.CODIGO = M.ID_ARTICULO "
        + "LEFT JOIN TIPO_MOVIMIENTO TM "
        + "ON M.ID_TIPO_MOVIMIENTO = TM.CODIGO "
        + "ORDER BY A.CODIGO, TM.CODIGO";

        Calendar calendario = Calendar.getInstance();
        calendario.setTime(hasta);

        // Incluir todo el día de la fecha Hasta
        calendario.add(Calendar.DAY_OF_MONTH, 1);

        Timestamp fechaInicio =
                new Timestamp(desde.getTime());

        Timestamp fechaFin =
                new Timestamp(calendario.getTimeInMillis());

        try (PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setTimestamp(1, fechaInicio);
            ps.setTimestamp(2, fechaFin);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    MovimientoMatriz movimiento =
                            new MovimientoMatriz();

                    movimiento.setCodigoArticulo(
                            rs.getInt("CODIGO_ARTICULO"));

                    movimiento.setArticulo(
                            rs.getString("ARTICULO"));

                    movimiento.setCodigoTipoMovimiento(
                            rs.getInt("CODIGO_TIPO"));

                    movimiento.setTipoMovimiento(
                            rs.getString("TIPO_MOVIMIENTO"));

                    movimiento.setCantidad(
                            rs.getInt("CANTIDAD"));

                    lista.add(movimiento);
                }
            }
        }

        return lista;
    }
}