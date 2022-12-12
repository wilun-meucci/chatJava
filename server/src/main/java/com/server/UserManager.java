package com.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class UserManager {

    private static HashMap<String, ServerThread> connectedUsers = new HashMap<>();


    public static boolean adduser(String username, ServerThread t) {

        if(!checkUser(username))
        {
            connectedUsers.put(username, t);
            return true;
        }
        return false;
    }
    public static Set<String> getUserList()
    {
        return connectedUsers.keySet();
    }


    public static boolean checkUser(String username)
    {
        if (connectedUsers.containsKey(username))
            return true;
        return false;
    }



    public HashMap<String, ServerThread> getList() {
        return connectedUsers; 
    }
    
}
