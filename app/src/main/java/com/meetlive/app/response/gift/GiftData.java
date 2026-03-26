package com.meetlive.app.response.gift;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GiftData {
    @SerializedName("last_page")
    @Expose
    private int lastPage;
    @SerializedName("total")
    @Expose
    private int total;
    @SerializedName("gifts")
    @Expose
    private List<Gift> gifts = null;

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Gift> getGifts() {
        return gifts;
    }

    public void setGifts(List<Gift> gifts) {
        this.gifts = gifts;
    }

}
