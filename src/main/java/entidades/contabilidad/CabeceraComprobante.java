/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidades.contabilidad;

/**
 *
 * @author USER
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CabeceraComprobante implements Serializable {
    private int numero;
    private Date fecha;
    private String observaciones;
    private List<DetalleComprobante> detalles;

    public CabeceraComprobante() {
        this.detalles = new ArrayList<>();
    }

    public CabeceraComprobante(int numero, Date fecha, String observaciones) {
        this.numero = numero;
        this.fecha = fecha;
        this.observaciones = observaciones;
        this.detalles = new ArrayList<>();
    }

    public int getNumero() { 
        return numero; 
    }
    
    public void setNumero(int numero) { 
        this.numero = numero; 
    }
    
    public Date getFecha() { 
        return fecha; 
    }
    
    public void setFecha(Date fecha) { 
        this.fecha = fecha; 
    }
    
    public String getObservaciones() { 
        return observaciones; 
    }
    
    public void setObservaciones(String observaciones) { 
        this.observaciones = observaciones; 
    }
    
    public List<DetalleComprobante> getDetalles() { 
        return detalles; 
    }
    
    public void setDetalles(List<DetalleComprobante> detalles) { 
        this.detalles = detalles; 
    }
}
