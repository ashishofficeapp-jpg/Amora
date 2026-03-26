package com.meetlive.app.response.Broadcast.BroadcastList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BroadCastAudience {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private BroadCastAudienceData broadCastAudienceData;
    @SerializedName("broadcast_id")
    @Expose
    private Integer broadcastId;
    @SerializedName("on_call")
    @Expose
    private Integer onCall;
    @SerializedName("call_seat_position")
    @Expose
    private String callSeatPosition;
    @SerializedName("status")
    @Expose
    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BroadCastAudienceData getBroadCastAudienceData() {
        return broadCastAudienceData;
    }

    public void setBroadCastAudienceData(BroadCastAudienceData broadCastAudienceData) {
        this.broadCastAudienceData = broadCastAudienceData;
    }

    public Integer getBroadcastId() {
        return broadcastId;
    }

    public void setBroadcastId(Integer broadcastId) {
        this.broadcastId = broadcastId;
    }

    public Integer getOnCall() {
        return onCall;
    }

    public void setOnCall(Integer onCall) {
        this.onCall = onCall;
    }

    public String getCallSeatPosition() {
        return callSeatPosition;
    }

    public void setCallSeatPosition(String callSeatPosition) {
        this.callSeatPosition = callSeatPosition;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
