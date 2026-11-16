/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.activos;

import datos.activos.TipoActivoDAO;
import entidades.activos.TipoActivo;
import java.util.List;

/**
 *
 * @author USUARIO
 */
public class TipoActivoBL {
    private final TipoActivoDAO tipoActivoDAO = new TipoActivoDAO();
    
    public String guardar(TipoActivo tipo) {
        if (tipo.getNombre() == null || tipo.getNombre().trim().isEmpty()) {
         return "El nombre del tipo de activo es obligatorio";
    }
        if (tipo.getCuentaGasto() == null || tipo.getCuentaGasto().trim().isEmpty()) {
         return "La cuenta de gasto es obligatorio";
    }
        if (tipo.getCuentaDepAcumulada() == null || tipo.getCuentaDepAcumulada().trim().isEmpty()) {
         return "La cuenta de depreciacion acumulada es obligatorio";
    }
        
        if (tipoActivoDAO.insertar(tipo)) {
            return "Tipo de activo guardado correctamente";
        }
        return "Error al gardar el tipo de activo en la BAse  de Datos";
    }
    
    public String actualizar (TipoActivo tipo) {
        if (tipo.getCodigo()<=0) {
            return "Codigo no valido";
        }
        if (tipo.getNombre() == null || tipo.getNombre().trim().isEmpty()) {
            return "EL nombre es obligatorio";
        }
        if (tipo.getCuentaGasto() == null || tipo.getCuentaGasto().trim().isEmpty()) {
        return "La cuenta es obligatoria";
    }
        if (tipo.getCuentaDepAcumulada() == null || tipo.getCuentaDepAcumulada().trim().isEmpty()) {
        return "La cuenta de depreciacion acumulada es obligatoria";
    }
        if (tipoActivoDAO.actualizar(tipo)){
            return"Tipo de activo actualizado";
        }
        return "Error al Actualizar";
    }
    public String eliminar (int codigo) {
        if (codigo <=0) {
            return "Sellecione un registro valido";
        }
        if (tipoActivoDAO.eliminar(codigo)) {
            return "Tipo de activo eliminado correctamente";
        }
        return "No se pudo eliminar";
    }
    public List <TipoActivo> listarTodos() {
        return tipoActivoDAO.listarTodos();
    }

}
