/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.edu.ucb.est;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author PACHECO
 */
public class Cliente {
    private String nombre;
    private String userID;
    private String pin;
    private List<Cuenta> cuentas;
    
    public Cliente(String nombre, String userID) {
        this.nombre = nombre;
        this.userID = userID;
        this.cuentas = new ArrayList();
    }

    public void agregarCuenta(Cuenta cuenta) {
        this.cuentas.add(cuenta);
    }
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
    
    public List<Cuenta> getCuentas() {
        return cuentas;
    }

    public void setCuentas(List<Cuenta> cuentas) {
        this.cuentas = cuentas;
    }
    
    public Cuenta buscarUltimaCuenta(){
        int tamanio=getCuentas().size();
        Cuenta ultima_cuenta=getCuentas().get(tamanio-1);
        return ultima_cuenta;
    }
    public String listaCuentas(){
        String cad = "Seleccione una cuenta:\n";
        System.out.println(getCuentas().size());
        System.out.println(getCuentas());
        for (int i = 0; i<getCuentas().size(); i++){
            cad = cad + (i+1) +". " + (getCuentas().get(i).getNumCuenta()) + "\n";
        }
        return cad;
    }
}
