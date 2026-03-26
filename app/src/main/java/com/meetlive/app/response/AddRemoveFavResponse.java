package com.meetlive.app.response;

public class AddRemoveFavResponse {

    boolean success;
    String result, error;

    public boolean isSuccess() {
        return success;
    }

    public String getResult() {
        return result;
    }

    public String getError() {
        return error;
    }
}
