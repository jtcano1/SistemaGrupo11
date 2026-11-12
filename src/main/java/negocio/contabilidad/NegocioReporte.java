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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NegocioReporte {

    private DAOReporte dao = new DAOReporte();

    // ---------- BALANCE GENERAL CON FECHAS (el que pide el ingeniero) ----------
    public List<String[]> reporteBalanceGeneral(Date desde, Date hasta) {
        return dao.reporteBalanceGeneral(desde, hasta);
    }

    // ---------- ESTADO DE RESULTADOS CON FECHAS ----------
    public List<String[]> reporteEstadoResultados(Date desde, Date hasta) {
        return dao.reporteEstadoResultados(desde, hasta);
    }

    // ---------- UTILIDAD = Ingresos - Egresos ----------
    public double calcularUtilidad(Date desde, Date hasta) {
        List<String[]> lista = dao.reporteEstadoResultados(desde, hasta);
        double ingresos = 0;
        double egresos = 0;
        for (String[] fila : lista) {
            String codigo = fila[0];
            double saldo = Double.parseDouble(fila[5]);
            if (codigo.startsWith("4")) {
                ingresos += saldo;
            } else if (codigo.startsWith("5")) {
                // En Estado de Resultados el saldo de egresos viene negativo o positivo
                // segun tu formula, lo tomamos en valor absoluto para restar
                egresos += Math.abs(saldo);
            }
        }
        return ingresos - egresos;
    }

    // ---------- ESTOS 3 SON PARA QUE NO TE SALGA ROJO SI LA PANTALLA VIEJA LO LLAMA SIN FECHAS ----------
    // Si no pones fecha, usa desde 01/01/2000 hasta hoy
    public List<String[]> reporteBalanceGeneral() {
        return dao.reporteBalanceGeneral(fechaInicioDefault(), new Date());
    }

    public List<String[]> reporteEstadoResultados() {
        return dao.reporteEstadoResultados(fechaInicioDefault(), new Date());
    }

    public double calcularUtilidad() {
        return calcularUtilidad(fechaInicioDefault(), new Date());
    }

    private Date fechaInicioDefault() {
        Calendar cal = Calendar.getInstance();
        cal.set(2000, Calendar.JANUARY, 1, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
}
