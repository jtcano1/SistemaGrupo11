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

public class DetalleComprobante implements Serializable {
    private int idDetalle;
    private int numeroComprobante;
    private Cuenta cuenta;
    private double debe;
    private double haber;

    public DetalleComprobante() {
    }

    public DetalleComprobante(Cuenta cuenta, double debe, double haber) {
        this.cuenta = cuenta;
        this.debe = debe;
        this.haber = haber;
    }

    // --- Estos 2 son los que te faltaban y por eso salía rojo ---
    public int getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(int idDetalle) {
        this.idDetalle = idDetalle;
    }

    public int getNumeroComprobante() {
        return numeroComprobante;
    }

    public void setNumeroComprobante(int numeroComprobante) {
        this.numeroComprobante = numeroComprobante;
    }
    // -----------------------------------------------------------

    public Cuenta getCuenta() {
        return cuenta;
    }

    public void setCuenta(Cuenta cuenta) {
        this.cuenta = cuenta;
    }

    public double getDebe() {
        return debe;
    }

    public void setDebe(double debe) {
        this.debe = debe;
    }

    public double getHaber() {
        return haber;
    }

    public void setHaber(double haber) {
        this.haber = haber;
    }
}