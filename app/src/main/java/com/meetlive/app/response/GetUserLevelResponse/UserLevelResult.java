package com.meetlive.app.response.GetUserLevelResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserLevelResult {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("level_id")
    @Expose
    private Integer levelId;
    @SerializedName("start_coins")
    @Expose
    private Integer startCoins;
    @SerializedName("end_coins")
    @Expose
    private Integer endCoins;
    @SerializedName("other")
    @Expose
    private Object other;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLevelId() {
        return levelId;
    }

    public void setLevelId(Integer levelId) {
        this.levelId = levelId;
    }

    public Integer getStartCoins() {
        return startCoins;
    }

    public void setStartCoins(Integer startCoins) {
        this.startCoins = startCoins;
    }

    public Integer getEndCoins() {
        return endCoins;
    }

    public void setEndCoins(Integer endCoins) {
        this.endCoins = endCoins;
    }

    public Object getOther() {
        return other;
    }

    public void setOther(Object other) {
        this.other = other;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

}
