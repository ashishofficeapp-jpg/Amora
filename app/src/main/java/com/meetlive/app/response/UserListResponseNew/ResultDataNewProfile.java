package com.meetlive.app.response.UserListResponseNew;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResultDataNewProfile  {
    @SerializedName("ratings_average")
    @Expose
    private String ratingsAverage;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("profile_id")
    @Expose
    private Integer profileId;
    @SerializedName("call_rate")
    @Expose
    private Integer callRate;
    @SerializedName("audio_call_rate")
    @Expose
    private Integer audioCallRate;
    @SerializedName("is_online")
    @Expose
    private Integer isOnline;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("is_busy")
    @Expose
    private Integer isBusy;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("favorite_count")
    @Expose
    private Integer favoriteCount;
    @SerializedName("favorite_by_you_count")
    @Expose
    private Integer favoriteByYouCount;
    @SerializedName("female_images")
    @Expose
    private List<FemaleImage> femaleImages = null;
    @SerializedName("female_video")
    @Expose
    private List<Object> femaleVideo = null;
    @SerializedName("female_wallet")
    @Expose
    private List<FemaleWallet> femaleWallet = null;
    @SerializedName("get_rating_tag")
    @Expose
    private List<GetRatingTag> getRatingTag = null;

    public String getRatingsAverage() {
        return ratingsAverage;
    }

    public void setRatingsAverage(String ratingsAverage) {
        this.ratingsAverage = ratingsAverage;
    }

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

    public Integer getAudioCallRate() {
        return audioCallRate;
    }

    public void setAudioCallRate(Integer audioCallRate) {
        this.audioCallRate = audioCallRate;
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

    public Integer getIsBusy() {
        return isBusy;
    }

    public void setIsBusy(Integer isBusy) {
        this.isBusy = isBusy;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(Integer favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public Integer getFavoriteByYouCount() {
        return favoriteByYouCount;
    }

    public void setFavoriteByYouCount(Integer favoriteByYouCount) {
        this.favoriteByYouCount = favoriteByYouCount;
    }

    public List<FemaleImage> getFemaleImages() {
        return femaleImages;
    }

    public void setFemaleImages(List<FemaleImage> femaleImages) {
        this.femaleImages = femaleImages;
    }

    public List<Object> getFemaleVideo() {
        return femaleVideo;
    }

    public void setFemaleVideo(List<Object> femaleVideo) {
        this.femaleVideo = femaleVideo;
    }

    public List<FemaleWallet> getFemaleWallet() {
        return femaleWallet;
    }

    public void setFemaleWallet(List<FemaleWallet> femaleWallet) {
        this.femaleWallet = femaleWallet;
    }

    public List<GetRatingTag> getGetRatingTag() {
        return getRatingTag;
    }

    public void setGetRatingTag(List<GetRatingTag> getRatingTag) {
        this.getRatingTag = getRatingTag;
    }

}






