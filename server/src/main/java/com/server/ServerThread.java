package com.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
public class ServerThread extends Thread{
    public Socket client;
    public  ServerSocket server;
    public ManagementServerThread m;
    ServerThread (Socket client, ManagementServerThread m, ServerSocket server)
    {
        this.client = client;
        this.m = m;
        this.server = server;
    }
    public void run() {
        try {
            communicate();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("ciao");
        }
    }
    public void communicate() throws IOException {

        String recv = "";
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        DataOutputStream out = new DataOutputStream(client.getOutputStream());

        System.out.println(in);
        while(!recv.toUpperCase().equals("FINE")){

            recv = in.readLine();
            if(recv.toUpperCase().equals("SPEGNI"))
            {

                m.closeClient(server);
                return ;
            }
            System.out.println(recv);
            System.out.println("Stringa ricevuta: " + recv);
            String modifiedRecv = recv.toUpperCase();
            out.writeBytes(modifiedRecv + '\n');

        }
        client.close();

    }
}
