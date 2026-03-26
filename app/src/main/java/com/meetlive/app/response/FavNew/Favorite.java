package com.meetlive.app.response.FavNew;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.meetlive.app.response.UserListResponse;

import java.util.List;

public class Favorite {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private Object email;
    @SerializedName("mobile")
    @Expose
    private Object mobile;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("profile_id")
    @Expose
    private Integer profileId;
    @SerializedName("call_rate")
    @Expose
    private Integer callRate;
    @SerializedName("is_online")
    @Expose
    private Integer isOnline;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("about_user")
    @Expose
    private String aboutUser;
    @SerializedName("profile_images")
    @Expose
    private List<UserListResponse.UserPics> profileImages = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getEmail() {
        return email;
    }

    public void setEmail(Object email) {
        this.email = email;
    }

    public Object getMobile() {
        return mobile;
    }

    public void setMobile(Object mobile) {
        this.mobile = mobile;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getProfileId() {
        return profileId;
    }

    public void setProfileId(Integer profileId) {
        this.profileId = profileId;
    }

    public Integer getCallRate() {
        return callRate;
    }

    public void setCallRate(Integer callRate) {
        this.callRate = callRate;
    }

    public Integer getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(Integer isOnline) {
        this.isOnline = isOnline;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAboutUser() {
        return aboutUser;
    }

    public void setAboutUser(String aboutUser) {
        this.aboutUser = aboutUser;
    }

    public List<UserListResponse.UserPics> getProfileImages() {
        return profileImages;
    }

    public void setProfileImages(List<UserListResponse.UserPics> profileImages) {
        this.profileImages = profileImages;
    }
}
