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
import entidades.contabilidad.TipoCuenta;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DAOTipoCuenta {

    public boolean insertar(TipoCuenta t) {
        String sql = "INSERT INTO TIPO_CUENTA (CODIGO, NOMBRE) VALUES (?, ?)";
        try (Connection con = new ConexionOracle().conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, t.getCodigo());
            ps.setString(2, t.getNombre());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error insertar TipoCuenta: " + e.getMessage());
            return false;
        }
    }

    public List<TipoCuenta> listar() {
        List<TipoCuenta> lista = new ArrayList<>();
        String sql = "SELECT CODIGO, NOMBRE FROM TIPO_CUENTA ORDER BY CODIGO";
        try (Connection con = new ConexionOracle().conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new TipoCuenta(rs.getInt(1), rs.getString(2)));
            }
        } catch (Exception e) {
            System.out.println("Error listar TipoCuenta: " + e.getMessage());
        }
        return lista;
    }
}
