package com.meetlive.app.response.Call;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Points {

    @SerializedName("total_point")
    @Expose
    private Integer totalPoint;
    @SerializedName("redemablePoints")
    @Expose
    private Integer redemablePoints;

    public Integer getTotalPoint() {
        return totalPoint;
    }

    public void setTotalPoint(Integer totalPoint) {
        this.totalPoint = totalPoint;
    }

    public Integer getRedemablePoints() {
        return redemablePoints;
    }

    public void setRedemablePoints(Integer redemablePoints) {
        this.redemablePoints = redemablePoints;
    }
}
