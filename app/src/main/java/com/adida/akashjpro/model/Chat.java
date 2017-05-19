package com.adida.akashjpro.model;

/**
 * Created by Aka on 12/31/2016.
 */

public class Chat {
    private String idUser;
    private String name;
    private String messages;

    public Chat() {
    }

    public Chat(String idUser, String name, String messages) {
        this.idUser = idUser;
        this.name = name;
        this.messages = messages;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }
}
