package com.amora.app.response.UserListResponseNew;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FemaleImage {
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("is_profile_image")
    @Expose
    private Integer isProfileImage;
    @SerializedName("image_name")
    @Expose
    private String imageName;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getIsProfileImage() {
        return isProfileImage;
    }

    public void setIsProfileImage(Integer isProfileImage) {
        this.isProfileImage = isProfileImage;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}
