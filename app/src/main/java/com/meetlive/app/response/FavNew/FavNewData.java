package com.meetlive.app.response.FavNew;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FavNewData {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("favorite_id")
    @Expose
    private Integer favoriteId;
    @SerializedName("points")
    @Expose
    private Integer points;
    @SerializedName("favorite_count")
    @Expose
    private Integer favoriteCount;
    @SerializedName("favorite_by_you_count")
    @Expose
    private Integer favoriteByYouCount;
    @SerializedName("favorites")
    @Expose
    private List<Favorite> favorites = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFavoriteId() {
        return favoriteId;
    }

    public void setFavoriteId(Integer favoriteId) {
        this.favoriteId = favoriteId;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
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

    public List<Favorite> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<Favorite> favorites) {
        this.favorites = favorites;
    }
}
