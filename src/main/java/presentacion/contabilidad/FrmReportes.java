package presentacion.contabilidad;

import datos.contabilidad.DAOCuenta;
import datos.contabilidad.DAOComprobante;
import entidades.contabilidad.CabeceraComprobante;
import entidades.contabilidad.Cuenta;
import entidades.contabilidad.DetalleComprobante;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class FrmReportes extends javax.swing.JFrame {

    private DAOCuenta daoCuenta = new DAOCuenta();
    private DAOComprobante daoComp = new DAOComprobante();
    private DefaultTableModel modelo;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public FrmReportes() {
        initComponents();
        modelo = new DefaultTableModel();
        tblReporte.setModel(modelo);
        // SOLO LOS 2 QUE PIDE LA FOTO
        cmbReporte.addItem("Balance General");
        cmbReporte.addItem("Estado de Resultados");
        txtDesde.setText("01/01/2026");
        txtHasta.setText("31/12/2026");
        setLocationRelativeTo(null);
    }

    private void generar() {
        try {
            Date desde = sdf.parse(txtDesde.getText().trim());
            Date hasta = sdf.parse(txtHasta.getText().trim());
            if (desde.after(hasta)) { JOptionPane.showMessageDialog(this, "Fecha Desde no puede ser mayor que Hasta"); return; }
            if (cmbReporte.getSelectedItem().toString().equals("Balance General")) reporteBalanceGeneral(desde, hasta);
            else reporteEstadoResultados(desde, hasta);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Fecha inválida. Usa dd/MM/yyyy  Ej: 01/01/2026");
        }
    }

    private boolean enRango(Date fecha, Date desde, Date hasta) {
        if (fecha == null) return false;
        try { fecha = sdf.parse(sdf.format(fecha)); } catch (Exception e) {}
        return !fecha.before(desde) && !fecha.after(hasta);
    }

    private Map<String, Double> saldosPorCuenta(Date desde, Date hasta) {
        Map<String, Double> saldos = new HashMap<>();
        for (CabeceraComprobante cab : daoComp.listar()) {
            if (!enRango(cab.getFecha(), desde, hasta)) continue;
            CabeceraComprobante completo = daoComp.buscarPorNumero(cab.getNumero());
            if (completo == null) continue;
            for (DetalleComprobante d : completo.getDetalles()) {
                String cod = d.getCuenta().getCodigo();
                double saldo = d.getDebe() - d.getHaber();
                saldos.put(cod, saldos.getOrDefault(cod, 0.0) + saldo);
            }
        }
        return saldos;
    }

    private void reporteBalanceGeneral(Date desde, Date hasta) {
        modelo.setColumnIdentifiers(new String[]{"Código", "Cuenta", "Saldo"});
        modelo.setRowCount(0);
        Map<String, Double> saldos = saldosPorCuenta(desde, hasta);
        double totalActivo = 0, totalPasivo = 0, totalPatrimonio = 0, totalIngresos = 0, totalEgresos = 0;
        for (String cod : saldos.keySet()) {
            double saldo = saldos.get(cod);
            char tipo = cod.charAt(0);
            if (tipo == '1') totalActivo += saldo;
            else if (tipo == '2') totalPasivo += (-saldo);
            else if (tipo == '3') totalPatrimonio += (-saldo);
            else if (tipo == '4') totalIngresos += (-saldo);
            else if (tipo == '5') totalEgresos += saldo;
        }
        double utilidad = totalIngresos - totalEgresos;
        modelo.addRow(new Object[]{"PERIODO", "Del " + txtDesde.getText() + " al " + txtHasta.getText(), ""});
        modelo.addRow(new Object[]{"1", "ACTIVO", ""});
        for (Cuenta c : daoCuenta.listar()) {
            if (c.getCodigo().startsWith("1") && saldos.containsKey(c.getCodigo())) {
                modelo.addRow(new Object[]{"1.1", c.getCodigo() + " - " + c.getNombre(), saldos.get(c.getCodigo())});
            }
        }
        modelo.addRow(new Object[]{"", "TOTAL ACTIVO", totalActivo});
        modelo.addRow(new Object[]{"", "", ""});
        modelo.addRow(new Object[]{"2", "PASIVO", ""});
        for (Cuenta c : daoCuenta.listar()) {
            if (c.getCodigo().startsWith("2") && saldos.containsKey(c.getCodigo())) {
                modelo.addRow(new Object[]{"2.1", c.getCodigo() + " - " + c.getNombre(), -saldos.get(c.getCodigo())});
            }
        }
        modelo.addRow(new Object[]{"", "TOTAL PASIVO", totalPasivo});
        modelo.addRow(new Object[]{"", "", ""});
        modelo.addRow(new Object[]{"3", "CAPITAL O PATRIMONIO", ""});
        for (Cuenta c : daoCuenta.listar()) {
            if (c.getCodigo().startsWith("3") && saldos.containsKey(c.getCodigo())) {
                modelo.addRow(new Object[]{"3.1", c.getCodigo() + " - " + c.getNombre(), -saldos.get(c.getCodigo())});
            }
        }
        modelo.addRow(new Object[]{"3.1.1", "Utilidad del Ejercicio", utilidad});
        modelo.addRow(new Object[]{"", "TOTAL PATRIMONIO + UTILIDAD", totalPatrimonio + utilidad});
        modelo.addRow(new Object[]{"", "TOTAL PASIVO + PATRIMONIO", totalPasivo + totalPatrimonio + utilidad});
        boolean cuadra = Math.abs(totalActivo - (totalPasivo + totalPatrimonio + utilidad)) < 0.01;
        lblTotal.setText("Activo: " + totalActivo + "  |  Pasivo+Patrimonio: " + (totalPasivo + totalPatrimonio + utilidad) + "  |  " + (cuadra ? "✅ CUADRADO" : "⚠️ NO CUADRA"));
    }

    private void reporteEstadoResultados(Date desde, Date hasta) {
        modelo.setColumnIdentifiers(new String[]{"Código", "Cuenta", "Monto"});
        modelo.setRowCount(0);
        Map<String, Double> saldos = saldosPorCuenta(desde, hasta);
        double totalIngresos = 0, totalEgresos = 0;
        modelo.addRow(new Object[]{"PERIODO", "Del " + txtDesde.getText() + " al " + txtHasta.getText(), ""});
        modelo.addRow(new Object[]{"4", "INGRESOS", ""});
        for (Cuenta c : daoCuenta.listar()) {
            if (c.getCodigo().startsWith("4") && saldos.containsKey(c.getCodigo())) {
                double monto = -saldos.get(c.getCodigo());
                modelo.addRow(new Object[]{"4.1", c.getCodigo() + " - " + c.getNombre(), monto});
                totalIngresos += monto;
            }
        }
        modelo.addRow(new Object[]{"", "TOTAL INGRESOS", totalIngresos});
        modelo.addRow(new Object[]{"", "", ""});
        modelo.addRow(new Object[]{"5", "EGRESOS", ""});
        for (Cuenta c : daoCuenta.listar()) {
            if (c.getCodigo().startsWith("5") && saldos.containsKey(c.getCodigo())) {
                double monto = saldos.get(c.getCodigo());
                modelo.addRow(new Object[]{"5.1", c.getCodigo() + " - " + c.getNombre(), monto});
                totalEgresos += monto;
            }
        }
        modelo.addRow(new Object[]{"", "TOTAL EGRESOS", totalEgresos});
        modelo.addRow(new Object[]{"", "", ""});
        double utilidad = totalIngresos - totalEgresos;
        modelo.addRow(new Object[]{"", "UTILIDAD = Ingresos - Egresos", utilidad});
        lblTotal.setText("Ingresos: " + totalIngresos + "  |  Egresos: " + totalEgresos + "  |  Utilidad: " + utilidad);
    }

    private void imprimir() {
        if (modelo.getRowCount() == 0) { JOptionPane.showMessageDialog(this, "Primero genera un reporte"); return; }
        try {
            String titulo = cmbReporte.getSelectedItem().toString() + "  |  Del " + txtDesde.getText() + " al " + txtHasta.getText();
            java.text.MessageFormat header = new java.text.MessageFormat(titulo);
            java.text.MessageFormat footer = new java.text.MessageFormat("Página {0}  |  Sistema Grupo 11 - Contabilidad");
            boolean ok = tblReporte.print(javax.swing.JTable.PrintMode.FIT_WIDTH, header, footer, true, null, true);
            if (ok) JOptionPane.showMessageDialog(this, "✅ Impresión enviada");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al imprimir: " + ex.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        lblTitulo = new javax.swing.JLabel(); lblReporte = new javax.swing.JLabel();
        lblDesde = new javax.swing.JLabel(); lblHasta = new javax.swing.JLabel();
        cmbReporte = new javax.swing.JComboBox<>(); btnGenerar = new javax.swing.JButton(); btnImprimir = new javax.swing.JButton();
        txtDesde = new javax.swing.JTextField(); txtHasta = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane(); tblReporte = new javax.swing.JTable(); lblTotal = new javax.swing.JLabel();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Reportes Contables");
        lblTitulo.setText("REPORTES CONTABLES"); lblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 14));
        lblReporte.setText("Reporte:"); lblDesde.setText("Desde (dd/MM/yyyy):"); lblHasta.setText("Hasta:");
        lblTotal.setText("Seleccione un reporte, rango de fechas y dele Generar");
        txtDesde.setText("01/01/2026"); txtHasta.setText("31/12/2026");
        btnGenerar.setText("Generar Reporte"); btnGenerar.addActionListener(e -> generar());
        btnImprimir.setText("🖨️ Imprimir Reporte"); btnImprimir.addActionListener(e -> imprimir());
        tblReporte.setModel(new DefaultTableModel(new Object[][]{}, new String[]{})); jScrollPane1.setViewportView(tblReporte);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane()); getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup().addGap(20,20,20).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addComponent(lblTitulo)
                .addGroup(layout.createSequentialGroup().addComponent(lblReporte).addGap(10,10,10).addComponent(cmbReporte, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(10,10,10).addComponent(btnGenerar).addGap(10,10,10).addComponent(btnImprimir))
                .addGroup(layout.createSequentialGroup().addComponent(lblDesde).addGap(10,10,10).addComponent(txtDesde, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(15,15,15).addComponent(lblHasta).addGap(10,10,10).addComponent(txtHasta, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 640, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(lblTotal)).addContainerGap(20, Short.MAX_VALUE)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup().addGap(15,15,15).addComponent(lblTitulo).addGap(10,10,10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(lblReporte).addComponent(cmbReporte, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(btnGenerar).addComponent(btnImprimir)).addGap(10,10,10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(lblDesde).addComponent(txtDesde, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(lblHasta).addComponent(txtHasta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(15,15,15)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(10,10,10)
                .addComponent(lblTotal).addContainerGap(20, Short.MAX_VALUE)));
        pack();
    }
    public static void main(String args[]) { java.awt.EventQueue.invokeLater(() -> new FrmReportes().setVisible(true)); }
    private javax.swing.JButton btnGenerar; private javax.swing.JButton btnImprimir;
    private javax.swing.JComboBox<String> cmbReporte; private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblDesde; private javax.swing.JLabel lblHasta; private javax.swing.JLabel lblReporte; private javax.swing.JLabel lblTitulo; private javax.swing.JLabel lblTotal;
    private javax.swing.JTable tblReporte;
    private javax.swing.JTextField txtDesde; private javax.swing.JTextField txtHasta;
}