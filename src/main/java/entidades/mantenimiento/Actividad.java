package entidades.mantenimiento;

import java.io.Serializable;

public class Actividad implements Serializable {

    private int codigo;
    private String nombre;

    public Actividad() {
    }

    public Actividad(int codigo, String nombre) {
        this.codigo = codigo;
        this.nombre = nombre;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
