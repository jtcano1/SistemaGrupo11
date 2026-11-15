package presentacion.mantenimiento;

import javax.swing.*;

public class FrmMenuMantenimiento extends JFrame {

    public FrmMenuMantenimiento() {

        setTitle("Módulo Mantenimiento");
        setSize(350,250);
        setLocationRelativeTo(null);

        JLabel lbl =
                new JLabel(
                        "Modulo Mantenimiento pendiente de integracion",
                        SwingConstants.CENTER);

        add(lbl);
    }
}