package com.client;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class test 
{
    public static void main( String[] args ) 
    {
        
    }

    private static void login() {
        String userName = "";
         while(true)
        {
        Scanner keyboard = new Scanner(System.in);
        System.out.print("Inserisci userName da inviare al server" + '\n');
        userName = keyboard.next();
        if (isValidUserName(userName))
        {
            break;
        }
        }

        Message m1 = new Message("#","command","access",userName);
        
    }

    private static boolean isValidUserName(String userName) {

        Pattern pattern = Pattern.compile("[*# ]", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(userName);
        
        if(userName.length()>4)
        {
            if (!matcher.find()) 
            {
                System.out.println("ok");
                return true;
            }
            else 
                return false;
        }
        else
        return false;
    }
    
}