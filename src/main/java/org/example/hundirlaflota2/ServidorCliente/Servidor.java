package org.example.hundirlaflota2.ServidorCliente;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

            // Elección de personajes uno ataca y otro recibe
            List<String> listaAcciones = new ArrayList<>(
                    Arrays.asList(
                            "Atacas",
                            "Esperas"
                    )
            );

            for (int i = 0; i < 3; i++){
                outOne.writeUTF(listaAcciones.getFirst());
                outTwo.writeUTF(listaAcciones.getLast());

                //------------------------------Turno------------------------------
                // Ataca el Atacante y le manda el Ataque al que Espera
                String messageAtacante = inOne.readUTF();
                outTwo.writeUTF(messageAtacante);
                // El que Espera manda la respuesta de que toco al Atacante
                int respuestaEsperador = inTwo.readInt();
                outOne.writeInt(respuestaEsperador);

                String primerElemento = listaAcciones.removeFirst();
                listaAcciones.add(primerElemento);
            }


        } catch (Exception e) {
            System.out.println("Error en el servidor: " + e.getMessage());
        }

    }
}
