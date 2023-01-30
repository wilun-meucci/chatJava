package com.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    /*
     * public void inviaMessaggio(Message m) throws IOException {
     * // serializzo il messaggio in una stringa (Message -> String)
     * String msgDaInviare = mapper.writeValueAsString(m);
     * // invio il messaggio al server
     * out.writeBytes(msgDaInviare + "\n");
     * }
     */

    public Message riceviMessaggio() throws IOException {
        /*
         * // leggo il messaggio sotto forma di stringa dal server
         * String msgRicevuto = in.readLine();
         * // deserializzo il messaggio (String -> Message)
         * Message m = mapper.readValue(msgRicevuto, Message.class);
         * return m;
         */

        String serverString = "";
        DataInputStream incopia = in;

        ObjectMapper json = new ObjectMapper();

        serverString = incopia.readLine();

        Message pacchetto = json.readValue(serverString, Message.class);

       
        return pacchetto;
    }

    // ascolta quello che il server manda
    private void startListening() throws IOException {

        Message pacchetto = riceviMessaggio();
        // ricreo i singoli campi
        String nome = pacchetto.getSendTo();
        String tipo = pacchetto.getType();
        String username = pacchetto.getUserName();
        String stringa = pacchetto.getTextString();
        // controllo che il pacchetto sia formatto correttamente per me

        // controllo che tipo di messagio sia

        controllType(pacchetto);

    }

    // serve per fare istruzioni su un determianto tipo
    private void controllType(Message pacchetto) {
        // controllo il tipo e mando nella funzione del giusto tipo
        String nome = pacchetto.getSendTo();
        String tipo = pacchetto.getType();
        String username = pacchetto.getUserName();
        String stringa = pacchetto.getTextString();
        if (tipo.equals("message")) {

            tipoMessaggio(pacchetto);

        }
        if (tipo.equals("command")) {
            tipoCommmand(pacchetto);
        }
    }

    private void tipoCommmand(Message pacchetto) {
        String comando = pacchetto.getTextString();

        if (comando.equals("list")) // a tutti
        {
            commandList(pacchetto);

        }
        /*
         * else
         * {
         * messageToSingle(pacchetto);
         * 
         * }
         */
    }

    private void commandList(Message pacchetto) {
       // System.out.println("get content: " + pacchetto.getContent().toString());

        List<String> user = getUserList(pacchetto);

        System.out.println("n Utenti: " + user.size());
        for (String i : user) {
            System.out.println("utente: " + i);
        }
    }

    private List<String> getUserList(Message pacchetto) {
        Set<String> a = pacchetto.getContent();

        if (a == null) {
            App.sendMessage(new Message("#", "command", "list", App.userName));
            //messageToSingle(pacchetto);
            try {
                a = riceviMessaggio().getContent();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        List<String> user = new ArrayList<>(a);
        return user;
    }

    // controlliamo chi ha inviato il messaggio
    private void tipoMessaggio(Message pacchetto) {
        String nome = pacchetto.getUserName();

        if (nome.equals("*")) // a tutti
        {
            messageToAll(pacchetto);

        } else if (nome.equals("#"))// al server
        {
            messageToServer(pacchetto);

        } else {
            if (usernameExist(pacchetto)) {
                messageToSingle(pacchetto);

            }
        }

    }

    private boolean usernameExist(Message pacchetto) {

        List<String> user = getUserList(pacchetto);

        for (String i : user) {

            if (i.equals(pacchetto.getUserName())) {
                return true;
            }
        }

        return false;
    }

    private void messageToSingle(Message pacchetto) {

        String nome = pacchetto.getSendTo();
        String tipo = pacchetto.getType();
        String username = pacchetto.getUserName();
        String stringa = pacchetto.getTextString();

        System.out.println("DM: [" + nome + "] " + stringa);
    }

    private void messageToServer(Message pacchetto) {

        String nome = pacchetto.getSendTo();
        String tipo = pacchetto.getType();
        String username = pacchetto.getUserName();
        String stringa = pacchetto.getTextString();

        System.out.println("from server: " + stringa);
    }

    private void messageToAll(Message pacchetto) {

        String nome = pacchetto.getSendTo();
        String tipo = pacchetto.getType();
        String username = pacchetto.getUserName();
        String stringa = pacchetto.getTextString();

        System.out.println("[" + nome + "] " + stringa);
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
