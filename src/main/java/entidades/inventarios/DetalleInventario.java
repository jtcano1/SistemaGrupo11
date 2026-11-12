
package entidades.inventarios;


public class DetalleInventario {
    private int idDetalle;
    private int numeroComprobante;
    private int idArticulo;
    private int cantidad;
    private double precio;
    
    public DetalleInventario(){
    }

    public DetalleInventario(int idDetalle, int numeroComprobante, int idArticulo, int cantidad, double precio) {
        this.idDetalle = idDetalle;
        this.numeroComprobante = numeroComprobante;
        this.idArticulo = idArticulo;
        this.cantidad = cantidad;
        this.precio = precio;
    }

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

    public int getIdArticulo() {
        return idArticulo;
    }

    public void setIdArticulo(int idArticulo) {
        this.idArticulo = idArticulo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
    
    
}
