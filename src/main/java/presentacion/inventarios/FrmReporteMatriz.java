package presentacion.inventarios;

import entidades.inventarios.ColumnaMovimiento;
import entidades.inventarios.MovimientoMatriz;
import negocio.inventarios.ReporteMatrizBL;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;

import javax.swing.table.DefaultTableModel;

public class FrmReporteMatriz extends JFrame {

    private final ReporteMatrizBL reporteBL =
            new ReporteMatrizBL();

    private JTable tabla;
    private DefaultTableModel modelo;

    private JSpinner spDesde;
    private JSpinner spHasta;

    private JButton btnActualizar;
    private JButton btnImprimir;
    private JButton btnCerrar;

    public FrmReporteMatriz() {

        setTitle("Reporte matricial de movimientos");
        setSize(950, 600);
        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        crearComponentes();
        cargarReporte();
    }

    private void crearComponentes() {

        setLayout(new BorderLayout(10, 10));

        // PANEL SUPERIOR
        JPanel panelSuperior = new JPanel(
                new BorderLayout(5, 10));

        JLabel titulo = new JLabel(
                "REPORTE MATRICIAL DE MOVIMIENTOS",
                JLabel.CENTER);

        titulo.setFont(new Font("Arial", Font.BOLD, 20));

        panelSuperior.add(titulo, BorderLayout.NORTH);

        // FECHAS
        JPanel panelFechas = new JPanel(
                new FlowLayout(FlowLayout.CENTER));

        JLabel lblDesde = new JLabel("Desde:");

        Calendar inicio = Calendar.getInstance();
        inicio.set(2020, Calendar.JANUARY, 1, 0, 0, 0);
        inicio.set(Calendar.MILLISECOND, 0);

        spDesde = new JSpinner(
                new SpinnerDateModel(
                        inicio.getTime(),
                        null,
                        null,
                        Calendar.DAY_OF_MONTH));

        spDesde.setEditor(
                new JSpinner.DateEditor(
                        spDesde, "dd/MM/yyyy"));

        JLabel lblHasta = new JLabel("Hasta:");

        Date hoy = new Date();

        spHasta = new JSpinner(
                new SpinnerDateModel(
                        hoy,
                        null,
                        null,
                        Calendar.DAY_OF_MONTH));

        spHasta.setEditor(
                new JSpinner.DateEditor(
                        spHasta, "dd/MM/yyyy"));

        panelFechas.add(lblDesde);
        panelFechas.add(spDesde);
        panelFechas.add(lblHasta);
        panelFechas.add(spHasta);

        panelSuperior.add(
                panelFechas, BorderLayout.SOUTH);

        add(panelSuperior, BorderLayout.NORTH);

        // TABLA
        modelo = new DefaultTableModel() {

            @Override
            public boolean isCellEditable(
                    int fila, int columna) {

                return false;
            }
        };

        tabla = new JTable(modelo);
        tabla.setRowHeight(25);

        JScrollPane scroll = new JScrollPane(tabla);

        add(scroll, BorderLayout.CENTER);

        // BOTONES
        JPanel panelBotones = new JPanel(
                new FlowLayout(FlowLayout.CENTER));

        btnActualizar = new JButton("Consultar");
        btnImprimir = new JButton("Imprimir");
        btnCerrar = new JButton("Cerrar");

        panelBotones.add(btnActualizar);
        panelBotones.add(btnImprimir);
        panelBotones.add(btnCerrar);

        add(panelBotones, BorderLayout.SOUTH);

        btnActualizar.addActionListener(
                e -> cargarReporte());

        btnImprimir.addActionListener(
                e -> imprimirReporte());

        btnCerrar.addActionListener(
                e -> dispose());
    }

    private void cargarReporte() {

        Date desde = (Date) spDesde.getValue();
        Date hasta = (Date) spHasta.getValue();

        if (desde.after(hasta)) {

            JOptionPane.showMessageDialog(
                    this,
                    "La fecha Desde no puede ser posterior a Hasta.");

            return;
        }

        modelo.setRowCount(0);
        modelo.setColumnCount(0);

        // CONSULTAR TIPOS
        List<ColumnaMovimiento> tipos =
                reporteBL.listarTipos();

        // CONSULTAR MOVIMIENTOS DEL PERIODO
        List<MovimientoMatriz> movimientos =
                reporteBL.listarMovimientos(
                        desde, hasta);

        if (tipos.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "No existen tipos de movimiento registrados.");

            return;
        }

        // CREAR COLUMNAS
        modelo.addColumn("Código");
        modelo.addColumn("Artículo");

        for (ColumnaMovimiento tipo : tipos) {
            modelo.addColumn(tipo.getNombre());
        }

        // INDICES DE LAS COLUMNAS
        Map<Integer, Integer> indicesTipos =
                new HashMap<>();

        for (int i = 0; i < tipos.size(); i++) {

            indicesTipos.put(
                    tipos.get(i).getCodigo(),
                    i + 2);
        }

        // AGRUPAR POR ARTICULO
        Map<Integer, Object[]> filas =
                new HashMap<>();

        for (MovimientoMatriz movimiento : movimientos) {

            int codigoArticulo =
                    movimiento.getCodigoArticulo();

            if (!filas.containsKey(codigoArticulo)) {

                Object[] fila =
                        new Object[tipos.size() + 2];

                fila[0] = codigoArticulo;
                fila[1] = movimiento.getArticulo();

                for (int i = 2; i < fila.length; i++) {
                    fila[i] = 0;
                }

                filas.put(codigoArticulo, fila);
            }

            Integer indice =
                    indicesTipos.get(
                            movimiento.getCodigoTipoMovimiento());

            if (indice != null) {

                filas.get(codigoArticulo)[indice] =
                        movimiento.getCantidad();
            }
        }

        // MOSTRAR FILAS
        for (Object[] fila : filas.values()) {
            modelo.addRow(fila);
        }

        if (filas.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "No se encontraron artículos.");
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