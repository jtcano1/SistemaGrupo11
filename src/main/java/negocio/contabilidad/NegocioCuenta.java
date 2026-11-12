/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.contabilidad;

/**
 *
 * @author USER
 */
import datos.contabilidad.DAOCuenta;
import entidades.contabilidad.Cuenta;
import java.util.List;

public class NegocioCuenta {
    private DAOCuenta dao = new DAOCuenta();

    public synchronized String guardar(Cuenta c) {
        if (c.getCodigo() == null || c.getCodigo().isEmpty()) {
            return "Error: Codigo obligatorio";
        }
        if (dao.insertar(c)) return "OK: Cuenta guardada";
        return "Error: No se pudo guardar cuenta";
    }

    public synchronized List<Cuenta> listar() {
        return dao.listar();
    }
}
