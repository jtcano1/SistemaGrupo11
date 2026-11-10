package datos.inventarios;

import datos.inventarios.ArticuloDAO;
import entidades.inventarios.Articulo;

public class PruebaArticulo {

    public static void main(String[] args) {

        Articulo articulo = new Articulo();

        articulo.setNombre("Impresora");

        articulo.setPrecio(350);

        ArticuloDAO dao = new ArticuloDAO();

        if (dao.guardar(articulo)) {

            System.out.println("Articulo guardado correctamente");

        } else {

            System.out.println("No se pudo guardar");

        }

    }
}