/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.edu.ucb.est;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
/**
 *
 * @author PACHECO
 */
public class Cajero extends TelegramLongPollingBot{
    /**
     * estado = 0 >>> Pedir el nombre al usuario para su registro
     * estado = 1 >>> Se agrega al cliente y se le pide un pin
     * estado = 2 >>> El usuario ingresa el pin y es registrado
     * estado = 3 >>> Bienvenida al usuario existente y se pide el pin para entrar
     * estado = 4 >>> Se muestra el menu si el pin es correcto
     * estado = 5 >>> Se lee la opcion del menu para ir al estado correspondiente
     * estado = 6 >>> Creacion de una cuenta nueva: elegir tipo de cuenta
     * estado = 7 >>> Creacion de una cuenta nueva: elegir moneda
     * estado = 8 >>> Ver saldo o informacion de la cuenta
     * estado = 9 >>> Retiro: Pedir monto para retirar
     * estado = 10 >>> Retiro: Verificar monto y ejecutar retiro
     * estado = 11 >>> Deposito: Pedir monto para depositar
     * estado = 12 >>> Deposito: Verificar monto y ejecutar deposito
     */
    private Map estadoUsuario = new HashMap();
    private Map<String,Integer> cuentaActual=new HashMap<String,Integer>();
    Banco b=new Banco("Banco de la fortuna");
    private int n_cuenta=1000001;
    Cuenta c_actual=null;
    @Override
    public String getBotToken() {
        return "";
    }

