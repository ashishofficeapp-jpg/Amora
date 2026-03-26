package com.amora.app.response.Broadcast.BroadcastStart;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BroadCastResponce {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("result")
    @Expose
    private BroadCastData broadCastData;
    @SerializedName("error")
    @Expose
    private Object error;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public BroadCastData getBroadCastData() {
        return broadCastData;
    }

    public void setBroadCastData(BroadCastData broadCastData) {
        this.broadCastData = broadCastData;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }

}
