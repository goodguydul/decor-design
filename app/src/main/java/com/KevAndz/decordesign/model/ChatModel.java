package com.KevAndz.decordesign.model;

public class ChatModel {
    private int image;
    private String sender;
    private String message;

    public int getSenderImage() {
        return image;
    }
    public void setSenderImage(int image) {
        this.image = image;
    }
    public String getSender() {
        return sender;
    }
    public void setSenderName(String sender) {
        this.sender = sender;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String majoring) {
        this.message = majoring;
    }
}
