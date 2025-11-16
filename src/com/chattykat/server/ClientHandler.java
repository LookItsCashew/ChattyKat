package com.chattykat.server;

import java.io.PrintWriter;
import java.net.*;
import java.util.List;
import java.io.*;
import java.math.*;

public class ClientHandler implements Runnable {
    private int clientId;
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

    public void assignClientId(int assignedId) {
        if (clientId == 0 && assignedId != 0) {
            clientId = Math.abs(assignedId); // make sure the id is always positive
        }
    }

    public int getClientId() {
        return clientId;
    }

    @Override
    public void run() {
        try {
            String inputLine;
            while ((inputLine = this.in.readLine()) != null) {
                for (var client : this.clients) {
                    client.out.println(String.format("[%d]: %s", this.clientId, inputLine));
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
