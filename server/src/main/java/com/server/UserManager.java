package com.server;

import java.util.HashMap;

public class UserManager {

    private static HashMap<String, ServerThread> connectedUsers = new HashMap<>();


    public static boolean adduser(String username, ServerThread t) {

        // se in connectedUsers c'è già una chiave con username ritorno false
        if (connectedUsers.containsKey(username)) {
            return false;
        } else {
            connectedUsers.put(username, t);
            return true;
        }


    }



    public HashMap<String, ServerThread> getList() {
        return connectedUsers; 
    }
    
}
