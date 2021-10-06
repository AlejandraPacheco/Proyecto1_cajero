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
public class Banco {
    private String nombre;
    private List<Cliente> clientes;
    
    public Banco(String nombre) {
        this.nombre = nombre;
        this.clientes = new ArrayList();
    }
    
    public String getNombre(){
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public List<Cliente> getClientes() {
        return this.clientes;
    }
    
    public void agregarCliente(Cliente cliente) {
        clientes.add(cliente);
    }
    
    public String buscarNombre(String userID) {
        String nombre = null;
        for ( int i = 0; i < clientes.size(); i++) {
            Cliente cli = clientes.get(i); // Sacando elemento por elemento
            if (cli.getUserID().equals(userID)) {
                nombre=cli.getNombre();
                break;
            }
        }
        return nombre;
    }
    
    public Cliente buscarCliente(String userID){
        Cliente cliente = null;
        for (int i = 0; i<clientes.size(); i++){
            Cliente cli = clientes.get(i);
            if(cli.getUserID().equals(userID)){
                cliente = cli;
                break;
            }
        }
        return cliente;
    }
        
    public Cliente buscarClientePorId(String userID, String pin) {
        for ( int i = 0; i < clientes.size(); i++) {
            Cliente cli = clientes.get(i); // Sacando elemento por elemento
            if (cli.getUserID().equals(userID) && cli.getPin().equals(pin)) {
                System.out.println(cli);
                return cli;
            }
        }
        return null;
    }
    
}