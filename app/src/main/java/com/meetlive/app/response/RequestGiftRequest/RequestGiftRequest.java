package com.meetlive.app.response.RequestGiftRequest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestGiftRequest {
    @SerializedName("gift_id")
    @Expose
    private String giftId;
    @SerializedName("receiver_id")
    @Expose
    private String receiverId;

    public RequestGiftRequest(String giftId, String receiverId) {
        this.giftId = giftId;
        this.receiverId = receiverId;
    }
}
