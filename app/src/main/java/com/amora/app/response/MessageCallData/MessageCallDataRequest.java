package com.amora.app.response.MessageCallData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MessageCallDataRequest {
    @SerializedName("profile_id")
    @Expose
    private String profileId;

    public MessageCallDataRequest(String profileId) {
        this.profileId = profileId;
    }
}
