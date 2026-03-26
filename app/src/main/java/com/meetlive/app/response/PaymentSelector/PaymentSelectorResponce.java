package com.meetlive.app.response.PaymentSelector;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PaymentSelectorResponce {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("result")
    @Expose
    private ArrayList<PaymentSelectorData> paymentSelectorData=null;
    @SerializedName("error")
    @Expose
    private Object error;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public ArrayList<PaymentSelectorData> getPaymentSelectorData() {
        return paymentSelectorData;
    }

    public void setPaymentSelectorData(ArrayList<PaymentSelectorData> paymentSelectorData) {
        this.paymentSelectorData = paymentSelectorData;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }
}
