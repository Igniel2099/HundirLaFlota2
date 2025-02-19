package org.example.hundirlaflota2.ServidorCliente;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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

            sendPrimaryMessageServer(outOne, outTwo);

            System.out.println("Conectadas dos personas al servidor");

            // Las Ventanas se comunican entre ellas para saber que no he perdido la conexión
            // y son 3 iteraciones porque son 3 ventanas
            for (int i = 0; i < 3; i++) {
                sendMessageBetweenToWindows(inOne,inTwo,outOne,outTwo);
            }

            // Elección de personajes uno ataca y otro recibe
            List<String> listActions = new ArrayList<>(
                    Arrays.asList(
                            "Atacas",
                            "Esperas"
                    )
            );

            List<DataInputStream> listInputs = new ArrayList<>(
                    Arrays.asList(
                            inOne,
                            inTwo

                    )
            );

            List<DataOutputStream> listOutputs = new ArrayList<>(
                    Arrays.asList(
                            outOne,
                            outTwo
                    )
            );

            // Este es el primer mensaje para recibir el estado inicial del botón
            outOne.writeUTF(listActions.getFirst());
            outTwo.writeUTF(listActions.getLast());
            for (int i = 0; i < 3; i++){
                outOne.writeUTF(listActions.getFirst());
                outTwo.writeUTF(listActions.getLast());

                //------------------------------Turno------------------------------
                // Ataca el Atacante y le manda el Ataque al que Espera
                shiftControlGame(listInputs,listOutputs);

                String primerElemento = listActions.removeFirst();
                listActions.add(primerElemento);

            }


        } catch (Exception e) {
            System.out.println("Error en el servidor: " + e.getMessage());
        }
    }

    private static void sendPrimaryMessageServer(
            DataOutputStream outOne,
            DataOutputStream outTwo) throws IOException
    {
        outOne.writeUTF("Te has conectado al servidor :)");
        outTwo.writeUTF("Te has conectado al servidor :)");
    }

    private static void sendMessageBetweenToWindows(
            DataInputStream inOne,
            DataInputStream inTwo,
            DataOutputStream outOne,
            DataOutputStream outTwo) throws IOException {
        String msOne = inOne.readUTF();
        String msTwo = inTwo.readUTF();

        System.out.println(msOne + "\n" + msTwo);

        outTwo.writeUTF(msOne);
        outOne.writeUTF(msTwo);
    }

    private static void shiftControlGame(
            List<DataInputStream> listInputs,
            List<DataOutputStream> listOutputs
    )throws IOException{
        String messageAtacante = listInputs.getFirst().readUTF();
        listOutputs.getLast().writeUTF(messageAtacante);
        // El que Espera manda la respuesta de que toco al Atacante
        int respuestaEsperador = listInputs.getLast().readInt();
        listOutputs.getFirst().writeInt(respuestaEsperador);

        DataInputStream primerIn = listInputs.removeFirst();
        listInputs.add(primerIn);
        DataOutputStream primerOut = listOutputs.removeFirst();
        listOutputs.add(primerOut);
    }
}
