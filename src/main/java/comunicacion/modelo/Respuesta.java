package comunicacion.modelo;

public class Respuesta {

    private boolean correcto;
    private String mensaje;
    // Información adicional enviada por el servidor.
    private String dato;

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
    public String getDato() {
        return dato;
    }
    public void setDato(String dato) {
        this.dato = dato;
    }
}