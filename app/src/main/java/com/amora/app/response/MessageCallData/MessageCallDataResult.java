package com.amora.app.response.MessageCallData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MessageCallDataResult {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("call_rate")
    @Expose
    private Integer callRate;
    @SerializedName("audio_call_rate")
    @Expose
    private Integer audioCallRate;

    @SerializedName("chat")
    @Expose
    private Integer chat;

    @SerializedName("expiry")
    @Expose
    private String expiry;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCallRate() {
        return callRate;
    }

    public void setCallRate(Integer callRate) {
        this.callRate = callRate;
    }

    public Integer getAudioCallRate() {
        return audioCallRate;
    }

    public void setAudioCallRate(Integer audioCallRate) {
        this.audioCallRate = audioCallRate;
    }

    public Integer getChat() {
        return chat;
    }

    public void setChat(Integer chat) {
        this.chat = chat;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }
}