    @Override
        public void onUpdateReceived(Update update) {
        //SendMessage message = new SendMessage();
        //SendMessage message2 = new SendMessage();
        int cuenta_elegida=0;
        String Id=update.getMessage().getChatId().toString();
        Integer estado = (Integer) estadoUsuario.get(Id);
        if (estado == null){
            estado = 0;
            estadoUsuario.put(Id,estado);
        }
        if(update.hasMessage()) {
            //Cuenta cuenta = null;
            /*if(b.buscarCliente(Id).buscarUltimaCuenta()!=null){
                Cuenta cuenta = b.buscarCliente(Id).buscarUltimaCuenta();
            }*/
            //Cuenta cuenta = b.buscarCliente(Id).buscarUltimaCuenta();
            System.out.println(update.getMessage().toString());
            //String mensajeAMostrar = null;
            //String mensajeAMostrar2 = null;
        // -------------------------------------------------------------------------------------------------
            String Bienvenida_banco= "Bienvenido al " + b.getNombre();
            String registro= "He notado que aún no eres cliente, procedamos a registrarte.";
            String nuevo_nombre= "¿Cuál es tu nombre completo?";
            String crear_pin = "Por favor elige un PIN de seguridad, este te será requerido cada que ingreses al sistema.";
            String error_pin="El pin debe ser de 4 dígitos.";
            String registro_correcto = "Te hemos registrado correctamente.";
            String error = "Error, intente nuevamente.";
        // -------------------------------------------------------------------------------------------------
            String hola_usuario = "Hola de nuevo "; // + b.buscarNombre(userId);
            String pedir_pin="Solo por seguridad ¿cuál es tu PIN.?";
            String codigo_incorrecto = "Lo siento, el código es incorrecto.";
            String bienvenido_menu= "Bienvenido.\n";
            String menu="Elige una opción:\n\n"+ "1. Ver Saldo\n" +
            "2. Retirar dinero.\n" +"3. Depositar dinero.\n" + "4. Crear cuenta\n" + "5. Salir";
            String usuario_sincuenta = "Usted no tiene cuentas, cree una primero.";
            String tipo_cuenta ="Seleccione el tipo de cuenta:\n" + "1. Cuenta Corriente\n" +"2. Caja de Ahorros";
            String tipo_moneda="Seleccione la moneda:\n" + "1. Dólares\n" +"2. Bolivianos";
            String cuentacreada="Se le ha creado una cuenta en ";
        // -------------------------------------------------------------------------------------------------
            String ver_saldo="Saldo de la cuenta:";
            String retiro="Ingrese la cantidad a retirar en ";
            String error_retiro="La cantidad ingresada es mayor al saldo de su cuenta, intente nuevamente.";
            String deposito="Ingrese la cantidad a depositar en ";
            String transaccion="Transacción exitosa";
            String error_cero="El monto ingresado para hacer el retiro no puede ser cero, intente nuevamente.";
            
            String mensajeUsuario = null;
            switch (estado){
                case 0: //Pedir el nombre al usuario para su registro
                    //String Usuario=String.valueOf(Id);
                    salida_mensaje(Bienvenida_banco, Id);
                    salida_mensaje(registro, Id);
                    salida_mensaje(nuevo_nombre, Id);
                    estadoUsuario.put(Id,1);
                    break;
                case 1: // Se agrega al cliente y se le pide un pin
                    //String Usuario1=String.valueOf(userId);
                    mensajeUsuario = update.getMessage().getText();
                    try {
                        Cliente cliente=new Cliente(mensajeUsuario, Id);
                        b.agregarCliente(cliente);
                        salida_mensaje(crear_pin, Id);
                        estadoUsuario.put(Id,2);
                    }catch(Exception ex){
                        System.out.println(ex);
                        salida_mensaje(error, Id);
                        estadoUsuario.put(Id,1);
                    }
                    break;
                case 2: // El usuario ingresa el pin y es registrado
                    mensajeUsuario = update.getMessage().getText();
                     try {
                        int pin = Integer.parseInt(mensajeUsuario);
                        if (pin>=0000 && pin<=9999){
                        for (int i = 0; i<b.getClientes().size(); i++){
                            if(b.getClientes().get(i).getUserID().equals(Id)){
                                b.getClientes().get(i).setPin(mensajeUsuario);
                                System.out.println(b.getClientes().get(i).getPin());
                                salida_mensaje(registro_correcto,Id);
                                break;
                            } 
                        }
                        estadoUsuario.put(Id,3);
                        }
                        else{
                            salida_mensaje(error_pin, Id);
                            salida_mensaje(crear_pin, Id);
                            estadoUsuario.put(Id,2);
                        }
                    }
                    catch(Exception ex){
                        salida_mensaje(error, Id);
                        estadoUsuario.put(Id,0);
                    }
                    break;
                case 3: //Bienvenida al usuario existente y se pide el pin para entrar
                    //String Usuario3=String.valueOf(Id);
                    mensajeUsuario = update.getMessage().getText();
                    try {
                        salida_mensaje(hola_usuario+b.buscarNombre(Id), Id);
                        salida_mensaje(pedir_pin,Id);
                        estadoUsuario.put(Id,4);
                    }
                    catch(Exception ex){
                        salida_mensaje(error, Id);
                    }
                    break;
                case 4: // Se muestra el menu si el pin es correcto
                    //String Usuario4=String.valueOf(Id);
                    mensajeUsuario = update.getMessage().getText();
                    try {
                        Cliente cli = b.buscarClientePorId(Id, mensajeUsuario);
                        if (cli==null){
                            salida_mensaje(codigo_incorrecto,Id);
                            salida_mensaje(hola_usuario+b.buscarNombre(Id), Id);
                            salida_mensaje(pedir_pin,Id);
                            estadoUsuario.put(Id,4);
                        }
                        else{
                            salida_mensaje(bienvenido_menu,Id);
                            salida_mensaje(menu,Id);
                            estadoUsuario.put(Id,5);
                        }
                    }
                    catch(Exception ex){
                        salida_mensaje(error, Id);
                        estadoUsuario.put(Id,5);
                    }
                     break;
                case 5: // Se lee la opcion del menu para ir al estado correspondiente
                    mensajeUsuario = update.getMessage().getText();
                    //Cuenta cuenta;
                    try{
                        int eleccion=Integer.parseInt(mensajeUsuario);
                        System.out.println(b.buscarCliente(Id).getCuentas());
                        if(b.buscarCliente(Id).getCuentas().isEmpty()){
                            if(eleccion==1 || eleccion==2 || eleccion==3 || eleccion==5){
                                salida_mensaje(usuario_sincuenta,Id);
                                salida_mensaje(menu,Id);
                                estadoUsuario.put(Id,5);
                            }
                            if(eleccion==4){
                            Cuenta cuenta = new Cuenta(n_cuenta);//Se creo la cuenta pero aun no se la agrego a la lista de cuentas del cliente debido a que falta completar informacion de la misma.
                            b.buscarCliente(Id).agregarCuenta(cuenta);
                            n_cuenta++;
                            salida_mensaje(tipo_cuenta,Id);
                            estadoUsuario.put(Id, 6);
                            }
                        }
                        else{
                            if(eleccion==1){
                                salida_mensaje(b.buscarCliente(Id).listaCuentas(),Id);
                                estadoUsuario.put(Id, 8);
                            }
                            if(eleccion==2){
                                salida_mensaje(b.buscarCliente(Id).listaCuentas(),Id);
                                estadoUsuario.put(Id, 9);
                            }
                            if(eleccion==3){
                                salida_mensaje(b.buscarCliente(Id).listaCuentas(),Id);
                                estadoUsuario.put(Id, 11);
                            }
                            if(eleccion==4){
                                Cuenta cuenta = new Cuenta(n_cuenta);
                                b.buscarCliente(Id).agregarCuenta(cuenta);
                                salida_mensaje(tipo_cuenta,Id);
                                estadoUsuario.put(Id, 6);
                            }
                            if(eleccion==5){
                                salida_mensaje(hola_usuario+b.buscarNombre(Id), Id);
                                salida_mensaje(pedir_pin,Id);
                                estadoUsuario.put(Id,4);
                            }
                        }
                    }catch(Exception e){
                        salida_mensaje(error,Id);
                        estadoUsuario.put(Id,5);
                    }
                    break;
                case 6: // Creacion de una cuenta nueva: elegir tipo de cuenta
                    mensajeUsuario = update.getMessage().getText();
                    try {
                        int opcion = Integer.parseInt(mensajeUsuario);
                        if (opcion ==1 || opcion==2){
                            if (opcion == 1){
                                b.buscarCliente(Id).buscarUltimaCuenta().setTipo("Cuenta Corriente");
                                salida_mensaje(tipo_moneda,Id);
                                estadoUsuario.put(Id,7);
                            }
                            if (opcion == 2){
                                b.buscarCliente(Id).buscarUltimaCuenta().setTipo("Caja de ahorros");
                                salida_mensaje(tipo_moneda,Id);
                                estadoUsuario.put(Id,7);
                            }
                        }
                        else {
                            salida_mensaje(error,Id);
                            salida_mensaje(bienvenido_menu,Id);
                            salida_mensaje(menu,Id);
                            estadoUsuario.put(Id,5);
                        }
                    }
                    catch(Exception ex){
                        salida_mensaje(error,Id);
                        salida_mensaje(menu,Id);
                        estadoUsuario.put(Id,5);
                    }
                break;
                case 7: // Creacion de una cuenta nueva: elegir moneda
                    mensajeUsuario = update.getMessage().getText();
                    //Cuenta cuenta2=b.buscarCliente(Id).buscarCuenta(n_cuenta);
                    try {
                        int opcion = Integer.parseInt(mensajeUsuario);
                        if (opcion ==1 || opcion==2){
                            //Cuenta cuenta = b.buscarCliente(Id).buscarUltimaCuenta();
                            if (opcion == 1){
                                b.buscarCliente(Id).buscarUltimaCuenta().setMoneda("Dólares");
                                salida_mensaje(cuentacreada+b.buscarCliente(Id).buscarUltimaCuenta().getMoneda()+" con saldo cero, cuyo número es "+b.buscarCliente(Id).buscarUltimaCuenta().getNumCuenta(),Id);
                                //b.buscarCliente(Id).agregarCuenta(cuenta);
                                salida_mensaje(menu,Id);
                                estadoUsuario.put(Id,5);
                            }
                            if (opcion == 2){
                                b.buscarCliente(Id).buscarUltimaCuenta().setMoneda("Bolivianos");
                                salida_mensaje(cuentacreada+b.buscarCliente(Id).buscarUltimaCuenta().getMoneda()+" con saldo cero, cuyo número es "+b.buscarCliente(Id).buscarUltimaCuenta().getNumCuenta(),Id);
                                //b.buscarCliente(Id).agregarCuenta(cuenta);
                                salida_mensaje(menu,Id);
                                estadoUsuario.put(Id,5);
                            }
                        }
                        else {
                            salida_mensaje(error,Id);
                            salida_mensaje(menu,Id);
                            estadoUsuario.put(Id,5);
                        }
                    }
                    catch(Exception ex){
                        salida_mensaje(error,Id);
                        salida_mensaje(menu,Id);
                        estadoUsuario.put(Id,5);
                    }
                    break;
                case 8: // Ver saldo o informacion de la cuenta
                    mensajeUsuario = update.getMessage().getText();
                    //int cuenta_elegida = Integer.parseInt(mensajeUsuario);
                    cuenta_elegida = Integer.parseInt(mensajeUsuario);
                    try {
                        if (cuenta_elegida>0 && cuenta_elegida<=b.buscarCliente(Id).getCuentas().size()){
                            salida_mensaje(b.buscarCliente(Id).getCuentas().get(cuenta_elegida-1).verSaldo(),Id);
                            salida_mensaje(menu,Id);
                            estadoUsuario.put(Id,5);
                        }
                        else{
                            salida_mensaje(error,Id);
                            estadoUsuario.put(Id,3);
                        }
                    }
                    catch(Exception ex){
                        salida_mensaje(error,Id);
                        salida_mensaje(menu,Id);
                        estadoUsuario.put(Id,5);
                    }
                    break;
                case 9: //Retiro: Pedir monto para retirar
                    mensajeUsuario = update.getMessage().getText();
                    //int cuenta_elegida = Integer.parseInt(mensajeUsuario);
                    cuenta_elegida = Integer.parseInt(mensajeUsuario);
                    //Cuenta c_actual=b.buscarCliente(Id).getCuentas().get(cuentaActual.get(Id)-1);
                    try {
                        if (cuenta_elegida>0 || cuenta_elegida<=b.buscarCliente(Id).getCuentas().size()){
                            Cuenta cuenta_retiro = b.buscarCliente(Id).getCuentas().get(cuenta_elegida-1);
                            salida_mensaje("El saldo actual de esta cuenta es de "+cuenta_retiro.getSaldo()+" "+cuenta_retiro.getMoneda(),Id);
                            salida_mensaje(retiro+cuenta_retiro.getMoneda()+":",Id);
                            cuentaActual.put(Id,cuenta_elegida);
                            estadoUsuario.put(Id,10);
                        }
                        else{
                            salida_mensaje(error,Id);
                            estadoUsuario.put(Id,3);
                        }
                    }
                    catch(Exception e){
                        salida_mensaje(error,Id);
                        salida_mensaje(menu,Id);
                        estadoUsuario.put(Id,5);
                    }
                    break;
                case 10: // Retiro: Verificar monto y ejecutar retiro
                    mensajeUsuario = update.getMessage().getText();
                    //int cuenta_actual = b.buscarCliente(Id).getCuentas().get(cuenta_elegida-1).getNumCuenta();
                    Cuenta c_actual=b.buscarCliente(Id).getCuentas().get(cuentaActual.get(Id)-1);
                    try {
                        double monto_retiro= Double.parseDouble(mensajeUsuario);
                        if(monto_retiro==0){
                            salida_mensaje(error_cero,Id);
                            salida_mensaje(menu,Id);
                            estadoUsuario.put(Id,5);
                            break;
                        }
                        if(c_actual.retirar(monto_retiro)==true){
                        salida_mensaje(transaccion,Id);
                        salida_mensaje(menu,Id);
                        estadoUsuario.put(Id,5);
                        }
                        else{
                            salida_mensaje(error_retiro,Id);
                            salida_mensaje(retiro+c_actual.getMoneda()+":",Id);
                            estadoUsuario.put(Id,10);
                        }
                    }
                    catch(Exception ex){
                        salida_mensaje(error,Id);
                        salida_mensaje(menu,Id);
                        estadoUsuario.put(Id,5);
                    }
                    break;
                case 11: //Deposito: Pedir monto para depositar
                    mensajeUsuario = update.getMessage().getText();
                    
                     try {
                        cuenta_elegida=Integer.parseInt(mensajeUsuario);
                        if (cuenta_elegida>0 || cuenta_elegida<=b.buscarCliente(Id).getCuentas().size()){
                            Cuenta cuenta_deposito = b.buscarCliente(Id).getCuentas().get(cuenta_elegida-1);
                            salida_mensaje(cuenta_deposito.verSaldo(),Id);
                            salida_mensaje(deposito+cuenta_deposito.getMoneda(),Id);
                            cuentaActual.put(Id,cuenta_elegida);
                            estadoUsuario.put(Id,12);
                        }
                        else{
                            salida_mensaje(error,Id);
                            estadoUsuario.put(Id,3);
                        }
                    }
                    catch(Exception ex){
                        salida_mensaje(error,Id);
                        salida_mensaje(menu,Id);
                        estadoUsuario.put(Id,5);
                    }
                    break;
                case 12: // Deposito: Verificar monto y ejecutar deposito
                    mensajeUsuario = update.getMessage().getText();
                    //int cuentaActual = b.buscarCliente(Id).getCuentas().get(cuenta_elegida-1).getNumCuenta();
                    c_actual=b.buscarCliente(Id).getCuentas().get(cuentaActual.get(Id)-1);
                    System.out.println(c_actual);
                    try {
                        double monto_deposito= Double.parseDouble(mensajeUsuario);
                            if(c_actual.depositar(monto_deposito)==true){
                            salida_mensaje(transaccion,Id);
                            salida_mensaje(menu,Id);
                            estadoUsuario.put(Id,5);
                            }
                            else{
                                salida_mensaje(error,Id);
                                salida_mensaje(deposito+c_actual.getMoneda(),Id);
                                estadoUsuario.put(Id,12);
                            }
                        }
                    catch(Exception ex){
                        salida_mensaje(error,Id);
                        salida_mensaje(menu,Id);
                        estadoUsuario.put(Id,5);
                    }
                    break;
                default:
                    salida_mensaje(error,Id);
                    estadoUsuario.put(Id,1);
                    break;
            }
        }
    }
    
    public void salida_mensaje(String mensaje, String userId){
        SendMessage message = new SendMessage();
        message.setChatId(userId);
        message.setText(mensaje);
        message.setChatId(userId+"");
        try {
            execute(message);
        } catch (TelegramApiException e) {
            System.out.println(e);
        }
    }
    
    @Override
    public String getBotUsername() {
        return "proyecto_cajero_bot";
    }
}