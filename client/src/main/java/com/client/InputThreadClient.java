package com.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.http.WebSocket.Listener;
import java.util.Scanner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * L'istanza della 
 * classe che gestisce il client
 * ovvero ha il riferimento al socket e output/input stream
 * 
 * Il Thread gestisce i messaggi ricevuti dal server
 * 
 * 
 * 
 
 */
public class InputThreadClient extends Thread {

    private Socket socket;

    private DataInputStream in;
    private DataOutputStream out;

    private ObjectMapper mapper = new ObjectMapper();

    public InputThreadClient(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new DataOutputStream(socket.getOutputStream());
        this.in = new DataInputStream(socket.getInputStream());
    }


    public void inviaMessaggio(Message m) throws IOException {
        // serializzo il messaggio in una stringa (Message -> String)
        String msgDaInviare = mapper.writeValueAsString(m);
        // invio il messaggio al server
        out.writeBytes(msgDaInviare + "\n");
    }

    public Message riceviMessaggio() throws IOException {
        /*
        // leggo il messaggio sotto forma di stringa dal server
        String msgRicevuto = in.readLine();
        // deserializzo il messaggio (String -> Message)
        Message m = mapper.readValue(msgRicevuto, Message.class);
        return m;
        */
        System.out.println("dentro  riceviMessaggio");
        String serverString = "";
        ObjectMapper json = new ObjectMapper();
        serverString = in.readLine();
        System.out.println("dentro  riceviMessaggio readLine");
        Message pacchetto = json.readValue(serverString, Message.class);
        System.out.println("dentro  riceviMessaggio readValue");
        System.out.println(pacchetto);
        System.out.println(pacchetto.getTextString());
        return pacchetto;
    }

    private void startListening() throws IOException {
        Message pacchetto = riceviMessaggio();
        //controllo che il pacchetto sia formatto correttamente per me

        //ricreo i singoli campi

        //controllo che tipo di messagio sia
        controllType();
            
    }

     private void controllType() {
        //controllo il tipo e mando nella funzione del giusto tipo

        
    }


    /**
     * Thread: il thread viene avviato dopo la login
     */
    public void run() {

        for (;;) {
            try {
                startListening();

                // in base al messaggio ricevuto decidi cosa fare
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    

}
