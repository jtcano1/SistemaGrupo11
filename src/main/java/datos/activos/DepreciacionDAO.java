/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package datos.activos;
import conexion.ConexionOracle;
import entidades.activos.CabeceraDepreciacion;
import entidades.activos.DetalleDepreciacion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
/**
 *
 * @author USUARIO
 */
public class DepreciacionDAO {
     private final ConexionOracle conexionOracle = new ConexionOracle();

    public int obtenerPeriodosDepreciados(int codigoActivo) {
        String sql = "SELECT COUNT(*) FROM DETALLE_DEPRECIACION WHERE CODIGO_ACTIVO = ?";
        try (Connection conn = conexionOracle.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, codigoActivo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar períodos depreciados: " + e.getMessage());
        }
        return 0;
    }

    public boolean registrarDepreciacionCompleta(CabeceraDepreciacion cabecera, List<DetalleDepreciacion> detalles) {
        String sqlCabecera = "INSERT INTO CABECERA_DEPRECIACION (FECHA, OBSERVACIONES, NUMERO_COMPROBANTE) VALUES (?, ?, ?)";
        String sqlDetalle = "INSERT INTO DETALLE_DEPRECIACION (NUMERO_DEPRECIACION, VALOR_DEPRECIACION, CODIGO_ACTIVO) VALUES (?, ?, ?)";

        Connection conn = null;
        try {
            conn = conexionOracle.conectar();
            conn.setAutoCommit(false);
            
            int numeroGenerado = 0;

            try (PreparedStatement psCab = conn.prepareStatement(sqlCabecera, new String[]{"NUMERO_DEPRECIACION"})) {
                psCab.setDate(1, java.sql.Date.valueOf(cabecera.getFecha()));
                psCab.setString(2, cabecera.getObservaciones());
                psCab.setInt(3, cabecera.getNumeroComprobante());
                psCab.executeUpdate();

                try (ResultSet rsKeys = psCab.getGeneratedKeys()) {
                    if (rsKeys.next()) {
                        numeroGenerado = rsKeys.getInt(1);
                    }
                }
            }

            try (PreparedStatement psDet = conn.prepareStatement(sqlDetalle)) {
                for (DetalleDepreciacion det : detalles) {
                    psDet.setInt(1, numeroGenerado);
                    psDet.setBigDecimal(2, det.getValorDepreciacion());
                    psDet.setInt(3, det.getActivo().getId());
                    psDet.addBatch();
                }
                psDet.executeBatch();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            System.out.println("Error en transacción de depreciación: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.out.println("Error en rollback: " + ex.getMessage());
                }
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    System.out.println("Error al cerrar conexión: " + e.getMessage());
                }
            }
        }
    }
}
