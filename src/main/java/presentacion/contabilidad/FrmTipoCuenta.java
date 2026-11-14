package presentacion.contabilidad;

import entidades.contabilidad.TipoCuenta;
import datos.contabilidad.DAOTipoCuenta;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class FrmTipoCuenta extends javax.swing.JFrame {

    private DAOTipoCuenta dao = new DAOTipoCuenta();
    private DefaultTableModel modelo;

    public FrmTipoCuenta() {
        initComponents();
        modelo = new DefaultTableModel(new String[]{"Código", "Nombre"}, 0);
        tblTipoCuenta.setModel(modelo);
        listar();
        setLocationRelativeTo(null);
    }

    private void listar() {
        modelo.setRowCount(0);
        for (TipoCuenta t : dao.listar()) {
            modelo.addRow(new Object[]{t.getCodigo(), t.getNombre()});
        }
    }

    private void limpiar() {
        txtCodigo.setText("");
        txtNombre.setText("");
        txtBuscar.setText("");
        txtCodigo.setEditable(true);
        txtCodigo.requestFocus();
    }

    private void cargarDesdeTabla() {
        int fila = tblTipoCuenta.getSelectedRow();
        if (fila >= 0) {
            txtCodigo.setText(modelo.getValueAt(fila, 0).toString());
            txtNombre.setText(modelo.getValueAt(fila, 1).toString());
            txtCodigo.setEditable(false); // no dejar cambiar PK al modificar
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        lblCodigo = new javax.swing.JLabel();
        lblNombre = new javax.swing.JLabel();
        lblBuscar = new javax.swing.JLabel();
        txtCodigo = new javax.swing.JTextField();
        txtNombre = new javax.swing.JTextField();
        txtBuscar = new javax.swing.JTextField();
        btnNuevo = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnBuscar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblTipoCuenta = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Tipo de Cuenta");

        lblCodigo.setText("Código:");
        lblNombre.setText("Nombre:");
        lblBuscar.setText("Buscar:");

        // --- NUEVO ---
        btnNuevo.setText("Nuevo");
        btnNuevo.addActionListener(e -> limpiar());

        // --- GUARDAR / INSERTAR ---
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(e -> {
            try {
                if (txtCodigo.getText().trim().isEmpty() || txtNombre.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Llena código y nombre");
                    return;
                }
                TipoCuenta t = new TipoCuenta(Integer.parseInt(txtCodigo.getText()), txtNombre.getText());
                if (dao.buscarPorCodigo(t.getCodigo()) != null) {
                    JOptionPane.showMessageDialog(this, "Ese código ya existe, usa Modificar");
                    return;
                }
                dao.guardar(t);
                JOptionPane.showMessageDialog(this, "Guardado correctamente");
                listar(); limpiar();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        // --- MODIFICAR ---
        btnModificar.setText("Modificar");
        btnModificar.addActionListener(e -> {
            try {
                if (txtCodigo.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Primero busca o selecciona un registro");
                    return;
                }
                TipoCuenta t = new TipoCuenta(Integer.parseInt(txtCodigo.getText()), txtNombre.getText());
                dao.modificar(t);
                JOptionPane.showMessageDialog(this, "Modificado correctamente");
                listar(); limpiar();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        // --- ELIMINAR ---
        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(e -> {
            try {
                if (txtCodigo.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Primero busca o selecciona un registro");
                    return;
                }
                int resp = JOptionPane.showConfirmDialog(this, "¿Eliminar el Tipo Cuenta " + txtCodigo.getText() + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
                if (resp == JOptionPane.YES_OPTION) {
                    dao.eliminar(Integer.parseInt(txtCodigo.getText()));
                    JOptionPane.showMessageDialog(this, "Eliminado correctamente");
                    listar(); limpiar();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        // --- BUSCAR ---
        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(e -> {
            String texto = txtBuscar.getText().trim();
            if (texto.isEmpty()) { listar(); return; }
            modelo.setRowCount(0);
            try {
                // Si es número, busca por código
                int codigo = Integer.parseInt(texto);
                TipoCuenta t = dao.buscarPorCodigo(codigo);
                if (t != null) {
                    modelo.addRow(new Object[]{t.getCodigo(), t.getNombre()});
                    txtCodigo.setText(String.valueOf(t.getCodigo()));
                    txtNombre.setText(t.getNombre());
                    txtCodigo.setEditable(false);
                } else {
                    JOptionPane.showMessageDialog(this, "No encontrado");
                }
            } catch (NumberFormatException nfe) {
                // Si es texto, busca por nombre
                for (TipoCuenta t : dao.buscarPorNombre(texto)) {
                    modelo.addRow(new Object[]{t.getCodigo(), t.getNombre()});
                }
                if (modelo.getRowCount() == 0) JOptionPane.showMessageDialog(this, "No encontrado");
            }
        });

        btnLimpiar.setText("Limpiar");
        btnLimpiar.addActionListener(e -> { limpiar(); listar(); });

        tblTipoCuenta.setModel(new DefaultTableModel(new Object[][]{}, new String[]{"Código", "Nombre"}));
        tblTipoCuenta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) { cargarDesdeTabla(); }
        });
        jScrollPane1.setViewportView(tblTipoCuenta);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 430, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblCodigo).addComponent(lblNombre).addComponent(lblBuscar))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(btnBuscar))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnNuevo).addGap(5, 5, 5)
                        .addComponent(btnGuardar).addGap(5, 5, 5)
                        .addComponent(btnModificar).addGap(5, 5, 5)
                        .addComponent(btnEliminar).addGap(5, 5, 5)
                        .addComponent(btnLimpiar)))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCodigo).addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNombre).addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblBuscar).addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscar))
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNuevo).addComponent(btnGuardar).addComponent(btnModificar).addComponent(btnEliminar).addComponent(btnLimpiar))
                .addGap(15, 15, 15)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );
        pack();
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new FrmTipoCuenta().setVisible(true));
    }

    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblBuscar;
    private javax.swing.JLabel lblCodigo;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JTable tblTipoCuenta;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JTextField txtCodigo;
    private javax.swing.JTextField txtNombre;
}