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
public class Activo {
    
    private int id;
    private String nombre;
    private int periodosDepreciacion;
    private BigDecimal valorCompra;
    private TipoActivo tipoActivo;
    
    public Activo () {
        
    }
    
    public Activo (int id, String nombre, int periodosDepreciacion,
            BigDecimal valorCompra, TipoActivo tipoActivo) {
        
        this.id = id;
        this.nombre = nombre;
        this.periodosDepreciacion = periodosDepreciacion;
        this.valorCompra = valorCompra;
        this.tipoActivo = tipoActivo;
    }
    
    public int getId() {
        return id;
    }
    public void setId (int id) {
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre){
        this.nombre = nombre;
     }
     public int getPeriodosDepreciacion() {
        return periodosDepreciacion;
     }
    public void setPeriodosDepreciacion(int periodosDepreciacion) {
        this.periodosDepreciacion = periodosDepreciacion;
    }
    public BigDecimal getValorCompra() {
        return valorCompra;
    }
    public void setValorCompra(BigDecimal valorCompra) {
        this.valorCompra = valorCompra;
    }
    public TipoActivo getTipoActivo() { 
        return tipoActivo;
      }
    public void setTipoActivo(TipoActivo tipoActivo){
        this.tipoActivo = tipoActivo;
    }

    public int getCodigoActivo() {
        return this.id;
    }
    
}
