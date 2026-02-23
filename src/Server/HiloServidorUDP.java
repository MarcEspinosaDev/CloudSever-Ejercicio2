package Server;

import java.io.*;
import java.net.*;

public class HiloServidorUDP extends Thread {
    DatagramSocket socket;
    DatagramPacket paquete;

    public HiloServidorUDP(DatagramSocket socket, DatagramPacket paquete) {
        this.socket = socket;
        this.paquete = paquete;
    }

    public void run() {
        try {
            String mensaje = new String(paquete.getData(), 0, paquete.getLength()).trim();
            InetAddress direccionCliente = paquete.getAddress();
            int puertoCliente = paquete.getPort();
            ServidorUDP.textarea.append("[" + direccionCliente.getHostAddress() +
                    "] Petición: " + mensaje + "\n");
            int numero = Integer.parseInt(mensaje);
            String resultado = calcularPrimos(numero);
            ServidorUDP.textarea.append("[" + direccionCliente.getHostAddress() +
                    "] Respuesta: " + resultado + "\n");
            byte[] respuesta = resultado.getBytes();
            DatagramPacket paqueteRespuesta = new DatagramPacket(
                    respuesta, respuesta.length, direccionCliente, puertoCliente);
            socket.send(paqueteRespuesta);

        } catch (NumberFormatException e) {
            try {
                String error = "ERROR: introduce un número entero positivo";
                byte[] respuesta = error.getBytes();
                DatagramPacket paqueteError = new DatagramPacket(
                        respuesta, respuesta.length,
                        paquete.getAddress(), paquete.getPort());
                socket.send(paqueteError);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String calcularPrimos(int n) {
        if (n < 2) return "No hay números primos";

        StringBuilder sb = new StringBuilder();
        for (int i = 2; i <= n; i++) {
            if (esPrimo(i)) {
                if (sb.length() > 0) sb.append(",");
                sb.append(i);
            }
        }
        return sb.toString();
    }

    private boolean esPrimo(int n) {
        if (n < 2) return false;
        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) return false;
        }
        return true;
    }
}