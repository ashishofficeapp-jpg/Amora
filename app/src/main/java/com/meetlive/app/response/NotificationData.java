package com.meetlive.app.response;

public class NotificationData {

    String user, body, title, sented, type;
    long sent_time;
    int icon, unread_count;

    public NotificationData(String user, String body, String title, String sented, int icon,
                            String type, long currentTimeMillis, int unread_count) {

        this.user = user;
        this.body = body;
        this.title = title;
        this.sented = sented;
        this.icon = icon;
        this.type = type;
        this.sent_time = currentTimeMillis;
        this.unread_count = unread_count;

    }

    public NotificationData() {
    }

    public int getUnread_count() {
        return unread_count;
    }

    public void setUnread_count(int unread_count) {
        this.unread_count = unread_count;
    }

    public long getCurrentTimeMillis() {
        return sent_time;
    }

    public void setCurrentTimeMillis(long currentTimeMillis) {
        this.sent_time = currentTimeMillis;
    }


    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSented() {
        return sented;
    }

    public void setSented(String sented) {
        this.sented = sented;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
