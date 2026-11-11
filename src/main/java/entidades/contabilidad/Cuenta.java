/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidades.contabilidad;

/**
 *
 * @author USER
 */
public class Cuenta {
     private int codigo;
    private String nombre;
    private TipoCuenta tipoCuenta;

    public Cuenta() {
    }

    public Cuenta(int codigo, String nombre, TipoCuenta tipoCuenta) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.tipoCuenta = tipoCuenta;
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

    public TipoCuenta getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(TipoCuenta tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    @Override
    public String toString() {
        return codigo + " - " + nombre;
    }
}
