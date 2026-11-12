
package negocio.inventarios;

import datos.inventarios.ArticuloDAO;
import entidades.inventarios.Articulo;
import java.util.List;

public class ArticuloBL {

    private final ArticuloDAO articuloDAO;

    public ArticuloBL() {
        articuloDAO = new ArticuloDAO();
    }

    public List<Articulo> listar() {
        return articuloDAO.listar();
    }

    public Articulo buscar(int codigo) {
        return articuloDAO.buscar(codigo);
    }

    public boolean guardar(Articulo articulo) {
        return articuloDAO.guardar(articulo);
    }

    public boolean modificar(Articulo articulo) {
        return articuloDAO.modificar(articulo);
    }

    public boolean eliminar(int codigo) {
        return articuloDAO.eliminar(codigo);
    }
}
