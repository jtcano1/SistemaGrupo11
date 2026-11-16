package negocio.mantenimiento;

import datos.mantenimiento.ActividadDAO;
import datos.mantenimiento.ActivoDAO;
import datos.mantenimiento.CabeceraMantenimientoDAO;
import entidades.mantenimiento.Actividad;
import entidades.mantenimiento.Activo;
import entidades.mantenimiento.CabeceraMantenimiento;
import entidades.mantenimiento.DetalleMantenimiento;
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
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import presentacion.mantenimiento.PanelMantenimiento;

public class MantenimientoNegocio implements ActionListener, ListSelectionListener, AncestorListener {

    private static final Color COLOR_OK = new Color(120, 200, 120);
    private static final Color COLOR_ERROR = new Color(230, 110, 110);

    private final PanelMantenimiento vista;
    private final CabeceraMantenimientoDAO dao = new CabeceraMantenimientoDAO();
    private final ActivoDAO activoDAO = new ActivoDAO();
    private final ActividadDAO actividadDAO = new ActividadDAO();
    private final SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
    private final DefaultTableModel modeloCabeceras = new DefaultTableModel();
    private final DefaultTableModel modeloDetalles = new DefaultTableModel();

    private List<CabeceraMantenimiento> cabeceras = new ArrayList<>();
    private List<DetalleMantenimiento> detalles = new ArrayList<>();
    private boolean cargando = false;

    public MantenimientoNegocio(PanelMantenimiento vista) {
        this.vista = vista;
        configurarVista();
        registrarEventos();
        recargarCombos();
        listar();
    }

    private void configurarVista() {
        modeloCabeceras.addColumn("Número");
        modeloCabeceras.addColumn("Fecha");
        modeloCabeceras.addColumn("Responsable");
        vista.getTablaCabeceras().setModel(modeloCabeceras);
        vista.getTablaCabeceras().setDefaultEditor(Object.class, null);
        vista.getTablaCabeceras().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        vista.getTablaCabeceras().setForeground(Color.WHITE);

        modeloDetalles.addColumn("Activo");
        modeloDetalles.addColumn("Actividad");
        modeloDetalles.addColumn("Valor");
        vista.getTablaDetalles().setModel(modeloDetalles);
        vista.getTablaDetalles().setDefaultEditor(Object.class, null);
        vista.getTablaDetalles().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        vista.getTablaDetalles().setForeground(Color.WHITE);

        vista.getInputNumero().setEditable(false);

        JSpinner spinner = vista.getSpinnerFecha();
        spinner.setEditor(new JSpinner.DateEditor(spinner, "dd/MM/yyyy"));
        spinner.setValue(new Date());

        vista.getLabelEstado().setText("");
        mostrarTotal();
    }

    private void registrarEventos() {
        vista.getBtnGuardar().addActionListener(this);
        vista.getBtnModificar().addActionListener(this);
        vista.getBtnEliminar().addActionListener(this);
        vista.getBtnLimpiar().addActionListener(this);
        vista.getBtnBuscar().addActionListener(this);
        vista.getInputBuscar().addActionListener(this);
        vista.getBtnAgregarDetalle().addActionListener(this);
        vista.getBtnQuitarDetalle().addActionListener(this);
        vista.getTablaCabeceras().getSelectionModel().addListSelectionListener(this);
        vista.addAncestorListener(this);
    }

    @Override
    public void ancestorAdded(AncestorEvent e) {
        recargarCombos();
    }

    @Override
    public void ancestorRemoved(AncestorEvent e) {
    }

