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
            IO.println(String.format(
                    "Server running at %s, listening for incoming connections on port %d...",
                    ChattyKatConstants.SERVER_IP,
                    ChattyKatConstants.APPLICATION_PORT
            ));

            Random rand = new Random(10); // seed given for consistent clientIds

            while (true) {
                // Continuously monitor for incoming connections until interrupted
                Socket clientSocket = serverSocket.accept();

                // spawn clientHandler to handle each client
                ClientHandler clientHandler = new ClientHandler(clientSocket, clients);
                clientHandler.assignClientId(rand.nextInt());
                IO.println(String.format("Client %d connected", clientHandler.getClientId()));

                clients.add(clientHandler);
                new Thread(clientHandler).start();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
