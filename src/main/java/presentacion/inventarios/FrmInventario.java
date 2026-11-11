
package presentacion.inventarios;

import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import negocio.inventarios.ArticuloBL;
import negocio.inventarios.TipoMovimientoBL;
import entidades.inventarios.Articulo;
import entidades.inventarios.TipoMovimiento;
import java.util.List;
import javax.swing.JOptionPane;


public class FrmInventario extends javax.swing.JFrame {
        private DefaultTableModel modeloDetalle;
        private final ArticuloBL articuloBL = new ArticuloBL();
        private final TipoMovimientoBL tipoMovimientoBL = new TipoMovimientoBL();

        private List<Articulo> listaArticulos;
        private List<TipoMovimiento> listaTiposMovimiento;

    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrmInventario.class.getName());

    /**
     * Creates new form FrmInventario
     */

public FrmInventario() {

    initComponents();
    configurarFormulario();
    cargarDatosIniciales();

    // Mostrar el precio al seleccionar un artículo
    cmbArticulo.addActionListener(
        evt -> cargarPrecioArticulo()
    );
}


//Carga los artículos y tipos de movimiento desde Oracle.

private void cargarDatosIniciales() {

    try {
        listaTiposMovimiento = tipoMovimientoBL.listar();
        cmbTipoMovimiento.removeAllItems();
        for (TipoMovimiento tipo : listaTiposMovimiento) {
            cmbTipoMovimiento.addItem(tipo.getNombre());
        }
        // Cargar artículos
        listaArticulos = articuloBL.listar();
        cmbArticulo.removeAllItems();
        for (Articulo articulo : listaArticulos) {
            cmbArticulo.addItem(articulo.getNombre());
        }

        // Dejar los ComboBox sin selección inicial
        cmbTipoMovimiento.setSelectedIndex(-1);
        cmbArticulo.setSelectedIndex(-1);

        if (listaArticulos.isEmpty()) {
            System.out.println("No hay artículos registrados.");
        }

        if (listaTiposMovimiento.isEmpty()) {
            System.out.println("No hay tipos de movimiento registrados.");
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(
            this,
            "Error al cargar los datos iniciales:\n" + e.getMessage(),
            "Error de conexión",
            JOptionPane.ERROR_MESSAGE
        );
    }
}

/**
 * Muestra el precio del artículo seleccionado.
 */
private void cargarPrecioArticulo() {

    int indice = cmbArticulo.getSelectedIndex();
    if (indice >= 0 && listaArticulos != null
            && indice < listaArticulos.size()) {
        Articulo articulo = listaArticulos.get(indice);

        txtPrecio.setText(String.format(
                java.util.Locale.US,"%.2f",articulo.getPrecio())
        );

    } else {
        txtPrecio.setText("");
    }
}
  

    //Configura los elementos iniciales del formulario.
    private void configurarFormulario() {

        // Configurar campos que no deben modificarse manualmente
        txtNumero.setEditable(false);
        txtTotal.setEditable(false);
        txtIVA.setEditable(false);
        txtTotalPagar.setEditable(false);

        // Establecer valores iniciales
        txtNumero.setText("");
        txtTotal.setText("0.00");
        txtIVA.setText("0.00");
        txtTotalPagar.setText("0.00");

        // Establecer fecha actual
        txtFecha.setText(
            LocalDate.now().format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy")
            )
        );

        // Limpiar elementos provisionales de los ComboBox
        cmbTipoMovimiento.removeAllItems();
        cmbArticulo.removeAllItems();

        // Crear estructura de la tabla de detalles
        modeloDetalle = new DefaultTableModel(
            new Object[]{
                "Código",
                "Artículo",
                "Cantidad",
                "Precio",
                "Subtotal"
            },
            0
        ) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        // Asignar el modelo a la tabla
        tblDetalle.setModel(modeloDetalle);

        // Selección de filas completas
        tblDetalle.setSelectionMode(
            javax.swing.ListSelectionModel.SINGLE_SELECTION
        );
    }


    //Limpia los campos para registrar un nuevo comprobante.

    private void limpiarFormulario() {

        // Limpiar número
        txtNumero.setText("");
        txtFecha.setText(
            LocalDate.now().format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy")
            )
        );

        // Reiniciar selecciones
        cmbTipoMovimiento.setSelectedIndex(-1);
        cmbArticulo.setSelectedIndex(-1);

        // Limpiar campos del detalle
        txtCantidad.setText("");
        txtPrecio.setText("");

        // Eliminar todas las filas de la tabla
        modeloDetalle.setRowCount(0);

        // Reiniciar valores económicos
        txtTotal.setText("0.00");
        txtIVA.setText("0.00");
        txtTotalPagar.setText("0.00");

        // Posicionar el cursor en el tipo de movimiento
        cmbTipoMovimiento.requestFocus();
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        lblNumero = new javax.swing.JLabel();
        lblFecha = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        btnAgregarDetalle = new javax.swing.JButton();
        btnNuevo = new javax.swing.JButton();
        btnGuardarComprobante = new javax.swing.JButton();
        btnBuscar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        txtNumero = new javax.swing.JTextField();
        txtFecha = new javax.swing.JTextField();
        txtCantidad = new javax.swing.JTextField();
        txtPrecio = new javax.swing.JTextField();
        cmbTipoMovimiento = new javax.swing.JComboBox<>();
        cmbArticulo = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDetalle = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtTotal = new javax.swing.JTextField();
        txtIVA = new javax.swing.JTextField();
        txtTotalPagar = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(204, 255, 255));

        lblTitulo.setText("GESTIÓN INVENTARIO");

        lblNumero.setText("Número:");

        lblFecha.setText("Fecha:");

        jLabel3.setText("Tipo Movimiento:");

        jLabel4.setText("Artículo:");

        jLabel5.setText("Cantidad:");

        jLabel6.setText("Precio:");

        btnAgregarDetalle.setText("Agregar");

        btnNuevo.setText("Nuevo");
        btnNuevo.addActionListener(this::btnNuevoActionPerformed);

        btnGuardarComprobante.setText("Guardar");

        btnBuscar.setText("Buscar");

        btnModificar.setText("Modificar");

        btnEliminar.setText("Eliminar");

        txtNumero.setEditable(false);

        cmbTipoMovimiento.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cmbArticulo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        tblDetalle.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Código", "Artículo", "Cantidad", "Precio", "Subtotal"
            }
        ));
        jScrollPane1.setViewportView(tblDetalle);

        jLabel1.setText("Total:");

        jLabel2.setText("IVA:");

        jLabel7.setText("Total a pagar:");

        txtTotal.setEditable(false);

        txtIVA.setEditable(false);

        txtTotalPagar.setEditable(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(279, 279, 279)
                                .addComponent(lblTitulo))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(41, 41, 41)
                                .addComponent(btnNuevo)
                                .addGap(63, 63, 63)
                                .addComponent(btnGuardarComprobante)
                                .addGap(58, 58, 58)
                                .addComponent(btnBuscar))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(131, 131, 131)
                                .addComponent(btnModificar)
                                .addGap(39, 39, 39)
                                .addComponent(btnEliminar)))
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel3)
                                    .addGap(36, 36, 36)
                                    .addComponent(cmbTipoMovimiento, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel4)
                                        .addComponent(jLabel5))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(btnAgregarDetalle, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(cmbArticulo, 0, 105, Short.MAX_VALUE)
                                            .addComponent(txtCantidad)
                                            .addComponent(txtPrecio)))))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                    .addComponent(lblNumero)
                                    .addGap(83, 83, 83)
                                    .addComponent(txtNumero))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                    .addComponent(lblFecha)
                                    .addGap(96, 96, 96)
                                    .addComponent(txtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(279, 279, 279)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel1))
                                .addGap(59, 59, 59)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtTotal)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(txtTotalPagar, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addComponent(txtIVA))
                                .addGap(82, 82, 82))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(77, 77, 77)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(lblTitulo)
                .addGap(29, 29, 29)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtIVA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(9, 9, 9)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtTotalPagar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblNumero)
                            .addComponent(txtNumero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblFecha)
                            .addComponent(txtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(cmbTipoMovimiento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(29, 29, 29)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(cmbArticulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(9, 9, 9)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(txtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(28, 28, 28)
                        .addComponent(btnAgregarDetalle, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnGuardarComprobante)
                            .addComponent(btnNuevo)
                            .addComponent(btnBuscar))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnModificar)
                            .addComponent(btnEliminar))))
                .addGap(21, 21, 21))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 824, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(40, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(28, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        // TODO add your handling code here:
        limpiarFormulario();
    }//GEN-LAST:event_btnNuevoActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new FrmInventario().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregarDetalle;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardarComprobante;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JComboBox<String> cmbArticulo;
    private javax.swing.JComboBox<String> cmbTipoMovimiento;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblFecha;
    private javax.swing.JLabel lblNumero;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JTable tblDetalle;
    private javax.swing.JTextField txtCantidad;
    private javax.swing.JTextField txtFecha;
    private javax.swing.JTextField txtIVA;
    private javax.swing.JTextField txtNumero;
    private javax.swing.JTextField txtPrecio;
    private javax.swing.JTextField txtTotal;
    private javax.swing.JTextField txtTotalPagar;
    // End of variables declaration//GEN-END:variables
}
