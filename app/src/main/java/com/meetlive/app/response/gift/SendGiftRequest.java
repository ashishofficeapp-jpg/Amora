package com.meetlive.app.response.gift;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SendGiftRequest {
    @SerializedName("receiver_id")
    @Expose
    private Integer receiverId;
    @SerializedName("unique_id")
    @Expose
    private Integer uniqueId;
    @SerializedName("gift_id")
    @Expose
    private Integer giftId;

    @SerializedName("call_unique_id")
    @Expose
    private String call_unique_id;

    @SerializedName("gift_price")
    @Expose
    private Integer giftPrice;

    @SerializedName("call_start_timestamp")
    @Expose
    private String callStartTimestamp;
    @SerializedName("gift_sending_timestamp")
    @Expose
    private String giftSendingTimestamp;


    public SendGiftRequest(Integer receiverId, String call_unique_id, Integer giftId, Integer giftPrice, String callStartTimestamp, String giftSendingTimestamp) {
        this.receiverId = receiverId;
        this.call_unique_id = call_unique_id;
        this.giftId = giftId;
        this.giftPrice = giftPrice;
        this.callStartTimestamp = callStartTimestamp;
        this.giftSendingTimestamp = giftSendingTimestamp;
    }

    public SendGiftRequest(Integer receiverId, Integer giftId, Integer giftPrice) {
        this.receiverId = receiverId;
        this.giftId = giftId;
        this.giftPrice = giftPrice;
    }

    public Integer getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Integer receiverId) {
        this.receiverId = receiverId;
    }

    public String getCall_unique_id() {
        return call_unique_id;
    }

    public void setCall_unique_id(String call_unique_id) {
        this.call_unique_id = call_unique_id;
    }

    public Integer getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(Integer uniqueId) {
        this.uniqueId = uniqueId;
    }

    public Integer getGiftId() {
        return giftId;
    }

    public void setGiftId(Integer giftId) {
        this.giftId = giftId;
    }

    public Integer getGiftPrice() {
        return giftPrice;
    }

    public void setGiftPrice(Integer giftPrice) {
        this.giftPrice = giftPrice;
    }

}