    @Override
    public void ancestorMoved(AncestorEvent e) {
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
        } else if (origen == vista.getBtnAgregarDetalle()) {
            agregarDetalle();
        } else if (origen == vista.getBtnQuitarDetalle()) {
            quitarDetalle();
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting() && !cargando) {
            seleccionar();
        }
    }

    private void recargarCombos() {
        vista.getComboActivo().removeAllItems();
        for (Activo a : activoDAO.listar("")) {
            vista.getComboActivo().addItem(a);
        }
        vista.getComboActividad().removeAllItems();
        for (Actividad a : actividadDAO.listar("")) {
            vista.getComboActividad().addItem(a);
        }
    }

    private void listar() {
        cargando = true;
        modeloCabeceras.setRowCount(0);
        cabeceras = dao.listar(vista.getInputBuscar().getText());
        for (CabeceraMantenimiento c : cabeceras) {
            modeloCabeceras.addRow(new Object[]{
                c.getNumero(), formato.format(c.getFecha()), c.getResponsable()});
        }
        cargando = false;
        mostrarMensaje(cabeceras.size() + " mantenimiento(s) encontrado(s)", false);
    }

    private void seleccionar() {
        int fila = vista.getTablaCabeceras().getSelectedRow();
        if (fila < 0 || fila >= cabeceras.size()) {
            return;
        }
        CabeceraMantenimiento c = cabeceras.get(fila);
        vista.getInputNumero().setText(String.valueOf(c.getNumero()));
        vista.getSpinnerFecha().setValue(c.getFecha());
        vista.getInputResponsable().setText(c.getResponsable());
        detalles = dao.listarDetalles(c.getNumero());
        mostrarDetalles();
    }

    private void agregarDetalle() {
        Activo activo = (Activo) vista.getComboActivo().getSelectedItem();
        Actividad actividad = (Actividad) vista.getComboActividad().getSelectedItem();
        if (activo == null) {
            mostrarMensaje("Seleccione un activo", true);
            return;
        }
        if (actividad == null) {
            mostrarMensaje("Seleccione una actividad", true);
            return;
        }

        double valor;
        try {
            valor = Double.parseDouble(vista.getInputValor().getText().trim());
        } catch (NumberFormatException ex) {
            mostrarMensaje("Ingrese un valor numerico valido", true);
            return;
        }
        if (valor <= 0) {
            mostrarMensaje("El valor debe ser mayor que cero", true);
            return;
        }

        for (DetalleMantenimiento d : detalles) {
            if (d.getActivo().getCodigo() == activo.getCodigo()
                    && d.getActividad().getCodigo() == actividad.getCodigo()) {
                mostrarMensaje("Ese activo ya tiene esa actividad en el detalle", true);
                return;
            }
        }

        DetalleMantenimiento detalle = new DetalleMantenimiento();
        detalle.setActivo(activo);
        detalle.setActividad(actividad);
        detalle.setValor(Math.round(valor * 100.0) / 100.0);
        detalles.add(detalle);

        vista.getInputValor().setText("");
        mostrarDetalles();
        mostrarMensaje("Linea agregada", false);
    }

    private void quitarDetalle() {
        int fila = vista.getTablaDetalles().getSelectedRow();
        if (fila < 0 || fila >= detalles.size()) {
            mostrarMensaje("Seleccione una linea del detalle", true);
            return;
        }
        detalles.remove(fila);
        mostrarDetalles();
        mostrarMensaje("Linea quitada", false);
    }

    private void mostrarDetalles() {
        modeloDetalles.setRowCount(0);
        for (DetalleMantenimiento d : detalles) {
            modeloDetalles.addRow(new Object[]{
                d.getActivo().getNombre(),
                d.getActividad().getNombre(),
                String.format("%.2f", d.getValor())});
        }
        mostrarTotal();
    }

    private double calcularTotal() {
        double total = 0;
        for (DetalleMantenimiento d : detalles) {
            total = total + d.getValor();
        }
        return Math.round(total * 100.0) / 100.0;
    }

    private void mostrarTotal() {
        vista.getLabelTotal().setText("Total: " + String.format("%.2f", calcularTotal()));
    }

    private CabeceraMantenimiento leerFormulario() {
        CabeceraMantenimiento cab = new CabeceraMantenimiento();
        Date fecha = (Date) vista.getSpinnerFecha().getValue();
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha es obligatoria");
        }
        cab.setFecha(fecha);
        cab.setResponsable(Validacion.texto(vista.getInputResponsable().getText(), "Responsable"));
        if (detalles.isEmpty()) {
            throw new IllegalArgumentException("Ingrese al menos una linea de detalle");
        }
        cab.setDetalles(detalles);
        return cab;
    }

    private void guardar() {
        if (!vista.getInputNumero().getText().isEmpty()) {
            mostrarMensaje("El mantenimiento ya existe, use Modificar o Limpiar", true);
            return;
        }
        try {
            CabeceraMantenimiento cab = leerFormulario();
            if (dao.guardar(cab)) {
                int numero = cab.getNumero();
                limpiar();
                listar();
                mostrarMensaje("Mantenimiento " + numero + " guardado", false);
            } else {
                mostrarMensaje("No se pudo guardar el mantenimiento", true);
            }
        } catch (IllegalArgumentException ex) {
            mostrarMensaje(ex.getMessage(), true);
        }
    }

    private void modificar() {
        if (vista.getInputNumero().getText().isEmpty()) {
            mostrarMensaje("Seleccione un mantenimiento de la tabla", true);
            return;
        }
        try {
            CabeceraMantenimiento cab = leerFormulario();
            cab.setNumero(Integer.parseInt(vista.getInputNumero().getText()));
            if (dao.modificar(cab)) {
                limpiar();
                listar();
                mostrarMensaje("Mantenimiento modificado", false);
            } else {
                mostrarMensaje("No se pudo modificar el mantenimiento", true);
            }
        } catch (IllegalArgumentException ex) {
            mostrarMensaje(ex.getMessage(), true);
        }
    }

    private void eliminar() {
        if (vista.getInputNumero().getText().isEmpty()) {
            mostrarMensaje("Seleccione un mantenimiento de la tabla", true);
            return;
        }
        int opcion = JOptionPane.showConfirmDialog(vista,
                "¿Eliminar el mantenimiento seleccionado y sus detalles?", "Confirmar",
                JOptionPane.YES_NO_OPTION);
        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }
        int numero = Integer.parseInt(vista.getInputNumero().getText());
        if (dao.eliminar(numero)) {
            limpiar();
            listar();
            mostrarMensaje("Mantenimiento eliminado", false);
        } else {
            mostrarMensaje("No se pudo eliminar el mantenimiento", true);
        }
    }

    private void limpiar() {
        vista.getTablaCabeceras().clearSelection();
        vista.getInputNumero().setText("");
        vista.getSpinnerFecha().setValue(new Date());
        vista.getInputResponsable().setText("");
        vista.getInputValor().setText("");
        detalles = new ArrayList<>();
        mostrarDetalles();
        recargarCombos();
        vista.getInputResponsable().requestFocus();
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
