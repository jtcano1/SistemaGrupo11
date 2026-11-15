package presentacion.activos;

import javax.swing.*;

public class FrmMenuActivos extends JFrame {

    public FrmMenuActivos() {

        setTitle("Módulo Activos");
        setSize(350,250);
        setLocationRelativeTo(null);

        JLabel lbl =
                new JLabel(
                        "Modulo Activos pendiente de integracion",
                        SwingConstants.CENTER);

        add(lbl);
    }
}