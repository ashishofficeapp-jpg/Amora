package com.meetlive.app.socketmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SocketCallOperation {
    @SerializedName("from")
    @Expose
    private String from;
    @SerializedName("to")
    @Expose
    private String to;

    public SocketCallOperation(String from, String to) {
        this.from = from;
        this.to = to;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
