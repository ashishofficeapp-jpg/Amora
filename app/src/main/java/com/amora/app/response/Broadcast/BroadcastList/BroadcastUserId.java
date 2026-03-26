package com.amora.app.response.Broadcast.BroadcastList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BroadcastUserId {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("profile_id")
    @Expose
    private Integer profileId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("profile_images")
    @Expose
    private List<BroadcastProfileImage> broadcastProfileImages = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProfileId() {
        return profileId;
    }

    public void setProfileId(Integer profileId) {
        this.profileId = profileId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<BroadcastProfileImage> getBroadcastProfileImages() {
        return broadcastProfileImages;
    }

    public void setBroadcastProfileImages(List<BroadcastProfileImage> broadcastProfileImages) {
        this.broadcastProfileImages = broadcastProfileImages;
    }
}
