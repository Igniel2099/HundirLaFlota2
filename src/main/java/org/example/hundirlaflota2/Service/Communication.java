package org.example.hundirlaflota2.Service;

import org.example.hundirlaflota2.ServidorCliente.Cliente;

public class Communication {

    private final Cliente client;

    public Cliente getClient() {
        return client;
    }

    public Communication(Cliente client) {
        this.client = client;
    }

    public boolean startCommunication(){
        try{
            getClient().sendMessageString("He Cambiado de pantalla soy " + getClient().getNombreCliente() + "Estoy en StartWindow");
            System.out.println(getClient().receiveMessageString());

            return getClient().receiveMessageString().equals("Atacas");
        }catch(Exception e){
            System.err.println("Error en " + getClass().getSimpleName() + ": " + e.getMessage());
            for (StackTraceElement element : e.getStackTrace()) {
                System.err.println("\tat " + element);
            }
        }
        return false;
    }

    public void mainCommunication(){
        try{
            getClient().sendMessageString("Estoy listo " + getClient().getNombreCliente());
        }catch(Exception e){
            System.err.println("Error en " + getClass().getSimpleName() + ": " + e.getMessage());
            for (StackTraceElement element : e.getStackTrace()) {
                System.err.println("\tat " + element);
            }
        }
    }

}
