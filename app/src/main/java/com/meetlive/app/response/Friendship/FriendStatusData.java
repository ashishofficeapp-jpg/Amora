package com.meetlive.app.response.Friendship;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FriendStatusData {
    @SerializedName("message_already_sent")
    @Expose
    private int messageAlreadySent;
    @SerializedName("is_friend")
    @Expose
    private int isFriend;

    public int getMessageAlreadySent() {
        return messageAlreadySent;
    }

    public void setMessageAlreadySent(int messageAlreadySent) {
        this.messageAlreadySent = messageAlreadySent;
    }

    public int getIsFriend() {
        return isFriend;
    }

    public void setIsFriend(int isFriend) {
        this.isFriend = isFriend;
    }
}
