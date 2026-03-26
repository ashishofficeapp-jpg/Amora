package com.meetlive.app.response.message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Message {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("messages")
    @Expose
    private List<Message_> messages = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Message_> getMessages() {
        return messages;
    }

    public void setMessages(List<Message_> messages) {
        this.messages = messages;
    }
}
