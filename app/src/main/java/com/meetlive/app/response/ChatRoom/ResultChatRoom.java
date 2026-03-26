package com.meetlive.app.response.ChatRoom;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResultChatRoom {
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private ChatRoom data;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ChatRoom getData() {
        return data;
    }

    public void setData(ChatRoom data) {
        this.data = data;
    }
}
