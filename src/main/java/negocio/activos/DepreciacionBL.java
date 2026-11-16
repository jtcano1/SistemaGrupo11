/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.activos;

import datos.activos.ActivoDAO;
import datos.activos.DepreciacionDAO;
import entidades.activos.Activo;
import entidades.activos.CabeceraDepreciacion;
import entidades.activos.DetalleDepreciacion;
import entidades.contabilidad.CabeceraComprobante;
import entidades.contabilidad.Cuenta;
import entidades.contabilidad.DetalleComprobante;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import negocio.contabilidad.NegocioComprobante;
/**
 *
 * @author USUARIO
 */
public class DepreciacionBL {
   private final ActivoDAO activoDAO = new ActivoDAO();
    private final DepreciacionDAO depreciacionDAO = new DepreciacionDAO();
    private final NegocioComprobante negocioContabilidad = new NegocioComprobante();

    public String depreciarTodosActivosDelMes(String observaciones) {
        List<Activo> todosLosActivos = activoDAO.listarTodos();
        List<DetalleDepreciacion> detallesAProcesar = new ArrayList<>();
        BigDecimal montoTotalMes = BigDecimal.ZERO;

        CabeceraDepreciacion cabecera = new CabeceraDepreciacion();
        cabecera.setFecha(LocalDate.now());
        cabecera.setObservaciones(observaciones != null && !observaciones.trim().isEmpty() 
                ? observaciones : "Depreciacion mensual de activos");

        // Mapas para sumar cuánto se deprecia por cada cuenta contable
        Map<String, Double> totalesDebe = new HashMap<>();
        Map<String, Double> totalesHaber = new HashMap<>();

        for (Activo activo : todosLosActivos) {
            int periodosYaDepreciados = depreciacionDAO.obtenerPeriodosDepreciados(activo.getCodigoActivo());

            // Solo deprecia si aún no cumple su vida útil
            if (periodosYaDepreciados < activo.getPeriodosDepreciacion()) {
                BigDecimal valorCuota = activo.getValorCompra()
                        .divide(new BigDecimal(activo.getPeriodosDepreciacion()), 2, RoundingMode.HALF_UP);

                DetalleDepreciacion detalle = new DetalleDepreciacion();
                detalle.setCabeceraDepreciacion(cabecera);
                detalle.setActivo(activo);
                detalle.setValorDepreciacion(valorCuota);

                detallesAProcesar.add(detalle);
                montoTotalMes = montoTotalMes.add(valorCuota);

                // Acumular valores para el asiento contable
                if (activo.getTipoActivo() == null || activo.getTipoActivo().getCuentaGasto() == null 
                        || activo.getTipoActivo().getCuentaDepAcumulada() == null) {
                    return "Error: El activo '" + activo.getNombre() + "' no tiene cuentas contables configuradas en su Tipo de Activo.";
                }

                String ctaGasto = activo.getTipoActivo().getCuentaGasto();
                String ctaAcum = activo.getTipoActivo().getCuentaDepAcumulada();
                double valorDouble = valorCuota.doubleValue();

                totalesDebe.put(ctaGasto, totalesDebe.getOrDefault(ctaGasto, 0.0) + valorDouble);
                totalesHaber.put(ctaAcum, totalesHaber.getOrDefault(ctaAcum, 0.0) + valorDouble);
            }
        }

        if (detallesAProcesar.isEmpty()) {
            return "Todos los activos ya cumplieron su vida útil o no hay activos registrados.";
        }

        // 1. Construir el Comprobante Contable
        CabeceraComprobante cabComp = new CabeceraComprobante();
        cabComp.setFecha(new Date());
        cabComp.setObservaciones("Depreciacion Activos - " + cabecera.getObservaciones());
        
        
        for (Map.Entry<String, Double> entry : totalesDebe.entrySet()) {
            Cuenta c = new Cuenta();
            c.setCodigo(entry.getKey());
            cabComp.getDetalles().add(new DetalleComprobante(c, entry.getValue(), 0.0));
        }

        for (Map.Entry<String, Double> entry : totalesHaber.entrySet()) {
            Cuenta c = new Cuenta();
            c.setCodigo(entry.getKey());
            cabComp.getDetalles().add(new DetalleComprobante(c, 0.0, entry.getValue()));
        }

        // 2. Enviar a Contabilidad para que valide y guarde el asiento
        String respuestaContabilidad = negocioContabilidad.guardar(cabComp);
        if (!respuestaContabilidad.startsWith("OK")) {
            return "No se pudo contabilizar la depreciación: " + respuestaContabilidad;
        }

        // 3. Enlazar el número de comprobante y guardar la depreciación en Activos
        cabecera.setNumeroComprobante(cabComp.getNumero());
        boolean guardado = depreciacionDAO.registrarDepreciacionCompleta(cabecera, detallesAProcesar);

        if (guardado) {
            return "Depreciación procesada y contabilizada por $" + montoTotalMes + "\n" + respuestaContabilidad;
        } else {
            return "Error al guardar la depreciación en la base de datos de Activos.";
        }
    }
    
}