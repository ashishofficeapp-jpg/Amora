package com.meetlive.app.response.coin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CoinPlan {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("coins")
    @Expose
    private int coins;
    @SerializedName("amount")
    @Expose
    private double amount;
    @SerializedName("percent_off")
    @Expose
    private int percentOff;
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("best_offer")
    @Expose
    private int bestOffer;
    @SerializedName("created")
    @Expose
    private String created;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getPercentOff() {
        return percentOff;
    }

    public void setPercentOff(int percentOff) {
        this.percentOff = percentOff;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public int getBestOffer() {
        return bestOffer;
    }

    public void setBestOffer(int bestOffer) {
        this.bestOffer = bestOffer;
    }
}
