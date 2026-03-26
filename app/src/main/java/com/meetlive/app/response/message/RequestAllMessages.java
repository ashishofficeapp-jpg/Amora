package com.meetlive.app.response.message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestAllMessages {
    @SerializedName("_id")
    @Expose
    private int id;
    @SerializedName("page")
    @Expose
    private int page;
    @SerializedName("conversationId")
    @Expose
    private String conversationId;
    @SerializedName("type")
    @Expose
    private String type;

    public RequestAllMessages(int id, int page, String conversationId, String type) {
        this.id = id;
        this.page = page;
        this.conversationId = conversationId;
        this.type = type;
    }

    public RequestAllMessages(int id, String conversationId, String type) {
        this.id = id;
        this.conversationId = conversationId;
        this.type = type;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
