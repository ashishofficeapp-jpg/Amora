package com.meetlive.app.response;

public class OnlineStatusResponse {

    Result result;
    String error;

    public Result getResult() {
        return result;
    }

    public String getError() {
        return error;
    }

    public static class Result {
        String msg;
        int is_online;

        public String getMsg() {
            return msg;
        }

        public int getIs_online() {
            return is_online;
        }
    }
}
