package com.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import com.fasterxml.jackson.databind.ObjectMapper;
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

        String userString = "";
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        DataOutputStream out = new DataOutputStream(client.getOutputStream());
        ObjectMapper json = new ObjectMapper();


        System.out.println(in);
        
        while(true)
        {
        
            /*
            if(recv.toUpperCase().equals("SPEGNI"))
            {

                m.closeClient(server);
                return ;
            } 
            String modifiedRecv = recv.toUpperCase();
            out.writeBytes(modifiedRecv + '\n');
            */
            userString = in.readLine();
            System.out.println("Stringa ricevuta: " + userString);
            Message m = json.readValue(userString, Message.class);
            ControlSendTo(m);
            

        }
        
    }
/*
             * 
             * { 
            “sendTo”: "#”;
            "type":"command",
            "text":"list", 
            "userName": "dawid"   username da validare dal server
}

        String sendTo = m.getSendTo();
        String type = m.getType();
        String text = m.getTextString();
        String userName = m.getUserName();
             */
    public void ControlSendTo (Message messageObj)
    {
        
        
        if(messageObj.getSendTo().equals("#"))
        {
            messageToServer(messageObj);
        }
        else if(messageObj.getSendTo().equals("*"))
        {
            messageToAll(messageObj);
        }
        else {
            messageToSingle(messageObj);
        }
    }
    private void messageToSingle(Message messageObj) 
    {

    }
    private void messageToAll(Message messageObj) 
    {
//# /list 
    }
    private void messageToServer(Message m) 
    {
        String type = m.getType();
        String text = m.getTextString();
        String userName = m.getUserName();
        if (type.equals("command"))
        {
            if (text.equals("access")) {
                if (!userNameExist(userName)) {
                    
                }
            }
            if (text.equals("list")) {
                
            }
        }
    }
    private boolean userNameExist(String userName) //call messageToServer
    {
        boolean aggiunto = UserManager.adduser(userName, this);
        return !aggiunto;
    }
}
