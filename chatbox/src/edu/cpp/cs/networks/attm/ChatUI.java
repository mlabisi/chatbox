package edu.cpp.cs.networks.attm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChatUI {
    public JFrame frame = new JFrame("Group Chat");
    private JPanel panel = new JPanel();
    private JFrame frame1 = new JFrame("Group Chat");
    private JPanel panel1 = new JPanel();
    private JTextField field;
    private JTextArea seeChat= new JTextArea("Welcome ");
    private JTextField typeMessage;
    private JButton submit;
    private JButton send;
    private String username;
    public Boolean userNameEntered=false;
    private String message="";
    public boolean openedChat=false;
    public JLabel label= new JLabel("Please enter your desired username");

    /**
     * Opens the username prompt
     * NEEDS USERNAME CHECK
     */
    public String enterUsername() throws InterruptedException {

        panel.setBorder(BorderFactory.createEmptyBorder(50, 200, 100, 200));
        frame.add(panel, BorderLayout.CENTER);
        submit = new JButton("Submit");
        field = new JTextField(30);
        frame.pack();
        frame.setVisible(true);
        panel.add(label);
        panel.add(field);
        panel.add(submit);
        frame.add(panel);
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                username = field.getText();
                userNameEntered=true;
                frame.setVisible(false);
            }
        });
        while(userNameEntered==false){
            Thread.sleep(200);
        }
        return username;
    }

    /**
     * Open message dialog
     */
    public void openMessageDialog(){
        panel1.setBorder(BorderFactory.createEmptyBorder(50, 300, 350, 300));//100, 300, 300, 300));
        frame1.add(panel1, BorderLayout.CENTER);
        send = new JButton("Send");
        seeChat=new JTextArea(16,50);
        typeMessage= new JTextField(50);
        seeChat.setEditable(false);
        typeMessage.setEditable(true);
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame1.setSize(400,300);
        frame1.pack();
        frame1.setVisible(false);
        panel1.add(seeChat);
        panel1.add(typeMessage);
        panel1.add(send);
        frame1.add(panel1);

        openedChat=true;

    }
    public JButton getButton() {
        return send;
    }
    public void changeLabel(String text){
        label.setText(text);
    }
    public void showMessages(Boolean show){
        frame1.setVisible(show);
    }

    public void showUsernameScreen(){
        frame.setVisible(true);
    }

    public JTextField getTextField(){
        return typeMessage;
    }


    /**
     * write message to frame
     * returns message to be sent
     */
    public void writeMessage(String mssg){

        seeChat.append(mssg+"\n");
    }

}
