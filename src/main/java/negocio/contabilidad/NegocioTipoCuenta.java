package negocio.contabilidad;

import datos.contabilidad.DAOTipoCuenta;
import entidades.contabilidad.TipoCuenta;
import java.util.List;

public class NegocioTipoCuenta {
    private DAOTipoCuenta dao = new DAOTipoCuenta();

    // GUARDAR / INSERTAR
    public synchronized String guardar(TipoCuenta t) {
        if (t == null) return "Error: Tipo nulo";
        if (t.getNombre() == null || t.getNombre().trim().isEmpty()) return "Error: Nombre vacío";
        if (dao.buscarPorCodigo(t.getCodigo()) != null) return "Error: Ese código ya existe";
        if (dao.insertar(t)) return "OK: Tipo guardado";
        return "Error: No se pudo guardar tipo";
    }

    public synchronized String insertar(TipoCuenta t) {
        return guardar(t);
    }

    // MODIFICAR
    public synchronized String modificar(TipoCuenta t) {
        if (t == null) return "Error: Tipo nulo";
        if (t.getNombre() == null || t.getNombre().trim().isEmpty()) return "Error: Nombre vacío";
        if (dao.buscarPorCodigo(t.getCodigo()) == null) return "Error: No existe ese código";
        if (dao.modificar(t)) return "OK: Tipo modificado";
        return "Error: No se pudo modificar";
    }

    // ELIMINAR
    public synchronized String eliminar(int codigo) {
        if (dao.buscarPorCodigo(codigo) == null) return "Error: No existe ese código";
        if (dao.eliminar(codigo)) return "OK: Tipo eliminado";
        return "Error: No se pudo eliminar - puede estar usado en una Cuenta";
    }

    // BUSCAR POR CODIGO
    public synchronized TipoCuenta buscarPorCodigo(int codigo) {
        return dao.buscarPorCodigo(codigo);
    }

    // BUSCAR POR NOMBRE
    public synchronized List<TipoCuenta> buscarPorNombre(String nombre) {
        return dao.buscarPorNombre(nombre);
    }

    // LISTAR
    public synchronized List<TipoCuenta> listar() {
        return dao.listar();
    }
}