package com.server;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException
    {
        System.out.println("Server avviato dal 'app java'");
        mainThreadServer serverMain = new mainThreadServer();
        serverMain.start();

        
    }
}
