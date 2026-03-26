package com.meetlive.app.response;

public class ChatPurchaseValidity {

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
        int chat;
        String expiry;

        public int getChat() {
            return chat;
        }

        public String getExpiry() {
            return expiry;
        }
    }
}