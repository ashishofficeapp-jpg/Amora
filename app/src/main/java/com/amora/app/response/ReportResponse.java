package com.amora.app.response;

public class ReportResponse {
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
