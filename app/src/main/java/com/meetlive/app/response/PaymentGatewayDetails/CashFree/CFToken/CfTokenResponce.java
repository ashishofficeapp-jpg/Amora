package com.meetlive.app.response.PaymentGatewayDetails.CashFree.CFToken;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CfTokenResponce {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("cftoken")
    @Expose
    private String cftoken;
    @SerializedName("orderId")
    @Expose
    private String orderId;
    @SerializedName("app_id")
    @Expose
    private String appId;
    @SerializedName("deductable_amout")
    @Expose
    private String deductableAmout;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCftoken() {
        return cftoken;
    }

    public void setCftoken(String cftoken) {
        this.cftoken = cftoken;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getDeductableAmout() {
        return deductableAmout;
    }

    public void setDeductableAmout(String deductableAmout) {
        this.deductableAmout = deductableAmout;
    }
}
