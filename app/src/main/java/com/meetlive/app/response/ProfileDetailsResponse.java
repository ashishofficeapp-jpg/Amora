package com.meetlive.app.response;


public class ProfileDetailsResponse {

    UserListResponse.Data success;

    public UserListResponse.Data getSuccess() {
        return success;
    }

  /*    public static class Result {

        int id, favorite_count, favorite_by_you_count, profile_id, call_rate, is_online;
        String name, username, dob, about_user;
        List<UserListResponse.UserPics> profile_images;
        long mobile;
    }*/
}
