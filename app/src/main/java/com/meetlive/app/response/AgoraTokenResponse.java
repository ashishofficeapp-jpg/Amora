package com.meetlive.app.response;

import java.util.List;

public class AgoraTokenResponse {

    boolean success;
    String error;
    Result result;

    public boolean isSuccess() {
        return success;
    }

    public String getError() {
        return error;
    }

    public Result getResult() {
        return result;
    }

    public static class Result {

        String token, unique_id;
        Notification notification;

        public String getToken() {
            return token;
        }

        public Notification getNotification() {
            return notification;
        }

        public String getUnique_id() {
            return unique_id;
        }
    }

    public static class Notification {
        long multicast_id;
        int success, failure, canonical_ids;
        List<ResultId> results;

        public long getMulticast_id() {
            return multicast_id;
        }

        public int getSuccess() {
            return success;
        }

        public int getFailure() {
            return failure;
        }

        public int getCanonical_ids() {
            return canonical_ids;
        }

        public List<ResultId> getResults() {
            return results;
        }
    }

    public static class ResultId {
        String message_id;

        public String getMessage_id() {
            return message_id;
        }
    }
}