package entidades.mantenimiento;

import java.io.Serializable;
import java.util.Date;

public class Activo implements Serializable {

    private int codigo;
    private String nombre;
    private Date fechaCompra;

    public Activo() {
    }

    public Activo(int codigo, String nombre, Date fechaCompra) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.fechaCompra = fechaCompra;
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

    public Date getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(Date fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
