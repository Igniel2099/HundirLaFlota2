package org.example.hundirlaflota2.ServidorCliente;

import java.io.*;
import java.net.Socket;

public class Cliente {
    public final String nombreCliente;
    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;

    public String getNombreCliente() {
        return nombreCliente;
    }

    public Socket getSocket() {
        return socket;
    }

    public DataOutputStream getOut() {
        return out;
    }

    public DataInputStream getIn() {
        return in;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void setOut(DataOutputStream out) {
        this.out = out;
    }

    public void setIn(DataInputStream in) {
        this.in = in;
    }

    public Cliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public void connectionToServer() throws IOException {

        setSocket(new Socket("192.168.1.29",5000));
        setIn(new DataInputStream(getSocket().getInputStream()));
        setOut(new DataOutputStream(getSocket().getOutputStream()));
        System.out.println("Conectado al servidor");

    }

    public void sendMessage(String message) throws IOException {
        getOut().writeUTF(message);
    }

    public String receiveMessage() throws IOException {
        return (String) getIn().readUTF();
    }

}
