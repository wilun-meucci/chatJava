package com.client;


import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;
import java.util.regex.*;

import com.fasterxml.jackson.databind.ObjectMapper;

public class App 
{
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
        {
            /*
             * 
             * { 
            “sendTo”: “#”;
            "type":"command",
            "text":"access", 
            "userName": "dawid"   username da validare dal server
}
             */
            Thread.sleep(500);
            login();
            userString = "";

            
            if(userString.toUpperCase().equals("FINE"))
            {
                System.out.println("fine");
                break;
            }

            else{
                out.writeBytes(userString + '\n');
                
                
            }
            
            
        }
        
        socket.close();
    }

    private static void login() {
        Scanner keyboard = new Scanner(System.in);
        System.out.print("Inserisci userName da inviare al server" + '\n');
        String userName = keyboard.next();
        if (isValidUserName(userName))
        {}
    }

    private static boolean isValidUserName(String userName) {

        Pattern pattern = Pattern.compile("[ \t\n\x0B\f\r]", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(userName);
        
        if (!matcher.find()) {
            System.out.println("ok");
        }
        if(userName.equals("*") 
        || userName.equals("#") 
        || userName.equals("") 
        || userName.equals(" ") 
        )
        {
            return false;
        }
        return true;
        
    }

}

