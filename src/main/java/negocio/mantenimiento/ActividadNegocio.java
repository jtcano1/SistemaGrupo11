package negocio.mantenimiento;

import datos.mantenimiento.ActividadDAO;
import entidades.mantenimiento.Actividad;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import presentacion.mantenimiento.PanelActividad;

public class ActividadNegocio implements ActionListener, ListSelectionListener {

    private static final Color COLOR_OK = new Color(120, 200, 120);
    private static final Color COLOR_ERROR = new Color(230, 110, 110);

    private final PanelActividad vista;
    private final ActividadDAO dao = new ActividadDAO();
    private final DefaultTableModel modelo = new DefaultTableModel();
    private List<Actividad> actividades = new ArrayList<>();
    private boolean cargando = false;

    public ActividadNegocio(PanelActividad vista) {
        this.vista = vista;
        configurarVista();
        registrarEventos();
        listar();
    }

    private void configurarVista() {
        modelo.addColumn("Código");
        modelo.addColumn("Nombre");
        vista.getTablaActividad().setModel(modelo);
        vista.getTablaActividad().setDefaultEditor(Object.class, null);
        vista.getTablaActividad().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        vista.getTablaActividad().setForeground(Color.WHITE);
        vista.getInputCodigo().setEditable(false);
        vista.getLabelEstado().setText("");
    }

    private void registrarEventos() {
        vista.getBtnGuardar().addActionListener(this);
        vista.getBtnModificar().addActionListener(this);
        vista.getBtnEliminar().addActionListener(this);
        vista.getBtnLimpiar().addActionListener(this);
        vista.getBtnBuscar().addActionListener(this);
        vista.getInputBuscar().addActionListener(this);
        vista.getTablaActividad().getSelectionModel().addListSelectionListener(this);
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
        actividades = dao.listar(vista.getInputBuscar().getText());
        for (Actividad a : actividades) {
            modelo.addRow(new Object[]{a.getCodigo(), a.getNombre()});
        }
        cargando = false;
        mostrarMensaje(actividades.size() + " actividad(es) encontrada(s)", false);
    }

    private void seleccionar() {
        int fila = vista.getTablaActividad().getSelectedRow();
        if (fila < 0 || fila >= actividades.size()) {
            return;
        }
        Actividad a = actividades.get(fila);
        vista.getInputCodigo().setText(String.valueOf(a.getCodigo()));
        vista.getInputNombre().setText(a.getNombre());
    }

    private void guardar() {
        if (!vista.getInputCodigo().getText().isEmpty()) {
            mostrarMensaje("La actividad ya existe, use Modificar o Limpiar", true);
            return;
        }
        try {
            Actividad a = new Actividad();
            a.setNombre(Validacion.texto(vista.getInputNombre().getText(), "Nombre"));
            if (dao.guardar(a)) {
                limpiar();
                listar();
                mostrarMensaje("Actividad guardada", false);
            } else {
                mostrarMensaje("No se pudo guardar la actividad", true);
            }
        } catch (IllegalArgumentException ex) {
            mostrarMensaje(ex.getMessage(), true);
        }
    }

    private void modificar() {
        if (vista.getInputCodigo().getText().isEmpty()) {
            mostrarMensaje("Seleccione una actividad de la tabla", true);
            return;
        }
        try {
            Actividad a = new Actividad();
            a.setCodigo(Integer.parseInt(vista.getInputCodigo().getText()));
            a.setNombre(Validacion.texto(vista.getInputNombre().getText(), "Nombre"));
            if (dao.modificar(a)) {
                limpiar();
                listar();
                mostrarMensaje("Actividad modificada", false);
            } else {
                mostrarMensaje("No se pudo modificar la actividad", true);
            }
        } catch (IllegalArgumentException ex) {
            mostrarMensaje(ex.getMessage(), true);
        }
    }

    private void eliminar() {
        if (vista.getInputCodigo().getText().isEmpty()) {
            mostrarMensaje("Seleccione una actividad de la tabla", true);
            return;
        }
        int opcion = JOptionPane.showConfirmDialog(vista,
                "¿Eliminar la actividad seleccionada?", "Confirmar",
                JOptionPane.YES_NO_OPTION);
        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }
        int codigo = Integer.parseInt(vista.getInputCodigo().getText());
        if (dao.eliminar(codigo)) {
            limpiar();
            listar();
            mostrarMensaje("Actividad eliminada", false);
        } else {
            mostrarMensaje("No se pudo eliminar, puede estar usada en un mantenimiento", true);
        }
    }

    private void limpiar() {
        vista.getTablaActividad().clearSelection();
        vista.getInputCodigo().setText("");
        vista.getInputNombre().setText("");
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
