package presentacion.mantenimiento;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTable;

public class PanelReporteGasto extends javax.swing.JPanel {

    public PanelReporteGasto() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        labelEstado = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        spinnerDesde = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        spinnerHasta = new javax.swing.JSpinner();
        btnConsultar = new javax.swing.JButton();
        btnImprimir = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaReporte = new javax.swing.JTable();

        setBackground(new java.awt.Color(204, 204, 204));
        setMinimumSize(new java.awt.Dimension(620, 620));
        setPreferredSize(new java.awt.Dimension(620, 620));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(51, 51, 51));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Gasto por Activo");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 15, -1, -1));

        jPanel3.setLayout(new java.awt.CardLayout());

        labelEstado.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelEstado.setText("Estado");
        labelEstado.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel3.add(labelEstado, "card2");

        jPanel2.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 10, 360, 40));

        add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 600, 60));

        jPanel1.setBackground(new java.awt.Color(51, 51, 51));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Segoe UI", 2, 14)); // NOI18N
        jLabel2.setText("Desde");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 25, -1, -1));

        spinnerDesde.setModel(new javax.swing.SpinnerDateModel());
        jPanel1.add(spinnerDesde, new org.netbeans.lib.awtextra.AbsoluteConstraints(75, 20, 120, 30));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 2, 14)); // NOI18N
        jLabel3.setText("Hasta");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(215, 25, -1, -1));

        spinnerHasta.setModel(new javax.swing.SpinnerDateModel());
        jPanel1.add(spinnerHasta, new org.netbeans.lib.awtextra.AbsoluteConstraints(265, 20, 120, 30));

        btnConsultar.setText("Consultar");
        jPanel1.add(btnConsultar, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 20, 90, 30));

        btnImprimir.setText("Imprimir");
        jPanel1.add(btnImprimir, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 20, 90, 30));

        add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 600, 70));

        tablaReporte.setBackground(new java.awt.Color(51, 51, 51));
        tablaReporte.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Activo", "Total"
            }
        ));
        jScrollPane1.setViewportView(tablaReporte);

        add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 600, 450));
    }// </editor-fold>//GEN-END:initComponents

    public JButton getBtnConsultar() {
        return btnConsultar;
    }

    public void setBtnConsultar(JButton btnConsultar) {
        this.btnConsultar = btnConsultar;
    }

    public JButton getBtnImprimir() {
        return btnImprimir;
    }

    public void setBtnImprimir(JButton btnImprimir) {
        this.btnImprimir = btnImprimir;
    }

    public JLabel getLabelEstado() {
        return labelEstado;
    }

    public void setLabelEstado(JLabel labelEstado) {
        this.labelEstado = labelEstado;
    }

    public JSpinner getSpinnerDesde() {
        return spinnerDesde;
    }

    public void setSpinnerDesde(JSpinner spinnerDesde) {
        this.spinnerDesde = spinnerDesde;
    }

    public JSpinner getSpinnerHasta() {
        return spinnerHasta;
    }

    public void setSpinnerHasta(JSpinner spinnerHasta) {
        this.spinnerHasta = spinnerHasta;
    }

    public JTable getTablaReporte() {
        return tablaReporte;
    }

    public void setTablaReporte(JTable tablaReporte) {
        this.tablaReporte = tablaReporte;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnConsultar;
    private javax.swing.JButton btnImprimir;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labelEstado;
    private javax.swing.JSpinner spinnerDesde;
    private javax.swing.JSpinner spinnerHasta;
    private javax.swing.JTable tablaReporte;
    // End of variables declaration//GEN-END:variables
}
