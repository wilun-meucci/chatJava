package com.client;

public class Message {
    private String sendTo; // destinatario
    private String type; // tipo (message,notification,command)
    private String textString; //testo inviato
    private String userName; //username del client

    
    public Message() {
    }
    public Message(String sendTo, String type, String textString, String userName) {
        this.sendTo = sendTo;
        this.type = type;
        this.textString = textString;
        this.userName = userName;
    }
    public String getSendTo() {
        return sendTo;
    }
    public void setSendTo(String sendTo) {
        this.sendTo = sendTo;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getTextString() {
        return textString;
    }
    public void setTextString(String textString) {
        this.textString = textString;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
}
