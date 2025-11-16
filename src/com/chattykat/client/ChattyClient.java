package com.chattykat.client;

import java.net.*;
import java.io.*;
import java.util.function.Consumer;

import com.chattykat.server.ClientHandler;

public class ChattyClient {
    private Socket socket = null;
    private PrintWriter out = null;
    private BufferedReader in = null;
    private Consumer<String> onMessageReceived;
    private Consumer<Integer> onConnectedToRoom;
    private ClientHandler handler;

    public ChattyClient(
            String srvAddress,
            int port,
            Consumer<String> onMessageReceived,
            Consumer<Integer> onConnectedToRoom
    ) throws IOException {
        this.socket = new Socket(srvAddress, port);
        IO.println("Connected to server at "
                .concat(srvAddress)
                .concat(" on port ")
                .concat(String.valueOf(port))
        );

        //this.input = new BufferedReader(new InputStreamReader(System.in));
        this.out = new PrintWriter(this.socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        this.onMessageReceived = onMessageReceived;
        this.onConnectedToRoom = onConnectedToRoom;
        //this.onConnectedToRoom.accept();
        }

    public void sendMessage(String message) {
        this.out.println(message);
    }

    public void setHandler(ClientHandler srvHandler) {
        this.handler = srvHandler;
    }

    public void startClient() {
        new Thread(() -> {
            try {
               String line;
               while ((line = this.in.readLine()) != null) {
                   this.onMessageReceived.accept(line);
               }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }).start();
    }
}
