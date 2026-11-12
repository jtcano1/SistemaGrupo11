
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
import entidades.inventarios.CabeceraInventario;
import entidades.inventarios.DetalleInventario;
import negocio.inventarios.InventarioBL;
import java.util.ArrayList;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;


public class FrmInventario extends javax.swing.JFrame {
        private DefaultTableModel modeloDetalle;
        private final ArticuloBL articuloBL = new ArticuloBL();
        private final TipoMovimientoBL tipoMovimientoBL = new TipoMovimientoBL();
        private final InventarioBL inventarioBL = new InventarioBL();
        
        private List<Articulo> listaArticulos;
        private List<TipoMovimiento> listaTiposMovimiento;

    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrmInventario.class.getName());

public FrmInventario() {

    initComponents();
    configurarFormulario();
    cargarDatosIniciales();

    // Mostrar automáticamente el precio del artículo seleccionado
    cmbArticulo.addActionListener(evt -> cargarPrecioArticulo());

    // Agregar artículos a la tabla de detalles
    btnAgregarDetalle.addActionListener(evt -> agregarDetalle());

    // Cargar los datos cuando se seleccione una fila
    tblDetalle.getSelectionModel().addListSelectionListener(evt -> {
        if (!evt.getValueIsAdjusting()) {
            cargarDetalleSeleccionado();
        }
    });
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
/**
 * Agrega el artículo seleccionado a la tabla de detalles.
 */
private void agregarDetalle() {

    // Verificar que exista un tipo de movimiento seleccionado
    if (cmbTipoMovimiento.getSelectedIndex() == -1) {
        JOptionPane.showMessageDialog(
            this,
            "Seleccione un tipo de movimiento.",
            "Validación",
            JOptionPane.WARNING_MESSAGE
        );
        cmbTipoMovimiento.requestFocus();
        return;
    }

    // Verificar que exista un artículo seleccionado
    int indiceArticulo = cmbArticulo.getSelectedIndex();

    if (indiceArticulo == -1 || listaArticulos == null
            || indiceArticulo >= listaArticulos.size()) {

        JOptionPane.showMessageDialog(
            this,
            "Seleccione un artículo.",
            "Validación",
            JOptionPane.WARNING_MESSAGE
        );
        cmbArticulo.requestFocus();
        return;
    }

    // Validar que se haya ingresado una cantidad
    if (txtCantidad.getText().trim().isEmpty()) {

        JOptionPane.showMessageDialog(
            this,
            "Ingrese la cantidad del artículo.",
            "Validación",
            JOptionPane.WARNING_MESSAGE
        );
        txtCantidad.requestFocus();
        return;
    }

    try {

        // Obtener la cantidad ingresada
        int cantidad = Integer.parseInt(txtCantidad.getText().trim());

        // Validar que la cantidad sea positiva
        if (cantidad <= 0) {
            JOptionPane.showMessageDialog(
                this,
                "La cantidad debe ser mayor que cero.",
                "Validación",
                JOptionPane.WARNING_MESSAGE
            );
            txtCantidad.requestFocus();
            return;
        }

        // Obtener el artículo seleccionado desde la lista
        Articulo articulo = listaArticulos.get(indiceArticulo);

        // Obtener el precio del artículo
        double precio = articulo.getPrecio();

        // Calcular el subtotal
        double subtotal = cantidad * precio;

        // Agregar una nueva fila a la tabla
        modeloDetalle.addRow(new Object[]{
            articulo.getCodigo(),
            articulo.getNombre(),
            cantidad,
            precio,
            subtotal
        });

        // Actualizar los totales del comprobante
        actualizarTotales();

        // Limpiar la cantidad para ingresar otro detalle
        txtCantidad.setText("");

        // Mantener el foco en el campo cantidad
        txtCantidad.requestFocus();

    } catch (NumberFormatException e) {

        JOptionPane.showMessageDialog(
            this,
            "Ingrese una cantidad válida utilizando números enteros.",
            "Error de cantidad",
            JOptionPane.ERROR_MESSAGE
        );

        txtCantidad.requestFocus();
    }
}



/**
 * Calcula el total de los detalles del comprobante.
 */
private void actualizarTotales() {

    double total = 0.0;

    // Recorrer todas las filas de la tabla
    for (int fila = 0; fila < modeloDetalle.getRowCount(); fila++) {

        Object valorSubtotal = modeloDetalle.getValueAt(fila, 4);

        if (valorSubtotal instanceof Number) {
            total += ((Number) valorSubtotal).doubleValue();
        }
    }

    // Mostrar el total del comprobante
    txtTotal.setText(
        String.format(java.util.Locale.US, "%.2f", total)
    );
}
private void cargarDetalleSeleccionado() {

    int filaSeleccionada = tblDetalle.getSelectedRow();

    if (filaSeleccionada == -1) {
        return;
    }

    int fila = tblDetalle.convertRowIndexToModel(filaSeleccionada);
    // Obtener el código del artículo de la fila
    int codigoArticulo = Integer.parseInt(
        modeloDetalle.getValueAt(fila, 0).toString()
    );

    // Buscar el artículo correspondiente
    for (int i = 0; i < listaArticulos.size(); i++) {
        if (listaArticulos.get(i).getCodigo() == codigoArticulo) {

            cmbArticulo.setSelectedIndex(i);
            break;
        }
    }

    // Cargar la cantidad
    txtCantidad.setText(
        modeloDetalle.getValueAt(fila, 2).toString()
    );

    // Cargar el precio
    txtPrecio.setText(
        modeloDetalle.getValueAt(fila, 3).toString()
    );
}
private void actualizarDetalle() {

    int filaSeleccionada = tblDetalle.getSelectedRow();

    // Verificar que exista una fila seleccionada
    if (filaSeleccionada == -1) {

        JOptionPane.showMessageDialog(
            this,
            "Seleccione un detalle de la tabla para actualizar.",
            "Validación",
            JOptionPane.WARNING_MESSAGE
        );

        return;
    }

    // Verificar que exista un artículo seleccionado
    int indiceArticulo = cmbArticulo.getSelectedIndex();

    if (indiceArticulo == -1 || listaArticulos == null
            || indiceArticulo >= listaArticulos.size()) {

        JOptionPane.showMessageDialog(
            this,
            "Seleccione un artículo.",
            "Validación",
            JOptionPane.WARNING_MESSAGE
        );

        cmbArticulo.requestFocus();
        return;
    }

    // Validar cantidad
    if (txtCantidad.getText().trim().isEmpty()) {

        JOptionPane.showMessageDialog(
            this,
            "Ingrese una cantidad.",
            "Validación",
            JOptionPane.WARNING_MESSAGE
        );

        txtCantidad.requestFocus();
        return;
    }

    try {

        int cantidad = Integer.parseInt(txtCantidad.getText().trim());

        if (cantidad <= 0) {

            JOptionPane.showMessageDialog(
                this,
                "La cantidad debe ser mayor que cero.",
                "Validación",
                JOptionPane.WARNING_MESSAGE
            );

            txtCantidad.requestFocus();
            return;
        }

        // Obtener el artículo seleccionado
        Articulo articulo = listaArticulos.get(indiceArticulo);

        double precio = articulo.getPrecio();

        // Calcular el nuevo subtotal
        double subtotal = cantidad * precio;

        // Obtener la fila del modelo
        int fila = tblDetalle.convertRowIndexToModel(filaSeleccionada);

        // Actualizar los valores de la fila
        modeloDetalle.setValueAt(articulo.getCodigo(), fila, 0);
        modeloDetalle.setValueAt(articulo.getNombre(), fila, 1);
        modeloDetalle.setValueAt(cantidad, fila, 2);
        modeloDetalle.setValueAt(precio, fila, 3);
        modeloDetalle.setValueAt(subtotal, fila, 4);

        // Actualizar el total del comprobante
        actualizarTotales();

        // Limpiar campos
        cmbArticulo.setSelectedIndex(-1);
        txtCantidad.setText("");
        txtPrecio.setText("");

        // Quitar selección de la tabla
        tblDetalle.clearSelection();

        JOptionPane.showMessageDialog(
            this,
            "Detalle actualizado correctamente.",
            "Actualización",
            JOptionPane.INFORMATION_MESSAGE
        );

    } catch (NumberFormatException e) {

        JOptionPane.showMessageDialog(
            this,
            "Ingrese una cantidad válida utilizando números enteros.",
            "Error",
            JOptionPane.ERROR_MESSAGE
        );

        txtCantidad.requestFocus();
    }
}
private void quitarDetalle() {

    int filaSeleccionada = tblDetalle.getSelectedRow();

    if (filaSeleccionada == -1) {

        JOptionPane.showMessageDialog(
            this,
            "Seleccione un detalle de la tabla para eliminar.",
            "Validación",
            JOptionPane.WARNING_MESSAGE
        );

        return;
    }

    int respuesta = JOptionPane.showConfirmDialog(
        this,
        "¿Está seguro de quitar este detalle?",
        "Confirmar eliminación",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE
    );

    if (respuesta == JOptionPane.YES_OPTION) {

        int fila = tblDetalle.convertRowIndexToModel(filaSeleccionada);

        modeloDetalle.removeRow(fila);

        actualizarTotales();

        cmbArticulo.setSelectedIndex(-1);
        txtCantidad.setText("");
        txtPrecio.setText("");

        JOptionPane.showMessageDialog(
            this,
            "Detalle eliminado correctamente.",
            "Eliminación",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
}

/**
 * Guarda la cabecera y todos los detalles del comprobante.
 */
private void guardarComprobante() {

    int indiceTipo = cmbTipoMovimiento.getSelectedIndex();

    // Validar tipo de movimiento
    if (indiceTipo == -1 || listaTiposMovimiento == null
            || indiceTipo >= listaTiposMovimiento.size()) {

        JOptionPane.showMessageDialog(
                this,
                "Seleccione un tipo de movimiento.",
                "Validación",
                JOptionPane.WARNING_MESSAGE
        );

        cmbTipoMovimiento.requestFocus();
        return;
    }

    // Validar que existan detalles
    if (modeloDetalle.getRowCount() == 0) {

        JOptionPane.showMessageDialog(
                this,
                "Agregue al menos un artículo al comprobante.",
                "Validación",
                JOptionPane.WARNING_MESSAGE
        );

        return;
    }

    try {

        // Convertir la fecha del formulario
        LocalDate fechaLocal = LocalDate.parse(
                txtFecha.getText().trim(),
                DateTimeFormatter.ofPattern("dd/MM/yyyy")
        );

        java.util.Date fecha = java.util.Date.from(
                fechaLocal.atStartOfDay(
                        ZoneId.systemDefault()
                ).toInstant()
        );

        // Obtener el tipo de movimiento seleccionado
        TipoMovimiento tipoSeleccionado =
                listaTiposMovimiento.get(indiceTipo);

        // Construir la cabecera
        CabeceraInventario cabecera = new CabeceraInventario();

        cabecera.setFecha(fecha);
        cabecera.setIdTipoMovimiento(
                tipoSeleccionado.getCodigo()
        );

        // Construir la lista de detalles
        List<DetalleInventario> detalles = new ArrayList<>();

        for (int fila = 0;
                fila < modeloDetalle.getRowCount();
                fila++) {

            DetalleInventario detalle = new DetalleInventario();

            int codigoArticulo = Integer.parseInt(
                    modeloDetalle.getValueAt(fila, 0).toString()
            );

            int cantidad = Integer.parseInt(
                    modeloDetalle.getValueAt(fila, 2).toString()
            );

            double precio = Double.parseDouble(
                    modeloDetalle.getValueAt(fila, 3).toString()
            );

            detalle.setIdArticulo(codigoArticulo);
            detalle.setCantidad(cantidad);
            detalle.setPrecio(precio);

            detalles.add(detalle);
        }

        // Enviar cabecera y detalles a la capa de negocio
        int numeroGenerado = inventarioBL.guardar(
                cabecera,
                detalles
        );

        if (numeroGenerado > 0) {

            txtNumero.setText(String.valueOf(numeroGenerado));
            btnGuardarComprobante.setEnabled(false);
            btnModificar.setEnabled(true);
            btnEliminar.setEnabled(true);

            JOptionPane.showMessageDialog(
                    this,
                    "Comprobante guardado correctamente.\n"
                    + "Número generado: " + numeroGenerado,
                    "Registro exitoso",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo guardar el comprobante.\n"
                    + "Revise la consola de NetBeans para conocer el error.",
                    "Error al guardar",
                    JOptionPane.ERROR_MESSAGE
            );
        }

    } catch (DateTimeParseException e) {

        JOptionPane.showMessageDialog(
                this,
                "La fecha no tiene un formato válido.\n"
                + "Utilice el formato dd/MM/yyyy.",
                "Error de fecha",
                JOptionPane.WARNING_MESSAGE
        );

        txtFecha.requestFocus();

    } catch (NumberFormatException e) {

        JOptionPane.showMessageDialog(
                this,
                "Hay datos numéricos inválidos en los detalles.",
                "Error de datos",
                JOptionPane.ERROR_MESSAGE
        );

    } catch (Exception e) {

        JOptionPane.showMessageDialog(
                this,
                "Ocurrió un error al guardar:\n" + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
        );

        e.printStackTrace();
    }
}


    // BUSCAR COMPROBANTE POR NÚMERO
    private void buscarComprobante() {

        try {

            if (txtNumero.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Ingrese el número del comprobante.",
                        "Validación",
                        JOptionPane.WARNING_MESSAGE
                );
                txtNumero.requestFocus();
                return;
            }

            int numero = Integer.parseInt(txtNumero.getText().trim());

            CabeceraInventario cabecera =
                    inventarioBL.buscarCabecera(numero);

            if (cabecera == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "No se encontró el comprobante.",
                        "Búsqueda",
                        JOptionPane.INFORMATION_MESSAGE
                );
                return;
            }

            List<DetalleInventario> detalles =
                    inventarioBL.listarDetalles(numero);

            if (detalles == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "No se pudieron consultar los detalles.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            // Mostrar fecha
            LocalDate fecha = new java.util.Date(
                    cabecera.getFecha().getTime()
            ).toInstant()
             .atZone(ZoneId.systemDefault())
             .toLocalDate();

            txtFecha.setText(
                    fecha.format(
                            DateTimeFormatter.ofPattern("dd/MM/yyyy")
                    )
            );

            // Seleccionar tipo de movimiento
            for (int i = 0; i < listaTiposMovimiento.size(); i++) {

                if (listaTiposMovimiento.get(i).getCodigo()
                        == cabecera.getIdTipoMovimiento()) {

                    cmbTipoMovimiento.setSelectedIndex(i);
                    break;
                }
            }

            // Limpiar tabla antes de cargar detalles
            modeloDetalle.setRowCount(0);

            // Cargar detalles
            for (DetalleInventario detalle : detalles) {

                String nombreArticulo = "Artículo no encontrado";

                for (Articulo articulo : listaArticulos) {

                    if (articulo.getCodigo()
                            == detalle.getIdArticulo()) {

                        nombreArticulo = articulo.getNombre();
                        break;
                    }
                }

                double subtotal =
                        detalle.getCantidad() * detalle.getPrecio();

                modeloDetalle.addRow(new Object[]{
                    detalle.getIdArticulo(),
                    nombreArticulo,
                    detalle.getCantidad(),
                    detalle.getPrecio(),
                    subtotal
                });
            }

            actualizarTotales();

            txtNumero.setText(String.valueOf(numero));

            // Habilitar operaciones sobre el comprobante encontrado
            btnGuardarComprobante.setEnabled(false);
            btnModificar.setEnabled(true);
            btnEliminar.setEnabled(true);

            JOptionPane.showMessageDialog(
                    this,
                    "Comprobante encontrado correctamente.",
                    "Búsqueda exitosa",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "El número del comprobante debe ser numérico.",
                    "Error",
                    JOptionPane.WARNING_MESSAGE
            );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al buscar:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }

    // OBTENER LOS DETALLES QUE ESTÁN EN LA TABLA
    private List<DetalleInventario> obtenerDetallesDeTabla() {

        List<DetalleInventario> detalles = new ArrayList<>();
        for (int fila = 0; fila < modeloDetalle.getRowCount(); fila++) {
            DetalleInventario detalle = new DetalleInventario();
            detalle.setIdArticulo(
                    Integer.parseInt(
                            modeloDetalle.getValueAt(fila, 0).toString()
                    )
            );

            detalle.setCantidad(
                    Integer.parseInt(
                            modeloDetalle.getValueAt(fila, 2).toString()
                    )
            );

            detalle.setPrecio(
                    Double.parseDouble(
                            modeloDetalle.getValueAt(fila, 3).toString()
                    )
            );

            detalles.add(detalle);
        }

        return detalles;
    }

    // MODIFICAR COMPROBANTE
    private void modificarComprobante() {

        try {

            if (txtNumero.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Primero busque un comprobante.",
                        "Validación",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            int numero = Integer.parseInt(txtNumero.getText().trim());

            int indiceTipo = cmbTipoMovimiento.getSelectedIndex();

            if (indiceTipo == -1) {
                JOptionPane.showMessageDialog(
                        this,
                        "Seleccione un tipo de movimiento.",
                        "Validación",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            if (modeloDetalle.getRowCount() == 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "El comprobante debe tener al menos un detalle.",
                        "Validación",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            LocalDate fechaLocal = LocalDate.parse(
                    txtFecha.getText().trim(),
                    DateTimeFormatter.ofPattern("dd/MM/yyyy")
            );

            java.util.Date fecha = java.util.Date.from(
                    fechaLocal.atStartOfDay(
                            ZoneId.systemDefault()
                    ).toInstant()
            );

            CabeceraInventario cabecera = new CabeceraInventario();

            cabecera.setNumero(numero);
            cabecera.setFecha(fecha);

            cabecera.setIdTipoMovimiento(
                    listaTiposMovimiento.get(indiceTipo).getCodigo()
            );

            List<DetalleInventario> detalles =
                    obtenerDetallesDeTabla();

            int respuesta = JOptionPane.showConfirmDialog(
                    this,
                    "¿Desea guardar los cambios del comprobante "
                    + numero + "?",
                    "Confirmar modificación",
                    JOptionPane.YES_NO_OPTION
            );

            if (respuesta != JOptionPane.YES_OPTION) {
                return;
            }

            boolean actualizado =
                    inventarioBL.modificar(cabecera, detalles);

            if (actualizado) {

                JOptionPane.showMessageDialog(
                        this,
                        "Comprobante modificado correctamente.",
                        "Actualización exitosa",
                        JOptionPane.INFORMATION_MESSAGE
                );

                btnModificar.setEnabled(false);

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "No se pudo modificar el comprobante. "
                        + "Revise la consola de NetBeans.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (DateTimeParseException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "La fecha debe tener el formato dd/MM/yyyy.",
                    "Error de fecha",
                    JOptionPane.WARNING_MESSAGE
            );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al modificar:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }

    // ELIMINAR COMPROBANTE
    private void eliminarComprobante() {

        try {

            if (txtNumero.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Primero busque el comprobante que desea eliminar.",
                        "Validación",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            int numero = Integer.parseInt(txtNumero.getText().trim());

            int respuesta = JOptionPane.showConfirmDialog(
                    this,
                    "¿Está seguro de eliminar el comprobante "
                    + numero + " y todos sus detalles?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (respuesta != JOptionPane.YES_OPTION) {
                return;
            }

            boolean eliminado = inventarioBL.eliminar(numero);

            if (eliminado) {

                JOptionPane.showMessageDialog(
                        this,
                        "Comprobante eliminado correctamente.",
                        "Eliminación exitosa",
                        JOptionPane.INFORMATION_MESSAGE
                );

                limpiarFormulario();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "No se pudo eliminar el comprobante.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Número de comprobante inválido.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al eliminar:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }

  

    //Configura los elementos iniciales del formulario.
    private void configurarFormulario() {

        txtNumero.setEditable(true);
        txtTotal.setEditable(false);

        txtNumero.setText("");
        txtTotal.setText("0.00");

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
        btnModificar.setEnabled(false);
        btnEliminar.setEnabled(false);
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

        // Posicionar el cursor en el tipo de movimiento
        cmbTipoMovimiento.requestFocus();
        
        btnGuardarComprobante.setEnabled(true);
        btnModificar.setEnabled(false);
        btnEliminar.setEnabled(false);
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
        txtTotal = new javax.swing.JTextField();
        btnActualizarDetalle = new javax.swing.JButton();
        btnQuitarDetalle = new javax.swing.JButton();

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
        btnGuardarComprobante.addActionListener(this::btnGuardarComprobanteActionPerformed);

        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(this::btnBuscarActionPerformed);

        btnModificar.setText("Modificar");
        btnModificar.addActionListener(this::btnModificarActionPerformed);

        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(this::btnEliminarActionPerformed);

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

        txtTotal.setEditable(false);

        btnActualizarDetalle.setText("Actualizar Detalle");
        btnActualizarDetalle.addActionListener(this::btnActualizarDetalleActionPerformed);

        btnQuitarDetalle.setText("Quitar Detalle");
        btnQuitarDetalle.addActionListener(this::btnQuitarDetalleActionPerformed);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(52, 52, 52)
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
                        .addComponent(btnEliminar))
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
                                .addGap(328, 328, 328)
                                .addComponent(jLabel1)
                                .addGap(53, 53, 53)
                                .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(77, 77, 77)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(9, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnQuitarDetalle, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnActualizarDetalle))
                .addGap(212, 212, 212))
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
                        .addGap(50, 50, 50)
                        .addComponent(btnActualizarDetalle)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnQuitarDetalle)
                        .addGap(0, 0, Short.MAX_VALUE))
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
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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

    private void btnQuitarDetalleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuitarDetalleActionPerformed
        // TODO add your handling code here:
        quitarDetalle();
    }//GEN-LAST:event_btnQuitarDetalleActionPerformed

    private void btnActualizarDetalleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarDetalleActionPerformed
        // TODO add your handling code here:
        actualizarDetalle();
    }//GEN-LAST:event_btnActualizarDetalleActionPerformed

    private void btnGuardarComprobanteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarComprobanteActionPerformed
        // TODO add your handling code here:
        guardarComprobante();
    }//GEN-LAST:event_btnGuardarComprobanteActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        // TODO add your handling code here:
        modificarComprobante();
    }//GEN-LAST:event_btnModificarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        // TODO add your handling code here:
        eliminarComprobante();
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        // TODO add your handling code here:
        buscarComprobante();
    }//GEN-LAST:event_btnBuscarActionPerformed

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
    private javax.swing.JButton btnActualizarDetalle;
    private javax.swing.JButton btnAgregarDetalle;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardarComprobante;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnQuitarDetalle;
    private javax.swing.JComboBox<String> cmbArticulo;
    private javax.swing.JComboBox<String> cmbTipoMovimiento;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblFecha;
    private javax.swing.JLabel lblNumero;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JTable tblDetalle;
    private javax.swing.JTextField txtCantidad;
    private javax.swing.JTextField txtFecha;
    private javax.swing.JTextField txtNumero;
    private javax.swing.JTextField txtPrecio;
    private javax.swing.JTextField txtTotal;
    // End of variables declaration//GEN-END:variables
}
