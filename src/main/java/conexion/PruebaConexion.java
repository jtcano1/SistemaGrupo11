package conexion;

import java.sql.Connection;

public class PruebaConexion {

    public static void main(String[] args) {

        ConexionOracle conexion = new ConexionOracle();

        Connection cn = conexion.conectar();

        if (cn != null) {

            System.out.println("Conectado correctamente");

        } else {

            System.out.println("No se pudo conectar");

        }

    }
}