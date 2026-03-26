package com.meetlive.app.response.MessageCallData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MessageCallDataResponce {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("result")
    @Expose
    private MessageCallDataResult result;
    @SerializedName("error")
    @Expose
    private Object error;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public MessageCallDataResult getResult() {
        return result;
    }

    public void setResult(MessageCallDataResult result) {
        this.result = result;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }

}
