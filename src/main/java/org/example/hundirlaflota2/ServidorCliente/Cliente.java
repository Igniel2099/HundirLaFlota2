package org.example.hundirlaflota2.ServidorCliente;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Cliente {
    public final String nombreCliente;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public String getNombreCliente() {
        return nombreCliente;
    }

    public Socket getSocket() {
        return socket;
    }

    public ObjectOutputStream getOut() {
        return out;
    }

    public ObjectInputStream getIn() {
        return in;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void setOut(ObjectOutputStream out) {
        this.out = out;
    }

    public void setIn(ObjectInputStream in) {
        this.in = in;
    }

    public Cliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public void connectionToServer() throws IOException {

        setSocket(new Socket("localhost",5000));
        setIn(new ObjectInputStream(getSocket().getInputStream()));
        setOut(new ObjectOutputStream(getSocket().getOutputStream()));
        System.out.println("Conectado al servidor");

    }

    public void sendMessage(String message) throws IOException {
        getOut().writeUTF(message);
    }

    public String receiveMessage() throws IOException {
        return (String) getIn().readUTF();
    }

}
