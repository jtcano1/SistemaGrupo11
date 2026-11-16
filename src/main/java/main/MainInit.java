package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import presentacion.main.MainFrame;

public class MainInit implements ActionListener {

    private final MainFrame ventana;
    private final Map<JMenuItem, JPanel> pantallas = new HashMap<>();
    private JPanel panelActual;

    public MainInit(MainFrame ventana) {
        this.ventana = ventana;
    }

    public void iniciar() {
        ModuloMantenimiento mantenimiento = new ModuloMantenimiento();
        mantenimiento.registrar(this);
    }

    public JMenu registrarModulo(String nombre) {
        JMenu menu = new JMenu(nombre);
        ventana.getMenuNavegacion().add(menu);
        return menu;
    }

    public void registrarPantalla(JMenu menu, String titulo, JPanel panel) {
        JMenuItem item = new JMenuItem(titulo);
        item.addActionListener(this);
        menu.add(item);
        pantallas.put(item, panel);

        if (panelActual == null) {
            mostrar(panel);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JPanel panel = pantallas.get(e.getSource());
        if (panel != null) {
            mostrar(panel);
        }
    }

    public void mostrar(JPanel panel) {
        JPanel contenedor = ventana.getPanelMain();
        contenedor.removeAll();
        int ancho = panel.getPreferredSize().width;
        int alto = panel.getPreferredSize().height;
        contenedor.add(panel, new AbsoluteConstraints(0, 0, ancho, alto));
        contenedor.revalidate();
        contenedor.repaint();
        panelActual = panel;
    }
}
