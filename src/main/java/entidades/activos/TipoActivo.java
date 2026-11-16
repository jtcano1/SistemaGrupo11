/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidades.activos;

/**
 *
 * @author USUARIO
 */
public class TipoActivo {
    
    private int codigo;
    private String nombre;
    private String descripcion;
    private String cuentaGasto;
    private String cuentaDepAcumulada;
    public TipoActivo() {
    
    }
        
        public TipoActivo(int codigo, String nombre, String descripcion, String cuentaGasto,
        String cuentaDepAcumulada) {
            this.codigo = codigo;
            this.nombre = nombre;
            this.descripcion = descripcion;
            this.cuentaGasto = cuentaGasto;
            this.cuentaDepAcumulada = cuentaDepAcumulada;
        }
        public int getCodigo(){
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
        
        public String getDescripcion() {
            return descripcion;
        }
        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }
        
        public String getCuentaGasto() {
            return cuentaGasto;
        }
        public void setCuentaGasto(String cuentaGasto) {
            this.cuentaGasto = cuentaGasto;
        }
        
        public String getCuentaDepAcumulada() {
            return cuentaDepAcumulada;
        }
        public void setCuentaDepAcumulada(String cuentaDepAcumulada) {
            this.cuentaDepAcumulada = cuentaDepAcumulada;
        }
        //metodo combox llama automaticamnete al toString
         @Override
        public String toString() {
        return nombre;
    }

  
    
}
