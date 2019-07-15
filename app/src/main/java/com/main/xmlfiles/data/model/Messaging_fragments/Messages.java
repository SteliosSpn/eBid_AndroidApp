package com.main.xmlfiles.data.model.Messaging_fragments;


public class Messages {
    private Integer message_id;
    private String sender;
    private String receiver;
    private String message;
    private boolean deleted;
    private boolean is_read;

    public Messages(String sender, String message, boolean is_read){
        this.sender = sender;
        this.message = message;
        this.is_read = is_read;
    }

    public Messages(String receiver, String message){
        this.receiver = receiver;
        this.message = message;
    }

    public Integer getMessage_id() {
        return message_id;
    }

    public void setMessage_id(Integer message_id) {
        this.message_id = message_id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isIs_read() {
        return is_read;
    }

    public void setIs_read(boolean is_read) {
        this.is_read = is_read;
    }
}
