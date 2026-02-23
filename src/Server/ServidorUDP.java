package Server;

import java.io.*;
import java.net.*;
import java.awt.event.*;
import javax.swing.*;

public class ServidorUDP extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    static DatagramSocket socket;
    static final int PUERTO = 55555;
    static int ACTUALES = 0;

    static JTextField mensaje = new JTextField("");
    private JScrollPane scrollpane1;
    static JTextArea textarea;
    JButton salir = new JButton("Salir");

    public ServidorUDP() {
        super(" Server UDP - Números Primos ");
        setLayout(null);

        mensaje.setBounds(10, 10, 400, 30);
        add(mensaje);
        mensaje.setEditable(false);

        textarea = new JTextArea();
        scrollpane1 = new JScrollPane(textarea);
        scrollpane1.setBounds(10, 50, 400, 300);
        add(scrollpane1);

        salir.setBounds(420, 10, 100, 30);
        add(salir);

        textarea.setEditable(false);
        salir.addActionListener(this);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == salir) {
            socket.close();
            System.exit(0);
        }
    }

    public static void main(String args[]) throws IOException {
        socket = new DatagramSocket(PUERTO);

        ServidorUDP pantalla = new ServidorUDP();
        pantalla.setBounds(0, 0, 540, 400);
        pantalla.setVisible(true);

        mensaje.setText("Esperando peticiones...");
        textarea.append("=== SERVIDOR UDP - NÚMEROS PRIMOS ===\n");
        textarea.append("Escuchando en puerto " + PUERTO + "\n\n");

        byte[] buffer = new byte[1024];

        while (true) {
            DatagramPacket paquete = new DatagramPacket(buffer, buffer.length);
            try {
                socket.receive(paquete);
            } catch (SocketException se) {
                break;
            }
            ACTUALES++;
            mensaje.setText("PETICIONES ATENDIDAS: " + ACTUALES);
            HiloServidorUDP hilo = new HiloServidorUDP(socket, paquete);
            hilo.start();
        }
        System.out.println("Servidor finalizado...");
    }
}