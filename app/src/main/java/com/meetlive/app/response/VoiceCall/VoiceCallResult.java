package com.meetlive.app.response.VoiceCall;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.meetlive.app.response.Call.Points;

public class VoiceCallResult {
    @SerializedName("data")
    @Expose
    private VoiceCallData data;
    @SerializedName("points")
    @Expose
    private Points points;

    public VoiceCallData getData() {
        return data;
    }

    public void setData(VoiceCallData data) {
        this.data = data;
    }

    public Points getPoints() {
        return points;
    }

    public void setPoints(Points points) {
        this.points = points;
    }
}
