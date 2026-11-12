/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.contabilidad;

/**
 *
 * @author USER
 */
import datos.contabilidad.DAOComprobante;
import entidades.contabilidad.CabeceraComprobante;
import entidades.contabilidad.DetalleComprobante;

public class NegocioComprobante {

    private DAOComprobante dao = new DAOComprobante();
    // CONCURRENCIA: un solo candado para todos los comprobantes
    // Así 2 clientes no pueden guardar el mismo numero al mismo tiempo
    private static final Object CANDADO = new Object();

    // synchronized + candado = nadie más entra mientras se guarda uno
    public String guardar(CabeceraComprobante cab) {
        synchronized (CANDADO) {
            // 1. Validar que tenga detalles
            if (cab.getDetalles() == null || cab.getDetalles().isEmpty()) {
                return "Error: El comprobante debe tener al menos un detalle";
            }

            // 2. Validar NO DESCUADRADO
            double totalDebe = 0;
            double totalHaber = 0;
            for (DetalleComprobante d : cab.getDetalles()) {
                totalDebe += d.getDebe();
                totalHaber += d.getHaber();
                // Cada detalle no puede tener debe y haber a la vez
                if (d.getDebe() > 0 && d.getHaber() > 0) {
                    return "Error: Un detalle no puede tener Debe y Haber a la vez";
                }
                if (d.getDebe() < 0 || d.getHaber() < 0) {
                    return "Error: No se permiten valores negativos";
                }
            }

            // Aquí está la regla del profe
            if (Math.abs(totalDebe - totalHaber) > 0.01) {
                return "Error: Asiento descuadrado. Debe=" + totalDebe + " Haber=" + totalHaber + ". No se guardó.";
            }

            // 3. Guardar en base
            boolean ok = dao.insertar(cab);
            if (ok) {
                return "OK: Comprobante " + cab.getNumero() + " guardado. Debe=" + totalDebe + " Haber=" + totalHaber;
            } else {
                return "Error: No se pudo guardar. Quizás el numero ya existe";
            }
        }
    }
}
