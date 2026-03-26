package com.meetlive.app.response.PaymentGatewayDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Stripe {
    @SerializedName("publishable-key")
    @Expose
    private String publishableKey;
    @SerializedName("secret-key")
    @Expose
    private String secretKey;

    public String getPublishableKey() {
        return publishableKey;
    }

    public void setPublishableKey(String publishableKey) {
        this.publishableKey = publishableKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
