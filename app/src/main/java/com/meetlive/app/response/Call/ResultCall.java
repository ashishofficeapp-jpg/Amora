package com.meetlive.app.response.Call;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResultCall {
    @SerializedName("success")
    @Expose
    private boolean success;
    @SerializedName("result")
    @Expose
    private Result result;
    @SerializedName("error")
    @Expose
    private String error;

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }



}
