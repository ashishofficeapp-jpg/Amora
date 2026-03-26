package com.meetlive.app.response.Chat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChatList {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("isPaid")
    @Expose
    private boolean isPaid;
    @SerializedName("isAdmin")
    @Expose
    private int isAdmin;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("hidec")
    @Expose
    private String hidec;
    @SerializedName("chattingWith")
    @Expose
    private ChattingWith chattingWith;
    @SerializedName("unread")
    @Expose
    private int unread;
    @SerializedName("lastMessage")
    @Expose
    private LastMessage lastMessage;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isIsPaid() {
        return isPaid;
    }

    public void setIsPaid(boolean isPaid) {
        this.isPaid = isPaid;
    }

    public int getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(int isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHidec() {
        return hidec;
    }

    public void setHidec(String hidec) {
        this.hidec = hidec;
    }

    public ChattingWith getChattingWith() {
        return chattingWith;
    }

    public void setChattingWith(ChattingWith chattingWith) {
        this.chattingWith = chattingWith;
    }

    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public LastMessage getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(LastMessage lastMessage) {
        this.lastMessage = lastMessage;
    }

}
