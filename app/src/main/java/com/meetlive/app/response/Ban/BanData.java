package com.meetlive.app.response.Ban;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BanData {
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("is_banned")
    @Expose
    private int isBanned;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getIsBanned() {
        return isBanned;
    }

    public void setIsBanned(int isBanned) {
        this.isBanned = isBanned;
    }
}
