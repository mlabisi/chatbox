package edu.cpp.cs.networks.attm;

import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * This class represents an individual chat user.
 */
public class ChatClient {
    private Socket clientSocket;
    private String hostName;
    private int portNum;

    private boolean connected;
    private DataOutputStream outToServer;
    private DataInputStream inFromServer;

    private JFrame frame = new JFrame("Group Chat");
    private JPanel panel = new JPanel();
    private Scanner sc = new Scanner(System.in);

    private static final Logger LOG = Logger.getLogger(ChatClient.class.getName());

    /**
     * Creates an individual Chat client.
     *
     * @param host The hostname of the server
     * @param port The server's port number
     */
    public ChatClient(String host, int port) {
        this.hostName = host;
        this.portNum = port;
        initializeSocket();
        initializeIO();
        //openChatBox();
    }

    /**
     * Called within the constructor, this function creates the TCP socket for the client.
     */
    private void initializeSocket() {
        try {
            this.clientSocket = new Socket(this.hostName, this.portNum);
            this.connected = true;
        } catch (IOException e) {
            LOG.severe("‼️ Could not initialize client\n" + e.getMessage());
        }
    }

    /**
     * Called within the constructor, this function initializes the input and output streams.
     */
    private void initializeIO() {
        try {
            this.outToServer = new DataOutputStream(this.clientSocket.getOutputStream());
            this.inFromServer = new DataInputStream(clientSocket.getInputStream());
            LOG.info("✅ Client I/O streams initialized on client side");
        } catch (IOException e) {
            LOG.severe("‼️ Could not initialize client IO streams on client side\n" + e.getMessage());
        }
    }


    private void openChatBox() {
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
        //p.setLayout(new GridLayout(0,1));
        frame.add(panel, BorderLayout.CENTER);
        JTextField field = new JTextField(30);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //end program = close window
        frame.pack();
        frame.setVisible(true);
        panel.add(field);
        frame.add(panel);
        field.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
    }

    /**
     * The client side is encapsulated in this method. Two threads are created--one for capturing user input and sending
     * it to the server, and another for capturing messages from the server and displaying them.
     */
    public void start() {
        Thread chat = new Thread(new Runnable() {
            @Override
            public void run() {
                LOG.info("‼️ Client chat thread started\n");
                while (connected) {
                    try {
                        String message = sc.nextLine();
                        if (message.equals(".")) {
                            connected = false;
                        }
                        outToServer.writeUTF(message);
                    } catch (IOException e) {
                        LOG.severe("‼️ Client couldn't write line to server\n");
                    }
                }
            }
        });

        Thread listen = new Thread(new Runnable() {
            @Override
            public void run() {
                LOG.info("‼️ Client listener thread started\n");
                while (connected) {
                    try {
                        String line = inFromServer.readUTF();
                        if (line.equals(ClientStatus.LOGGING_OUT.toString())) {
                            connected = false;
                            return;
                        }
                        System.out.println(line);
                    } catch (IOException e) {
                        LOG.severe("‼️ Client couldn't read line from server\n");
                    }
                }
            }
        });

        chat.start();
        listen.start();
    }

    /**
     * Create a chat client and start it. Make sure to run this after the server has been started.
     *
     * @param args Holds command line arguments
     */
    public static void main(String[] args) {
        String hostName = "34.209.49.228";
        // String hostName = "localhost";

        ChatClient c1 = new ChatClient(hostName, 4321);
        c1.start();
    }
}
