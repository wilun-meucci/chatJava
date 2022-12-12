package com.server;

import java.io.IOException;

public class test {
    public static void main( String[] args ) throws IOException
    {
        
        HashMap<String, String> test = new HashMap<>();
        public static boolean adduser(String username, String boh)
         {


            if(!checkUser(username))
            {
                dio.put(username, boh);
                return true;
            }
            return false;
        }

    
        public  boolean checkUser(String username)
        {
            if (dio.containsKey(username))
                return true;
            return false;
        }
    
    
    

        
        
    }
}
