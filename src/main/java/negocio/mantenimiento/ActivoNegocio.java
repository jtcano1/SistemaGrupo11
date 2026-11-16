package negocio.mantenimiento;

import datos.mantenimiento.ActivoDAO;
import entidades.mantenimiento.Activo;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import presentacion.mantenimiento.PanelActivo;

public class ActivoNegocio implements ActionListener, ListSelectionListener {

    private static final Color COLOR_OK = new Color(120, 200, 120);
    private static final Color COLOR_ERROR = new Color(230, 110, 110);

    private final PanelActivo vista;
    private final ActivoDAO dao = new ActivoDAO();
    private final SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
    private final DefaultTableModel modelo = new DefaultTableModel();
    private List<Activo> activos = new ArrayList<>();
    private boolean cargando = false;

    public ActivoNegocio(PanelActivo vista) {
        this.vista = vista;
        configurarVista();
        registrarEventos();
        listar();
    }

    private void configurarVista() {
        modelo.addColumn("Código");
        modelo.addColumn("Nombre");
        modelo.addColumn("Fecha de compra");
        vista.getTablaActivo().setModel(modelo);
        vista.getTablaActivo().setDefaultEditor(Object.class, null);
        vista.getTablaActivo().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        vista.getTablaActivo().setForeground(Color.WHITE);
        vista.getInputCodigo().setEditable(false);

        JSpinner spinner = vista.getSpinnerFechaCompra();
        spinner.setEditor(new JSpinner.DateEditor(spinner, "dd/MM/yyyy"));
        spinner.setValue(new Date());

        vista.getLabelEstado().setText("");
    }

    private void registrarEventos() {
        vista.getBtnGuardar().addActionListener(this);
        vista.getBtnModificar().addActionListener(this);
        vista.getBtnEliminar().addActionListener(this);
        vista.getBtnLimpiar().addActionListener(this);
        vista.getBtnBuscar().addActionListener(this);
        vista.getInputBuscar().addActionListener(this);
        vista.getTablaActivo().getSelectionModel().addListSelectionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object origen = e.getSource();
        if (origen == vista.getBtnGuardar()) {
            guardar();
        } else if (origen == vista.getBtnModificar()) {
            modificar();
        } else if (origen == vista.getBtnEliminar()) {
            eliminar();
        } else if (origen == vista.getBtnLimpiar()) {
            limpiar();
        } else if (origen == vista.getBtnBuscar() || origen == vista.getInputBuscar()) {
            listar();
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting() && !cargando) {
            seleccionar();
        }
    }

    private void listar() {
        cargando = true;
        modelo.setRowCount(0);
        activos = dao.listar(vista.getInputBuscar().getText());
        for (Activo a : activos) {
            String fecha = "";
            if (a.getFechaCompra() != null) {
                fecha = formato.format(a.getFechaCompra());
            }
            modelo.addRow(new Object[]{a.getCodigo(), a.getNombre(), fecha});
        }
        cargando = false;
        mostrarMensaje(activos.size() + " activo(s) encontrado(s)", false);
    }

    private void seleccionar() {
        int fila = vista.getTablaActivo().getSelectedRow();
        if (fila < 0 || fila >= activos.size()) {
            return;
        }
        Activo a = activos.get(fila);
        vista.getInputCodigo().setText(String.valueOf(a.getCodigo()));
        vista.getInputNombre().setText(a.getNombre());
        if (a.getFechaCompra() != null) {
            vista.getSpinnerFechaCompra().setValue(a.getFechaCompra());
        }
    }

    private Activo leerFormulario() {
        Activo a = new Activo();
        a.setNombre(Validacion.texto(vista.getInputNombre().getText(), "Nombre"));
        Date fecha = (Date) vista.getSpinnerFechaCompra().getValue();
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha de compra es obligatoria");
        }
        if (fecha.after(new Date())) {
            throw new IllegalArgumentException("La fecha de compra no puede ser futura");
        }
        a.setFechaCompra(fecha);
        return a;
    }

    private void guardar() {
        if (!vista.getInputCodigo().getText().isEmpty()) {
            mostrarMensaje("El activo ya existe, use Modificar o Limpiar", true);
            return;
        }
        try {
            Activo a = leerFormulario();
            if (dao.guardar(a)) {
                limpiar();
                listar();
                mostrarMensaje("Activo guardado", false);
            } else {
                mostrarMensaje("No se pudo guardar el activo", true);
            }
        } catch (IllegalArgumentException ex) {
            mostrarMensaje(ex.getMessage(), true);
        }
    }

    private void modificar() {
        if (vista.getInputCodigo().getText().isEmpty()) {
            mostrarMensaje("Seleccione un activo de la tabla", true);
            return;
        }
        try {
            Activo a = leerFormulario();
            a.setCodigo(Integer.parseInt(vista.getInputCodigo().getText()));
            if (dao.modificar(a)) {
                limpiar();
                listar();
                mostrarMensaje("Activo modificado", false);
            } else {
                mostrarMensaje("No se pudo modificar el activo", true);
            }
        } catch (IllegalArgumentException ex) {
            mostrarMensaje(ex.getMessage(), true);
        }
    }

    private void eliminar() {
        if (vista.getInputCodigo().getText().isEmpty()) {
            mostrarMensaje("Seleccione un activo de la tabla", true);
            return;
        }
        int opcion = JOptionPane.showConfirmDialog(vista,
                "¿Eliminar el activo seleccionado?", "Confirmar",
                JOptionPane.YES_NO_OPTION);
        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }
        int codigo = Integer.parseInt(vista.getInputCodigo().getText());
        if (dao.eliminar(codigo)) {
            limpiar();
            listar();
            mostrarMensaje("Activo eliminado", false);
        } else {
            mostrarMensaje("No se pudo eliminar, puede tener mantenimientos registrados", true);
        }
    }

    private void limpiar() {
        vista.getTablaActivo().clearSelection();
        vista.getInputCodigo().setText("");
        vista.getInputNombre().setText("");
        vista.getSpinnerFechaCompra().setValue(new Date());
        vista.getInputNombre().requestFocus();
        vista.getLabelEstado().setText("");
    }

    private void mostrarMensaje(String mensaje, boolean error) {
        if (error) {
            vista.getLabelEstado().setForeground(COLOR_ERROR);
        } else {
            vista.getLabelEstado().setForeground(COLOR_OK);
        }
        vista.getLabelEstado().setText(mensaje);
    }
}
