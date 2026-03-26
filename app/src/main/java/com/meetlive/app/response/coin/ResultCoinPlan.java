package com.meetlive.app.response.coin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResultCoinPlan {
    @SerializedName("status")
    @Expose
    private boolean status;
    @SerializedName("code")
    @Expose
    private int code;
    @SerializedName("data")
    @Expose
    private CoinData data;
    @SerializedName("message")
    @Expose
    private String message;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public CoinData getData() {
        return data;
    }

    public void setData(CoinData data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
