package Cliente;

import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class ClienteUDP extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    DatagramSocket socket;
    InetAddress direccionServidor;
    static final int PUERTO = 55555;

    static JTextField campoNumero = new JTextField();
    private JScrollPane scrollpane1;
    static JTextArea textarea;
    JButton botonEnviar = new JButton("Enviar");
    JButton botonSalir = new JButton("Salir");

    public ClienteUDP(String host) throws IOException {
        super(" Cliente UDP - Números Primos ");
        setLayout(null);

        campoNumero.setBounds(10, 10, 400, 30);
        add(campoNumero);

        textarea = new JTextArea();
        scrollpane1 = new JScrollPane(textarea);
        scrollpane1.setBounds(10, 50, 400, 300);
        add(scrollpane1);

        botonEnviar.setBounds(420, 10, 100, 30);
        add(botonEnviar);

        botonSalir.setBounds(420, 50, 100, 30);
        add(botonSalir);

        textarea.setEditable(false);
        botonEnviar.addActionListener(this);
        botonSalir.addActionListener(this);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        socket = new DatagramSocket();
        direccionServidor = InetAddress.getByName(host);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == botonEnviar) {
            String texto = campoNumero.getText().trim();
            if (!texto.isEmpty()) {
                try {
                    // Enviar petición
                    byte[] datos = texto.getBytes();
                    DatagramPacket paquete = new DatagramPacket(
                            datos, datos.length, direccionServidor, PUERTO);
                    socket.send(paquete);
                    textarea.append("Enviado: " + texto + "\n");

                    // Recibir respuesta
                    byte[] buffer = new byte[4096];
                    DatagramPacket respuesta = new DatagramPacket(buffer, buffer.length);
                    socket.receive(respuesta);
                    String resultado = new String(respuesta.getData(), 0, respuesta.getLength());
                    textarea.append("Primos: " + resultado + "\n\n");

                    campoNumero.setText("");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

        if (e.getSource() == botonSalir) {
            socket.close();
            System.exit(0);
        }
    }

    public static void main(String args[]) {
        String host = "localhost";
        if (args.length > 0) host = args[0];

        try {
            ClienteUDP cliente = new ClienteUDP(host);
            cliente.setBounds(0, 0, 540, 400);
            cliente.setVisible(true);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al iniciar el cliente\n" + e.getMessage(),
                    "<<ERROR>>", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }
}