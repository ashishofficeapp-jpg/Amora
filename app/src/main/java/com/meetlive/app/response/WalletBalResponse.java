package com.meetlive.app.response;

public class WalletBalResponse {

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
        int total_point, redemablePoints;

        public int getTotal_point() {
            return total_point;
        }

        public int getRedemablePoints() {
            return redemablePoints;
        }
    }
}
