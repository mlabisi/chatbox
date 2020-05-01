package edu.cpp.cs.networks.attm;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.*;
import java.util.logging.Logger;

/**
 * This class is the TCP server for the chat application.
 */
public class ChatServer {
    private final int port;
    private int cliCt;
    private Map<String, DataOutputStream> users;

    private static final Logger LOG = Logger.getLogger(ChatServer.class.getName());

    /**
     * Creates the server.
     *
     * @param port Server's port number
     */
    public ChatServer(int port) {
        this.port = port;
        this.cliCt = 0;
        this.users = new HashMap<>();
    }

    public void startListening() {
        try (ServerSocket server = new ServerSocket(this.port)) {
            LOG.info("🔊 Server now listening on port " + this.port);
            while (true) {
                Socket client = server.accept();
                LOG.info("➕ Client connection #" + ++cliCt + " made");

                new ClientThread(client).start();
            }
        } catch (IOException e) {
            LOG.severe("‼️ Could not start client thread\n" + e.toString());
        }
    }

    /**
     * This method will send the same message to all clients that are currently connected to the server.
     *
     * @param message The message to be sent
     */
    private void broadcast(String message) {
        if(users.isEmpty()) {
            return;
        }

        users.values().parallelStream().forEach(
                stream -> {
                    try {
                        stream.writeUTF(message + "\n" + new Timestamp(System.currentTimeMillis()));
                    } catch (IOException e) {
                        LOG.severe("‼️ Could not broadcast to clients\n" + e.toString());
                    }
                });
    }

    /**
     * This method will send the same message to all clients who have not been marked as "excluded". This method is
     * used on the backend to announce newly connected users to the other members in the chat.
     *
     * @param message The message to be sent
     * @param exclude The excluded users who should not receive this message
     */
    private void shadowBroadcast(String message, String... exclude) {
        if(users.isEmpty()) {
            return;
        }

        users.entrySet().parallelStream().filter(entry -> !Arrays.asList(exclude).contains(entry.getKey())).forEach(
                entry -> {
                    try {
                        entry.getValue().writeUTF(message + "\n" + new Timestamp(System.currentTimeMillis()));
                    } catch (IOException e) {
                        LOG.severe("‼️ Could not broadcast to clients\n" + e.toString());
                    }
                });
    }

    /**
     * This method will send a message to the specified user. This method is used on the backend to welcome a newly
     * connected user to the chat room.
     *
     * @param username The user to send the message to
     * @param message The message to be sent
     * @param timestamp Whether or not the timestamp should be included
     * @throws IOException
     */
    private void directMessage(String username, String message, boolean timestamp)  throws IOException{
        if(users.isEmpty()) {
            return;
        }

        users.get(username).writeUTF(message + (timestamp ? "\n" + new Timestamp(System.currentTimeMillis()) : ""));
    }

    /**
     * Creates the chat server and runs it.
     *
     * @param args Holds command line arguments
     */
    public static void main(String[] args) {
        ChatServer chatServer = new ChatServer(4321);
        chatServer.startListening();
    }

    /**
     * This inner class manages the server's interaction with an individual client.
     */
    private class ClientThread extends Thread {
        private Socket client;
        private ClientStatus currentStatus;
        private String username;
        private DataInputStream inFromClient;
        private DataOutputStream outToClient;

        /**
         * Creates a new client thread for the given client.
         *
         * @param client The client that this thread represents
         */
        private ClientThread(Socket client) {
            this.client = client;
            this.currentStatus = ClientStatus.NOT_REGISTERED;
            this.username = "";
            initializeIO();
        }

        /**
         * Runs the thread.
         */
        public void run() {
            register();
            runChatLoop();
        }

        /**
         * Called within the constructor, this function initializes the input and output streams.
         */
        private void initializeIO() {
            try {
                this.inFromClient = new DataInputStream(client.getInputStream());
                this.outToClient = new DataOutputStream(client.getOutputStream());
                LOG.info("✅ Client I/O streams initialized on server side");
            } catch (IOException e) {
                LOG.severe("‼️ Could not initialize client IO streams on server side\n" + e.toString());
            }
        }

        /**
         * Handles the creation of the username and stores the user representation in memory
         */
        synchronized private void register() {
            try {
                int attemptCt = 0;
                do {
                    attemptCt++;
                    outToClient.writeUTF(attemptCt == 1
                            ? currentStatus.toString()
                            : "Oops! That username has been taken. " + currentStatus.toString());
                    username = inFromClient.readUTF();
                } while(users.containsKey(username));

                users.put(username, outToClient);
                directMessage(username, "Welcome " + username + "!", false);
                shadowBroadcast(username + (currentStatus = ClientStatus.LOGGED_IN).toString(), username);
                LOG.info("✅ " + username + " registered successfully");
            } catch (IOException e) {
                LOG.severe("‼️ Could not communicate with client\n" + e.toString());
            }
        }

        /**
         * Handles the disconnection of the user and removes them from in memory storage
         */
        synchronized private void logout() {
            try {
                inFromClient.close();
                outToClient.close();
                client.close();
                users.remove(username);
                shadowBroadcast(username + (currentStatus = ClientStatus.LOGGING_OUT).toString(), username);
                LOG.info("✅ " + username + " disconnected successfully");
            } catch (IOException e) {
                LOG.severe("‼️ Could not close client connection\n" + e.toString());
            }
        }

        /**
         * Reads in messages from this client and broadcasts them for everyone until the disconnect symbol is entered.
         */
        private void runChatLoop() {
            try {
                String message;
                while (!(message = inFromClient.readUTF()).equals(".")) {
                    broadcast(username + ": " + message);
                    LOG.info("✅ " + username + "'s message broadcast successful");
                    LOG.info("📢 " + message);
                }
                directMessage(username, ClientStatus.LOGGING_OUT.toString(), false);
                logout();
            } catch (IOException e) {
                LOG.severe("‼️ Could not broadcast client message\n" + e.toString());
            }
        }
    }
}
