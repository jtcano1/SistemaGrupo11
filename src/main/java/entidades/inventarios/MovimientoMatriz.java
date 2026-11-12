package entidades.inventarios;

public class MovimientoMatriz {

    private int codigoArticulo;
    private String articulo;
    private int codigoTipoMovimiento;
    private String tipoMovimiento;
    private int cantidad;

    public MovimientoMatriz() {
    }

    public MovimientoMatriz(
            int codigoArticulo,
            String articulo,
            int codigoTipoMovimiento,
            String tipoMovimiento,
            int cantidad) {

        this.codigoArticulo = codigoArticulo;
        this.articulo = articulo;
        this.codigoTipoMovimiento = codigoTipoMovimiento;
        this.tipoMovimiento = tipoMovimiento;
        this.cantidad = cantidad;
    }

    public int getCodigoArticulo() {
        return codigoArticulo;
    }

    public void setCodigoArticulo(int codigoArticulo) {
        this.codigoArticulo = codigoArticulo;
    }

    public String getArticulo() {
        return articulo;
    }

    public void setArticulo(String articulo) {
        this.articulo = articulo;
    }

    public int getCodigoTipoMovimiento() {
        return codigoTipoMovimiento;
    }

    public void setCodigoTipoMovimiento(int codigoTipoMovimiento) {
        this.codigoTipoMovimiento = codigoTipoMovimiento;
    }

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}