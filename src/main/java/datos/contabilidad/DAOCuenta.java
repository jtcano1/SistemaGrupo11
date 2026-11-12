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
import java.util.ArrayList;
import java.util.List;

public class DAOCuenta {

    public boolean insertar(Cuenta c) {
        String sql = "INSERT INTO CUENTA (CODIGO, NOMBRE, CODIGO_TIPO_CUENTA) VALUES (?, ?, ?)";
        try (Connection con = new ConexionOracle().conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getCodigo());
            ps.setString(2, c.getNombre());
            ps.setInt(3, c.getTipoCuenta().getCodigo());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error insertar Cuenta: " + e.getMessage());
            return false;
        }
    }

    public List<Cuenta> listar() {
        List<Cuenta> lista = new ArrayList<>();
        String sql = "SELECT c.CODIGO, c.NOMBRE, t.CODIGO, t.NOMBRE FROM CUENTA c JOIN TIPO_CUENTA t ON c.CODIGO_TIPO_CUENTA = t.CODIGO ORDER BY c.CODIGO";
        try (Connection con = new ConexionOracle().conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                TipoCuenta t = new TipoCuenta(rs.getInt(3), rs.getString(4));
                lista.add(new Cuenta(rs.getString(1), rs.getString(2), t));
            }
        } catch (Exception e) {
            System.out.println("Error listar Cuenta: " + e.getMessage());
        }
        return lista;
    }
}
