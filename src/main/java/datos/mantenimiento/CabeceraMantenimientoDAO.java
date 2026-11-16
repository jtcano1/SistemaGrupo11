package datos.mantenimiento;

import conexion.ConexionOracle;
import entidades.mantenimiento.Actividad;
import entidades.mantenimiento.Activo;
import entidades.mantenimiento.CabeceraMantenimiento;
import entidades.mantenimiento.DetalleMantenimiento;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CabeceraMantenimientoDAO {

    public boolean guardar(CabeceraMantenimiento cab) {
        Connection con = null;
        try {
            con = new ConexionOracle().conectar();
            con.setAutoCommit(false);

            int numero;
            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT SEQ_CABECERA_MANTENIMIENTO.NEXTVAL FROM DUAL");
                 ResultSet rs = ps.executeQuery()) {
                rs.next();
                numero = rs.getInt(1);
            }

            try (PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO CABECERA_MANTENIMIENTO (NUMERO, FECHA, RESPONSABLE) "
                    + "VALUES (?, ?, ?)")) {
                ps.setInt(1, numero);
                ps.setDate(2, new java.sql.Date(cab.getFecha().getTime()));
                ps.setString(3, cab.getResponsable());
                ps.executeUpdate();
            }

            insertarDetalles(con, numero, cab.getDetalles());

            con.commit();
            cab.setNumero(numero);
            return true;
        } catch (Exception e) {
            rollback(con);
            System.out.println("Error al guardar mantenimiento: " + e.getMessage());
            return false;
        } finally {
            cerrar(con);
        }
    }

    public boolean modificar(CabeceraMantenimiento cab) {
        Connection con = null;
        try {
            con = new ConexionOracle().conectar();
            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement(
                    "UPDATE CABECERA_MANTENIMIENTO SET FECHA = ?, RESPONSABLE = ? "
                    + "WHERE NUMERO = ?")) {
                ps.setDate(1, new java.sql.Date(cab.getFecha().getTime()));
                ps.setString(2, cab.getResponsable());
                ps.setInt(3, cab.getNumero());
                if (ps.executeUpdate() == 0) {
                    throw new SQLException("El mantenimiento " + cab.getNumero() + " no existe");
                }
            }

            try (PreparedStatement ps = con.prepareStatement(
                    "DELETE FROM DETALLE_MANTENIMIENTO WHERE NUMERO_MANTENIMIENTO = ?")) {
                ps.setInt(1, cab.getNumero());
                ps.executeUpdate();
            }

            insertarDetalles(con, cab.getNumero(), cab.getDetalles());

            con.commit();
            return true;
        } catch (Exception e) {
            rollback(con);
            System.out.println("Error al modificar mantenimiento: " + e.getMessage());
            return false;
        } finally {
            cerrar(con);
        }
    }

    public boolean eliminar(int numero) {
        Connection con = null;
        try {
            con = new ConexionOracle().conectar();
            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement(
                    "DELETE FROM DETALLE_MANTENIMIENTO WHERE NUMERO_MANTENIMIENTO = ?")) {
                ps.setInt(1, numero);
                ps.executeUpdate();
            }

            int filas;
            try (PreparedStatement ps = con.prepareStatement(
                    "DELETE FROM CABECERA_MANTENIMIENTO WHERE NUMERO = ?")) {
                ps.setInt(1, numero);
                filas = ps.executeUpdate();
            }

            con.commit();
            return filas > 0;
        } catch (Exception e) {
            rollback(con);
            System.out.println("Error al eliminar mantenimiento: " + e.getMessage());
            return false;
        } finally {
            cerrar(con);
        }
    }

    public CabeceraMantenimiento buscar(int numero) {
        String sql = "SELECT NUMERO, FECHA, RESPONSABLE FROM CABECERA_MANTENIMIENTO "
                + "WHERE NUMERO = ?";
        try (Connection con = new ConexionOracle().conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, numero);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return leerCabecera(rs);
                }
            }
        } catch (Exception e) {
            System.out.println("Error al buscar mantenimiento: " + e.getMessage());
        }
        return null;
    }

    public List<CabeceraMantenimiento> listar(String filtroResponsable) {
        List<CabeceraMantenimiento> lista = new ArrayList<>();
        String sql = "SELECT NUMERO, FECHA, RESPONSABLE FROM CABECERA_MANTENIMIENTO "
                + "WHERE UPPER(RESPONSABLE) LIKE UPPER(?) ORDER BY NUMERO DESC";
        try (Connection con = new ConexionOracle().conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + (filtroResponsable == null ? "" : filtroResponsable.trim()) + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(leerCabecera(rs));
                }
            }
        } catch (Exception e) {
            System.out.println("Error al listar mantenimientos: " + e.getMessage());
        }
        return lista;
    }

    public List<DetalleMantenimiento> listarDetalles(int numeroMantenimiento) {
        List<DetalleMantenimiento> lista = new ArrayList<>();
        String sql = "SELECT d.VALOR, a.CODIGO, a.NOMBRE, a.FECHA_COMPRA, "
                + "t.CODIGO, t.NOMBRE "
                + "FROM DETALLE_MANTENIMIENTO d "
                + "JOIN ACTIVOS_M a ON a.CODIGO = d.CODIGO_ACTIVO "
                + "JOIN ACTIVIDAD t ON t.CODIGO = d.CODIGO_ACTIVIDAD "
                + "WHERE d.NUMERO_MANTENIMIENTO = ? ORDER BY a.NOMBRE, t.NOMBRE";
        try (Connection con = new ConexionOracle().conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, numeroMantenimiento);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DetalleMantenimiento d = new DetalleMantenimiento();
                    d.setNumeroMantenimiento(numeroMantenimiento);
                    d.setValor(rs.getDouble(1));
                    d.setActivo(new Activo(rs.getInt(2), rs.getString(3), rs.getDate(4)));
                    d.setActividad(new Actividad(rs.getInt(5), rs.getString(6)));
                    lista.add(d);
                }
            }
        } catch (Exception e) {
            System.out.println("Error al listar detalles: " + e.getMessage());
        }
        return lista;
    }

    private void insertarDetalles(Connection con, int numeroMantenimiento,
            List<DetalleMantenimiento> detalles) throws SQLException {
        String sql = "INSERT INTO DETALLE_MANTENIMIENTO "
                + "(NUMERO_MANTENIMIENTO, CODIGO_ACTIVO, CODIGO_ACTIVIDAD, VALOR) "
                + "VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            for (DetalleMantenimiento d : detalles) {
                d.setNumeroMantenimiento(numeroMantenimiento);
                ps.setInt(1, numeroMantenimiento);
                ps.setInt(2, d.getActivo().getCodigo());
                ps.setInt(3, d.getActividad().getCodigo());
                ps.setDouble(4, d.getValor());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private CabeceraMantenimiento leerCabecera(ResultSet rs) throws SQLException {
        CabeceraMantenimiento c = new CabeceraMantenimiento();
        c.setNumero(rs.getInt("NUMERO"));
        c.setFecha(rs.getDate("FECHA"));
        c.setResponsable(rs.getString("RESPONSABLE"));
        return c;
    }

    private void rollback(Connection con) {
        try {
            if (con != null) {
                con.rollback();
            }
        } catch (SQLException e) {
            System.out.println("Error al revertir: " + e.getMessage());
        }
    }

    private void cerrar(Connection con) {
        try {
            if (con != null) {
                con.setAutoCommit(true);
                con.close();
            }
        } catch (SQLException e) {
            System.out.println("Error al cerrar conexion: " + e.getMessage());
        }
    }
}
