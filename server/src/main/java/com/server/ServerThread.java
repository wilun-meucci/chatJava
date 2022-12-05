package com.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.*;

import com.fasterxml.jackson.databind.ObjectMapper;
public class ServerThread extends Thread{
    public Socket client;
    public  ServerSocket server;
    ServerThread (Socket client,  ServerSocket server)
    {
        this.client = client;
        
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
            Message paccheto = json.readValue(userString, Message.class);
            if(!isValidPackege(paccheto))
            break;
            ControlSendTo(paccheto);
            

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
    private boolean isValidPackege(Message p) 
    {
        String sendTo = p.getSendTo();
        String type = p.getType();
        String text = p.getTextString();
        String userName = p.getUserName();

        if (isNullCamp(sendTo,type,text,userName))
            return false;

        if (!isValidSendTo(sendTo))
            return false;
        if (!isValidType(type))
            return false;
        if (!isValidText(text))
            return false;
        if (!isValidUsername(userName))
            return false;
        

        
        Pattern pattern = Pattern.compile("[ ]", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(sendTo);
        
        if(sendTo.length()>4)
        {
            if (!matcher.find()) 
            {
                return true;
            }
            else 
                return false;
                
        }
        return true;
    }
    
    
    
    private boolean isValidSendTo(String sendTo) 
    {
        if (sendTo.equals("#") || sendTo.equals("*"))
            return true;
        if (sendTo.length()>4)
        {
            if (userNameExist(sendTo))
            {
                return true;
            }
        }
        return false;
    }
    private boolean isValidType(String type) 
    {
        if(type.equals("access") 
        || type.equals("command"))
            return true;
        return false;
        
    }
    private boolean isValidText(String text) {
        return false;
    }
    private boolean isValidUsername(String userName) {
        return false;
    }
    private boolean isNullCamp(String sendTo, String type, String text, String userName) 
    {
        if (sendTo.isEmpty() || sendTo.equals(""))
            return true;
        if (type.isEmpty() || type.equals(""))
            return true;
        if (text.isEmpty() || text.equals(""))
            return true;
        if (userName.isEmpty() || userName.equals(""))
            return true;
        return false;
    }
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
        return UserManager.checkUser(userName); //true if not exist || false if exist
    }
}
