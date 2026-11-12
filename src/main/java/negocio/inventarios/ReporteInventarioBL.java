
package negocio.inventarios;

import conexion.ConexionOracle;
import datos.inventarios.ReporteInventarioDAO;
import entidades.inventarios.SaldoArticulo;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;


public class ReporteInventarioBL {

    private final ReporteInventarioDAO reporteDAO;

    public ReporteInventarioBL() {
        reporteDAO = new ReporteInventarioDAO();
    }

    public List<SaldoArticulo> listarSaldos(Date hasta) {

        try (Connection cn = new ConexionOracle().conectar()) {

            if (cn == null) {
                System.out.println(
                        "No se pudo conectar con Oracle.");

                return new ArrayList<>();
            }

            return reporteDAO.listarSaldos(cn, hasta);

        } catch (SQLException e) {

            System.out.println(
                    "Error al generar reporte de saldos: "
                    + e.getMessage());

            return new ArrayList<>();
        }
    }
}
