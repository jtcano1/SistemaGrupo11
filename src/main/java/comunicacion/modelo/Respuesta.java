
package comunicacion.modelo;

public class Respuesta {

    private boolean correcto;
    private String mensaje;

    public Respuesta() {
    }

    public boolean isCorrecto() {
        return correcto;
    }

    public void setCorrecto(boolean correcto) {
        this.correcto = correcto;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}