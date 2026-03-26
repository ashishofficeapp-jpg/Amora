package com.meetlive.app.response;

public class DeleteImageResponse {

    boolean success;
    Result result;
    String error;

    public boolean isSuccess() {
        return success;
    }

    public Result getResult() {
        return result;
    }

    public String getError() {
        return error;
    }

    public static class Result {
        String msg;

        public String getMsg() {
            return msg;
        }
    }
}
