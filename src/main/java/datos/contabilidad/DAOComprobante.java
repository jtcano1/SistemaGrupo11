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
import entidades.contabilidad.Cuenta;
import entidades.contabilidad.DetalleComprobante;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DAOComprobante {

    // INSERTAR CABECERA + DETALLES con transaccion y secuencia
    public boolean insertar(CabeceraComprobante cab) {
        String sqlCab = "INSERT INTO CABECERA_COMPROBANTE (NUMERO, FECHA, OBSERVACIONES) VALUES (SEQ_CABECERA_COMPROBANTE.NEXTVAL, ?, ?)";
        String sqlDet = "INSERT INTO DETALLE_COMPROBANTE (ID_DETALLE, NUMERO_COMPROBANTE, CODIGO_CUENTA, CANTIDAD_DEBE, CANTIDAD_HABER) VALUES (SEQ_DETALLE_COMPROBANTE.NEXTVAL, ?, ?, ?, ?)";
        Connection con = null;
        try {
            con = new ConexionOracle().conectar();
            con.setAutoCommit(false);

            // 1. Sacar el numero que va a usar con la secuencia
            int numeroGenerado = 0;
            try (PreparedStatement psSeq = con.prepareStatement("SELECT SEQ_CABECERA_COMPROBANTE.NEXTVAL FROM DUAL");
                 ResultSet rsSeq = psSeq.executeQuery()) {
                if (rsSeq.next()) numeroGenerado = rsSeq.getInt(1);
            }
            cab.setNumero(numeroGenerado);

            // 2. Insertar cabecera con ese numero
            try (PreparedStatement psCab = con.prepareStatement("INSERT INTO CABECERA_COMPROBANTE (NUMERO, FECHA, OBSERVACIONES) VALUES (?, ?, ?)")) {
                psCab.setInt(1, numeroGenerado);
                psCab.setDate(2, new java.sql.Date(cab.getFecha().getTime()));
                psCab.setString(3, cab.getObservaciones());
                psCab.executeUpdate();
            }

            // 3. Insertar detalles
            try (PreparedStatement psDet = con.prepareStatement(sqlDet)) {
                for (DetalleComprobante d : cab.getDetalles()) {
                    psDet.setInt(1, numeroGenerado);
                    psDet.setString(2, d.getCuenta().getCodigo());
                    psDet.setDouble(3, d.getDebe());
                    psDet.setDouble(4, d.getHaber());
                    psDet.executeUpdate();
                }
            }

            con.commit();
            return true;
        } catch (Exception e) {
            try { if (con != null) con.rollback(); } catch (Exception ex) {}
            System.out.println("Error insertar Comprobante: " + e.getMessage());
            return false;
        } finally {
            try { if (con != null) { con.setAutoCommit(true); con.close(); } } catch (Exception ex) {}
        }
    }

    // BUSCAR por numero - trae cabecera + detalles
    public CabeceraComprobante buscarPorNumero(int numero) {
        String sqlCab = "SELECT NUMERO, FECHA, OBSERVACIONES FROM CABECERA_COMPROBANTE WHERE NUMERO = ?";
        String sqlDet = "SELECT d.ID_DETALLE, d.CANTIDAD_DEBE, d.CANTIDAD_HABER, c.CODIGO, c.NOMBRE " +
                        "FROM DETALLE_COMPROBANTE d JOIN CUENTA c ON c.CODIGO = d.CODIGO_CUENTA " +
                        "WHERE d.NUMERO_COMPROBANTE = ? ORDER BY d.ID_DETALLE";
        try (Connection con = new ConexionOracle().conectar();
             PreparedStatement psCab = con.prepareStatement(sqlCab)) {
            psCab.setInt(1, numero);
            try (ResultSet rsCab = psCab.executeQuery()) {
                if (!rsCab.next()) return null;
                CabeceraComprobante cab = new CabeceraComprobante();
                cab.setNumero(rsCab.getInt("NUMERO"));
                cab.setFecha(rsCab.getDate("FECHA"));
                cab.setObservaciones(rsCab.getString("OBSERVACIONES"));
                cab.setDetalles(new ArrayList<>());

                try (PreparedStatement psDet = con.prepareStatement(sqlDet)) {
                    psDet.setInt(1, numero);
                    try (ResultSet rsDet = psDet.executeQuery()) {
                        while (rsDet.next()) {
                            DetalleComprobante d = new DetalleComprobante();
                            d.setIdDetalle(rsDet.getInt("ID_DETALLE"));
                            d.setNumeroComprobante(numero);
                            d.setDebe(rsDet.getDouble("CANTIDAD_DEBE"));
                            d.setHaber(rsDet.getDouble("CANTIDAD_HABER"));
                            Cuenta cu = new Cuenta();
                            cu.setCodigo(rsDet.getString("CODIGO"));
                            cu.setNombre(rsDet.getString("NOMBRE"));
                            d.setCuenta(cu);
                            cab.getDetalles().add(d);
                        }
                    }
                }
                return cab;
            }
        } catch (Exception e) {
            System.out.println("Error buscar Comprobante: " + e.getMessage());
            return null;
        }
    }

    // LISTAR todas las cabeceras
    public List<CabeceraComprobante> listar() {
        List<CabeceraComprobante> lista = new ArrayList<>();
        String sql = "SELECT NUMERO, FECHA, OBSERVACIONES FROM CABECERA_COMPROBANTE ORDER BY NUMERO DESC";
        try (Connection con = new ConexionOracle().conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                CabeceraComprobante cab = new CabeceraComprobante();
                cab.setNumero(rs.getInt("NUMERO"));
                cab.setFecha(rs.getDate("FECHA"));
                cab.setObservaciones(rs.getString("OBSERVACIONES"));
                cab.setDetalles(new ArrayList<>());
                lista.add(cab);
            }
        } catch (Exception e) {
            System.out.println("Error listar Comprobante: " + e.getMessage());
        }
        return lista;
    }

    // MODIFICAR cabecera + detalles (borra detalles viejos y mete nuevos)
    public boolean modificar(CabeceraComprobante cab) {
        Connection con = null;
        try {
            con = new ConexionOracle().conectar();
            con.setAutoCommit(false);
            try (PreparedStatement ps = con.prepareStatement("UPDATE CABECERA_COMPROBANTE SET FECHA = ?, OBSERVACIONES = ? WHERE NUMERO = ?")) {
                ps.setDate(1, new java.sql.Date(cab.getFecha().getTime()));
                ps.setString(2, cab.getObservaciones());
                ps.setInt(3, cab.getNumero());
                ps.executeUpdate();
            }
            try (PreparedStatement ps = con.prepareStatement("DELETE FROM DETALLE_COMPROBANTE WHERE NUMERO_COMPROBANTE = ?")) {
                ps.setInt(1, cab.getNumero());
                ps.executeUpdate();
            }
            try (PreparedStatement ps = con.prepareStatement("INSERT INTO DETALLE_COMPROBANTE (ID_DETALLE, NUMERO_COMPROBANTE, CODIGO_CUENTA, CANTIDAD_DEBE, CANTIDAD_HABER) VALUES (SEQ_DETALLE_COMPROBANTE.NEXTVAL, ?, ?, ?, ?)")) {
                for (DetalleComprobante d : cab.getDetalles()) {
                    ps.setInt(1, cab.getNumero());
                    ps.setString(2, d.getCuenta().getCodigo());
                    ps.setDouble(3, d.getDebe());
                    ps.setDouble(4, d.getHaber());
                    ps.executeUpdate();
                }
            }
            con.commit();
            return true;
        } catch (Exception e) {
            try { if (con != null) con.rollback(); } catch (Exception ex) {}
            System.out.println("Error modificar Comprobante: " + e.getMessage());
            return false;
        } finally {
            try { if (con != null) { con.setAutoCommit(true); con.close(); } } catch (Exception ex) {}
        }
    }

    // ELIMINAR cabecera + detalles
    public boolean eliminar(int numero) {
        Connection con = null;
        try {
            con = new ConexionOracle().conectar();
            con.setAutoCommit(false);
            try (PreparedStatement ps = con.prepareStatement("DELETE FROM DETALLE_COMPROBANTE WHERE NUMERO_COMPROBANTE = ?")) {
                ps.setInt(1, numero);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = con.prepareStatement("DELETE FROM CABECERA_COMPROBANTE WHERE NUMERO = ?")) {
                ps.setInt(1, numero);
                ps.executeUpdate();
            }
            con.commit();
            return true;
        } catch (Exception e) {
            try { if (con != null) con.rollback(); } catch (Exception ex) {}
            System.out.println("Error eliminar Comprobante: " + e.getMessage());
            return false;
        } finally {
            try { if (con != null) { con.setAutoCommit(true); con.close(); } } catch (Exception ex) {}
        }
    }
}
