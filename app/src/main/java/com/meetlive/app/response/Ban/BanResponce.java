package com.meetlive.app.response.Ban;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BanResponce {
    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("result")
    @Expose
    private BanData result;
    @SerializedName("error")
    @Expose
    private String error;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public BanData getResult() {
        return result;
    }

    public void setResult(BanData result) {
        this.result = result;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
