package com.meetlive.app.response.Misscall;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestMissCall {
    @SerializedName("_id")
    @Expose
    private int id;
    @SerializedName("receiverId")
    @Expose
    private int receiverId;
    @SerializedName("conversationId")
    @Expose
    private String conversationId;
    @SerializedName("mimeType")
    @Expose
    private String mimeType;

    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("call_rate")
    @Expose
    private int call_rate;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;

    public RequestMissCall(int id, int receiverId, String conversationId, String mimeType) {
        this.id = id;
        this.receiverId = receiverId;
        this.conversationId = conversationId;
        this.mimeType = mimeType;
    }

    public RequestMissCall(int id, int receiverId, String conversationId, String mimeType, String duration, int call_rate, String timestamp) {
        this.id = id;
        this.receiverId = receiverId;
        this.conversationId = conversationId;
        this.mimeType = mimeType;
        this.duration = duration;
        this.call_rate = call_rate;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
