package com.meetlive.app.response.Broadcast.BroadcastList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BroadcastListData {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private BroadcastUserId broadcastUserId;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("unique_id")
    @Expose
    private String uniqueId;
    @SerializedName("channel_name")
    @Expose
    private String channelName;
    @SerializedName("status")
    @Expose
    private Integer status;

    @SerializedName("is_host")
    @Expose
    private String is_host;
    @SerializedName("broad_cast_audiences")
    @Expose
    private List<BroadCastAudience> broadCastAudiences = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BroadcastUserId getBroadcastUserId() {
        return broadcastUserId;
    }

    public void setBroadcastUserId(BroadcastUserId broadcastUserId) {
        this.broadcastUserId = broadcastUserId;
    }

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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getIs_host() {
        return is_host;
    }

    public void setIs_host(String is_host) {
        this.is_host = is_host;
    }

    public List<BroadCastAudience> getBroadCastAudiences() {
        return broadCastAudiences;
    }

    public void setBroadCastAudiences(List<BroadCastAudience> broadCastAudiences) {
        this.broadCastAudiences = broadCastAudiences;
    }
}
