package presentacion.contabilidad;

import datos.contabilidad.DAOCuenta;
import datos.contabilidad.DAOTipoCuenta;
import entidades.contabilidad.Cuenta;
import entidades.contabilidad.TipoCuenta;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class FrmCuenta extends javax.swing.JFrame {

    private DAOCuenta dao = new DAOCuenta();
    private DAOTipoCuenta daoTipo = new DAOTipoCuenta();
    private DefaultTableModel modelo;

    public FrmCuenta() {
        initComponents();
        modelo = new DefaultTableModel(new String[]{"Código", "Nombre", "Tipo"}, 0);
        tblCuenta.setModel(modelo);
        cargarTipos();
        listar();
        setLocationRelativeTo(null);
    }

    private void cargarTipos() {
        cmbTipo.removeAllItems();
        for (TipoCuenta t : daoTipo.listar()) {
            cmbTipo.addItem(t.getCodigo() + " - " + t.getNombre());
        }
    }

    private TipoCuenta getTipoSeleccionado() {
        if (cmbTipo.getSelectedItem() == null) return null;
        String item = cmbTipo.getSelectedItem().toString();
        int codigo = Integer.parseInt(item.split(" - ")[0]);
        return daoTipo.buscarPorCodigo(codigo);
    }
    
    private boolean validarCodigoConTipo() {
        String codigo = txtCodigo.getText().trim();
        TipoCuenta tipo = getTipoSeleccionado();
        if (tipo == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un Tipo de Cuenta");
            return false;
        }
        if (codigo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Falta el Código");
            return false;
        }
        // 1xx = 1 Activo, 2xx = 2 Pasivo, 3xx = 3 Patrimonio, 4xx = 4 Ingresos, 5xx = 5 Gastos
        String primerDigito = codigo.substring(0, 1);
        String codigoTipo = String.valueOf(tipo.getCodigo());

        if (!primerDigito.equals(codigoTipo)) {
            JOptionPane.showMessageDialog(this,
                "❌ ERROR: Código " + codigo + " no coincide con el Tipo.\n\n" +
                "Elegiste: " + tipo.getCodigo() + " - " + tipo.getNombre() + "\n" +
                "Tu código debe empezar con " + codigoTipo + ".\n\n" +
                "Ej: 1-Activo -> 101, 102, 103\n" +
                "Ej: 2-Pasivo -> 201, 202, 203\n" +
                "Ej: 4-Ingresos -> 401, 402");
            return false;
        }
        return true;
    }

    private void seleccionarTipoEnCombo(int codigoTipo) {
        for (int i = 0; i < cmbTipo.getItemCount(); i++) {
            if (cmbTipo.getItemAt(i).startsWith(codigoTipo + " -")) {
                cmbTipo.setSelectedIndex(i);
                return;
            }
        }
    }

    private void listar() {
        modelo.setRowCount(0);
        for (Cuenta c : dao.listar()) {
            String nomTipo = (c.getTipoCuenta() != null) ? c.getTipoCuenta().getNombre() : "";
            modelo.addRow(new Object[]{c.getCodigo(), c.getNombre(), nomTipo});
        }
    }

    private void limpiar() {
        txtCodigo.setText("");
        txtNombre.setText("");
        txtBuscar.setText("");
        if (cmbTipo.getItemCount() > 0) cmbTipo.setSelectedIndex(0);
        txtCodigo.setEditable(true);
        txtCodigo.requestFocus();
    }

    private void cargarDesdeTabla() {
        int fila = tblCuenta.getSelectedRow();
        if (fila >= 0) {
            String codigo = modelo.getValueAt(fila, 0).toString();
            Cuenta c = dao.buscarPorCodigo(codigo);
            if (c != null) {
                txtCodigo.setText(c.getCodigo());
                txtNombre.setText(c.getNombre());
                if (c.getTipoCuenta() != null) seleccionarTipoEnCombo(c.getTipoCuenta().getCodigo());
                txtCodigo.setEditable(false);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        lblCodigo = new javax.swing.JLabel();
        lblNombre = new javax.swing.JLabel();
        lblTipo = new javax.swing.JLabel();
        lblBuscar = new javax.swing.JLabel();
        txtCodigo = new javax.swing.JTextField();
        txtNombre = new javax.swing.JTextField();
        txtBuscar = new javax.swing.JTextField();
        cmbTipo = new javax.swing.JComboBox<>();
        btnNuevo = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnBuscar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblCuenta = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Cuenta");

        lblCodigo.setText("Código:");
        lblNombre.setText("Nombre:");
        lblTipo.setText("Tipo Cuenta:");
        lblBuscar.setText("Buscar:");

        btnNuevo.setText("Nuevo");
        btnNuevo.addActionListener(e -> limpiar());

        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(e -> {
            try {
                if (txtCodigo.getText().trim().isEmpty() || txtNombre.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Llena código y nombre");
                    return;
                }
                if (!validarCodigoConTipo()) return;
                if (dao.buscarPorCodigo(txtCodigo.getText().trim()) != null) {
                    JOptionPane.showMessageDialog(this, "Ese código ya existe, usa Modificar");
                    return;
                }
                Cuenta c = new Cuenta();
                c.setCodigo(txtCodigo.getText().trim());
                c.setNombre(txtNombre.getText().trim());
                c.setTipoCuenta(getTipoSeleccionado());
                dao.insertar(c);
                JOptionPane.showMessageDialog(this, "Guardado correctamente");
                listar(); limpiar();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        btnModificar.setText("Modificar");
        btnModificar.addActionListener(e -> {
            try {
                if (txtCodigo.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Primero busca o selecciona una cuenta");
                    return;
                }
                if (!validarCodigoConTipo()) return;
                Cuenta c = new Cuenta();
                c.setCodigo(txtCodigo.getText().trim());
                c.setNombre(txtNombre.getText().trim());
                c.setTipoCuenta(getTipoSeleccionado());
                dao.modificar(c);
                JOptionPane.showMessageDialog(this, "Modificado correctamente");
                listar(); limpiar();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(e -> {
            try {
                if (txtCodigo.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Primero busca o selecciona una cuenta");
                    return;
                }
                int resp = JOptionPane.showConfirmDialog(this, "¿Eliminar la cuenta " + txtCodigo.getText() + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
                if (resp == JOptionPane.YES_OPTION) {
                    dao.eliminar(txtCodigo.getText().trim());
                    JOptionPane.showMessageDialog(this, "Eliminado correctamente");
                    listar(); limpiar();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(e -> {
            String texto = txtBuscar.getText().trim();
            if (texto.isEmpty()) { listar(); return; }
            // 1. Intenta por código exacto
            Cuenta c = dao.buscarPorCodigo(texto);
            modelo.setRowCount(0);
            if (c != null) {
                String nomTipo = (c.getTipoCuenta() != null) ? c.getTipoCuenta().getNombre() : "";
                modelo.addRow(new Object[]{c.getCodigo(), c.getNombre(), nomTipo});
                txtCodigo.setText(c.getCodigo());
                txtNombre.setText(c.getNombre());
                if (c.getTipoCuenta() != null) seleccionarTipoEnCombo(c.getTipoCuenta().getCodigo());
                txtCodigo.setEditable(false);
            } else {
                // 2. Si no es código, busca por nombre
                boolean encontro = false;
                for (Cuenta cu : dao.listar()) {
                    if (cu.getNombre().toUpperCase().contains(texto.toUpperCase())) {
                        String nomTipo = (cu.getTipoCuenta() != null) ? cu.getTipoCuenta().getNombre() : "";
                        modelo.addRow(new Object[]{cu.getCodigo(), cu.getNombre(), nomTipo});
                        encontro = true;
                    }
                }
                if (!encontro) JOptionPane.showMessageDialog(this, "No encontrado");
            }
        });

        btnLimpiar.setText("Limpiar");
        btnLimpiar.addActionListener(e -> { limpiar(); listar(); });

        tblCuenta.setModel(new DefaultTableModel(new Object[][]{}, new String[]{"Código", "Nombre", "Tipo"}));
        tblCuenta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) { cargarDesdeTabla(); }
        });
        jScrollPane1.setViewportView(tblCuenta);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 480, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblCodigo).addComponent(lblNombre).addComponent(lblTipo).addComponent(lblBuscar))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                    .addComponent(lblTipo).addComponent(cmbTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblBuscar).addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscar))
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNuevo).addComponent(btnGuardar).addComponent(btnModificar).addComponent(btnEliminar).addComponent(btnLimpiar))
                .addGap(15, 15, 15)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );
        pack();
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new FrmCuenta().setVisible(true));
    }

    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JComboBox<String> cmbTipo;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblBuscar;
    private javax.swing.JLabel lblCodigo;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JLabel lblTipo;
    private javax.swing.JTable tblCuenta;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JTextField txtCodigo;
    private javax.swing.JTextField txtNombre;
}
