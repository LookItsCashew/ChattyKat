package com.chattykat.server;

import java.net.*;
import java.util.*;
import com.chattykat.ChattyKatConstants;

import java.io.IOException;

public class ChattyServer {
    static List<ClientHandler> clients = new ArrayList<>();

    public static void main() {
        try (var serverSocket = new ServerSocket(ChattyKatConstants.APPLICATION_PORT)) {
            IO.println(String.format(
                    "Server running at %s, listening for incoming connections on port %d...",
                    ChattyKatConstants.SERVER_IP,
                    ChattyKatConstants.APPLICATION_PORT
            ));

            // Continuously monitor for incoming connections until interrupted
            while (true) {
                Socket clientSocket = serverSocket.accept();
                IO.println("Client connected: ".concat(clientSocket.toString()));

                // spawn thread to handle each client
                ClientHandler thread = new ClientHandler(clientSocket, clients);
                clients.add(thread);
                new Thread(thread).start();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
