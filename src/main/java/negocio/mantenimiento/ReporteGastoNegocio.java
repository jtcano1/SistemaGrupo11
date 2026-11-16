package negocio.mantenimiento;

import datos.mantenimiento.ReporteMantenimientoDAO;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import presentacion.mantenimiento.PanelReporteGasto;

public class ReporteGastoNegocio implements ActionListener {

    private static final Color COLOR_OK = new Color(120, 200, 120);
    private static final Color COLOR_ERROR = new Color(230, 110, 110);

    private final PanelReporteGasto vista;
    private final ReporteMantenimientoDAO dao = new ReporteMantenimientoDAO();
    private final SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
    private final DefaultTableModel modelo = new DefaultTableModel();

    public ReporteGastoNegocio(PanelReporteGasto vista) {
        this.vista = vista;
        configurarVista();
        vista.getBtnConsultar().addActionListener(this);
        vista.getBtnImprimir().addActionListener(this);
    }

    private void configurarVista() {
        modelo.addColumn("Activo");
        modelo.addColumn("Total gastado");
        vista.getTablaReporte().setModel(modelo);
        vista.getTablaReporte().setDefaultEditor(Object.class, null);
        vista.getTablaReporte().setForeground(Color.WHITE);

        JSpinner desde = vista.getSpinnerDesde();
        desde.setEditor(new JSpinner.DateEditor(desde, "dd/MM/yyyy"));
        desde.setValue(primerDiaDelMes());

        JSpinner hasta = vista.getSpinnerHasta();
        hasta.setEditor(new JSpinner.DateEditor(hasta, "dd/MM/yyyy"));
        hasta.setValue(new Date());

        vista.getLabelEstado().setText("");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.getBtnConsultar()) {
            consultar();
        } else if (e.getSource() == vista.getBtnImprimir()) {
            imprimir();
        }
    }

    private void consultar() {
        Date desde = (Date) vista.getSpinnerDesde().getValue();
        Date hasta = (Date) vista.getSpinnerHasta().getValue();
        if (desde.after(hasta)) {
            mostrarMensaje("La fecha desde no puede ser mayor que la fecha hasta", true);
            return;
        }

        modelo.setRowCount(0);
        double totalGeneral = 0;
        List<Object[]> filas = dao.gastoPorActivo(desde, hasta);
        for (Object[] fila : filas) {
            double total = (Double) fila[1];
            totalGeneral = totalGeneral + total;
            modelo.addRow(new Object[]{fila[0], String.format("%.2f", total)});
        }
        if (!filas.isEmpty()) {
            modelo.addRow(new Object[]{"TOTAL", String.format("%.2f", totalGeneral)});
        }
        mostrarMensaje(filas.size() + " activo(s) en el rango", false);
    }

    private void imprimir() {
        if (modelo.getRowCount() == 0) {
            mostrarMensaje("Primero consulte el reporte", true);
            return;
        }
        String titulo = "Gasto por activo del "
                + formato.format((Date) vista.getSpinnerDesde().getValue())
                + " al " + formato.format((Date) vista.getSpinnerHasta().getValue());
        try {
            vista.getTablaReporte().print(JTable.PrintMode.FIT_WIDTH,
                    new MessageFormat(titulo), new MessageFormat("Pagina {0}"));
        } catch (PrinterException ex) {
            mostrarMensaje("No se pudo imprimir: " + ex.getMessage(), true);
        }
    }

    private Date primerDiaDelMes() {
        Calendar calendario = Calendar.getInstance();
        calendario.set(Calendar.DAY_OF_MONTH, 1);
        return calendario.getTime();
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
