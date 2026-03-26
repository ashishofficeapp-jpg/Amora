package com.meetlive.app.response;

public class SendTagModel {
    private String tag_name;
    public SendTagModel(String tag_name) {
        this.tag_name = tag_name;
    }

    public String getTag_name() {
        return tag_name;
    }

    public void setTag_name(String tag_name) {
        this.tag_name = tag_name;
    }
}

