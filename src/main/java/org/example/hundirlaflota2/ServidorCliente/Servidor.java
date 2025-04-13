package org.example.hundirlaflota2.ServidorCliente;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.net.InetAddress;


public class Servidor {
    public static void main(String[] args) {

        Socket SocketOne = null;
        Socket SocketTwo = null;

        final int PUERTO = 5000;

        try (ServerSocket serverSocket = new ServerSocket(PUERTO, 0, InetAddress.getByName("192.168.56.1"))){

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
                if (i == 1) {
                    String messageFirst = inOne.readUTF();
                    System.out.println(messageFirst);
                    outTwo.writeUTF(messageFirst);
                    String messageSecond = inTwo.readUTF();
                    outOne.writeUTF(messageSecond);
                }
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

            boolean seguirJugando = true;
            while (seguirJugando){ // Es el numero de rondas
                outOne.writeUTF(listActions.getFirst());
                outTwo.writeUTF(listActions.getLast());

                //------------------------------Turno------------------------------
                // Ataca el Atacante y le manda el Ataque al que Espera
                seguirJugando = shiftControlGame(listInputs,listOutputs);

                String primerElemento = listActions.removeFirst();
                listActions.add(primerElemento);
            }

            // Cerrar todos los inputs, outputs y los sockets
            outTwo.close();
            outOne.close();
            inOne.close();
            inTwo.close();
            SocketOne.close();
            SocketTwo.close();
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

    /**
     * Este método se encarga de obtener el mensaje del atacante y mandárselo al que espera,
     * el que espera le manda un int sea 3 o 2 para que identifique lo que ha tocado el disparo recibido
     * y finalmente el atacante manda un booleano al servidor para avisarle si ya ha acertado a 20 veces
     * o no.
     * En las dos listas que paso como parámetro son dos listas de dos elementos que contiene como primer elemento
     * al atacante y el segundo elemento el que espera.
     * @param listInputs Es una lista de inputs para recibir mensajes
     * @param listOutputs Es una lista de outputs para mandar mensajes
     * @throws IOException esta excepción controla los inputs y outputs
     * @return devuelve el contrario del booleano para determinar la victoria del atacante y que este cierre
     * el bucle de los turnos de juego
     */
    private static boolean shiftControlGame(
            List<DataInputStream> listInputs,
            List<DataOutputStream> listOutputs
    )throws IOException{
        String messageAtacante = listInputs.getFirst().readUTF();
        listOutputs.getLast().writeUTF(messageAtacante);
        // El que Espera manda la respuesta de que toco al Atacante
        int respuestaEsperador = listInputs.getLast().readInt();
        listOutputs.getFirst().writeInt(respuestaEsperador);
        // Aquí recibo el booleano de confirmación para saber si ha ganado o no
        boolean yaGano = listInputs.getFirst().readBoolean();
        // Aquí le mando el booleano al otro cliente para que sepa si ha perdido
        listOutputs.getLast().writeBoolean(yaGano);

        DataInputStream primerIn = listInputs.removeFirst();
        listInputs.add(primerIn);
        DataOutputStream primerOut = listOutputs.removeFirst();
        listOutputs.add(primerOut);

        return !yaGano;
    }
}
