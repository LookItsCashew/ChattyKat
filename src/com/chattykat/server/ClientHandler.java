package com.chattykat.server;

import java.io.PrintWriter;
import java.net.*;
import java.util.List;
import java.io.*;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private List<ClientHandler> clients;
    private PrintWriter out;
    private BufferedReader in;

    public ClientHandler(Socket socket, List<ClientHandler> clients) throws IOException {
        this.clientSocket = socket;
        this.clients = clients;
        this.out = new PrintWriter(this.clientSocket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
    }

    public Socket getClientSocket() {
        return this.clientSocket;
    }

    @Override
    public void run() {
        try {
            String inputLine;
            while ((inputLine = this.in.readLine()) != null) {
                for (var client : this.clients) {
                    client.out.println(inputLine);
                }
            }
        } catch (IOException ex) {
            IO.println("ERROR: " + ex.getMessage());
        } finally {
            try {
                this.in.close();
                this.out.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }
    }
}
