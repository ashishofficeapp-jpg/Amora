package com.meetlive.app.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PublicPhoto {
    @SerializedName("id")
    @Expose
    private float id;
    @SerializedName("user_id")
    @Expose
    private float userId;
    @SerializedName("public_photo")
    @Expose
    private String publicPhoto;
    @SerializedName("created")
    @Expose
    private String created;

    public float getId() {
        return id;
    }

    public void setId(float id) {
        this.id = id;
    }

    public float getUserId() {
        return userId;
    }

    public void setUserId(float userId) {
        this.userId = userId;
    }

    public String getPublicPhoto() {
        return publicPhoto;
    }

    public void setPublicPhoto(String publicPhoto) {
        this.publicPhoto = publicPhoto;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

}
