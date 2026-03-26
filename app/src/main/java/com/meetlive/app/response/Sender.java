package com.meetlive.app.response;

public class Sender {

    public NotificationData data;
    String to;

    public Sender(NotificationData data, String to) {
        this.data = data;
        this.to = to;
    }
}
