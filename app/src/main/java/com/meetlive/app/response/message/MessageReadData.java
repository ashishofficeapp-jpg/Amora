package com.meetlive.app.response.message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MessageReadData {
    @SerializedName("n")
    @Expose
    private int n;
    @SerializedName("nModified")
    @Expose
    private int nModified;
    @SerializedName("ok")
    @Expose
    private int ok;

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public int getNModified() {
        return nModified;
    }

    public void setNModified(int nModified) {
        this.nModified = nModified;
    }

    public int getOk() {
        return ok;
    }

    public void setOk(int ok) {
        this.ok = ok;
    }
}
