package com.client;


import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.*;

import com.fasterxml.jackson.databind.ObjectMapper;

public class App 
{

    //questa variabile diventa true quando il server ha accettato la login (username valido per il server)
    //sarà modificato dal ThreadClientInput alla ricezione del messaggio login ok
    // 0: non ancora inviato
    // 1: inviato e risposta non ancora ricevuta
    // 2: inviato e accettato
    // 3 inviato e non accettato
    public static Interger userStatus = 0;


    public static void main( String[] args ) throws IOException, InterruptedException
    {
        Scanner keyboard = new Scanner(System.in);
        String userString;
        
        Socket socket = new Socket(InetAddress.getLocalHost(), 34567);
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        //DataInputStream  in = new DataInputStream(socket.getInputStream());
        System.out.println("prima del input");
        InputThreadClient inputClient = new InputThreadClient(socket);
        inputClient.start();
        System.out.println("dopo input");
    
        while (true)
        {//ciao 
            /*
             * 
             * { 
            “sendTo”: "*”;
            "type":"command",
            "text":"access", 
            "userName": "dawid"   username da validare dal server
}
             */
            
            do {
                login();
                Thread.sleep(500);
            } while (!userConnected);
            
            userString = "";

            
            if(userString.toUpperCase().equals("FINE"))
            {
                System.out.println("fine");
                break;
            }

            else{
                out.writeBytes(userString + '\n');
                
                
            }

            Message m1 = new Message("#","command","access","userName");

            if(m1.getSendTo().equals("*"))
            {
                //messaggio rivolto a tutti

            }
            
        }
        
        socket.close();
    }

   private static void login() {
        String userName = "";
         while(true)
        {
        Scanner keyboard = new Scanner(System.in);
        System.out.print("Inserisci userName da inviare al server" + '\n' + "numero caratteri minimo: 4" + '\n' + "NO SPAZI" +'\n');
        userName = keyboard.next();
        if (isValidUserName(userName))
        {
            System.out.println("userName inserito correttamente!");
            break;
        }
        else
        System.out.println("userName inserito errato");
        }   

    }

    private static void nameList()
    {
        ArrayList <String> nomi=new ArrayList <String>();

    }

    private static boolean isValidUserName(String userName) {

        Pattern pattern = Pattern.compile("[ ]", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(userName);
        
        if(userName.length()>4)
        {
            if (!matcher.find()) 
            {
                return true;
            }
            else 
                return false;
        }
        else
        return false;
    }
}

