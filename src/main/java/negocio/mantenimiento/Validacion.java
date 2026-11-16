package negocio.mantenimiento;

public class Validacion {

    public static String texto(String valor, String campo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo " + campo + " es obligatorio");
        }
        String limpio = valor.trim();
        if (limpio.length() > 100) {
            throw new IllegalArgumentException(
                    "El campo " + campo + " admite maximo 100 caracteres");
        }
        return limpio;
    }
}
