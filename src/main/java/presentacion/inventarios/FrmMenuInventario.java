package presentacion.inventarios;

import javax.swing.*;
import java.awt.*;

public class FrmMenuInventario extends JFrame {

    public FrmMenuInventario() {

        setTitle("Módulo Inventario");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel titulo = new JLabel(
                "MODULO INVENTARIO",
                SwingConstants.CENTER);

        titulo.setFont(
                new Font("Times New Roman",
                        Font.BOLD,
                        18));

        JButton btnArticulo =
                new JButton("Artículo");

        JButton btnTipoMovimiento =
                new JButton("Tipo Movimiento");

        JButton btnComprobante =
                new JButton("Comprobante Inventario");

        JButton btnReporteSaldos =
                new JButton("Reporte Saldos");

        JButton btnReporteMatriz =
                new JButton("Reporte Matricial");

        JButton btnVolver =
                new JButton("Volver");

        btnArticulo.addActionListener(e ->
                new FrmArticulo().setVisible(true));

        btnTipoMovimiento.addActionListener(e ->
                new FrmTipoMovimiento().setVisible(true));

        btnComprobante.addActionListener(e ->
                new FrmInventario().setVisible(true));

        btnReporteSaldos.addActionListener(e ->
                new FrmReporteSaldos().setVisible(true));

        btnReporteMatriz.addActionListener(e ->
                new FrmReporteMatriz().setVisible(true));

        btnVolver.addActionListener(e ->
                dispose());

        JPanel panel = new JPanel();

        panel.setLayout(
                new GridLayout(7,1,10,10));

        panel.add(titulo);
        panel.add(btnArticulo);
        panel.add(btnTipoMovimiento);
        panel.add(btnComprobante);
        panel.add(btnReporteSaldos);
        panel.add(btnReporteMatriz);
        panel.add(btnVolver);

        add(panel);
    }

    public static void main(String[] args) {
        new FrmMenuInventario().setVisible(true);
    }
}