import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.*;
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

public class ChatClient {
    private Socket clientSocket;
    private DataOutputStream dos;
    private BufferedReader br;
    private JFrame frame = new JFrame("Group Chat");
    private JPanel panel = new JPanel();
    private String username;
    private Scanner sc = new Scanner(System.in);

    public ChatClient(){
        run();
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
    public void run(){
        System.out.print("Enter username: ");
        username = sc.next(); //get username
        try{
            clientSocket = new Socket("localhost", 4321);
            dos = new DataOutputStream(clientSocket.getOutputStream());
            br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            dos.writeBytes(username); //how to differentiate between new login and message?

            //wait for acknowledgment for username ie if(serverAck())

            while(true){
                dos.writeBytes(toSend()); //write messages to server
                //use frame to show messages - connect BufferedReader for server input stream
                //break if we get logging out ack
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    //get messages to send
    private String toSend(){
        String message;
        //prompt user for message
        System.out.print("/nNew message to group: ");
        message = sc.next();
        System.out.print(message);
        return message;
    }
    //main to test
    public static void main(String[] args){
        new ChatClient();
    }
}
