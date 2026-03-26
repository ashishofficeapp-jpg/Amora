package com.meetlive.app.response.FavNew;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FavNewResponce {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("result")
    @Expose
    private FavNewResult result;
    @SerializedName("error")
    @Expose
    private Object error;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public FavNewResult getResult() {
        return result;
    }

    public void setResult(FavNewResult result) {
        this.result = result;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }

}
