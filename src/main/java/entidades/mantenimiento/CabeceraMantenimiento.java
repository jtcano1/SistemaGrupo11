/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidades.mantenimiento;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CabeceraMantenimiento implements Serializable {
    private int id;
    private Date fecha;
    private String responsable;
    private List<DetalleMantenimiento> detalles = new ArrayList<>();

    public CabeceraMantenimiento() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public String getResponsable() { return responsable; }
    public void setResponsable(String responsable) { this.responsable = responsable; }

    public List<DetalleMantenimiento> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleMantenimiento> detalles) { this.detalles = detalles; }
}