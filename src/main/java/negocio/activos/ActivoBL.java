/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.activos;

import datos.activos.ActivoDAO;
import entidades.activos.Activo;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.Map;
/**
 *
 * @author USUARIO
 */
public class ActivoBL {
     private final ActivoDAO activoDAO = new ActivoDAO();

    public String guardar(Activo activo) {
        if (activo.getNombre() == null || activo.getNombre().trim().isEmpty()) {
            return "El nombre del activo es obligatorio.";
        }
        if (activo.getValorCompra() == null || activo.getValorCompra().compareTo(BigDecimal.ZERO) <= 0) {
            return "El valor de compra debe ser mayor a cero.";
        }
        if (activo.getPeriodosDepreciacion() <= 0) {
            return "La vida útil (períodos) debe ser mayor a cero.";
        }
        if (activo.getTipoActivo() == null || activo.getTipoActivo().getCodigo() <= 0) {
            return "Debe seleccionar un tipo de activo válido.";
        }

        if (activoDAO.insertar(activo)) {
            return "Activo registrado correctamente.";
        }
        return "Error al registrar el activo.";
    }

    public String actualizar(Activo activo) {
        if (activo.getId() <= 0) {
            return "Seleccione un activo válido para actualizar.";
        }
        if (activo.getNombre() == null || activo.getNombre().trim().isEmpty()) {
            return "El nombre del activo es obligatorio.";
        }
        if (activo.getValorCompra() == null || activo.getValorCompra().compareTo(BigDecimal.ZERO) <= 0) {
            return "El valor de compra debe ser mayor a cero.";
        }
        if (activo.getPeriodosDepreciacion() <= 0) {
            return "La vida útil (períodos) debe ser mayor a cero.";
        }

        if (activoDAO.actualizar(activo)) {
            return "Activo actualizado correctamente.";
        }
        return "Error al actualizar el activo.";
    }

    public String eliminar(int idActivo) {
        if (idActivo <= 0) {
            return "Seleccione un activo válido para eliminar.";
        }
        if (activoDAO.eliminar(idActivo)) {
            return "Activo eliminado correctamente.";
        }
        return "No se pudo eliminar (verifique que no tenga depreciaciones registradas).";
    }

    public List<Activo> listarTodos() {
        return activoDAO.listarTodos();
    }

    public Map<String, Integer> obtenerReportePorTipo() {
        return activoDAO.contarActivosPorTipo();
    }

    public List<Object[]> obtenerReporteDepreciadosPorFecha(Date inicio, Date fin) {
        return activoDAO.obtenerValoresDepreciadosPorRango(inicio, fin);
    }
}
