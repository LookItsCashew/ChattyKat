package com.chattykat.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import com.chattykat.ChattyKat;
import com.chattykat.ChattyKatConstants;

public class ClientGUI extends JFrame {
    JTextArea messageArea;
    JTextField textField;
    ChattyClient client;

    public ClientGUI() {
        super("ChattyKat");
        setSize(400, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        messageArea = new JTextArea();
        messageArea.setEditable(false);
        add(new JScrollPane(messageArea), BorderLayout.CENTER);

        textField = new JTextField();
        textField.addActionListener(e -> {
            client.sendMessage(textField.getText());
            textField.setText("");
        });
        add(textField, BorderLayout.SOUTH);

        try {
            client = new ChattyClient(
                    ChattyKatConstants.SERVER_IP,
                    ChattyKatConstants.APPLICATION_PORT,
                    this::onMessageReceived,
                    this::onConnectedToRoom
                    );
            client.startClient();
        } catch (IOException ex) {
            IO.println("ERROR: " + ex.getMessage());
            JOptionPane.showMessageDialog(
                    this,
                    "Error connecting to server",
                    "Connection error",
                    JOptionPane.ERROR_MESSAGE
            );
            System.exit(1);
        }
    }

    void onMessageReceived(String message) {
        SwingUtilities.invokeLater(() -> messageArea.append(message + "\n"));
    }

    void onConnectedToRoom(int clientId) {
        SwingUtilities.invokeLater(
                () -> setTitle(String.format("ChattyKat - Client %d", clientId))
        );
    }

    public static  void main() {
        SwingUtilities.invokeLater(() -> {
            new ClientGUI().setVisible(true);
        });
    }
}
