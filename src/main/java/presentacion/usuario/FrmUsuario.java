package presentacion.usuario;

import bl.usuario.UsuarioBL;
import entidades.usuario.Usuario;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class FrmUsuario extends javax.swing.JFrame {

    UsuarioBL bl = new UsuarioBL();
    DefaultTableModel modelo;

    public FrmUsuario() {
        initComponents();
        setLocationRelativeTo(null);
        setTitle("Mantenimiento de Usuarios - Caty");
        cargarTabla();
        // Valores por defecto para que no te olvides
        cboRol.setSelectedItem("USUARIO");
        cboEstado.setSelectedItem("ACTIVO");
    }

    private void cargarTabla() {
        String[] columnas = {"ID", "Nombre", "Usuario", "Rol", "Estado"};
        modelo = new DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        List<Usuario> lista = bl.listar();
        for (Usuario u : lista) {
            Object[] fila = {u.getId_usuario(), u.getNombre(), u.getUsuario(), u.getRol(), u.getEstado()};
            modelo.addRow(fila);
        }
        tblUsuarios.setModel(modelo);
    }

    private void limpiar() {
        txtId.setText("");
        txtNombre.setText("");
        txtUsuario.setText("");
        txtClave.setText("");
        cboRol.setSelectedItem("USUARIO");
        cboEstado.setSelectedItem("ACTIVO");
        txtNombre.requestFocus();
    }

    private Usuario capturarDatos() {
        Usuario u = new Usuario();
        if (!txtId.getText().isEmpty()) {
            u.setId_usuario(Integer.parseInt(txtId.getText()));
        }
        u.setNombre(txtNombre.getText().trim());
        u.setUsuario(txtUsuario.getText().trim());
        u.setClave(new String(txtClave.getPassword()));
        u.setRol(cboRol.getSelectedItem().toString());
        u.setEstado(cboEstado.getSelectedItem().toString());
        return u;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        txtNombre = new javax.swing.JTextField();
        txtUsuario = new javax.swing.JTextField();
        txtClave = new javax.swing.JPasswordField();
        cboRol = new javax.swing.JComboBox<>();
        cboEstado = new javax.swing.JComboBox<>();
        btnGuardar = new javax.swing.JButton();
        btnBuscar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblUsuarios = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("MANTENIMIENTO DE USUARIOS");

        jLabel2.setText("ID:");

        jLabel3.setText("Nombre:");

        jLabel4.setText("Usuario:");

        jLabel5.setText("Clave:");

        jLabel6.setText("Rol / Estado:");

        txtId.setEditable(false);
        txtId.setBackground(new java.awt.Color(230, 230, 230));

        cboRol.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ADMIN", "USUARIO" }));

        cboEstado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ACTIVO", "INACTIVO" }));

        btnGuardar.setText("GUARDAR");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnBuscar.setText("BUSCAR");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        btnModificar.setText("MODIFICAR");
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });

        btnEliminar.setText("ELIMINAR");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        btnLimpiar.setText("LIMPIAR");
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });

        tblUsuarios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {"ID", "Nombre", "Usuario", "Rol", "Estado"}
        ));
        tblUsuarios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblUsuariosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblUsuarios);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNombre)
                            .addComponent(txtUsuario)
                            .addComponent(txtClave)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(cboRol, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(cboEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(30, 30, 30)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnBuscar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnModificar, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
                            .addComponent(btnEliminar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnLimpiar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jLabel1))
                .addContainerGap(30, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1)
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnGuardar))
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscar))
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnModificar))
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtClave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEliminar))
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(cboRol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLimpiar))
                .addGap(20, 20, 20)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>                        

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {                                           
        String msg = bl.guardar(capturarDatos());
        JOptionPane.showMessageDialog(this, msg);
        cargarTabla();
        limpiar();
    }                                          

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {                                          
        String usuarioBuscar = JOptionPane.showInputDialog(this, "Ingrese el usuario a buscar:");
        if (usuarioBuscar == null || usuarioBuscar.isEmpty()) return;
        // Usamos tu DAO a través del BL - buscamos en la lista
        for (Usuario u : bl.listar()) {
            if (u.getUsuario().equalsIgnoreCase(usuarioBuscar)) {
                txtId.setText(String.valueOf(u.getId_usuario()));
                txtNombre.setText(u.getNombre());
                txtUsuario.setText(u.getUsuario());
                txtClave.setText(u.getClave());
                cboRol.setSelectedItem(u.getRol());
                cboEstado.setSelectedItem(u.getEstado());
                JOptionPane.showMessageDialog(this, "Usuario encontrado");
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Usuario no encontrado");
    }                                         

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {                                             
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Primero busca o selecciona un usuario de la tabla");
            return;
        }
        String msg = bl.modificar(capturarDatos());
        JOptionPane.showMessageDialog(this, msg);
        cargarTabla();
        limpiar();
    }                                            

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {                                            
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Primero busca o selecciona un usuario");
            return;
        }
        int resp = JOptionPane.showConfirmDialog(this, "¿Seguro que desea eliminar?", "Eliminar", JOptionPane.YES_NO_OPTION);
        if (resp == JOptionPane.YES_OPTION) {
            String msg = bl.eliminar(Integer.parseInt(txtId.getText()));
            JOptionPane.showMessageDialog(this, msg);
            cargarTabla();
            limpiar();
        }
    }                                           

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {                                           
        limpiar();
    }                                          

    private void tblUsuariosMouseClicked(java.awt.event.MouseEvent evt) {                                         
        int fila = tblUsuarios.getSelectedRow();
        if (fila >= 0) {
            txtId.setText(tblUsuarios.getValueAt(fila, 0).toString());
            txtNombre.setText(tblUsuarios.getValueAt(fila, 1).toString());
            txtUsuario.setText(tblUsuarios.getValueAt(fila, 2).toString());
            cboRol.setSelectedItem(tblUsuarios.getValueAt(fila, 3).toString());
            cboEstado.setSelectedItem(tblUsuarios.getValueAt(fila, 4).toString());
            // Buscamos la clave para poder modificarla
            for (Usuario u : bl.listar()) {
                if (u.getId_usuario() == Integer.parseInt(txtId.getText())) {
                    txtClave.setText(u.getClave());
                    break;
                }
            }
        }
    }                                        

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmUsuario().setVisible(true);
            }
        });
    }

    //Declaracion de variables                  
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JComboBox<String> cboEstado;
    private javax.swing.JComboBox<String> cboRol;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblUsuarios;
    private javax.swing.JPasswordField txtClave;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtUsuario;                  
}
