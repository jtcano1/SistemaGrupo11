/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidades.mantenimiento;

import java.io.Serializable;

public class DetalleMantenimiento implements Serializable {
    private int id;
    private int idCabecera;
    private Activo activo;
    private Actividad actividad;
    private double valor;

    public DetalleMantenimiento() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdCabecera() { return idCabecera; }
    public void setIdCabecera(int idCabecera) { this.idCabecera = idCabecera; }

    public Activo getActivo() { return activo; }
    public void setActivo(Activo activo) { this.activo = activo; }

    public Actividad getActividad() { return actividad; }
    public void setActividad(Actividad actividad) { this.actividad = actividad; }

    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }
}
