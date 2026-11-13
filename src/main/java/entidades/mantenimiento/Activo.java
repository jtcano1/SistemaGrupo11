/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidades.mantenimiento;

import java.io.Serializable;
import java.util.Date;

public class Activo implements Serializable {
    private int id;
    private String nombre;
    private Date fechaCompra;

    public Activo() {}

    public Activo(int id, String nombre, Date fechaCompra) {
        this.id = id;
        this.nombre = nombre;
        this.fechaCompra = fechaCompra;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Date getFechaCompra() { return fechaCompra; }
    public void setFechaCompra(Date fechaCompra) { this.fechaCompra = fechaCompra; }

    @Override
    public String toString() { return nombre; }
}
