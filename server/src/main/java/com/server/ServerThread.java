package com.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.*;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ServerThread extends Thread {
    public Socket client;
    public ServerSocket server;
    DataOutputStream out;
    BufferedReader in;

    ServerThread(Socket client, ServerSocket server) throws IOException {
        this.client = client;

        this.server = server;

        this.out = new DataOutputStream(client.getOutputStream());
        this.in = new BufferedReader(new InputStreamReader(client.getInputStream()));
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
        
        ObjectMapper json = new ObjectMapper();

        System.out.println(in);

        while (true) {

            /*
             * if(recv.toUpperCase().equals("SPEGNI")) {
             * 
             * m.closeClient(server); return ; } String modifiedRecv = recv.toUpperCase();
             * out.writeBytes(modifiedRecv + '\n');
             */
            userString = in.readLine();
            System.out.println("Stringa ricevuta: " + userString);
            Message paccheto = json.readValue(userString, Message.class);
            if (!isValidPacket(paccheto))
            {
                System.out.println("dentro if ");
                break;
            }
                
            ControlSendTo(paccheto);

        }

    }

    /*
     * 
     * { “sendTo”: "#”; "type":"command", "text":"list", "userName": "dawid"
     * username da validare dal server }
     * 
     * String sendTo = m.getSendTo(); String type = m.getType(); String text =
     * m.getTextString(); String userName = m.getUserName();
     */
    private boolean isValidPacket(Message p) {
        String sendTo = p.getSendTo();
        String type = p.getType();
        String text = p.getTextString();
        String userName = p.getUserName();

        if (isNullCamp(sendTo, type, text, userName))
            return false;

        if (!isValidSendTo(sendTo))
            return false;
        if (!isValidType(type))
            return false;
        if (!isValidText(text))
            return false;
        if (text.equals("access"))
            return true;
        if (!isValidUsername(userName))
            return false;

        return true;
    }

    private boolean isValidSendTo(String sendTo) {
        Pattern pattern = Pattern.compile("[ ]", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(sendTo);
        if (sendTo.equals("#") || sendTo.equals("*"))
            return true;
        if (sendTo.length() > 4) {
            if (!matcher.find() && userNameExist(sendTo))
                return true;
        }
        return false;
    }

    private boolean isValidType(String type) {
        if (type.equals("message") || type.equals("command") || type.equals("notification"))
            return true;
        return false;
    }

    private boolean isValidText(String text) {
        if (text.length() > 1 && text.equals("list") || text.equals("disconnect") || text.equals("connect")
                || text.equals("access"))
            return true;
        return false;
    }

    private boolean isValidUsername(String userName) {
        return userNameExist(userName);
    }

    private boolean isNullCamp(String sendTo, String type, String text, String userName) {
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

    private boolean checkTheAccess(String user) {
        Pattern pattern = Pattern.compile("[ ]", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(user);
        if (user.length() > 4) {
            if (!matcher.find())
                return true;
            else if (!userNameExist(user) && addUserToClient(user))
                return true;
            else
                return false;
        } else
            return false;
    }

    private boolean addUserToClient(String user) {
        return UserManager.adduser(user, this);
    }

    public void ControlSendTo(Message messageObj) {
        System.out.println("dentro ControlSendTo ");
        if (messageObj.getSendTo().equals("#")) {
            messageToServer(messageObj);
        } else if (messageObj.getSendTo().equals("*")) {
            messageToAll(messageObj);
        } else {
            messageToSingle(messageObj);
        }
    }

    private void messageToSingle(Message messageObj) {
        String sendTo = messageObj.getSendTo();
        String type = messageObj.getType();
        String text = messageObj.getTextString();
        String userName = messageObj.getUserName();
        if (userNameExist(sendTo))
        {
            Message m = new Message();
            m.setUserName(sendTo);
            m.setTextString(text);
            m.setType(type);
            m.setSendTo(userName);
            ServerThread  clientToSend = UserManager.getConnectedUsers().get(sendTo);
            clientToSend.sendMessage(m);

        }
    }

    private void messageToAll(Message messageObj) {
        // # /list
        String sendTo = messageObj.getSendTo();
        String type = messageObj.getType();
        String text = messageObj.getTextString();
        String userName = messageObj.getUserName();

        Message m = new Message();
            m.setUserName(sendTo);
            m.setTextString(text);
            m.setType(type);
            m.setSendTo(userName);
        
        for (ServerThread clientToSend : UserManager.getConnectedUsers().values()) 
        {
            clientToSend.sendMessage(m);
        }

        

    }

    private void messageToServer(Message message) {
        System.out.println("dentro messageToServer ");
        String type = message.getType();
        String text = message.getTextString();
        String userName = message.getUserName();
        if (type.equals("command")) {
            System.out.println("dentro command ");
            if (text.equals("access")) {
                System.out.println("dentro access ");
                if (checkTheAccess(userName)) {
                    System.out.println("dentro checkTheAccess(userName) ");
                    Message m = new Message();
                    m.setUserName(message.getSendTo());
                    m.setTextString("OK");
                    m.setType(type);
                    m.setSendTo(userName);
                    this.sendMessage(m);
                }
            }
            else if(text.equals("list")) {
                Message m = new Message();
                m.setUserName(message.getSendTo());
                m.setTextString(text);
                m.setType(type);
                m.setSendTo(userName);
                m.setContent(UserManager.getUserList());

                this.sendMessage(m);
            } else {
                System.out.println("dentro list else ");
                Message m = new Message();
                m.setSendTo(userName);
                m.setType("message");
                m.setTextString("KO");
                m.setUserName(message.getSendTo());
                this.sendMessage(m);
            }
        } else {
            System.out.println("dentro command else ");
            Message m = new Message();
            m.setSendTo(userName);
            m.setType("message");
            m.setTextString("KO");
            m.setUserName(message.getSendTo());
            this.sendMessage(m);
        }
    }

    private void sendMessage(Message m) {
        System.out.println("dentro sendMessage ");
        try {
            System.out.println("dentro try ");
            ObjectMapper json = new ObjectMapper();
            String j = json.writeValueAsString(m);
            System.out.println("dentro try json");
            out.writeBytes(j + '\n');
            System.out.println("dentro try writeBytes");
        } catch (Exception a) {
        }
        
    }

    private boolean userNameExist(String userName) // call messageToServer
    {
        return UserManager.checkUser(userName); // true if exist || false if not exist
    }
}
