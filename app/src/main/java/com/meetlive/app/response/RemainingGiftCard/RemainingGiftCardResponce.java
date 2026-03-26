package com.meetlive.app.response.RemainingGiftCard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RemainingGiftCardResponce {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("result")
    @Expose
    private RemainingGiftCardData result;
    @SerializedName("error")
    @Expose
    private Object error;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public RemainingGiftCardData getResult() {
        return result;
    }

    public void setResult(RemainingGiftCardData result) {
        this.result = result;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }
}
