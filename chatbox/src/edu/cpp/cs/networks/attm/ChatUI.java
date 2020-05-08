package edu.cpp.cs.networks.attm;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import java.awt.*;
import java.awt.event.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class ChatUI {
    public JFrame usernameFrame;
    private JPanel usernamePanel;
    private JFrame messageFrame;
    private JPanel messagePanel;
    public JTextField field;
    private JScrollPane scroll;
    private JTextPane chat;
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
        send = new JButton("Send");

        chat = new JTextPane();
        chat.setText("");
        chat.setEditable(false);

        chat.setBackground(Color.white);
        chat.setBorder(new EmptyBorder(10, 10, 0, 10));
        JPanel noWrapPanel = new JPanel(new BorderLayout());
        scroll = new JScrollPane(chat);
        noWrapPanel.add(scroll);
        typeMessage = new JTextField();

        typeMessage.setEditable(true);
        messageFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        messageFrame.setSize(400, 300);
        messageFrame.pack();
        messageFrame.setVisible(false);

        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 3;
        messagePanel.add(scroll, c);

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
        try {

            Document doc = chat.getDocument();
            for (String line : mssg.split("\n")) {
                // test if this line is a valid timestamp, and format
                try {
                    Timestamp time = Timestamp.valueOf(line);
                    String t = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a").format(time);

                    SimpleAttributeSet set = new SimpleAttributeSet();
                    StyleConstants.setForeground(set, Color.gray);
                    doc.insertString(doc.getLength(), t + "\n" + "\n", set);
                
                // if this is not a timestamp
                } catch (IllegalArgumentException e) {
                    SimpleAttributeSet set = new SimpleAttributeSet();

                    // if this was a user message, colorize
                    try {
                        String username = line.substring(0, line.indexOf(":"));
                        StyleConstants.setForeground(set, getUniqueColor(username));
                    
                    // else it was a system message (connect / disconnect)
                    } catch (IndexOutOfBoundsException ooe) {
                        System.err.println(ooe);
                        System.out.println("caused by:");
                        System.out.println(mssg);
                    }

                    doc.insertString(doc.getLength(), line + "\n", set);

                }

            }

            chat.revalidate();
            scroll.revalidate();
            
            chat.setCaretPosition(doc.getLength());
        } catch (Exception e) {
            System.err.println("exception in writeMessage");
            System.err.println(e);
        }
    }

    public void close() {

    }

    public Color getUniqueColor(String name) {
        float hue = (float) (name.hashCode() % 360) / 360.0f;
        return new Color(Color.HSBtoRGB(hue, .75f, .55f));
    }

    private static class RoundedBorder implements Border {

        private int radius;
        private Color color;

        RoundedBorder(int radius, Color color) {
            this.radius = radius;
            this.color = color;
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
            g.setColor(this.color);
            g.fillRoundRect(x, y, width, height, radius * 2, radius * 2);
            // g.clearRect(+radius, y+radius, width - (radius*2), height - (radius*2));
        }
    }

}
