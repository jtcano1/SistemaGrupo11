/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package comunicacion.cliente;

/**
 *
 * @author jtcanop
 */
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class ClienteSocket {

    // Metodo generico: manda un mensaje y devuelve lo que responde el servidor
    public String enviar(String mensaje) {
        try (Socket socket = new Socket("localhost", 5000);
             DataOutputStream salida = new DataOutputStream(socket.getOutputStream());
             DataInputStream entrada = new DataInputStream(socket.getInputStream())) {

            salida.writeUTF(mensaje);
            String respuesta = entrada.readUTF();
            System.out.println("Respuesta servidor: " + respuesta);
            return respuesta;

        } catch (Exception e) {
            System.out.println("Error ClienteSocket: " + e.getMessage());
            return "ERROR|SIN_CONEXION";
        }
    }

    // Tu metodo viejo lo dejamos para que no se rompa lo de tu compañera
    public void conectar() {
        String r = enviar("INVENTARIO|BUSCAR_ARTICULO|2");
        System.out.println(r);
    }

    // --- Helpers para que la pantalla no tenga que armar el texto a mano ---
    public String listarCuentas() { 
        return enviar("CONTABILIDAD|LISTAR_CUENTAS"); 
    }
    
    public String buscarCuenta(String codigo) { 
        return enviar("CONTABILIDAD|BUSCAR_CUENTA|" + codigo); 
    }
    
    public String guardarCuenta(String codigo, String nombre, int tipo) { 
        return enviar("CONTABILIDAD|GUARDAR_CUENTA|" + codigo + "|" + nombre + "|" + tipo); 
    }
    
    public String modificarCuenta(String codigo, String nombre, int tipo) { 
        return enviar("CONTABILIDAD|MODIFICAR_CUENTA|" + codigo + "|" + nombre + "|" + tipo); 
    }
    
    public String eliminarCuenta(String codigo) { 
        return enviar("CONTABILIDAD|ELIMINAR_CUENTA|" + codigo); 
    }
    
    public String listarTipoCuenta() { 
        return enviar("CONTABILIDAD|LISTAR_TIPO_CUENTA"); 
    }

    public String guardarComprobante(String fecha, String obs, String detalles) { 
        return enviar("CONTABILIDAD|GUARDAR_COMPROBANTE|" + fecha + "|" + obs + "|" + detalles); 
    }
    
    public String modificarComprobante(int numero, String fecha, String obs, String detalles) { 
        return enviar("CONTABILIDAD|MODIFICAR_COMPROBANTE|" + numero + "|" + fecha + "|" + obs + "|" + detalles); 
    }
    
    public String buscarComprobante(int numero) { 
        return enviar("CONTABILIDAD|BUSCAR_COMPROBANTE|" + numero); 
    }
    
    public String listarComprobantes() { 
        return enviar("CONTABILIDAD|LISTAR_COMPROBANTES"); 
    }
    
    public String eliminarComprobante(int numero) { 
        return enviar("CONTABILIDAD|ELIMINAR_COMPROBANTE|" + numero); 
    }

    public String reporteBalance(String desde, String hasta) { 
        return enviar("CONTABILIDAD|REPORTE_BALANCE|" + desde + "|" + hasta); 
    }
    public String reporteResultados(String desde, String hasta) { 
        return enviar("CONTABILIDAD|REPORTE_RESULTADOS|" + desde + "|" + hasta); 
    }
}
