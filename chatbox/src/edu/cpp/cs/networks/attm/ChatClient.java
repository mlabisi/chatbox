package edu.cpp.cs.networks.attm;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

public class ChatClient {
    private Socket clientSocket;
    private String hostName;
    private int portNum;
    private String status;
    private DataOutputStream dos;
    private BufferedReader br;
    private JFrame frame = new JFrame("Group Chat");
    private JPanel panel = new JPanel();
    private String username;
    private Scanner sc = new Scanner(System.in);

    public ChatClient(String host, int port){
        this.hostName = host;
        this.portNum=port;
        //openChatBox();
    }
    //get username and show chat
    private void openChatBox(){
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
                username = field.getText();
            }
        });
    }

    //connect to the server
    public void start(){
        try{
            clientSocket = new Socket(hostName, portNum); //connect to server
            dos = new DataOutputStream(clientSocket.getOutputStream());
            br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            status = br.readLine(); //get ack from server

            //user not registered yet
            if(status.equals("Please enter your desired username")){
                System.out.print("Welcome!\n" + status + ": ");
                username = sc.next();
                dos.writeBytes(username);
                status = br.readLine();
                System.out.print(status);
            }
            //while connected, get messages
            while(status.equals(" Connected")){
                status = chat(status, br, dos);
            }
        }
        catch(Exception e){
           System.out.println("We couldn't connect to the server :(");
        }
    }

    //only chatting if connected -- return status
    private String chat(String serverStatus, BufferedReader reader, DataOutputStream outStream) throws IOException{
        String sendMsg, rcvMsg, status = serverStatus;
            rcvMsg = reader.readLine(); //get input from server

            //keep receiving and sending messages -- only if messages are still being sent
            while((rcvMsg)!=null){
                //if you disconnect then return disconnected status
                if(rcvMsg.equals( " Disconnected")){
                    status = " Disconnected";
                }
                System.out.print(rcvMsg);
                rcvMsg = reader.readLine();
            }
            System.out.print("\nNew message: ");
            sendMsg = sc.next();
            outStream.writeBytes(sendMsg);
            return status;
    }

    //main to test
    public static void main(String[] args){
        ChatClient c1 = new ChatClient("localhost", 4321);
        c1.start();
    }
}
