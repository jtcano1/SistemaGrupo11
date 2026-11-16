package main;

import com.formdev.flatlaf.FlatDarkLaf;
import presentacion.main.MainFrame;

public class Main {

    public static void main(String[] args) {
        FlatDarkLaf.setup();

        MainFrame ventanaPrincipal = new MainFrame();

        MainInit inicio = new MainInit(ventanaPrincipal);
        inicio.iniciar();

        ventanaPrincipal.pack();
        ventanaPrincipal.setLocationRelativeTo(null);
        ventanaPrincipal.setVisible(true);
    }
}
