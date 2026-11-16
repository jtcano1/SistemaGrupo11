package entidades.mantenimiento;

import java.io.Serializable;

public class DetalleMantenimiento implements Serializable {

    private int numeroMantenimiento;
    private Activo activo;
    private Actividad actividad;
    private double valor;

    public DetalleMantenimiento() {
    }

    public int getNumeroMantenimiento() {
        return numeroMantenimiento;
    }

    public void setNumeroMantenimiento(int numeroMantenimiento) {
        this.numeroMantenimiento = numeroMantenimiento;
    }

    public Activo getActivo() {
        return activo;
    }

    public void setActivo(Activo activo) {
        this.activo = activo;
    }

    public Actividad getActividad() {
        return actividad;
    }

    public void setActividad(Actividad actividad) {
        this.actividad = actividad;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
}
