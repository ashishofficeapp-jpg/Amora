package com.meetlive.app.response.coin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CoinData {
    @SerializedName("last_page")
    @Expose
    private int lastPage;
    @SerializedName("total")
    @Expose
    private int total;
    @SerializedName("plans")
    @Expose
    private List<CoinPlan> plans = null;

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

    public List<CoinPlan> getPlans() {
        return plans;
    }

    public void setPlans(List<CoinPlan> plans) {
        this.plans = plans;
    }
}
