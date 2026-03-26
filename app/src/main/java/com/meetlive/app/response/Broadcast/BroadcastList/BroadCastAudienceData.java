package com.meetlive.app.response.Broadcast.BroadcastList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BroadCastAudienceData {
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
    private List<BroadCastAudienceProfileImage> broadCastAudienceProfileImages = null;

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

    public List<BroadCastAudienceProfileImage> getBroadCastAudienceProfileImages() {
        return broadCastAudienceProfileImages;
    }

    public void setBroadCastAudienceProfileImages(List<BroadCastAudienceProfileImage> broadCastAudienceProfileImages) {
        this.broadCastAudienceProfileImages = broadCastAudienceProfileImages;
    }
}
