
package presentacion.inventarios;

import entidades.inventarios.SaldoArticulo;
import comunicacion.cliente.ClienteReporteSaldos;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.util.Date;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import java.util.Calendar;

public class FrmReporteSaldos extends JFrame {

    private final ClienteReporteSaldos cliente =
            new ClienteReporteSaldos();

    private JTable tabla;
    private DefaultTableModel modelo;
    private JSpinner spHasta;

    private JButton btnActualizar;
    private JButton btnImprimir;
    private JButton btnCerrar;

    public FrmReporteSaldos() {

        setTitle("Reporte de saldos por artículo");
        setSize(750, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        crearComponentes();
        cargarReporte();
    }

    private void crearComponentes() {

        setLayout(new BorderLayout(10, 10));

        JLabel titulo = new JLabel(
                "REPORTE DE SALDOS POR ARTÍCULO",
                JLabel.CENTER);

        titulo.setFont(new Font("Arial", Font.BOLD, 20));

        JPanel panelSuperior =
        new JPanel(new BorderLayout());
        
        panelSuperior.add(titulo, BorderLayout.NORTH);
        JPanel panelFecha =new JPanel(new FlowLayout());
        
        panelFecha.add(new JLabel("Hasta:"));
        
        spHasta = new JSpinner(new SpinnerDateModel(
                new Date(),null,null,
                Calendar.DAY_OF_MONTH));
        
        spHasta.setEditor(new JSpinner.DateEditor(
                spHasta,"dd/MM/yyyy"));
        
        panelFecha.add(spHasta);
        
        panelSuperior.add(
        panelFecha,
        BorderLayout.SOUTH);
        
        add(panelSuperior, BorderLayout.NORTH);

///

        modelo = new DefaultTableModel(
                new Object[]{
                    "Código",
                    "Artículo",
                    "Precio",
                    "Stock actual"
                }, 0) {

            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tabla = new JTable(modelo);
        tabla.setRowHeight(25);

        JScrollPane scroll = new JScrollPane(tabla);

        add(scroll, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(
                new FlowLayout(FlowLayout.CENTER));

        btnActualizar = new JButton("Actualizar");
        btnImprimir = new JButton("Imprimir");
        btnCerrar = new JButton("Cerrar");

        panelBotones.add(btnActualizar);
        panelBotones.add(btnImprimir);
        panelBotones.add(btnCerrar);

        add(panelBotones, BorderLayout.SOUTH);

        btnActualizar.addActionListener(e -> cargarReporte());

        btnImprimir.addActionListener(e -> imprimirReporte());

        btnCerrar.addActionListener(e -> dispose());
    }
    
    
private void cargarReporte() {

    modelo.setRowCount(0);

    Date fechaHasta = (Date) spHasta.getValue();

    List<SaldoArticulo> lista =
            cliente.listarSaldos(fechaHasta);

    for (SaldoArticulo saldo : lista) {

        modelo.addRow(new Object[]{
            saldo.getCodigo(),
            saldo.getNombre(),
            saldo.getPrecio(),
            saldo.getStock()
        });
    }

    if (lista.isEmpty()) {

        JOptionPane.showMessageDialog(
                this,
                "No se encontraron artículos para el reporte.");
    }
}

    
    private void imprimirReporte() {

        try {

            boolean impreso = tabla.print(
                    JTable.PrintMode.FIT_WIDTH,
                    null,
                    null);

            if (impreso) {
                JOptionPane.showMessageDialog(
                        this,
                        "Reporte enviado a impresión.");
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al imprimir: " + e.getMessage());
        }
    }
}
