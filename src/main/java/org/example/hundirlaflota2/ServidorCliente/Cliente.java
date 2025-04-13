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

        setSocket(new Socket("10.134.75.114",5000));
        setIn(new DataInputStream(getSocket().getInputStream()));
        setOut(new DataOutputStream(getSocket().getOutputStream()));
        System.out.println("Conectado al servidor");

    }

    public void sendMessageString(String message) throws IOException {
        getOut().writeUTF(message);
    }

    public void sendMessageInt(int message) throws IOException {
        getOut().writeInt(message);
    }

    public void sendMessageBoolean(boolean message) throws IOException {
        getOut().writeBoolean(message);
    }

    public String receiveMessageString() throws IOException {
        return (String) getIn().readUTF();
    }

    public int receiveMessageInt() throws IOException {
        return getIn().readInt();
    }

    public boolean receiveMessageBoolean() throws IOException {
        return getIn().readBoolean();
    }

}
