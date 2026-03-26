package com.meetlive.app.response.coin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResultCoinUpdate {
    @SerializedName("data")
    @Expose
    private CoinUpdateData data;

    public CoinUpdateData getData() {
        return data;
    }

    public void setData(CoinUpdateData data) {
        this.data = data;
    }
}
