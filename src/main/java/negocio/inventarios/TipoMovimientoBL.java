
package negocio.inventarios;

import datos.inventarios.TipoMovimientoDAO;
import entidades.inventarios.TipoMovimiento;
import java.util.List;

public class TipoMovimientoBL {

    private final TipoMovimientoDAO tipoMovimientoDAO;

    public TipoMovimientoBL() {
        tipoMovimientoDAO = new TipoMovimientoDAO();
    }

    public List<TipoMovimiento> listar() {
        return tipoMovimientoDAO.listar();
    }

    public TipoMovimiento buscar(int codigo) {
        return tipoMovimientoDAO.buscar(codigo);
    }

    public boolean guardar(TipoMovimiento tipo) {
        return tipoMovimientoDAO.guardar(tipo);
    }

    public boolean modificar(TipoMovimiento tipo) {
        return tipoMovimientoDAO.modificar(tipo);
    }

    public boolean eliminar(int codigo) {
        return tipoMovimientoDAO.eliminar(codigo);
    }
}

