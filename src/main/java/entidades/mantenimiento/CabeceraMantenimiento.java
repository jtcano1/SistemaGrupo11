package entidades.mantenimiento;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CabeceraMantenimiento implements Serializable {

    private int numero;
    private Date fecha;
    private String responsable;
    private List<DetalleMantenimiento> detalles = new ArrayList<>();

    public CabeceraMantenimiento() {
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

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public List<DetalleMantenimiento> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleMantenimiento> detalles) {
        this.detalles = detalles;
    }
}
