package edu.cpp.cs.attm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import static edu.cpp.cs.attm.Status.*;

public class Server {
    private final int port;
    private Set<String> usernames;
    private Set<PrintWriter> clientStreams;

    private static final Logger LOG = Logger.getLogger(Server.class.getName());

    public Server(int port) {
        this.port = port;
        this.usernames = new HashSet<>();
        this.clientStreams = new HashSet<>();
    }

    private void startListening() {
        try(ServerSocket server = new ServerSocket(this.port)) {
            LOG.info("📞 Server now listening on port " + this.port);
            while (true) {
                Socket client = server.accept();
                LOG.info("➕ New client connection made");

                ClientThread thread = new ClientThread(client);
                thread.start();
            }
        } catch (IOException e) {
            LOG.severe("‼️ Could not start client thread\n" + e.getMessage());
        }
    }

    private void broadcast(String message) {
        clientStreams.parallelStream().forEach(
                stream -> stream.println(message + "\n" + new Timestamp(System.currentTimeMillis())));
    }

    public static void main(String[] args) {
        Server chatServer = new Server(4321);
        chatServer.startListening();
    }

    private class ClientThread extends Thread {
        private Socket client;
        private Status currentStatus;
        private String username;
        private BufferedReader inFromClient;
        private PrintWriter outToClient;

        private ClientThread(Socket client) {
            this.client = client;
            this.currentStatus = NOT_REGISTERED;
            this.username = "";
            initializeIO();
        }

        public void run() {
            register();
            runChatLoop();
            logout();
        }

        private void initializeIO() {
            try {
                this.inFromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
                this.outToClient = new PrintWriter(this.client.getOutputStream(), true);
                LOG.info("✅ Client I/O streams initialized");
            } catch (IOException e) {
                LOG.severe("‼️ Could not initialize client IO streams\n" + e.getMessage());
            }
        }

        private void register() {
            try {
                outToClient.println(currentStatus.getMessage());
                while(usernames.contains(username = inFromClient.readLine())) {
                    register();
                }

                usernames.add(username);
                clientStreams.add(outToClient);
                broadcast(username + (currentStatus = LOGGED_IN).getMessage());
                LOG.info("✅ " + username + " registered successfully");
            } catch (IOException e) {
                LOG.severe("‼️ Could not communicate with client\n" + e.getMessage());
            }
        }

        private void logout() {
            try {
                usernames.remove(username);
                clientStreams.remove(outToClient);
                client.close();
                broadcast(username + (currentStatus = LOGGING_OUT).getMessage());
                LOG.info("✅ " + username + " disconnected successfully");
            } catch (IOException e) {
                LOG.severe("‼️ Could not close client connection\n" + e.getMessage());
            }
        }

        private void runChatLoop() {
            try {
                String message;
                while (!(message = inFromClient.readLine()).equals(".")) {
                    broadcast(message);
                    LOG.info("✅ " + username + "'s message broadcast successful");
                    LOG.info("📢 " + message);
                }
            } catch (IOException e) {
                LOG.severe("‼️ Could not broadcast client message\n" + e.getMessage());
            }
        }
    }
}
