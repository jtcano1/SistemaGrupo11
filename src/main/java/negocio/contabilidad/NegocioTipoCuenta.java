/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio.contabilidad;

/**
 *
 * @author USER
 */
import datos.contabilidad.DAOTipoCuenta;
import entidades.contabilidad.TipoCuenta;
import java.util.List;

public class NegocioTipoCuenta {
    private DAOTipoCuenta dao = new DAOTipoCuenta();

    public synchronized String guardar(TipoCuenta t) {
        if (dao.insertar(t)) return "OK: Tipo guardado";
        return "Error: No se pudo guardar tipo";
    }

    public synchronized List<TipoCuenta> listar() {
        return dao.listar();
    }
}
