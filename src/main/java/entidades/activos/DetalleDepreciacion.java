    /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidades.activos;

import java.math.BigDecimal;

/**
 *
 * @author USUARIO
 */
public class DetalleDepreciacion {
    
    private int idDetalle;
    private BigDecimal valorDepreciacion;
    private CabeceraDepreciacion cabeceraDepreciacion;
    private Activo activo;
    
    public DetalleDepreciacion(){   
     }
    public DetalleDepreciacion (int idDetalle, BigDecimal valorDepreciacion,
            CabeceraDepreciacion cabeceraDepreciacion, Activo activo) {
        
        this.idDetalle = idDetalle;
        this.valorDepreciacion = valorDepreciacion;
        this.cabeceraDepreciacion = cabeceraDepreciacion;
        this.activo= activo;
    }
    
    public int getIdDetalle() {
        return idDetalle;
    }
    public void setIdDetalle(int idDetalle) {
        this.idDetalle = idDetalle;
    }
    public BigDecimal getValorDepreciacion() {
        return valorDepreciacion;
    }
    public void setValorDepreciacion(BigDecimal valorDepreciacion){
        this.valorDepreciacion = valorDepreciacion;
     }
     public CabeceraDepreciacion getCabeceraDepreciacion() {
        return cabeceraDepreciacion;
     }
    public void setCabeceraDepreciacion(CabeceraDepreciacion cabeceraDeprecion) {
        this.cabeceraDepreciacion = cabeceraDepreciacion;
    }
    
    public Activo getActivo() { 
        return activo;
      }
    public void setActivo(Activo activo){
        this.activo = activo;
    }
}


