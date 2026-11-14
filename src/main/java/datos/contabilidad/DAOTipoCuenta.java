package datos.contabilidad;

import conexion.ConexionOracle;
import entidades.contabilidad.TipoCuenta;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DAOTipoCuenta {

    // INSERTAR - lo usa NegocioTipoCuenta
    public boolean insertar(TipoCuenta t) {
        String sql = "INSERT INTO TIPO_CUENTA (CODIGO, NOMBRE) VALUES (?, ?)";
        try (Connection con = new ConexionOracle().conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, t.getCodigo());
            ps.setString(2, t.getNombre());
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("Error insertar TipoCuenta: " + e.getMessage());
            return false;
        }
    }

    // GUARDAR - lo usa FrmTipoCuenta - es lo mismo que insertar
    public boolean guardar(TipoCuenta t) {
        return insertar(t);
    }

    // LISTAR
    public List<TipoCuenta> listar() {
        List<TipoCuenta> lista = new ArrayList<>();
        String sql = "SELECT CODIGO, NOMBRE FROM TIPO_CUENTA ORDER BY CODIGO";
        try (Connection con = new ConexionOracle().conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new TipoCuenta(rs.getInt("CODIGO"), rs.getString("NOMBRE")));
            }
        } catch (Exception e) {
            System.out.println("Error listar TipoCuenta: " + e.getMessage());
        }
        return lista;
    }

    // MODIFICAR
    public boolean modificar(TipoCuenta t) {
        String sql = "UPDATE TIPO_CUENTA SET NOMBRE = ? WHERE CODIGO = ?";
        try (Connection con = new ConexionOracle().conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, t.getNombre());
            ps.setInt(2, t.getCodigo());
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("Error modificar TipoCuenta: " + e.getMessage());
            return false;
        }
    }

    // ACTUALIZAR - alias por si alguien lo llama así
    public boolean actualizar(TipoCuenta t) {
        return modificar(t);
    }

    // ELIMINAR
    public boolean eliminar(int codigo) {
        String sql = "DELETE FROM TIPO_CUENTA WHERE CODIGO = ?";
        try (Connection con = new ConexionOracle().conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, codigo);
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("Error eliminar TipoCuenta: " + e.getMessage());
            return false;
        }
    }

    // BUSCAR POR CODIGO
    public TipoCuenta buscarPorCodigo(int codigo) {
        String sql = "SELECT CODIGO, NOMBRE FROM TIPO_CUENTA WHERE CODIGO = ?";
        try (Connection con = new ConexionOracle().conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return new TipoCuenta(rs.getInt("CODIGO"), rs.getString("NOMBRE"));
            }
        } catch (Exception e) {
            System.out.println("Error buscar TipoCuenta: " + e.getMessage());
        }
        return null;
    }

    // BUSCAR POR NOMBRE - lo usa el botón Buscar del Form
    public List<TipoCuenta> buscarPorNombre(String nombre) {
        List<TipoCuenta> lista = new ArrayList<>();
        String sql = "SELECT CODIGO, NOMBRE FROM TIPO_CUENTA WHERE UPPER(NOMBRE) LIKE ? ORDER BY CODIGO";
        try (Connection con = new ConexionOracle().conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + nombre.toUpperCase() + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new TipoCuenta(rs.getInt("CODIGO"), rs.getString("NOMBRE")));
                }
            }
        } catch (Exception e) {
            System.out.println("Error buscar por nombre: " + e.getMessage());
        }
        return lista;
    }
}