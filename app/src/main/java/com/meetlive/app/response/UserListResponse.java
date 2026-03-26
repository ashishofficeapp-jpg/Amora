package com.meetlive.app.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class UserListResponse implements Serializable {

    boolean success;
    Result result;
    String error;

    public boolean isSuccess() {
        return success;
    }

    public Result getResult() {
        return result;
    }

    public String getError() {
        return error;
    }


    public void setResult(Result result) {
        this.result = result;
    }

    public static class Result implements Serializable {
        List<Data> data;
        int current_page, from, last_page, per_page, to, total;
        String last_page_url, first_page_url, path, next_page_url, prev_page_url;

        private String ratingsAverage;
        private List<GetRatingTag> getRatingTag;

        public List<Data> getData() {
            return data;
        }

        public int getCurrent_page() {
            return current_page;
        }

        public int getFrom() {
            return from;
        }

        public int getLast_page() {
            return last_page;
        }

        public int getPer_page() {
            return per_page;
        }

        public int getTo() {
            return to;
        }

        public int getTotal() {
            return total;
        }

        public String getLast_page_url() {
            return last_page_url;
        }

        public String getFirst_page_url() {
            return first_page_url;
        }

        public String getPath() {
            return path;
        }

        public String getNext_page_url() {
            return next_page_url;
        }

        public String getPrev_page_url() {
            return prev_page_url;
        }


        public String getRatingsAverage() {
            return ratingsAverage;
        }

        public void setRatingsAverage(String ratingsAverage) {
            this.ratingsAverage = ratingsAverage;
        }

        public List<GetRatingTag> getGetRatingTag() {
            return getRatingTag;
        }

        public void setGetRatingTag(List<GetRatingTag> getRatingTag) {
            this.getRatingTag = getRatingTag;
        }
    }

    public static class Data implements Serializable {
        int id, favorite_count, favorite_by_you_count, profile_id, call_rate, is_online, is_busy, audio_call_rate;
        String name, username, dob, about_user, city, login_type, firebase_status;
        List<UserPics> profile_images;

        @SerializedName("profile_video")
        @Expose
        private List<ProfileVideo> profileVideo = null;

        long mobile;
        public int getAudio_call_rate() {
            return audio_call_rate;
        }

        public void setAudio_call_rate(int audio_call_rate) {
            this.audio_call_rate = audio_call_rate;
        }

        public String getFirebase_status() {
            return firebase_status;
        }

        public void setFirebase_status(String firebase_status) {
            this.firebase_status = firebase_status;
        }

        public String getLogin_type() {
            return login_type;
        }

        public int getIs_busy() {
            return is_busy;
        }

        public String getCity() {
            return city;
        }

        public long getMobile() {
            return mobile;
        }

        public int getCall_rate() {
            return call_rate;
        }

        public int getIs_online() {
            return is_online;
        }

        public String getDob() {
            return dob;
        }

        public String getAbout_user() {
            return about_user;
        }

        public int getId() {
            return id;
        }

        public int getFavorite_count() {
            return favorite_count;
        }

        public int getFavorite_by_you_count() {
            return favorite_by_you_count;
        }

        public String getName() {
            return name;
        }

        public String getUsername() {
            return username;
        }

        public int getProfile_id() {
            return profile_id;
        }

        public List<UserPics> getProfile_images() {
            return profile_images;
        }

        /////////////

        public void setId(int id) {
            this.id = id;
        }

        public void setFavorite_count(int favorite_count) {
            this.favorite_count = favorite_count;
        }

        public void setFavorite_by_you_count(int favorite_by_you_count) {
            this.favorite_by_you_count = favorite_by_you_count;
        }

        public void setProfile_id(int profile_id) {
            this.profile_id = profile_id;
        }

        public void setCall_rate(int call_rate) {
            this.call_rate = call_rate;
        }

        public void setIs_online(int is_online) {
            this.is_online = is_online;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public void setDob(String dob) {
            this.dob = dob;
        }

        public void setAbout_user(String about_user) {
            this.about_user = about_user;
        }

        public void setProfile_images(List<UserPics> profile_images) {
            this.profile_images = profile_images;
        }

        public void setMobile(long mobile) {
            this.mobile = mobile;
        }


        public List<ProfileVideo> getProfileVideo() {
            return profileVideo;
        }

        public void setProfileVideo(List<ProfileVideo> profileVideo) {
            this.profileVideo = profileVideo;
        }
    }

    public static class UserPics implements Serializable {
        int id, user_id, is_profile_image;
        String image_name, image_type, created_at, updated_at;


        public int getId() {
            return id;
        }

        public int getUser_id() {
            return user_id;
        }

        public int getIs_profile_image() {
            return is_profile_image;
        }

        public String getImage_name() {
            return image_name;
        }

        public String getImage_type() {
            return image_type;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        /////////////

        public void setId(int id) {
            this.id = id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public void setIs_profile_image(int is_profile_image) {
            this.is_profile_image = is_profile_image;
        }

        public void setImage_name(String image_name) {
            this.image_name = image_name;
        }

        public void setImage_type(String image_type) {
            this.image_type = image_type;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }




    }

    public class GetRatingTag {
        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("host_id")
        @Expose
        private Integer hostId;
        @SerializedName("tag")
        @Expose
        private String tag;
        @SerializedName("totalCount")
        @Expose
        private Integer totalCount;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getHostId() {
            return hostId;
        }

        public void setHostId(Integer hostId) {
            this.hostId = hostId;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public Integer getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(Integer totalCount) {
            this.totalCount = totalCount;
        }

    }


    public class ProfileVideo {
        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("user_id")
        @Expose
        private Integer userId;
        @SerializedName("video_name")
        @Expose
        private String videoName;
        @SerializedName("video_extension")
        @Expose
        private String videoExtension;
        @SerializedName("video_url")
        @Expose
        private String videoUrl;
        @SerializedName("is_approved")
        @Expose
        private Integer isApproved;
        @SerializedName("Video_thumbnail")
        @Expose
        private String videoThumbnail;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public String getVideoName() {
            return videoName;
        }

        public void setVideoName(String videoName) {
            this.videoName = videoName;
        }

        public String getVideoExtension() {
            return videoExtension;
        }

        public void setVideoExtension(String videoExtension) {
            this.videoExtension = videoExtension;
        }

        public String getVideoUrl() {
            return videoUrl;
        }

        public void setVideoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
        }

        public Integer getIsApproved() {
            return isApproved;
        }

        public void setIsApproved(Integer isApproved) {
            this.isApproved = isApproved;
        }

        public String getVideoThumbnail() {
            return videoThumbnail;
        }

        public void setVideoThumbnail(String videoThumbnail) {
            this.videoThumbnail = videoThumbnail;
        }

    }



}
