package com.meetlive.app.response.Friendship;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestFriendShipStatus {
    @SerializedName("friend_id")
    @Expose
    private int friendId;
    @SerializedName("conversation_id")
    @Expose
    private String conversationId;

    public RequestFriendShipStatus(int friendId) {
        this.friendId = friendId;
    }

    public RequestFriendShipStatus(int friendId, String conversationId) {
        this.friendId = friendId;
        this.conversationId = conversationId;
    }

    public int getFriendId() {
        return friendId;
    }

    public void setFriendId(int friendId) {
        this.friendId = friendId;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}
