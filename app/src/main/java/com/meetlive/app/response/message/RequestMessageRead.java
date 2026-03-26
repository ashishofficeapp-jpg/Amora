package com.meetlive.app.response.message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestMessageRead {
    @SerializedName("_id")
    @Expose
    private int id;
    @SerializedName("conversationId")
    @Expose
    private String conversationId;

    public RequestMessageRead(int id, String conversationId) {
        this.id = id;
        this.conversationId = conversationId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}
