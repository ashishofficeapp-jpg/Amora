package com.meetlive.app.response.Call;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("unique_id")
    @Expose
    private String uniqueId;
    @SerializedName("channelName")
    @Expose
    private String channelName;

    /*@SerializedName("notification")
    @Expose
    private Notification notification;
*/
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

  /*  public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }
*/
}
