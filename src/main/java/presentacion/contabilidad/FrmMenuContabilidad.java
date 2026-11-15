package presentacion.contabilidad;

import javax.swing.*;
import java.awt.*;

public class FrmMenuContabilidad extends JFrame {

    public FrmMenuContabilidad() {

        setTitle("Módulo Contabilidad");
        setSize(400,300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel titulo =
                new JLabel(
                        "MODULO CONTABILIDAD",
                        SwingConstants.CENTER);

        titulo.setFont(
                new Font(
                        "Times New Roman",
                        Font.BOLD,
                        18));

        JButton btnTipoCuenta =
                new JButton("Tipo Cuenta");

        JButton btnCuenta =
                new JButton("Cuenta");

        JButton btnComprobante =
                new JButton("Comprobante Contable");

        JButton btnReportes =
                new JButton("Reportes");

        JButton btnVolver =
                new JButton("Volver");

        btnTipoCuenta.addActionListener(e ->
                new FrmTipoCuenta().setVisible(true));

        btnCuenta.addActionListener(e ->
                new FrmCuenta().setVisible(true));

        btnComprobante.addActionListener(e ->
                new FrmComprobante().setVisible(true));

        btnReportes.addActionListener(e ->
                new FrmReportes().setVisible(true));

        btnVolver.addActionListener(e ->
                dispose());

        JPanel panel = new JPanel();

        panel.setLayout(
                new GridLayout(6,1,10,10));

        panel.add(titulo);
        panel.add(btnTipoCuenta);
        panel.add(btnCuenta);
        panel.add(btnComprobante);
        panel.add(btnReportes);
        panel.add(btnVolver);

        add(panel);
    }

    public static void main(String[] args) {
        new FrmMenuContabilidad().setVisible(true);
    }
}