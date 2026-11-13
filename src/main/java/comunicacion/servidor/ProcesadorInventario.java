package comunicacion.servidor;

import datos.inventarios.ArticuloDAO;
import datos.inventarios.TipoMovimientoDAO;
import entidades.inventarios.Articulo;
import entidades.inventarios.TipoMovimiento;
import java.util.List;
import entidades.inventarios.CabeceraInventario;
import negocio.inventarios.InventarioBL;
import entidades.inventarios.DetalleInventario;
import entidades.inventarios.ColumnaMovimiento;
import entidades.inventarios.MovimientoMatriz;
import negocio.inventarios.ReporteMatrizBL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import entidades.inventarios.SaldoArticulo;
import negocio.inventarios.ReporteInventarioBL;
import java.util.Date;


public class ProcesadorInventario {

    // Procesar operaciones relacionadas
    // con el módulo Inventario.
    public String procesar(
            String operacion,
            String dato) {

        // Buscar artículo por código.
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
        // Formato:
        // NOMBRE|PRECIO
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
        // Formato:
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

        // Buscar un tipo de movimiento por código.
        if (operacion.equals(
                "BUSCAR_TIPO_MOVIMIENTO")) {

            int codigo =
                    Integer.parseInt(dato);

            TipoMovimientoDAO dao =
                    new TipoMovimientoDAO();

            TipoMovimiento tipo =
                    dao.buscar(codigo);

            if (tipo != null) {

                return tipo.getCodigo()
                        + "|"
                        + tipo.getNombre()
                        + "|"
                        + tipo.getSigno();
            }

            return "NO_ENCONTRADO";
        }

        // Obtener todos los tipos de movimiento.
        if (operacion.equals(
                "LISTAR_TIPOS_MOVIMIENTO")) {

            TipoMovimientoDAO dao =
                    new TipoMovimientoDAO();

            List<TipoMovimiento> lista =
                    dao.listar();

            StringBuilder respuesta =
                    new StringBuilder();

            for (TipoMovimiento tipo : lista) {

                respuesta.append(
                        tipo.getCodigo())
                        .append("|")
                        .append(tipo.getNombre())
                        .append("|")
                        .append(tipo.getSigno())
                        .append("\n");
            }

            return respuesta.toString();
        }

        // Guardar un nuevo tipo de movimiento.
        // Formato:
        // NOMBRE|SIGNO
        if (operacion.equals(
                "GUARDAR_TIPO_MOVIMIENTO")) {

            String[] datos =
                    dato.split("\\|");

            TipoMovimiento tipo =
                    new TipoMovimiento();

            tipo.setNombre(datos[0]);

            tipo.setSigno(
                    Integer.parseInt(
                            datos[1]));

            TipoMovimientoDAO dao =
                    new TipoMovimientoDAO();

            boolean guardado =
                    dao.guardar(tipo);

            return guardado
                    ? "GUARDADO"
                    : "NO_GUARDADO";
        }

        // Modificar tipo de movimiento.
        // Formato:
        // CODIGO|NOMBRE|SIGNO
        if (operacion.equals(
                "MODIFICAR_TIPO_MOVIMIENTO")) {

            String[] datos =
                    dato.split("\\|");

            TipoMovimiento tipo =
                    new TipoMovimiento();

            tipo.setCodigo(
                    Integer.parseInt(
                            datos[0]));

            tipo.setNombre(
                    datos[1]);

            tipo.setSigno(
                    Integer.parseInt(
                            datos[2]));

            TipoMovimientoDAO dao =
                    new TipoMovimientoDAO();

            boolean actualizado =
                    dao.modificar(tipo);

            return actualizado
                    ? "MODIFICADO"
                    : "NO_MODIFICADO";
        }

        // Eliminar tipo movimiento por código.
        if (operacion.equals(
                "ELIMINAR_TIPO_MOVIMIENTO")) {

            int codigo =
                    Integer.parseInt(dato);

            TipoMovimientoDAO dao =
                    new TipoMovimientoDAO();

            boolean eliminado =
                    dao.eliminar(codigo);

            return eliminado
                    ? "ELIMINADO"
                    : "NO_ELIMINADO";
        }
    
        
        // Buscar cabecera de comprobante.
        if (operacion.equals("BUSCAR_CABECERA")) {

            int numero = Integer.parseInt(dato);

            InventarioBL bl = new InventarioBL();

            CabeceraInventario cabecera =
                    bl.buscarCabecera(numero);

            if (cabecera != null) {

                SimpleDateFormat formato =
                        new SimpleDateFormat("dd/MM/yyyy");

                return cabecera.getNumero() + "|"
                        + formato.format(cabecera.getFecha()) + "|"
                        + cabecera.getIdTipoMovimiento();
            }

            return "NO_ENCONTRADO";
        }

        // Listar detalles de un comprobante.
        if (operacion.equals("LISTAR_DETALLES")) {

            int numero = Integer.parseInt(dato);

            InventarioBL bl = new InventarioBL();

            List<DetalleInventario> detalles =
                    bl.listarDetalles(numero);

            if (detalles == null || detalles.isEmpty()) {
                return "SIN_DETALLES";
            }

            StringBuilder respuesta = new StringBuilder();

            for (DetalleInventario detalle : detalles) {

                respuesta.append(detalle.getIdArticulo())
                        .append("|")
                        .append(detalle.getCantidad())
                        .append("|")
                        .append(detalle.getPrecio())
                        .append("\n");
            }

            return respuesta.toString();
        }

        // Eliminar comprobante completo.
        if (operacion.equals("ELIMINAR_COMPROBANTE")) {

            int numero = Integer.parseInt(dato);

            InventarioBL bl = new InventarioBL();

            boolean eliminado = bl.eliminar(numero);

            return eliminado ? "ELIMINADO" : "NO_ELIMINADO";
        }

        // Guardar comprobante con uno o varios detalles.
        // Formato:
        // FECHA|TIPO|CANTIDAD_DETALLES|ARTICULO|CANTIDAD|PRECIO...
        if (operacion.equals("GUARDAR_COMPROBANTE")) {

            try {

                String[] datos = dato.split("\\|");

                if (datos.length < 6) {
                    return "ERROR|Datos incompletos";
                }

                SimpleDateFormat formato =
                        new SimpleDateFormat("dd/MM/yyyy");

                formato.setLenient(false);

                CabeceraInventario cabecera =
                        new CabeceraInventario();

                cabecera.setFecha(formato.parse(datos[0]));

                cabecera.setIdTipoMovimiento(
                        Integer.parseInt(datos[1]));

                int cantidadDetalles =
                        Integer.parseInt(datos[2]);

                if (cantidadDetalles <= 0
                        || datos.length != 3 + cantidadDetalles * 3) {
                    return "ERROR|Cantidad de detalles incorrecta";
                }

                List<DetalleInventario> detalles =
                        new ArrayList<>();

                for (int i = 0; i < cantidadDetalles; i++) {

                    int posicion = 3 + (i * 3);

                    DetalleInventario detalle =
                            new DetalleInventario();

                    detalle.setIdArticulo(
                            Integer.parseInt(datos[posicion]));

                    detalle.setCantidad(
                            Integer.parseInt(datos[posicion + 1]));

                    detalle.setPrecio(
                            Double.parseDouble(datos[posicion + 2]));

                    detalles.add(detalle);
                }

                InventarioBL bl = new InventarioBL();

                int numero = bl.guardar(cabecera, detalles);

                if (numero > 0) {
                    return "GUARDADO|" + numero;
                }

                return "NO_GUARDADO";

            } catch (Exception e) {

                return "ERROR|" + e.getMessage();
            }
        }

        // Modificar comprobante con uno o varios detalles.
        // Formato:
        // NUMERO|FECHA|TIPO|CANTIDAD_DETALLES|ARTICULO|CANTIDAD|PRECIO...
        if (operacion.equals("MODIFICAR_COMPROBANTE")) {

            try {

                String[] datos = dato.split("\\|");

                if (datos.length < 7) {
                    return "ERROR|Datos incompletos";
                }

                SimpleDateFormat formato =
                        new SimpleDateFormat("dd/MM/yyyy");

                formato.setLenient(false);

                CabeceraInventario cabecera =
                        new CabeceraInventario();

                cabecera.setNumero(
                        Integer.parseInt(datos[0]));

                cabecera.setFecha(
                        formato.parse(datos[1]));

                cabecera.setIdTipoMovimiento(
                        Integer.parseInt(datos[2]));

                int cantidadDetalles =
                        Integer.parseInt(datos[3]);

                if (cantidadDetalles <= 0
                        || datos.length != 4 + cantidadDetalles * 3) {
                    return "ERROR|Cantidad de detalles incorrecta";
                }

                List<DetalleInventario> detalles =
                        new ArrayList<>();

                for (int i = 0; i < cantidadDetalles; i++) {

                    int posicion = 4 + (i * 3);

                    DetalleInventario detalle =
                            new DetalleInventario();

                    detalle.setIdArticulo(
                            Integer.parseInt(datos[posicion]));

                    detalle.setCantidad(
                            Integer.parseInt(datos[posicion + 1]));

                    detalle.setPrecio(
                            Double.parseDouble(datos[posicion + 2]));

                    detalles.add(detalle);
                }

                InventarioBL bl = new InventarioBL();

                boolean resultado =
                        bl.modificar(cabecera, detalles);

                if (resultado) {
                    return "MODIFICADO";
                }

                return "NO_MODIFICADO";

            } catch (Exception e) {

                return "ERROR|" + e.getMessage();
            }
        }

                // Listar tipos de movimiento para el reporte de matriz.
        if (operacion.equals("LISTAR_TIPOS_MATRIZ")) {

            ReporteMatrizBL reporteBL =
                    new ReporteMatrizBL();

            List<ColumnaMovimiento> tipos =
                    reporteBL.listarTipos();

            StringBuilder respuesta = new StringBuilder();

            for (ColumnaMovimiento tipo : tipos) {

                respuesta.append(tipo.getCodigo())
                        .append("|")
                        .append(tipo.getNombre())
                        .append("\n");
            }

            return respuesta.toString();
        }

        // Listar movimientos para el reporte de matriz.

        if (operacion.equals("LISTAR_MOVIMIENTOS_MATRIZ")) {

            try {

                String[] fechas = dato.split("\\|");

                java.text.SimpleDateFormat formato =
                        new java.text.SimpleDateFormat("dd/MM/yyyy");

                formato.setLenient(false);

                java.util.Date desde =
                        formato.parse(fechas[0]);

                java.util.Date hasta =
                        formato.parse(fechas[1]);

                ReporteMatrizBL reporteBL =
                        new ReporteMatrizBL();

                List<MovimientoMatriz> movimientos =
                        reporteBL.listarMovimientos(desde, hasta);

                StringBuilder respuesta = new StringBuilder();

                for (MovimientoMatriz movimiento : movimientos) {

                    respuesta.append(movimiento.getCodigoArticulo())
                            .append("|")
                            .append(movimiento.getArticulo())
                            .append("|")
                            .append(movimiento.getCodigoTipoMovimiento())
                            .append("|")
                            .append(movimiento.getTipoMovimiento())
                            .append("|")
                            .append(movimiento.getCantidad())
                            .append("\n");
                }

                return respuesta.toString();

            } catch (Exception e) {

                return "ERROR|" + e.getMessage();
            }
        }
        
        // Listar saldos de artículos para el reporte.
        if (operacion.equals("LISTAR_SALDOS")) {

            try {

                SimpleDateFormat formato =
                        new SimpleDateFormat("dd/MM/yyyy");

                formato.setLenient(false);

                Date hasta = formato.parse(dato);

                ReporteInventarioBL reporteBL =
                        new ReporteInventarioBL();

                List<SaldoArticulo> saldos =
                        reporteBL.listarSaldos(hasta);

                StringBuilder respuesta =
                        new StringBuilder();

                for (SaldoArticulo saldo : saldos) {

                    respuesta.append(saldo.getCodigo())
                            .append("|")
                            .append(saldo.getNombre())
                            .append("|")
                            .append(saldo.getPrecio())
                            .append("|")
                            .append(saldo.getStock())
                            .append("\n");
                }

                return respuesta.toString();

            } catch (Exception e) {

                return "ERROR|" + e.getMessage();
            }
        }

        
        
        return "OPERACION_NO_VALIDA";
    }
}