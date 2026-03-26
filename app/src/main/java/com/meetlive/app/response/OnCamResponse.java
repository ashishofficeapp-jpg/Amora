package com.meetlive.app.response;

import java.util.List;

public class OnCamResponse {

    boolean success;
    List<Result> result;
    String error;

    public OnCamResponse(List<Result> result) {
        this.result = result;
    }

    public boolean isSuccess() {
        return success;
    }

    public List<Result> getResult() {
        return result;
    }

    public String getError() {
        return error;
    }

    public static class Result {
        int user_id;
        String video_name, Video_thumbnail;
        User user;


        public Result(int user_id, String video_name, String video_thumbnail, User user) {
            this.user_id = user_id;
            this.video_name = video_name;
            Video_thumbnail = video_thumbnail;
            this.user = user;
        }

        public int getUser_id() {
            return user_id;
        }

        public String getVideo_name() {
            return video_name;
        }

        public String getVideo_thumbnail() {
            return Video_thumbnail;
        }

        public User getUser() {
            return user;
        }
    }

    public static class User {
        List<ProfileImages> profile_images;
        int id, profile_id;
        String name;


        public int getId() {
            return id;
        }

        public List<ProfileImages> getProfile_images() {
            return profile_images;
        }

        public int getProfile_id() {
            return profile_id;
        }

        public String getName() {
            return name;
        }
    }

    public static class ProfileImages {
        int id, user_id;
        String image_name;

        ProfileImages(int id, int user_id, String image_name) {
            this.id = id;
            this.user_id = user_id;
            this.image_name = image_name;
        }

        public int getId() {
            return id;
        }

        public int getUser_id() {
            return user_id;
        }

        public String getImage_name() {
            return image_name;
        }
    }
}
