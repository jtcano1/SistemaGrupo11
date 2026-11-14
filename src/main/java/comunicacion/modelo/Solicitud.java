package comunicacion.modelo;

public class Solicitud {
    // Módulo que recibirá la solicitud.
    private String modulo;
    //Acción que debe ejecutar el servidor, Ejemplo: BUSCAR_ARTICULO.
    private String operacion;
    // Dato adicional asociado a la operación, Ejemplo: código del artículo.
    private String dato;
    
    // Permite transportar entidades o listas
    // entre cliente y servidor.
    private Object objeto;

    public Solicitud() {
    }

    public Solicitud(
            String modulo,
            String operacion) {

        this.modulo = modulo;
        this.operacion = operacion;
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