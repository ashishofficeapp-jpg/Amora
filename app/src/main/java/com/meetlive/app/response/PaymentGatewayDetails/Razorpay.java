package com.meetlive.app.response.PaymentGatewayDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Razorpay {
    @SerializedName("key_id")
    @Expose
    private String keyId;
    @SerializedName("key_secret")
    @Expose
    private String keySecret;

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public String getKeySecret() {
        return keySecret;
    }

    public void setKeySecret(String keySecret) {
        this.keySecret = keySecret;
    }
}
