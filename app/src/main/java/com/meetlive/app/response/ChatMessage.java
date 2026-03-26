package com.meetlive.app.response;

import java.io.Serializable;

public class ChatMessage implements Serializable {

    private String from, message, type;
    long time_stamp;
    boolean is_seen;

    public ChatMessage() {
    }

    public ChatMessage(String from, String message, String type, long time_stamp, boolean is_seen) {
        this.from = from;
        this.message = message;
        this.type = type;
        this.time_stamp = time_stamp;
        this.is_seen = is_seen;
    }

    public String getFrom() {
        return from;
    }

    public String getMessage() {
        return message;
    }

    public String getType() {
        return type;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isIs_seen() {
        return is_seen;
    }

    public void setIs_seen(boolean is_seen) {
        this.is_seen = is_seen;
    }

    public long getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(long time_stamp) {
        this.time_stamp = time_stamp;
    }
}