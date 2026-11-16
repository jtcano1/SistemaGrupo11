package main;

import javax.swing.JMenu;
import negocio.mantenimiento.ActividadNegocio;
import negocio.mantenimiento.ActivoNegocio;
import negocio.mantenimiento.MantenimientoNegocio;
import negocio.mantenimiento.ReporteCruzadoNegocio;
import negocio.mantenimiento.ReporteGastoNegocio;
import presentacion.mantenimiento.PanelActividad;
import presentacion.mantenimiento.PanelActivo;
import presentacion.mantenimiento.PanelMantenimiento;
import presentacion.mantenimiento.PanelReporteCruzado;
import presentacion.mantenimiento.PanelReporteGasto;

public class ModuloMantenimiento {

    public void registrar(MainInit inicio) {
        PanelActividad panelActividad = new PanelActividad();
        new ActividadNegocio(panelActividad);

        PanelActivo panelActivo = new PanelActivo();
        new ActivoNegocio(panelActivo);

        PanelMantenimiento panelMantenimiento = new PanelMantenimiento();
        new MantenimientoNegocio(panelMantenimiento);

        PanelReporteGasto panelReporteGasto = new PanelReporteGasto();
        new ReporteGastoNegocio(panelReporteGasto);

        PanelReporteCruzado panelReporteCruzado = new PanelReporteCruzado();
        new ReporteCruzadoNegocio(panelReporteCruzado);

        JMenu menu = inicio.registrarModulo("Mantenimiento");
        inicio.registrarPantalla(menu, "Actividades", panelActividad);
        inicio.registrarPantalla(menu, "Activos", panelActivo);
        inicio.registrarPantalla(menu, "Mantenimientos", panelMantenimiento);
        menu.addSeparator();
        inicio.registrarPantalla(menu, "Reporte gasto por activo", panelReporteGasto);
        inicio.registrarPantalla(menu, "Reporte cruzado", panelReporteCruzado);
    }
}
