package com.KevAndz.decordesign.model;

public class ChatRoomModel {
    private String sender_image_url;
    private String receiver_name;
    private String receiver_id;
    private String message;

    public String getReceiverImageUrl() {
        return sender_image_url;
    }
    public void setReceiverImageUrl(String senderImageUrl) {
        this.sender_image_url = sender_image_url;
    }
    public String getReceiverName() {
        return receiver_name;
    }
    public void setReceiverName(String receiver_name) {
        this.receiver_name = receiver_name;
    }
    public String getReceiverId() {
        return receiver_id;
    }
    public void setReceiverId(String receiver_id) {
        this.receiver_id = receiver_id;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
