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
import java.util.List;

public class NegocioComprobante {

    private DAOComprobante dao = new DAOComprobante();
    // CONCURRENCIA: un solo candado para todos los comprobantes
    // Así 2 clientes no pueden guardar el mismo numero al mismo tiempo
    private static final Object CANDADO = new Object();

    // GUARDAR - devuelve String para que la pantalla muestre el mensaje 
    public String guardar(CabeceraComprobante cab) {
        synchronized (CANDADO) {
            String error = validar(cab);
            if (error != null) return error;

            boolean ok = dao.insertar(cab);
            if (ok) {
                return "OK: Comprobante " + cab.getNumero() + " guardado.";
            } else {
                return "Error: No se pudo guardar. Quizas el numero ya existe";
            }
        }
    }

    // MODIFICAR - misma validacion que guardar
    public String modificar(CabeceraComprobante cab) {
        synchronized (CANDADO) {
            String error = validar(cab);
            if (error != null) return error;

            boolean ok = dao.modificar(cab);
            if (ok) {
                return "OK: Comprobante " + cab.getNumero() + " modificado.";
            } else {
                return "Error: No se pudo modificar";
            }
        }
    }

    // BUSCAR - para el boton Buscar de la pantalla
    public CabeceraComprobante buscarPorNumero(int numero) {
        return dao.buscarPorNumero(numero);
    }

    // LISTAR - para llenar la tabla de la pantalla
    public List<CabeceraComprobante> listar() {
        return dao.listar();
    }

    // ELIMINAR - para el boton Eliminar
    public boolean eliminar(int numero) {
        synchronized (CANDADO) {
            return dao.eliminar(numero);
        }
    }

    // VALIDACION CENTRAL - la usa guardar y modificar, asi no se repite codigo
    private String validar(CabeceraComprobante cab) {
        if (cab == null) return "Error: Comprobante vacio";
        if (cab.getDetalles() == null || cab.getDetalles().isEmpty()) {
            return "Error: El comprobante debe tener al menos un detalle";
        }
        if (cab.getFecha() == null) return "Error: La fecha es obligatoria";

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
            if (d.getCuenta() == null || d.getCuenta().getCodigo() == null || d.getCuenta().getCodigo().isEmpty()) {
                return "Error: Hay un detalle sin cuenta";
            }
        }

        // Regla del ingeniero: Debe = Haber
        if (Math.abs(totalDebe - totalHaber) > 0.01) {
            return "Error: Asiento descuadrado. Debe=" + totalDebe + " Haber=" + totalHaber + ". No se guardo.";
        }
        return null; // null = todo bien
    }
}