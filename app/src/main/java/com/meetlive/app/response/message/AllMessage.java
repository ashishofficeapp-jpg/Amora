package com.meetlive.app.response.message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.meetlive.app.response.Users;

import java.util.List;

public class AllMessage {
    @SerializedName("users")
    @Expose
    private Users users;
    @SerializedName("total")
    @Expose
    private int total;
    @SerializedName("messages")
    @Expose
    private List<Message> messages = null;

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
