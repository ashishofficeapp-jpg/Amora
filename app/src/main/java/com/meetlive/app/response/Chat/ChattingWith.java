package com.meetlive.app.response.Chat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChattingWith {
    @SerializedName("userType")
    @Expose
    private String userType;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("_id")
    @Expose
    private int id;
    @SerializedName("coin_per_minute")
    @Expose
    private int coinperminute;
    @SerializedName("age")
    @Expose
    private int age;
    @SerializedName("country_name")
    @Expose
    private String countryname;

    @SerializedName("user_id")
    @Expose
    private String userId;


    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCoinperminute() {
        return coinperminute;
    }

    public void setCoinperminute(int coinperminute) {
        this.coinperminute = coinperminute;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCountryname() {
        return countryname;
    }

    public void setCountryname(String countryname) {
        this.countryname = countryname;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
