package Server;

import java.io.*;
import java.net.*;

public class ServidorUDP{
    static DatagramSocket socket;
    static final int PUERTO = 55555;
    static int actuales = 0;

    public static void main(String args[]) throws IOException {
        socket = new DatagramSocket(PUERTO);

        System.out.println("=== SERVIDOR UDP > NÚMEROS PRIMOS ===\n");
        System.out.println("Escuchando en puerto " + PUERTO + "\n\n");

        byte[] buffer = new byte[1024];

        while (true) {
            DatagramPacket paquete = new DatagramPacket(buffer, buffer.length);
            try {
                socket.receive(paquete);
            } catch (SocketException se) {
                break;
            }
            actuales++;
            System.out.println("PETICIONES ATENDIDAS: " + actuales);
            HiloServidorUDP hilo = new HiloServidorUDP(socket, paquete);
            hilo.start();
        }
        System.out.println("Servidor finalizado...");
    }
}