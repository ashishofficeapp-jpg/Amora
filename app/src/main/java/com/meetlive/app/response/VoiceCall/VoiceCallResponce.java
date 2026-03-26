package com.meetlive.app.response.VoiceCall;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VoiceCallResponce {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("result")
    @Expose
    private VoiceCallResult result;
    @SerializedName("error")
    @Expose
    private Object error;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public VoiceCallResult getResult() {
        return result;
    }

    public void setResult(VoiceCallResult result) {
        this.result = result;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }

}
