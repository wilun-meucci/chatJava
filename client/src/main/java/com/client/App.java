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
    public static String userName;
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
        System.out.println("     dentro prima di startchat");
        startChat();



        //TODO gestire le scelte dell'utente e utilizzare inputClient.inviaMessaggio per inviare messaggi al server
        

    }

    private static void startChat() 
    {
        System.out.println("    dentro startchat");
        while (true)
        {
            System.out.println("    dentro startchat while");
            //print regole per come chattare

            //prendo il imput del utente
                //String stringMessage = keyboard.next();
            //controlli che il input sia formato corretamente

            // formo il messagio 

            //invio messagio
                //sendMessage(m);


            String testo =keyboard.nextLine();
            Message m = new Message();
            String[] divisione=testo.split("\'");
            String[] parameters=divisione[0].split(" ");
           // System.out.println("    dopo sto cazzo"+parameters[0]+parameters[1]);
           System.out.println("    dopo sto cazzo "+divisione[0]);
           System.out.println("    dopo sto cazzo "+divisione[1]);
            if(isValidSendTo(parameters))
            {
                m.setSendTo(parameters[0]);
            }
            if(isValidType(parameters,divisione))
            {
               m.setType(parameters[1]);
            }
                System.out.println("    prima di setTextString(parameters[2])"+divisione[1]);
               m.setTextString(divisione[1]);
             
               System.out.println("    dopo prima di setUsername(username)"+userName);
               m.setUserName(userName);

            /*System.out.println("    dopo keyboard.next "+testo);
         
            System.out.println("    dopo Message  "+testo);
            m.setTextString(testo);
            System.out.println("    dopo keyboard.next "+m.getTextString());
            m.setUserName("pacio");
            m.setSendTo("#");
            m.setType("message");*/
            sendMessage(m);
            System.out.println("    dentro startchat while dopo sendmessage");
        }
    }

    private static boolean isValidText(String[] parameters) {
        
        return false;
    }

    private static boolean isValidType(String[] parameters,String[] divisione) {
        if((parameters[1].equals("message")||parameters[1].equals("notification")))
        {
            return true;
        }
        else if(parameters[1].equals("command")&&(divisione[1].equals("list")||divisione[1].equals("access")))
        return true;

        return false;
    }

    private static boolean isValidSendTo(String[] parameters) {
        if(parameters[0].equals("*")||parameters[0].equals("#")||usernameExist(parameters[0]))
        {
            return true;
        }
        return false;
    }

    private static Boolean usernameExist(String parameters) {

        return true;
    }

    private static String keyboardT() {
        String testo=keyboard.next();
        System.out.println("    dentro keyboard.next "+testo);
        return null;
    }

    public static void login() throws IOException {

        do {
            System.out.print("Inserisci userName da inviare al server" + '\n' + "numero caratteri minimo: 4" + '\n'
                    + "NO SPAZI" + '\n');

                    //leggo username da tastiera
            String userName = keyboard.nextLine();
            System.out.println("    dentro login  "+userName);
            if (isValidUserName(userName)) {
                System.out.println("userName inserito correttamente!");

                // messaggio da inviare per la login, ci metto  dentro lo username scelto dall'utente
                Message m = new Message();
                m.setSendTo("#");
                m.setType("command");
                m.setTextString("access");
                m.setUserName(userName);
                System.out.println("    dentro login  ");
                sendMessage(m);
                System.out.println("    dentro login  after sendMessage");

                //risposta del server alla login
                if(isValidToServer()) // true valido false non valido
                {
                    System.out.println("nome valido");
                    setUserName(m.getUserName());
                    // roba visiva dove dici al client che si è collegato
                    break;
                }


                //verificare la risposta del server

                
            } else {
                System.out.println("userName inserito errato");
            }
        } while(true); //fino a quando il server non dice ok(wilun mi deve dire quando è ok)
    }

    private static void setUserName(String userName) {
            App.userName = userName;
    }
    public String getUserName() {
        return userName;
    }

    private static void  sendMessage(Message m) 
     {
        try {
            System.out.println("    dentro   sendMessage");
            ObjectMapper json = new ObjectMapper();
            String j = json.writeValueAsString(m);
            out.writeBytes(j + '\n');
        } catch (Exception a) {
        }
    }
    private static boolean isValidToServer() throws IOException
    {
        System.out.println("    dentro login  isValidToServer");
        Message ricevuto = inputClient.riceviMessaggio();
        System.out.println("    dentro login  after riceviMessaggio");
        System.out.println(ricevuto);
        System.out.println(ricevuto.getTextString());
        if(ricevuto.getTextString().equals("OK"))
            return true;
        
        return false;
    }

    // controllo validità username (per il client)
    private static boolean isValidUserName(String userName) {

        Pattern pattern = Pattern.compile("[ ]", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(userName);

        if (userName.length() >= 4) {
            if (!matcher.find()) {
                return true;
            } else
                return false;
        } else
            return false;
    }

}
