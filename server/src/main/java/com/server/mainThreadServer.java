package com.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
public class mainThreadServer {

    public Socket start() throws IOException {
        ServerSocket server = new ServerSocket(34567);
        

        for (;;) {
            Socket client = server.accept();
            ServerThread t = new ServerThread(client, server);
           
           
            t.start();   
        }
    }
}
