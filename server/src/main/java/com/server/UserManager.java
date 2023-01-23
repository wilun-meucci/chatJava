package com.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class UserManager {

    private static HashMap<String, ServerThread> connectedUsers = new HashMap<>();


    public static boolean adduser(String username, ServerThread t) {
        System.out.println("    dentro alta classe prima dell'if "+ username);

        if(!checkUser(username))
        {
            connectedUsers.put(username, t);
            System.out.println("    dentro alta classe dentro if "+ username);
            return true;
        }
        return false;
    }
    public static Set<String> getUserList()
    {
        return connectedUsers.keySet();
    }



    public static HashMap<String, ServerThread> getConnectedUsers() {
        return connectedUsers;
    }
    public static void setConnectedUsers(HashMap<String, ServerThread> connectedUsers) {
        UserManager.connectedUsers = connectedUsers;
    }
    
    public static boolean checkUser(String username)
    {
        System.out.println("    dentro checkuser prima if "+ username);
        if (connectedUsers.containsKey(username))
        {
        System.out.println("    dentro checkuser dentro if "+ username);
            return true;
        }
        System.out.println("    dentro checkuser dopo tutto "+ username);
        return false;
    }



    public HashMap<String, ServerThread> getList() {
        return connectedUsers; 
    }
    
}
