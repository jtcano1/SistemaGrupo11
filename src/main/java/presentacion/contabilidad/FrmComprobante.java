package presentacion.contabilidad;

import datos.contabilidad.DAOCuenta;
import datos.contabilidad.DAOComprobante;
import entidades.contabilidad.CabeceraComprobante;
import entidades.contabilidad.Cuenta;
import entidades.contabilidad.DetalleComprobante;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class FrmComprobante extends javax.swing.JFrame {

    private DAOCuenta daoCuenta = new DAOCuenta();
    private DAOComprobante daoComp = new DAOComprobante();
    private DefaultTableModel modeloDet;
    private DefaultTableModel modeloCab;
    private int numeroEditando = 0; // 0 = nuevo, >0 = modificando

    public FrmComprobante() {
        initComponents();
        modeloDet = new DefaultTableModel(new String[]{"Código Cuenta", "Nombre Cuenta", "Debe", "Haber"}, 0);
        tblDetalle.setModel(modeloDet);
        modeloCab = new DefaultTableModel(new String[]{"N°", "Fecha", "Observaciones"}, 0);
        tblComprobantes.setModel(modeloCab);
        cargarCuentas();
        txtNumero.setText("AUTO");
        txtFecha.setText(new java.text.SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        listarComprobantes();
        setLocationRelativeTo(null);
    }

    private void cargarCuentas() {
        cmbCuenta.removeAllItems();
        for (Cuenta c : daoCuenta.listar()) {
            cmbCuenta.addItem(c.getCodigo() + " - " + c.getNombre());
        }
    }

    private void actualizarTotales() {
        double debe = 0, haber = 0;
        for (int i = 0; i < modeloDet.getRowCount(); i++) {
            debe += Double.parseDouble(modeloDet.getValueAt(i, 2).toString());
            haber += Double.parseDouble(modeloDet.getValueAt(i, 3).toString());
        }
        txtTotalDebe.setText(String.format("%.2f", debe));
        txtTotalHaber.setText(String.format("%.2f", haber));
        if (debe == haber && debe > 0) lblEstado.setText("✅ CUADRADO - Debe = Haber");
        else lblEstado.setText("⚠️ NO CUADRA - Debe debe ser igual a Haber");
    }

    private void listarComprobantes() {
        modeloCab.setRowCount(0);
        for (CabeceraComprobante c : daoComp.listar()) {
            modeloCab.addRow(new Object[]{c.getNumero(), c.getFecha(), c.getObservaciones()});
        }
    }

    private void limpiarTodo() {
        modeloDet.setRowCount(0);
        actualizarTotales();
        txtObservaciones.setText("");
        txtNumero.setText("AUTO");
        txtFecha.setText(new java.text.SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        numeroEditando = 0;
        txtDebe.setText("0");
        txtHaber.setText("0");
    }

    private List<DetalleComprobante> armarDetallesDesdeTabla() {
        List<DetalleComprobante> lista = new ArrayList<>();
        for (int i = 0; i < modeloDet.getRowCount(); i++) {
            DetalleComprobante d = new DetalleComprobante();
            Cuenta cu = new Cuenta();
            cu.setCodigo(modeloDet.getValueAt(i, 0).toString());
            cu.setNombre(modeloDet.getValueAt(i, 1).toString());
            d.setCuenta(cu);
            d.setDebe(Double.parseDouble(modeloDet.getValueAt(i, 2).toString()));
            d.setHaber(Double.parseDouble(modeloDet.getValueAt(i, 3).toString()));
            lista.add(d);
        }
        return lista;
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        lblNumero = new javax.swing.JLabel(); lblFecha = new javax.swing.JLabel(); lblObs = new javax.swing.JLabel();
        lblCuenta = new javax.swing.JLabel(); lblDebe = new javax.swing.JLabel(); lblHaber = new javax.swing.JLabel();
        lblTotalDebe = new javax.swing.JLabel(); lblTotalHaber = new javax.swing.JLabel(); lblEstado = new javax.swing.JLabel();
        lblBuscar = new javax.swing.JLabel();
        txtNumero = new javax.swing.JTextField(); txtFecha = new javax.swing.JTextField(); txtObservaciones = new javax.swing.JTextField();
        txtDebe = new javax.swing.JTextField(); txtHaber = new javax.swing.JTextField();
        txtTotalDebe = new javax.swing.JTextField(); txtTotalHaber = new javax.swing.JTextField(); txtBuscar = new javax.swing.JTextField();
        cmbCuenta = new javax.swing.JComboBox<>();
        btnAgregar = new javax.swing.JButton(); btnQuitar = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton(); btnModificar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton(); btnBuscar = new javax.swing.JButton(); btnNuevo = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane(); tblDetalle = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane(); tblComprobantes = new javax.swing.JTable();
        lblTitulo = new javax.swing.JLabel(); lblTitulo2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Comprobante Contable");

        lblTitulo.setText("COMPROBANTE CONTABLE - CABECERA + DETALLE");
        lblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 14));
        lblNumero.setText("N° Comprobante:"); lblFecha.setText("Fecha (dd/MM/yyyy):"); lblObs.setText("Observaciones:");
        lblCuenta.setText("Cuenta:"); lblDebe.setText("Debe:"); lblHaber.setText("Haber:");
        lblTotalDebe.setText("Total Debe:"); lblTotalHaber.setText("Total Haber:");
        lblEstado.setText("Agrega líneas al detalle");
        lblBuscar.setText("Buscar N°:");
        txtNumero.setEditable(false); txtTotalDebe.setEditable(false); txtTotalHaber.setEditable(false);
        txtDebe.setText("0"); txtHaber.setText("0");
        lblTitulo2.setText("Comprobantes Guardados - Haz clic en uno para editarlo:");

        btnAgregar.setText("Agregar Línea");
        btnAgregar.addActionListener(e -> {
            try {
                if (cmbCuenta.getSelectedItem() == null) { JOptionPane.showMessageDialog(this, "No hay cuentas, crea cuentas primero"); return; }
                double debe = Double.parseDouble(txtDebe.getText().trim());
                double haber = Double.parseDouble(txtHaber.getText().trim());
                if (debe == 0 && haber == 0) { JOptionPane.showMessageDialog(this, "Debe o Haber debe ser mayor a 0"); return; }
                if (debe > 0 && haber > 0) { JOptionPane.showMessageDialog(this, "Una línea es DEBE o HABER, no los dos"); return; }
                String item = cmbCuenta.getSelectedItem().toString();
                String codigo = item.split(" - ")[0];
                String nombre = item.substring(item.indexOf(" - ") + 3);
                modeloDet.addRow(new Object[]{codigo, nombre, debe, haber});
                actualizarTotales();
                txtDebe.setText("0"); txtHaber.setText("0");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error en montos: " + ex.getMessage()); }
        });

        btnQuitar.setText("Quitar Línea");
        btnQuitar.addActionListener(e -> {
            int fila = tblDetalle.getSelectedRow();
            if (fila >= 0) { modeloDet.removeRow(fila); actualizarTotales(); }
            else JOptionPane.showMessageDialog(this, "Selecciona una línea para quitar");
        });

        btnGuardar.setText("GUARDAR");
        btnGuardar.addActionListener(e -> guardar(false));

        btnModificar.setText("Modificar");
        btnModificar.addActionListener(e -> guardar(true));

        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(e -> {
            if (numeroEditando == 0) { JOptionPane.showMessageDialog(this, "Primero busca o selecciona un comprobante para eliminar"); return; }
            int resp = JOptionPane.showConfirmDialog(this, "¿Eliminar comprobante N° " + numeroEditando + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (resp == JOptionPane.YES_OPTION) {
                if (daoComp.eliminar(numeroEditando)) {
                    JOptionPane.showMessageDialog(this, "Eliminado correctamente");
                    limpiarTodo(); listarComprobantes();
                } else JOptionPane.showMessageDialog(this, "Error al eliminar");
            }
        });

        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(e -> {
            try {
                if (txtBuscar.getText().trim().isEmpty()) { listarComprobantes(); return; }
                int num = Integer.parseInt(txtBuscar.getText().trim());
                CabeceraComprobante cab = daoComp.buscarPorNumero(num);
                if (cab == null) { JOptionPane.showMessageDialog(this, "No encontrado"); return; }
                cargarCabecera(cab);
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Escribe un número válido"); }
        });

        btnNuevo.setText("Nuevo / Limpiar");
        btnNuevo.addActionListener(e -> limpiarTodo());

        tblDetalle.setModel(new DefaultTableModel(new Object[][]{}, new String[]{"Código", "Cuenta", "Debe", "Haber"}));
        jScrollPane1.setViewportView(tblDetalle);

        tblComprobantes.setModel(new DefaultTableModel(new Object[][]{}, new String[]{"N°", "Fecha", "Observaciones"}));
        tblComprobantes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int fila = tblComprobantes.getSelectedRow();
                if (fila >= 0) {
                    int num = Integer.parseInt(modeloCab.getValueAt(fila, 0).toString());
                    CabeceraComprobante cab = daoComp.buscarPorNumero(num);
                    if (cab != null) cargarCabecera(cab);
                }
            }
        });
        jScrollPane2.setViewportView(tblComprobantes);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup().addGap(20,20,20).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addComponent(lblTitulo)
                .addGroup(layout.createSequentialGroup().addComponent(lblNumero).addGap(10,10,10).addComponent(txtNumero, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(15,15,15).addComponent(lblFecha).addGap(10,10,10).addComponent(txtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(15,15,15).addComponent(lblBuscar).addGap(5,5,5).addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(5,5,5).addComponent(btnBuscar))
                .addGroup(layout.createSequentialGroup().addComponent(lblObs).addGap(10,10,10).addComponent(txtObservaciones, javax.swing.GroupLayout.PREFERRED_SIZE, 420, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createSequentialGroup().addComponent(lblCuenta).addGap(10,10,10).addComponent(cmbCuenta, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(10,10,10).addComponent(lblDebe).addGap(5,5,5).addComponent(txtDebe, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(10,10,10).addComponent(lblHaber).addGap(5,5,5).addComponent(txtHaber, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createSequentialGroup().addComponent(btnAgregar).addGap(10,10,10).addComponent(btnQuitar).addGap(10,10,10).addComponent(btnNuevo))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 580, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createSequentialGroup().addComponent(lblTotalDebe).addGap(10,10,10).addComponent(txtTotalDebe, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(20,20,20).addComponent(lblTotalHaber).addGap(10,10,10).addComponent(txtTotalHaber, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addComponent(lblEstado)
                .addGroup(layout.createSequentialGroup().addComponent(btnGuardar).addGap(10,10,10).addComponent(btnModificar).addGap(10,10,10).addComponent(btnEliminar))
                .addComponent(lblTitulo2)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 580, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addContainerGap(20, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup().addGap(15,15,15).addComponent(lblTitulo).addGap(10,10,10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(lblNumero).addComponent(txtNumero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(lblFecha).addComponent(txtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(lblBuscar).addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(btnBuscar)).addGap(10,10,10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(lblObs).addComponent(txtObservaciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(10,10,10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(lblCuenta).addComponent(cmbCuenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(lblDebe).addComponent(txtDebe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(lblHaber).addComponent(txtHaber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(10,10,10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(btnAgregar).addComponent(btnQuitar).addComponent(btnNuevo)).addGap(10,10,10)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(10,10,10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(lblTotalDebe).addComponent(txtTotalDebe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(lblTotalHaber).addComponent(txtTotalHaber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(5,5,5)
                .addComponent(lblEstado).addGap(10,10,10).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(btnGuardar).addComponent(btnModificar).addComponent(btnEliminar)).addGap(15,15,15)
                .addComponent(lblTitulo2).addGap(5,5,5).addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(20, Short.MAX_VALUE))
        );
        pack();
    }

    private void cargarCabecera(CabeceraComprobante cab) {
        numeroEditando = cab.getNumero();
        txtNumero.setText(String.valueOf(cab.getNumero()));
        txtFecha.setText(new java.text.SimpleDateFormat("dd/MM/yyyy").format(cab.getFecha()));
        txtObservaciones.setText(cab.getObservaciones());
        modeloDet.setRowCount(0);
        for (DetalleComprobante d : cab.getDetalles()) {
            modeloDet.addRow(new Object[]{d.getCuenta().getCodigo(), d.getCuenta().getNombre(), d.getDebe(), d.getHaber()});
        }
        actualizarTotales();
    }

    private void guardar(boolean esModificar) {
        try {
            if (modeloDet.getRowCount() < 2) { JOptionPane.showMessageDialog(this, "Mínimo 2 líneas: una al Debe y otra al Haber"); return; }
            if (txtObservaciones.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(this, "Falta Observaciones"); return; }
            double debe = Double.parseDouble(txtTotalDebe.getText());
            double haber = Double.parseDouble(txtTotalHaber.getText());
            if (debe != haber) { JOptionPane.showMessageDialog(this, "NO CUADRA: Debe (" + debe + ") debe ser igual a Haber (" + haber + ")"); return; }

            CabeceraComprobante cab = new CabeceraComprobante();
            if (esModificar) {
                if (numeroEditando == 0) { JOptionPane.showMessageDialog(this, "Primero busca o selecciona un comprobante para modificar"); return; }
                cab.setNumero(numeroEditando);
            }
            cab.setFecha(new java.text.SimpleDateFormat("dd/MM/yyyy").parse(txtFecha.getText().trim()));
            cab.setObservaciones(txtObservaciones.getText().trim());
            cab.setDetalles(armarDetallesDesdeTabla());

            boolean ok = esModificar ? daoComp.modificar(cab) : daoComp.insertar(cab);
            if (ok) {
                JOptionPane.showMessageDialog(this, esModificar ? "✅ Modificado" : "✅ Comprobante guardado - N° " + cab.getNumero());
                limpiarTodo(); listarComprobantes();
            } else JOptionPane.showMessageDialog(this, "Error al guardar");
        } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }
    }

    public static void main(String args[]) { java.awt.EventQueue.invokeLater(() -> new FrmComprobante().setVisible(true)); }

    private javax.swing.JButton btnAgregar; private javax.swing.JButton btnBuscar; private javax.swing.JButton btnEliminar; private javax.swing.JButton btnGuardar; private javax.swing.JButton btnModificar; private javax.swing.JButton btnNuevo; private javax.swing.JButton btnQuitar;
    private javax.swing.JComboBox<String> cmbCuenta; private javax.swing.JScrollPane jScrollPane1; private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblBuscar; private javax.swing.JLabel lblCuenta; private javax.swing.JLabel lblDebe; private javax.swing.JLabel lblEstado; private javax.swing.JLabel lblFecha; private javax.swing.JLabel lblHaber; private javax.swing.JLabel lblNumero; private javax.swing.JLabel lblObs; private javax.swing.JLabel lblTitulo; private javax.swing.JLabel lblTitulo2; private javax.swing.JLabel lblTotalDebe; private javax.swing.JLabel lblTotalHaber;
    private javax.swing.JTable tblComprobantes; private javax.swing.JTable tblDetalle;
    private javax.swing.JTextField txtBuscar; private javax.swing.JTextField txtDebe; private javax.swing.JTextField txtFecha; private javax.swing.JTextField txtHaber; private javax.swing.JTextField txtNumero; private javax.swing.JTextField txtObservaciones; private javax.swing.JTextField txtTotalDebe; private javax.swing.JTextField txtTotalHaber;
}