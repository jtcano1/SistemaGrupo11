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
import entidades.contabilidad.Cuenta;
import entidades.contabilidad.TipoCuenta;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class DAOCuenta {

    public boolean insertar(Cuenta c) {
        String sql = "INSERT INTO CUENTA (CODIGO, NOMBRE, CODIGO_TIPO_CUENTA) VALUES (?, ?, ?)";
        try (Connection con = new ConexionOracle().conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getCodigo());
            ps.setString(2, c.getNombre());
            if (c.getTipoCuenta() != null) {
                ps.setInt(3, c.getTipoCuenta().getCodigo());
            } else {
                ps.setNull(3, Types.INTEGER);
            }
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("Error insertar Cuenta: " + e.getMessage());
            return false;
        }
    }

    public List<Cuenta> listar() {
        List<Cuenta> lista = new ArrayList<>();
        String sql = "SELECT c.CODIGO, c.NOMBRE, c.CODIGO_TIPO_CUENTA, t.NOMBRE AS NOMBRE_TIPO " +
                     "FROM CUENTA c LEFT JOIN TIPO_CUENTA t ON c.CODIGO_TIPO_CUENTA = t.CODIGO " +
                     "ORDER BY c.CODIGO";
        try (Connection con = new ConexionOracle().conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (Exception e) {
            System.out.println("Error listar Cuenta: " + e.getMessage());
        }
        return lista;
    }

    public Cuenta buscarPorCodigo(String codigo) {
        String sql = "SELECT c.CODIGO, c.NOMBRE, c.CODIGO_TIPO_CUENTA, t.NOMBRE AS NOMBRE_TIPO " +
                     "FROM CUENTA c LEFT JOIN TIPO_CUENTA t ON c.CODIGO_TIPO_CUENTA = t.CODIGO " +
                     "WHERE c.CODIGO = ?";
        try (Connection con = new ConexionOracle().conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        } catch (Exception e) {
            System.out.println("Error buscar Cuenta: " + e.getMessage());
        }
        return null;
    }

    public List<Cuenta> listarPorTipo(int codigoTipo) {
        List<Cuenta> lista = new ArrayList<>();
        String sql = "SELECT c.CODIGO, c.NOMBRE, c.CODIGO_TIPO_CUENTA, t.NOMBRE AS NOMBRE_TIPO " +
                     "FROM CUENTA c LEFT JOIN TIPO_CUENTA t ON c.CODIGO_TIPO_CUENTA = t.CODIGO " +
                     "WHERE c.CODIGO_TIPO_CUENTA = ? ORDER BY c.CODIGO";
        try (Connection con = new ConexionOracle().conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, codigoTipo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        } catch (Exception e) {
            System.out.println("Error listar por tipo: " + e.getMessage());
        }
        return lista;
    }

    public boolean modificar(Cuenta c) {
        String sql = "UPDATE CUENTA SET NOMBRE = ?, CODIGO_TIPO_CUENTA = ? WHERE CODIGO = ?";
        try (Connection con = new ConexionOracle().conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getNombre());
            if (c.getTipoCuenta() != null) ps.setInt(2, c.getTipoCuenta().getCodigo());
            else ps.setNull(2, Types.INTEGER);
            ps.setString(3, c.getCodigo());
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("Error modificar Cuenta: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(String codigo) {
        String sql = "DELETE FROM CUENTA WHERE CODIGO = ?";
        try (Connection con = new ConexionOracle().conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, codigo);
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("Error eliminar Cuenta: " + e.getMessage());
            return false;
        }
    }

    private Cuenta mapear(ResultSet rs) throws Exception {
        Cuenta c = new Cuenta();
        c.setCodigo(rs.getString("CODIGO"));
        c.setNombre(rs.getString("NOMBRE"));
        int codigoTipo = rs.getInt("CODIGO_TIPO_CUENTA");
        if (!rs.wasNull()) {
            TipoCuenta t = new TipoCuenta();
            t.setCodigo(codigoTipo);
            t.setNombre(rs.getString("NOMBRE_TIPO"));
            c.setTipoCuenta(t);
        }
        return c;
    }
}