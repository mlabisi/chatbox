package edu.cpp.cs.networks.attm;


import java.io.*;
import java.net.*;
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

    ChatUI window = new ChatUI();

    private String message="";
    private String username="";
    private boolean userNameVerified=false;

    private static final Logger LOG = Logger.getLogger(ChatClient.class.getName());

    /**
     * Creates an individual Chat client.
     *
     * @param host The hostname of the server
     * @param port The server's port number
     */
    public ChatClient(String host, int port) throws InterruptedException {
        this.hostName = host;
        this.portNum = port;
        initializeSocket();
        initializeIO();
        window.openMessageDialog();
        try {
            window.getSendButton().addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent a) {
                    message = window.getTextField().getText();
                    window.getTextField().setText("");
                }
            });
        }catch(NullPointerException e){

        }
        window.enterUsername();
        window.getSubmitButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                message = window.field.getText();
                window.showUsernameScreen(true);
            }
        });

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
                        if(userNameVerified==false){
                            window.showUsernameScreen(true);
                        }else if(userNameVerified==true) {
                            window.showUsernameScreen(false);
                            window.showMessages(true);
                        }

                        if(message.length()>0) {
                            if (message.equals(".")) {
                                connected = false;
                            }
                            System.out.println("'"+message+"'");
                            outToServer.writeUTF(message);

                        }
                        message="";
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
                        if(line.startsWith("Oops! That username has been taken.")){
                            window.changeLabel(line);
                            window.showUsernameScreen(true);
                        }else if(!line.contains("Please enter your desired username: ")) {
                            userNameVerified=true;
                            window.showMessages(true);
                            window.writeMessage(line);
                        }
                        if (line.equals(ClientStatus.LOGGING_OUT.toString())) {
                            connected = false;
                            window.showMessages(false);
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
    public static void main(String[] args) throws InterruptedException {
        String hostName = "34.209.49.228";

        ChatClient c1 = new ChatClient("127.0.0.1", 4321);
        c1.start();
    }
}

