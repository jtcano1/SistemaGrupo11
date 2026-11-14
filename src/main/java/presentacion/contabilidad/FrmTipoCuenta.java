/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion.contabilidad;

/**
 *
 * @author USER
 */

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
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        lblCodigo = new javax.swing.JLabel();
        lblNombre = new javax.swing.JLabel();
        txtCodigo = new javax.swing.JTextField();
        txtNombre = new javax.swing.JTextField();
        btnGuardar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblTipoCuenta = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Tipo de Cuenta");

        lblCodigo.setText("Código:");
        lblNombre.setText("Nombre:");

        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(e -> {
            try {
                TipoCuenta t = new TipoCuenta(
                    Integer.parseInt(txtCodigo.getText()),
                    txtNombre.getText()
                );
                dao.guardar(t);
                JOptionPane.showMessageDialog(this, "Guardado correctamente");
                listar();
                limpiar();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        btnLimpiar.setText("Limpiar");
        btnLimpiar.addActionListener(e -> limpiar());

        tblTipoCuenta.setModel(new javax.swing.table.DefaultTableModel(
            new Object[][]{}, new String[]{"Código", "Nombre"}
        ));
        jScrollPane1.setViewportView(tblTipoCuenta);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblCodigo)
                            .addComponent(lblNombre))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnGuardar)
                        .addGap(10, 10, 10)
                        .addComponent(btnLimpiar)))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCodigo)
                    .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNombre)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGuardar)
                    .addComponent(btnLimpiar))
                .addGap(15, 15, 15)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );
        pack();
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new FrmTipoCuenta().setVisible(true));
    }

    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblCodigo;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JTable tblTipoCuenta;
    private javax.swing.JTextField txtCodigo;
    private javax.swing.JTextField txtNombre;
}
