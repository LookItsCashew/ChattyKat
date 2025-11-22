package com.chattykat.server;

import java.net.*;
import java.util.*;
import java.util.random.*;
import com.chattykat.ChattyKatConstants;

import java.io.IOException;

public class ChattyServer {
    static List<ClientHandler> clients = new ArrayList<>();

    static void main() {
        try (var serverSocket = new ServerSocket(ChattyKatConstants.APPLICATION_PORT)) {
            System.out.println("Server running, listening for incoming connections on port "
                    .concat(String.valueOf(ChattyKatConstants.APPLICATION_PORT))
                    .concat("...")
            );

            Random rand = new Random(10); // seed given for consistent clientIds

            while (true) {
                // Continuously monitor for incoming connections until interrupted
                Socket clientSocket = serverSocket.accept();

                // spawn clientHandler to handle each client
                ClientHandler clientHandler = new ClientHandler(clientSocket, clients);
                clientHandler.assignClientId(rand.nextInt());
                System.out.println("Client " + clientHandler.getClientId() + " connected");

                clients.add(clientHandler);
                new Thread(clientHandler).start();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
