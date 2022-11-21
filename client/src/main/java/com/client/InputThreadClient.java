package com.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.http.WebSocket.Listener;
import java.util.Scanner;

import com.fasterxml.jackson.databind.ObjectMapper;

public class InputThreadClient extends Thread {

    private DataInputStream in;
    private Socket socket;

    private String serverString = "";

    public InputThreadClient(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            listenerS();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("ciao");
        }
    }

    public void listenerS() throws IOException {
        this.in = new DataInputStream(socket.getInputStream());
        while (true) {
            serverString = in.readLine();
            if (!serverString.equals("")) {
                System.out.println("Risposta dal server: " + '\n' + serverString);
            }

        }

    }
}
