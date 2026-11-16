/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidades.activos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author USUARIO
 */
public class CabeceraDepreciacion {
   
    private int numeroDepreciacion;
    private LocalDate fecha;
    private String observaciones;
    private int numeroComprobante;
    
    private List<DetalleDepreciacion> detalles = new ArrayList<>();
    
    public CabeceraDepreciacion() {
        
    }
    public CabeceraDepreciacion(int numeroDepreciacion, LocalDate fecha, 
            String observaciones) {
        this.numeroDepreciacion = numeroDepreciacion;
        this.fecha = fecha;
        this.observaciones = observaciones;
    }
    public int getNumeroDepreciacion (){
        return numeroDepreciacion;
    }
    public void setNumeroDepreciacion (int numeroDepreciacion){
        this.numeroDepreciacion = numeroDepreciacion;
    }
    
    public LocalDate getFecha(){
        return fecha;
    }
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
    public String getObservaciones() {
        return observaciones;
    }
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
     public int getNumeroComprobante() {
        return numeroComprobante;
    }
    public void setNumeroComprobante(int numeroComprobante) {
        this.numeroComprobante = numeroComprobante;
    }
}
