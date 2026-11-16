package presentacion.activos;

import java.awt.GridLayout;
import javax.swing.*;

public class FrmMenuActivos extends JFrame {

   public FrmMenuActivos() {
        setTitle("Módulo de Activos");
        setSize(400, 320);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel lblTitulo = new JLabel("MENÚ MÓDULO ACTIVOS", SwingConstants.CENTER);
        JButton btnTipoActivo = new JButton("1. Tipos de Activo (Simple 1)");
        JButton btnActivo = new JButton("2. Gestión de Activos (Simple 2)");
        JButton btnDepreciacion = new JButton("3. Depreciación (Compleja)");
        JButton btnReportes = new JButton("4. Reportes de Activos");

        // Eventos para abrir tus pantallas reales
        btnTipoActivo.addActionListener(e -> new FrmTipoActivo().setVisible(true));
        btnActivo.addActionListener(e -> new FrmActivo().setVisible(true));
        btnDepreciacion.addActionListener(e -> new FrmDepreciacion().setVisible(true));
        btnReportes.addActionListener(e -> new FrmReporteActivos().setVisible(true));

        panel.add(lblTitulo);
        panel.add(btnTipoActivo);
        panel.add(btnActivo);
        panel.add(btnDepreciacion);
        panel.add(btnReportes);

        add(panel);
    }
}