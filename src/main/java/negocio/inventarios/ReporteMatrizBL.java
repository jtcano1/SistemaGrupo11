package negocio.inventarios;

import conexion.ConexionOracle;
import datos.inventarios.ReporteMatrizDAO;
import entidades.inventarios.ColumnaMovimiento;
import entidades.inventarios.MovimientoMatriz;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReporteMatrizBL {

    private final ReporteMatrizDAO reporteDAO;

    public ReporteMatrizBL() {
        reporteDAO = new ReporteMatrizDAO();
    }

    // LISTAR TIPOS DE MOVIMIENTO
    public List<ColumnaMovimiento> listarTipos() {

        try (Connection cn = new ConexionOracle().conectar()) {

            if (cn == null) {
                System.out.println(
                        "No se pudo conectar con Oracle.");

                return new ArrayList<>();
            }

            return reporteDAO.listarTipos(cn);

        } catch (SQLException e) {

            System.out.println(
                    "Error al consultar tipos de movimiento: "
                    + e.getMessage());

            return new ArrayList<>();
        }
    }

    // LISTAR MOVIMIENTOS POR FECHAS
    public List<MovimientoMatriz> listarMovimientos(
            Date desde,
            Date hasta) {

        if (desde == null
                || hasta == null
                || desde.after(hasta)) {

            return new ArrayList<>();
        }

        try (Connection cn = new ConexionOracle().conectar()) {

            if (cn == null) {
                System.out.println(
                        "No se pudo conectar con Oracle.");

                return new ArrayList<>();
            }

            return reporteDAO.listarMovimientos(
                    cn, desde, hasta);

        } catch (SQLException e) {

            System.out.println(
                    "Error al consultar movimientos: "
                    + e.getMessage());

            return new ArrayList<>();
        }
    }
}