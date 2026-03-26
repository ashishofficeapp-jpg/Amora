package com.meetlive.app.response.Chat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestChatList {
    @SerializedName("_id")
    @Expose
    private long id;

    public RequestChatList(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
