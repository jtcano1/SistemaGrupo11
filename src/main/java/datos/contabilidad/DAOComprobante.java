/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package datos.contabilidad;

/**
 *
 * @author USER
 */
import conexion.ConexionOracle;
import entidades.contabilidad.CabeceraComprobante;
import entidades.contabilidad.DetalleComprobante;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class DAOComprobante {

    public boolean insertar(CabeceraComprobante cab) {
        String sqlCab = "INSERT INTO CABECERA_COMPROBANTE (NUMERO, FECHA, OBSERVACIONES) VALUES (?, ?, ?)";
        String sqlDet = "INSERT INTO DETALLE_COMPROBANTE (ID_DETALLE, NUMERO_COMPROBANTE, CODIGO_CUENTA, CANTIDAD_DEBE, CANTIDAD_HABER) VALUES ((SELECT NVL(MAX(ID_DETALLE),0)+1 FROM DETALLE_COMPROBANTE), ?, ?, ?, ?)";

        try (Connection con = new ConexionOracle().conectar()) {
            con.setAutoCommit(false);

            try (PreparedStatement psCab = con.prepareStatement(sqlCab)) {
                psCab.setInt(1, cab.getNumero());
                psCab.setDate(2, new java.sql.Date(cab.getFecha().getTime()));
                psCab.setString(3, cab.getObservaciones());
                psCab.executeUpdate();
            }

            try (PreparedStatement psDet = con.prepareStatement(sqlDet)) {
                for (DetalleComprobante d : cab.getDetalles()) {
                    psDet.setInt(1, cab.getNumero());
                    psDet.setString(2, d.getCuenta().getCodigo());
                    psDet.setDouble(3, d.getDebe());
                    psDet.setDouble(4, d.getHaber());
                    psDet.executeUpdate();
                }
            }

            con.commit();
            return true;
        } catch (Exception e) {
            System.out.println("Error insertar Comprobante: " + e.getMessage());
            return false;
        }
    }
}
