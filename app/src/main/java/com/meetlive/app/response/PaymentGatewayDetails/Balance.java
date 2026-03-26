package com.meetlive.app.response.PaymentGatewayDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Balance {
    @SerializedName("total_point")
    @Expose
    private Integer totalPoint;

    public Integer getTotalPoint() {
        return totalPoint;
    }

    public void setTotalPoint(Integer totalPoint) {
        this.totalPoint = totalPoint;
    }

}
