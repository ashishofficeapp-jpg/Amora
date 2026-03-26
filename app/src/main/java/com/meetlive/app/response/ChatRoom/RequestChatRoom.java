package com.meetlive.app.response.ChatRoom;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestChatRoom {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("_id_1")
    @Expose
    private int id1;
    @SerializedName("name_1")
    @Expose
    private String name1;
    @SerializedName("image_1")
    @Expose
    private String image1;
    @SerializedName("_id_1_type")
    @Expose
    private String id1Type;
    @SerializedName("_id_2")
    @Expose
    private int id2;
    @SerializedName("name_2")
    @Expose
    private String name2;
    @SerializedName("image_2")
    @Expose
    private String image2;
    @SerializedName("_id_2_type")
    @Expose
    private String id2Type;
    @SerializedName("coin_per_minute_1")
    @Expose
    private int coinperminute;
    @SerializedName("coin_per_minute_2")
    @Expose
    private int coinperminute2;
    @SerializedName("age_1")
    @Expose
    private int age;
    @SerializedName("age_2")
    @Expose
    private int age2;
    @SerializedName("country_name_1")
    @Expose
    private String countryname;
    @SerializedName("country_name_2")
    @Expose
    private String countryname2;

    @SerializedName("user_id_2")
    @Expose
    private String user_id_2;

    /* public RequestChatRoom(String id, int id1, String name1, String image1, String id1Type, int id2, String name2, String image2, String id2Type) {
         this.id = id;
         this.id1 = id1;
         this.name1 = name1;
         this.image1 = image1;
         this.id1Type = id1Type;
         this.id2 = id2;
         this.name2 = name2;
         this.image2 = image2;
         this.id2Type = id2Type;
     }
 */
    public RequestChatRoom(String id, int id1, String name1, String image1, String id1Type, int id2, String name2, String image2, String id2Type, int coinperminute, int coinperminute2, int age, int age2, String countryname, String countryname2, String user_id_2) {
        this.id = id;
        this.id1 = id1;
        this.name1 = name1;
        this.image1 = image1;
        this.id1Type = id1Type;
        this.id2 = id2;
        this.name2 = name2;
        this.image2 = image2;
        this.id2Type = id2Type;
        this.coinperminute = coinperminute;
        this.coinperminute2 = coinperminute2;
        this.age = age;
        this.age2 = age2;
        this.countryname = countryname;
        this.countryname2 = countryname2;
        this.user_id_2 = user_id_2;
    }

    public RequestChatRoom(String id, int id1, String name1, String image1, String id1Type, int id2, String name2, String image2, String id2Type) {
        this.id = id;
        this.id1 = id1;
        this.name1 = name1;
        this.image1 = image1;
        this.id1Type = id1Type;
        this.id2 = id2;
        this.name2 = name2;
        this.image2 = image2;
        this.id2Type = id2Type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getId1() {
        return id1;
    }

    public void setId1(int id1) {
        this.id1 = id1;
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getId1Type() {
        return id1Type;
    }

    public void setId1Type(String id1Type) {
        this.id1Type = id1Type;
    }

    public int getId2() {
        return id2;
    }

    public void setId2(int id2) {
        this.id2 = id2;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getId2Type() {
        return id2Type;
    }

    public void setId2Type(String id2Type) {
        this.id2Type = id2Type;
    }

    public int getCoinperminute() {
        return coinperminute;
    }

    public void setCoinperminute(int coinperminute) {
        this.coinperminute = coinperminute;
    }

    public int getCoinperminute2() {
        return coinperminute2;
    }

    public void setCoinperminute2(int coinperminute2) {
        this.coinperminute2 = coinperminute2;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getAge2() {
        return age2;
    }

    public void setAge2(int age2) {
        this.age2 = age2;
    }

    public String getCountryname() {
        return countryname;
    }

    public void setCountryname(String countryname) {
        this.countryname = countryname;
    }

    public String getCountryname2() {
        return countryname2;
    }

    public void setCountryname2(String countryname2) {
        this.countryname2 = countryname2;
    }
}
