
package comunicacion.modelo;

public class Solicitud {

    private String modulo;
    private String operacion;

    public Solicitud() {
    }

    public String getModulo() {
        return modulo;
    }

    public void setModulo(String modulo) {
        this.modulo = modulo;
    }

    public String getOperacion() {
        return operacion;
    }

    public void setOperacion(String operacion) {
        this.operacion = operacion;
    }
}