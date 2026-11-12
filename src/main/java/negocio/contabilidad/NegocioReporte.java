/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.contabilidad;

/**
 *
 * @author USER
 */
import datos.contabilidad.DAOReporte;
import java.util.List;

public class NegocioReporte {

    private DAOReporte dao = new DAOReporte();

    // synchronized para que el reporte no se mezcle si 2 piden a la vez
    public synchronized List<String[]> balanceGeneral() {
        return dao.reporteBalanceGeneral();
    }

    public synchronized List<String[]> estadoResultados() {
        return dao.reporteEstadoResultados();
    }

    // UTILIDAD = Total Ingresos - Total Egresos
    public synchronized double calcularUtilidad() {
        List<String[]> datos = dao.reporteEstadoResultados();
        double ingresos = 0;
        double egresos = 0;
        for (String[] fila : datos) {
            String codigo = fila[0]; // ej: 4.1.1 o 5.1.1
            double saldo = Double.parseDouble(fila[5]);
            if (codigo.startsWith("4")) {
                ingresos += saldo;
            } else if (codigo.startsWith("5")) {
                egresos += saldo;
            }
        }
        return ingresos - egresos;
    }
}
