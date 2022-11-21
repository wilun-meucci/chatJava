package com.server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ManagementServerThread extends Thread
{
    ArrayList<Socket> clientsArray = new ArrayList<Socket>();
    public ServerSocket server;
    public ManagementServerThread (ServerSocket server) { this.server = server;}
    public void addClient(Socket client)
    {
        clientsArray.add(client);
    }
    public void closeClient(ServerSocket server) throws IOException
    {
        for (Socket clients : clientsArray) {
            DataOutputStream out = new DataOutputStream(clients.getOutputStream());
            out.writeBytes("morto" + '\n');
            clients.close();
            System.out.println("chiuso");

        }
        server.close();

    }

}
