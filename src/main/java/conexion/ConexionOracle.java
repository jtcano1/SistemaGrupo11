/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionOracle {


    
    private static final String URL =
            "jdbc:oracle:thin:@localhost:1521/XEPDB1";
    private static final String USUARIO = "INVENTARIO";
    private static final String PASSWORD = "Inv2026_Cano";

    public Connection conectar() {
        Connection conexion = null;
        try {
            conexion = DriverManager.getConnection(
                    URL,
                    USUARIO,
                    PASSWORD
            );

            System.out.println("Conexion exitosa");

        } catch (SQLException e) {

            System.out.println("Error de conexion: "
                    + e.getMessage());

        }

        return conexion;
    }
}