package edu.cpp.cs.networks.attm;

import javax.swing.*;
import java.awt.*;

public class ChatUI {
    public JFrame usernameFrame;
    private JPanel usernamePanel;
    private JFrame messageFrame;
    private JPanel messagePanel;
    public JTextField field;
    private JTextArea seeChat;
    JScrollPane scroll;
    private JTextField typeMessage;
    private JButton submit;
    private JButton send;
    public boolean openedChat;
    public JLabel label;

    public ChatUI() {
        usernameFrame = new JFrame("Group Chat");
        usernamePanel = new JPanel();
        messageFrame = new JFrame("Group Chat");
        messagePanel = new JPanel();
        seeChat = new JTextArea("Welcome ");
        openedChat = false;
        label= new JLabel("Please enter your desired username");
    }

    /**
     * Initialize the username prompt
     */
    public void initUsernameDialog() {
        usernamePanel.setBorder(BorderFactory.createEmptyBorder(50, 250, 100, 250));
        usernameFrame.add(usernamePanel, BorderLayout.CENTER);
        submit = new JButton("Submit");
        field = new JTextField(30);
        usernameFrame.pack();
        usernameFrame.setVisible(false);
        usernameFrame.setResizable(false);
        usernamePanel.add(label);
        usernamePanel.add(field);
        usernamePanel.add(submit);
        usernameFrame.add(usernamePanel);
    }

    /**
     * Initialize the message dialog
     */
    public void initMessageDialog(){
        messagePanel.setBorder(BorderFactory.createEmptyBorder(50, 300, 350, 300));//100, 300, 300, 300));
        messageFrame.add(messagePanel, BorderLayout.CENTER);
        messageFrame.setResizable(false);
        send = new JButton("Send");
        seeChat=new JTextArea(16,48);
        scroll = new JScrollPane (seeChat);
        typeMessage= new JTextField(50);
        seeChat.setEditable(false);
        typeMessage.setEditable(true);
        messageFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        messageFrame.setSize(400,300);
        messageFrame.pack();
        messageFrame.setVisible(false);
        // messagePanel.add(seeChat);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        messagePanel.add(scroll);
        messagePanel.add(typeMessage);
        messagePanel.add(send);
        messageFrame.add(messagePanel);

        openedChat=true;

    }
    public JButton getSendButton() {
        return send;
    }
    public JButton getSubmitButton() {
        return submit;
    }
    public void changeLabel(String text){
        label.setText(text);
    }

    /**
     * Display the Messages Dialog
     * @param show
     */
    public void showMessages(Boolean show){

        messageFrame.setVisible(show);
    }

    /**
     * Display the Username Dialog
     * @param show
     */
    public void showUsernameScreen(Boolean show){

        usernameFrame.setVisible(show);
    }

    public JTextField getTextField(){
        return typeMessage;
    }

    /**
     * write message to frame
     */
    public void writeMessage(String mssg){

        seeChat.append(mssg+"\n");
    }

    public void close() {

    }

}
