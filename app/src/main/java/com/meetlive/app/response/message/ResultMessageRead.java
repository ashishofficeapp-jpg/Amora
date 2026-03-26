package com.meetlive.app.response.message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResultMessageRead {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("data")
    @Expose
    private MessageReadData data;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public MessageReadData getData() {
        return data;
    }

    public void setData(MessageReadData data) {
        this.data = data;
    }

}
