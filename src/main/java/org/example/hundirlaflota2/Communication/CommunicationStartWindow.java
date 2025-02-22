package org.example.hundirlaflota2.Communication;

import org.example.hundirlaflota2.ServidorCliente.Cliente;

public class CommunicationStartWindow {

    private final Cliente client;

    public Cliente getClient() {
        return client;
    }

    public CommunicationStartWindow(Cliente client) {
        this.client = client;
    }

    public boolean startCommunication(){
        try{
            getClient().sendMessageString("He Cambiado de pantalla soy " + getClient().getNombreCliente() + "Estoy en StartWindow");
            System.out.println(getClient().receiveMessageString());

            return getClient().receiveMessageString().equals("Atacas");
        }catch(Exception e){
            System.out.println("--Error en el startCommunication de CommunicationStartWindow: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

}
