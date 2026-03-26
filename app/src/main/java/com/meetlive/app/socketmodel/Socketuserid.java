package com.meetlive.app.socketmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Socketuserid {
    @SerializedName("userId")
    @Expose
    private  int userid;

    public Socketuserid(int userid) {
        this.userid = userid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }
}
