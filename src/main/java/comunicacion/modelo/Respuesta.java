package comunicacion.modelo;

public class Respuesta {

    private boolean correcto;
    private String mensaje;
    // Información adicional enviada por el servidor.
    private String dato;
    
    
    // Permite devolver entidades o listas
    // desde el servidor al cliente.
    private Object objeto;

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
    public Object getObjeto() {
        return objeto;
    }
    public void setObjeto(Object objeto) {
        this.objeto = objeto;
    }
}