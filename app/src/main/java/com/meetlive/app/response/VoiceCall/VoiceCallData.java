package com.meetlive.app.response.VoiceCall;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VoiceCallData {
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("unique_id")
    @Expose
    private String uniqueId;
    @SerializedName("channelName")
    @Expose
    private String channelName;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }


}
