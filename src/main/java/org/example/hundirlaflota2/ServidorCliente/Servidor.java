package org.example.hundirlaflota2.ServidorCliente;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    public static void main(String[] args) {

        Socket SocketOne = null;
        Socket SocketTwo = null;


        final int PUERTO = 5000;

        try (ServerSocket serverSocket = new ServerSocket(PUERTO)){


            System.out.println("Servidor conectado");

            SocketOne = serverSocket.accept();
            SocketTwo = serverSocket.accept();

            DataInputStream inOne = new DataInputStream(SocketOne.getInputStream());
            DataOutputStream outOne = new DataOutputStream(SocketOne.getOutputStream());

            DataInputStream inTwo = new DataInputStream(SocketTwo.getInputStream());
            DataOutputStream outTwo = new DataOutputStream(SocketTwo.getOutputStream());

            outOne.writeUTF("Te has conectado al servidor :)");
            outTwo.writeUTF("Te has conectado al servidor :)");

            System.out.println("Conectado dos personas al servidor");

            for (int i = 0; i < 3; i++) {
                String msOne = inOne.readUTF();
                String msTwo = inTwo.readUTF();

                System.out.println(msOne + "\n" + msTwo);

                outTwo.writeUTF(msOne);
                outOne.writeUTF(msTwo);

            }


        } catch (Exception e) {
            System.out.println("Error en el servidor: " + e.getMessage());
        }

    }
}
