package comunicacion.servidor;

import datos.inventarios.ArticuloDAO;
import entidades.inventarios.Articulo;
import java.util.List;

public class ProcesadorInventario {

    // Procesar operaciones relacionadas
    // con el módulo Inventario.
public String procesar(
        String operacion,
        String dato) {

    if (operacion.equals(
            "BUSCAR_ARTICULO")) {

        int codigo =
                Integer.parseInt(dato);

        ArticuloDAO dao =
                new ArticuloDAO();

        Articulo articulo =
                dao.buscar(codigo);

        if (articulo != null) {

            return articulo.getCodigo()
                    + "|"
                    + articulo.getNombre()
                    + "|"
                    + articulo.getPrecio();
        }

        return "NO_ENCONTRADO";
    }

    // Obtener todos los artículos registrados.
    if (operacion.equals(
            "LISTAR_ARTICULOS")) {

        ArticuloDAO dao =
                new ArticuloDAO();

        List<Articulo> lista =
                dao.listar();

        StringBuilder respuesta =
                new StringBuilder();

        for (Articulo articulo : lista) {

            respuesta.append(
                    articulo.getCodigo())
                    .append("|")
                    .append(articulo.getNombre())
                    .append("|")
                    .append(articulo.getPrecio())
                    .append("\n");
        }

        return respuesta.toString();
    }
    // Eliminar artículo utilizando el código recibido
    // desde el cliente.
    if (operacion.equals(
        "ELIMINAR_ARTICULO")) {
        
        int codigo =
            Integer.parseInt(dato);
        
        ArticuloDAO dao =
            new ArticuloDAO();
        
        boolean eliminado =
            dao.eliminar(codigo);
        
        if (eliminado) {
            return "ELIMINADO";
        }
        
        return "NO_ELIMINADO";
    }
    // Guardar un nuevo artículo recibido
    // desde el cliente por socket.
    if (operacion.equals(
        "GUARDAR_ARTICULO")) {

    String[] datos =
            dato.split("\\|");

    String nombre =
            datos[0];

    double precio =
            Double.parseDouble(
                    datos[1]);

    Articulo articulo =
            new Articulo();

    articulo.setNombre(nombre);
    articulo.setPrecio(precio);

    ArticuloDAO dao =
            new ArticuloDAO();

    boolean guardado =
            dao.guardar(articulo);

    if (guardado) {
        return "GUARDADO";
    }

    return "NO_GUARDADO";
}
// Modificar un artículo existente.
// Formato esperado:
// CODIGO|NOMBRE|PRECIO
if (operacion.equals(
        "MODIFICAR_ARTICULO")) {

    String[] datos =
            dato.split("\\|");

    int codigo =
            Integer.parseInt(
                    datos[0]);

    String nombre =
            datos[1];

    double precio =
            Double.parseDouble(
                    datos[2]);

    Articulo articulo =
            new Articulo();

    articulo.setCodigo(codigo);
    articulo.setNombre(nombre);
    articulo.setPrecio(precio);

    ArticuloDAO dao =
            new ArticuloDAO();

    boolean actualizado =
            dao.modificar(articulo);

    if (actualizado) {
        return "MODIFICADO";
    }

    return "NO_MODIFICADO";
    }

    return "OPERACION_NO_VALIDA";
}
}