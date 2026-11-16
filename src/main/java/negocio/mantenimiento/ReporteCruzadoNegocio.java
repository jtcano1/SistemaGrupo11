package negocio.mantenimiento;

import datos.mantenimiento.ReporteMantenimientoDAO;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import presentacion.mantenimiento.PanelReporteCruzado;

public class ReporteCruzadoNegocio implements ActionListener {

    private static final Color COLOR_OK = new Color(120, 200, 120);
    private static final Color COLOR_ERROR = new Color(230, 110, 110);

    private final PanelReporteCruzado vista;
    private final ReporteMantenimientoDAO dao = new ReporteMantenimientoDAO();
    private final SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
    private final DefaultTableModel modelo = new DefaultTableModel();

    public ReporteCruzadoNegocio(PanelReporteCruzado vista) {
        this.vista = vista;
        configurarVista();
        vista.getBtnConsultar().addActionListener(this);
        vista.getBtnImprimir().addActionListener(this);
    }

    private void configurarVista() {
        modelo.addColumn("Activo");
        vista.getTablaReporte().setModel(modelo);
        vista.getTablaReporte().setDefaultEditor(Object.class, null);
        vista.getTablaReporte().setForeground(Color.WHITE);
        vista.getTablaReporte().getTableHeader().setReorderingAllowed(false);

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

        List<Object[]> datos = dao.gastoPorActivoYActividad(desde, hasta);

        List<String> activos = new ArrayList<>();
        List<String> actividades = new ArrayList<>();
        for (Object[] fila : datos) {
            String activo = (String) fila[0];
            String actividad = (String) fila[1];
            if (!activos.contains(activo)) {
                activos.add(activo);
            }
            if (!actividades.contains(actividad)) {
                actividades.add(actividad);
            }
        }
        Collections.sort(actividades);

        double[][] matriz = new double[activos.size()][actividades.size()];
        for (Object[] fila : datos) {
            int filaActivo = activos.indexOf((String) fila[0]);
            int columnaActividad = actividades.indexOf((String) fila[1]);
            double valor = (Double) fila[2];
            matriz[filaActivo][columnaActividad] = matriz[filaActivo][columnaActividad] + valor;
        }

        modelo.setRowCount(0);
        modelo.setColumnCount(0);
        modelo.addColumn("Activo");
        for (String actividad : actividades) {
            modelo.addColumn(actividad);
        }
        modelo.addColumn("Total");

        for (int i = 0; i < activos.size(); i++) {
            Object[] fila = new Object[actividades.size() + 2];
            fila[0] = activos.get(i);
            double totalActivo = 0;
            for (int j = 0; j < actividades.size(); j++) {
                fila[j + 1] = String.format("%.2f", matriz[i][j]);
                totalActivo = totalActivo + matriz[i][j];
            }
            fila[actividades.size() + 1] = String.format("%.2f", totalActivo);
            modelo.addRow(fila);
        }

        mostrarMensaje(activos.size() + " activo(s) x " + actividades.size() + " actividad(es)", false);
    }

    private void imprimir() {
        if (modelo.getRowCount() == 0) {
            mostrarMensaje("Primero consulte el reporte", true);
            return;
        }
        String titulo = "Reporte cruzado del "
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
