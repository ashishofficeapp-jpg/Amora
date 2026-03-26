package com.meetlive.app.response.FirebaseChat;

import com.meetlive.app.response.ChatMessage;

import java.io.Serializable;

public class ContactListCustomResponse implements Serializable {

    String name, status, image, uid;
    //    Map<String, Object> timestamp;
    ChatMessage message;
    long timestamp;
    int unread_message;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    /* public void setTimestamp() {
        HashMap<String, Object> timestampNow = new HashMap<>();
        timestampNow.put("timestamp", ServerValue.TIMESTAMP);
        this.timestamp = timestampNow;
    }

    *//*public Map<String, Object> getTimestamp() {
        return timestamp;
    }*//*

    @Exclude
    public long getTimestampLong() {
        return (long) timestamp.get("timestamp");
    }*/

    public int getUnread_message() {
        return unread_message;
    }

    public void setUnread_message(int unread_message) {
        this.unread_message = unread_message;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setMessage(ChatMessage message) {
        this.message = message;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public String getImage() {
        return image;
    }

    public String getUid() {
        return uid;
    }

    public ChatMessage getMessage() {
        return message;
    }
}