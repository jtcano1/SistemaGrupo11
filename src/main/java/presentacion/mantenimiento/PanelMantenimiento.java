package presentacion.mantenimiento;

import entidades.mantenimiento.Actividad;
import entidades.mantenimiento.Activo;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;

public class PanelMantenimiento extends javax.swing.JPanel {

    public PanelMantenimiento() {
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
        inputNumero = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        spinnerFecha = new javax.swing.JSpinner();
        jLabel4 = new javax.swing.JLabel();
        inputResponsable = new javax.swing.JTextField();
        btnGuardar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        inputBuscar = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaCabeceras = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        comboActivo = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        comboActividad = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        inputValor = new javax.swing.JTextField();
        btnAgregarDetalle = new javax.swing.JButton();
        btnQuitarDetalle = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaDetalles = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        labelTotal = new javax.swing.JLabel();

        setBackground(new java.awt.Color(204, 204, 204));
        setMinimumSize(new java.awt.Dimension(620, 620));
        setPreferredSize(new java.awt.Dimension(620, 620));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(51, 51, 51));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Mantenimiento");
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
        jLabel2.setText("Numero");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 15, -1, -1));
        jPanel1.add(inputNumero, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 10, 150, 30));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 2, 14)); // NOI18N
        jLabel3.setText("Fecha");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 55, -1, -1));

        spinnerFecha.setModel(new javax.swing.SpinnerDateModel());
        jPanel1.add(spinnerFecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 50, 150, 30));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 2, 14)); // NOI18N
        jLabel4.setText("Responsable");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 95, -1, -1));
        jPanel1.add(inputResponsable, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 90, 150, 30));

        btnGuardar.setText("Guardar");
        jPanel1.add(btnGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 10, 90, 30));

        btnModificar.setText("Modificar");
        jPanel1.add(btnModificar, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 50, 90, 30));

        btnEliminar.setText("Eliminar");
        jPanel1.add(btnEliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 10, 90, 30));

        btnLimpiar.setText("Limpiar");
        jPanel1.add(btnLimpiar, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 50, 90, 30));
        jPanel1.add(inputBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 90, 190, 30));

        btnBuscar.setText("Buscar");
        jPanel1.add(btnBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 90, 90, 30));

        add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 600, 130));

        tablaCabeceras.setBackground(new java.awt.Color(51, 51, 51));
        tablaCabeceras.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Numero", "Fecha", "Responsable"
            }
        ));
        jScrollPane1.setViewportView(tablaCabeceras);

        add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 220, 600, 120));

        jPanel4.setBackground(new java.awt.Color(51, 51, 51));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setFont(new java.awt.Font("Segoe UI", 2, 14)); // NOI18N
        jLabel5.setText("Activo");
        jPanel4.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 8, -1, -1));
        jPanel4.add(comboActivo, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 35, 170, 30));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 2, 14)); // NOI18N
        jLabel6.setText("Actividad");
        jPanel4.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 8, -1, -1));
        jPanel4.add(comboActividad, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 35, 170, 30));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 2, 14)); // NOI18N
        jLabel7.setText("Valor");
        jPanel4.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 8, -1, -1));
        jPanel4.add(inputValor, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 35, 90, 30));

        btnAgregarDetalle.setText("Agregar");
        jPanel4.add(btnAgregarDetalle, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 5, 100, 30));

        btnQuitarDetalle.setText("Quitar");
        jPanel4.add(btnQuitarDetalle, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 42, 100, 30));

        add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 350, 600, 80));

        tablaDetalles.setBackground(new java.awt.Color(51, 51, 51));
        tablaDetalles.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Activo", "Actividad", "Valor"
            }
        ));
        jScrollPane2.setViewportView(tablaDetalles);

        add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 440, 600, 130));

        jPanel5.setBackground(new java.awt.Color(51, 51, 51));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        labelTotal.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        labelTotal.setForeground(new java.awt.Color(255, 255, 255));
        labelTotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labelTotal.setText("Total: 0.00");
        jPanel5.add(labelTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 5, 200, 25));

        add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 575, 600, 35));
    }// </editor-fold>//GEN-END:initComponents

    public JButton getBtnAgregarDetalle() {
        return btnAgregarDetalle;
    }

    public void setBtnAgregarDetalle(JButton btnAgregarDetalle) {
        this.btnAgregarDetalle = btnAgregarDetalle;
    }

    public JButton getBtnBuscar() {
        return btnBuscar;
    }

    public void setBtnBuscar(JButton btnBuscar) {
        this.btnBuscar = btnBuscar;
    }

    public JButton getBtnEliminar() {
        return btnEliminar;
    }

    public void setBtnEliminar(JButton btnEliminar) {
        this.btnEliminar = btnEliminar;
    }

    public JButton getBtnGuardar() {
        return btnGuardar;
    }

    public void setBtnGuardar(JButton btnGuardar) {
        this.btnGuardar = btnGuardar;
    }

    public JButton getBtnLimpiar() {
        return btnLimpiar;
    }

    public void setBtnLimpiar(JButton btnLimpiar) {
        this.btnLimpiar = btnLimpiar;
    }

    public JButton getBtnModificar() {
        return btnModificar;
    }

    public void setBtnModificar(JButton btnModificar) {
        this.btnModificar = btnModificar;
    }

    public JButton getBtnQuitarDetalle() {
        return btnQuitarDetalle;
    }

    public void setBtnQuitarDetalle(JButton btnQuitarDetalle) {
        this.btnQuitarDetalle = btnQuitarDetalle;
    }

    public JComboBox<Activo> getComboActivo() {
        return comboActivo;
    }

    public void setComboActivo(JComboBox<Activo> comboActivo) {
        this.comboActivo = comboActivo;
    }

    public JComboBox<Actividad> getComboActividad() {
        return comboActividad;
    }

    public void setComboActividad(JComboBox<Actividad> comboActividad) {
        this.comboActividad = comboActividad;
    }

    public JTextField getInputBuscar() {
        return inputBuscar;
    }

    public void setInputBuscar(JTextField inputBuscar) {
        this.inputBuscar = inputBuscar;
    }

    public JTextField getInputNumero() {
        return inputNumero;
    }

    public void setInputNumero(JTextField inputNumero) {
        this.inputNumero = inputNumero;
    }

    public JTextField getInputResponsable() {
        return inputResponsable;
    }

    public void setInputResponsable(JTextField inputResponsable) {
        this.inputResponsable = inputResponsable;
    }

    public JTextField getInputValor() {
        return inputValor;
    }

    public void setInputValor(JTextField inputValor) {
        this.inputValor = inputValor;
    }

    public JLabel getLabelEstado() {
        return labelEstado;
    }

    public void setLabelEstado(JLabel labelEstado) {
        this.labelEstado = labelEstado;
    }

    public JLabel getLabelTotal() {
        return labelTotal;
    }

    public void setLabelTotal(JLabel labelTotal) {
        this.labelTotal = labelTotal;
    }

    public JSpinner getSpinnerFecha() {
        return spinnerFecha;
    }

    public void setSpinnerFecha(JSpinner spinnerFecha) {
        this.spinnerFecha = spinnerFecha;
    }

    public JTable getTablaCabeceras() {
        return tablaCabeceras;
    }

    public void setTablaCabeceras(JTable tablaCabeceras) {
        this.tablaCabeceras = tablaCabeceras;
    }

    public JTable getTablaDetalles() {
        return tablaDetalles;
    }

    public void setTablaDetalles(JTable tablaDetalles) {
        this.tablaDetalles = tablaDetalles;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregarDetalle;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnQuitarDetalle;
    private javax.swing.JComboBox<entidades.mantenimiento.Actividad> comboActividad;
    private javax.swing.JComboBox<entidades.mantenimiento.Activo> comboActivo;
    private javax.swing.JTextField inputBuscar;
    private javax.swing.JTextField inputNumero;
    private javax.swing.JTextField inputResponsable;
    private javax.swing.JTextField inputValor;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel labelEstado;
    private javax.swing.JLabel labelTotal;
    private javax.swing.JSpinner spinnerFecha;
    private javax.swing.JTable tablaCabeceras;
    private javax.swing.JTable tablaDetalles;
    // End of variables declaration//GEN-END:variables
}
