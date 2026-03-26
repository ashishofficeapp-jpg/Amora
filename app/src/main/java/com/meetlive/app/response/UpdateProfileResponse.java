package com.meetlive.app.response;

public class UpdateProfileResponse {

    String result, error;
    boolean success;

    public boolean getSuccess() {
        return success;
    }

    public String getResult() {
        return result;
    }

    public String getError() {
        return error;
    }
}
