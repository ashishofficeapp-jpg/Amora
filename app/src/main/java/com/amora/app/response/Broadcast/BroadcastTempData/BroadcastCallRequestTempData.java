package com.amora.app.response.Broadcast.BroadcastTempData;

public class BroadcastCallRequestTempData {
    private String profileImage;
    private String senderId;
    private String userName;


    public BroadcastCallRequestTempData(String profileImage, String senderId, String userName) {
        this.profileImage = profileImage;
        this.senderId = senderId;
        this.userName = userName;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
