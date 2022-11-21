package com.client;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

import com.fasterxml.jackson.databind.ObjectMapper;
public class OutputThreadClient extends Thread{
    private Socket socket;
    
    private DataOutputStream out;
    private Scanner keyboard = new Scanner(System.in);
    private String userString;

    public Socket connect() throws IOException
    {
        this.socket = new Socket(InetAddress.getLocalHost(), 34567);
    
        this.out = new DataOutputStream(socket.getOutputStream());
        this.userString = "";
        return socket;
    }

    public void startOut() throws IOException
    {
        while (true)
        {
            System.out.print("Inserisci la stringa da trasmettere al server" + '\n');
            this.userString = this.keyboard.next();
            
            if(userString.toUpperCase().equals("FINE"))
            {
                break;
            }

            else{
                out.writeBytes(userString + '\n');
                
                System.out.println("Risposta dal server: " + '\n' );
            }
            
            
        }
        
        socket.close();
    }
}


