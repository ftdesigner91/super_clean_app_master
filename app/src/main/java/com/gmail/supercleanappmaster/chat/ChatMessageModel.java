package com.gmail.supercleanappmaster.chat;

public class ChatMessageModel {


    private String client_id;
    private String client_name;
    private String client_image;


    private String employee_id;
    private String employee_name;
    private String employee_image;

    private String message;

    private String sender_id;
    private String receiver_id;

    public ChatMessageModel() {}
    public ChatMessageModel(String client_id, String client_name,
                            String client_image, String employee_id,
                            String employee_name, String employee_image,
                            String message, String sender_id, String receiver_id) {
        this.client_id = client_id;
        this.client_name = client_name;
        this.client_image = client_image;
        this.employee_id = employee_id;
        this.employee_name = employee_name;
        this.employee_image = employee_image;
        this.message = message;
        this.sender_id = sender_id;
        this.receiver_id = receiver_id;
    }


    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public String getClient_image() {
        return client_image;
    }

    public void setClient_image(String client_image) {
        this.client_image = client_image;
    }


    public String getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(String employee_id) {
        this.employee_id = employee_id;
    }

    public String getEmployee_name() {
        return employee_name;
    }

    public void setEmployee_name(String employee_name) {
        this.employee_name = employee_name;
    }

    public String getEmployee_image() {
        return employee_image;
    }

    public void setEmployee_image(String employee_image) {
        this.employee_image = employee_image;
    }

    public String getMessage() {return message;}

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(String receiver_id) {
        this.receiver_id = receiver_id;
    }
}
