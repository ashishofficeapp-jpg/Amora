package com.meetlive.app.response.Friendship;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestFriendRequest {
    @SerializedName("friend_id")
    @Expose
    private int friendId;

    public RequestFriendRequest(int friendId) {
        this.friendId = friendId;
    }

    public int getFriendId() {
        return friendId;
    }

    public void setFriendId(int friendId) {
        this.friendId = friendId;
    }

}
