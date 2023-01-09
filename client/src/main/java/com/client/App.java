package com.client;

import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.*;

import com.fasterxml.jackson.databind.ObjectMapper;

public class App {

    // questa variabile diventa true quando il server ha accettato la login
    // (username valido per il server)
    // sarà modificato dal ThreadClientInput alla ricezione del messaggio login ok
    // 0: non ancora inviato
    // 1: inviato e risposta non ancora ricevuta
    // 2: inviato e accettato
    // 3 inviato e non accettato
    public static Integer userStatus = 0;
    static boolean userConnected = false;
    static Scanner keyboard = new Scanner(System.in);
    public static Socket socket;
    static InputThreadClient inputClient;
    public  static DataOutputStream out ;
    public static void main(String[] args) throws IOException, InterruptedException {
        
        String userString;

        // creazione socket
        socket = new Socket(InetAddress.getLocalHost(), 34567);
        out = new DataOutputStream(socket.getOutputStream());
        // input dove arrivano le risposte del server
        inputClient = new InputThreadClient(socket);
        
        login();

        // lo start gestisce i messaggi in arrivo, lo avviamo dopo aver fatto la login
        inputClient.start();

        startChat();



        //TODO gestire le scelte dell'utente e utilizzare inputClient.inviaMessaggio per inviare messaggi al server
        

    }

    private static void startChat() 
    {
        while (true)
        {
            //print regole per come chattare

            //prendo il imput del utente
                //String stringMessage = keyboard.next();
            //controlli che il input sia formato corretamente

            // formo il messagio 

            //invio messagio
                //sendMessage(m);
        }
    }

    public static void login() throws IOException {

        do {
            System.out.print("Inserisci userName da inviare al server" + '\n' + "numero caratteri minimo: 4" + '\n'
                    + "NO SPAZI" + '\n');

                    //leggo username da tastiera
            String userName = keyboard.next();
            if (isValidUserName(userName)) {
                System.out.println("userName inserito correttamente!");

                // messaggio da inviare per la login, ci metto dentro lo username scelto dall'utente
                Message m = new Message();
                m.setSendTo("#");
                m.setType("command");
                m.setTextString("access");
                m.setUserName(userName);

                sendMessage(m);

                //risposta del server alla login
                if(isValidToServer())
                {
                    System.out.println("nome valido");
                    // roba visiva dove dici al client che si è collegato
                    break;
                }


                //verificare la risposta del server

                
            } else {
                System.out.println("userName inserito errato");
            }
        } while(true); //fino a quando il server non dice ok(wilun mi deve dire quando è ok)
    }

    private static void  sendMessage(Message m) 
     {
        try {
            
            ObjectMapper json = new ObjectMapper();
            String j = json.writeValueAsString(m);
            out.writeBytes(j + '\n');
        } catch (Exception a) {
        }
    }
    private static boolean isValidToServer() throws IOException
    {
        Message ricevuto = inputClient.riceviMessaggio();
        if(ricevuto.getTextString() == "OK")
            return true;
        
        return false;
    }

    // controllo validità username (per il client)
    private static boolean isValidUserName(String userName) {

        Pattern pattern = Pattern.compile("[ ]", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(userName);

        if (userName.length() > 4) {
            if (!matcher.find()) {
                return true;
            } else
                return false;
        } else
            return false;
    }

}
