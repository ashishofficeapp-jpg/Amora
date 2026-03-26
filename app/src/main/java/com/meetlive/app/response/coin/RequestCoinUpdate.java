package com.meetlive.app.response.coin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestCoinUpdate {
    @SerializedName("hostId")
    @Expose
    private int hostId;
    @SerializedName("remaining_minutes")
    @Expose
    private int remainingMinutes;

    public RequestCoinUpdate(int hostId, int remainingMinutes) {
        this.hostId = hostId;
        this.remainingMinutes = remainingMinutes;
    }

    public int getHostId() {
        return hostId;
    }

    public void setHostId(int hostId) {
        this.hostId = hostId;
    }

    public int getRemainingMinutes() {
        return remainingMinutes;
    }

    public void setRemainingMinutes(int remainingMinutes) {
        this.remainingMinutes = remainingMinutes;
    }

}
