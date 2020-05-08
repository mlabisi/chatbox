package edu.cpp.cs.networks.attm;


import java.io.*;
import java.net.*;
import java.sql.Timestamp;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    private ChatUI window;

    private String message;
    private boolean userNameVerified;

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

        this.window = new ChatUI();
        this.window.initUsernameDialog();
        this.window.initMessageDialog();
        this.message = "";
        this.userNameVerified = false;

        ActionListener nameListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                message = window.field.getText();
                try {
                    outToServer.writeUTF(message);
                } catch (IOException ex) {
                    LOG.severe("‼️ Couldn't get username from client\n");
                }
            }
        };
        window.getSubmitButton().addActionListener(nameListener);
        window.field.addActionListener(nameListener);

        ActionListener sendListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent a) {
                message = window.getTextField().getText();
                window.getTextField().setText("");
                window.getTextField().requestFocusInWindow();
                try {
                    if (message.length() > 0) {
                        if (message.equals(".")) {
                            connected = false;
                        }
                        outToServer.writeUTF(message);
                    }
                } catch (IOException e) {
                    LOG.severe("‼️ Client couldn't send message to server\n");
                }
            }
        };
        window.getSendButton().addActionListener(sendListener);
        window.getTextField().addActionListener(sendListener);

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

    /**
     * The client side is encapsulated in this method. One thread is created for capturing messages from the server
     * and displaying them. The listeners in the constructor take care of capturing user input and sending it to
     * the server
     */
    public void start() {
        Thread listen = new Thread(new Runnable() {
            @Override
            public void run() {
                LOG.info("‼️ Client listener thread started\n");
                while (connected) {
                    try {
                        String line = inFromServer.readUTF();
                        userNameVerified = !(line.startsWith("Oops! That username has been taken.") || line.startsWith("Please enter your desired username:"));
                        if (line.equals(ClientStatus.LOGGING_OUT.toString())) {
                            connected = false;
                            window.showMessages(false);
                            System.exit(0);

                        } else if (!userNameVerified) {
                            window.showMessages(false);
                            window.showUsernameScreen(true);
                            window.changeLabel(line);
                        } else if (line.startsWith("Welcome")) { // this might not work?
                            window.showMessages(true);
                            window.showUsernameScreen(false);
                            window.writeMessage(line);
                        } else {
                            window.writeMessage(line);
                        }
                    } catch (IOException e) {
                        LOG.severe("‼️ Client couldn't read line from server\n");
                    }
                }
            }
        });

        listen.start();
    }

    /**
     * Create a chat client and start it. Make sure to run this after the server has been started.
     *
     * @param args Holds command line arguments
     */
    public static void main(String[] args) {
//        String hostName = "34.209.49.228"; // aws server
        String hostName = "localhost"; // local server

        ChatClient c1 = new ChatClient(hostName, 4321);
        c1.start();
    }
}

