package edu.cpp.cs.networks.attm;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.*;

public class ChatUI {
    public JFrame usernameFrame;
    private JPanel usernamePanel;
    private JFrame messageFrame;
    private JPanel messagePanel;
    public JTextField field;
    private JScrollPane scroll;
    private Box chat;
    private JTextField typeMessage;
    private JButton submit;
    private JButton send;
    public boolean openedChat;
    public JLabel label;

    public ChatUI() {
        usernameFrame = new JFrame("Group Chat");
        usernamePanel = new JPanel();
        messageFrame = new JFrame("Group Chat");
        messagePanel = new JPanel(new GridBagLayout());

        openedChat = false;
        label = new JLabel("Please enter your desired username");
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
    public void initMessageDialog() {
        messagePanel.setPreferredSize(new Dimension(400, 300));
        messageFrame.add(messagePanel, BorderLayout.CENTER);
        // messageFrame.setResizable(false);
        send = new JButton("Send");

        chat = Box.createVerticalBox();
        // chat.setLayout(new BoxLayout(chat, BoxLayout.Y_AXIS));
        chat.setBackground(Color.white);
        chat.setBorder(new EmptyBorder(10, 10, 0, 10));
        scroll = new JScrollPane(chat);
        typeMessage = new JTextField();
        // seeChat.setEditable(false);
        typeMessage.setEditable(true);
        messageFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        messageFrame.setSize(400, 300);
        messageFrame.pack();
        messageFrame.setVisible(false);

        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 3;
        messagePanel.add(scroll, c);

        scroll.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {  
            public void adjustmentValueChanged(AdjustmentEvent e) {  
                e.getAdjustable().setValue(e.getAdjustable().getMaximum());  
            }
        });

        c = new GridBagConstraints();
        c.gridy = 1;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        messagePanel.add(typeMessage, c);

        c = new GridBagConstraints();
        c.gridy = 1;
        c.weightx = 0;
        c.gridx = 2;
        messagePanel.add(send, c);
        messageFrame.add(messagePanel);

        openedChat = true;

    }

    public JButton getSendButton() {
        return send;
    }

    public JButton getSubmitButton() {
        return submit;
    }

    public void changeLabel(String text) {
        label.setText(text);
    }

    /**
     * Display the Messages Dialog
     * 
     * @param show
     */
    public void showMessages(Boolean show) {

        messageFrame.setVisible(show);
    }

    /**
     * Display the Username Dialog
     * 
     * @param show
     */
    public void showUsernameScreen(Boolean show) {

        usernameFrame.setVisible(show);
    }

    public JTextField getTextField() {
        return typeMessage;
    }

    public void writeMessage(String mssg) {
        writeMessage(mssg, MessageTypes.MESSAGE);
    }

    /**
     * write message to frame
     */
    public void writeMessage(String mssg, MessageTypes type) {

        // messagesHTML += "<span class=\"message\">" + mssg + "</span>";
        JPanel box = new JPanel();
        box.setOpaque(false);
        box.setBorder(new RoundedBorder(10));
        JLabel label = new JLabel(mssg);
        label.setHorizontalAlignment(SwingConstants.LEFT);
        box.add(label);
        label.setText(mssg);

        box.setMaximumSize(box.getPreferredSize());
        box.setAlignmentX(Component.LEFT_ALIGNMENT);

        chat.add(box);
        chat.add(Box.createVerticalStrut(10));
        chat.revalidate();
    }

    public void close() {

    }

    private static class RoundedBorder implements Border {

        private int radius;

        RoundedBorder(int radius) {
            this.radius = radius;
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(this.radius, this.radius, this.radius, this.radius);
        }

        @Override
        public boolean isBorderOpaque() {
            return true;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.setColor(new Color(245, 245, 245));
            g.fillRoundRect(x, y, width, height, radius * 2, radius * 2);
            // g.clearRect(+radius, y+radius, width - (radius*2), height - (radius*2));
        }
    }

}
