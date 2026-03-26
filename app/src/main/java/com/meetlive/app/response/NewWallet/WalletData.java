package com.meetlive.app.response.NewWallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.TreeMap;


public class WalletData {

    @SerializedName("last_page")
    @Expose
    private int lastPage;

    @SerializedName("current_page")
    @Expose
    private int currentPage;

    @SerializedName("next_page_available")
    @Expose
    private boolean nextPageAvailable;


    @SerializedName("walletHistory")
    @Expose
    private TreeMap<String, List<WalletHistoryData>> walletHistory;

    @SerializedName("walletBalance")
    @Expose
    private WalletBalance walletBalance;

    public TreeMap<String, List<WalletHistoryData>> getWalletHistory() {
        return walletHistory;
    }

    public void setWalletHistory(TreeMap<String, List<WalletHistoryData>> walletHistory) {
        this.walletHistory = walletHistory;
    }

    public WalletBalance getWalletBalance() {
        return walletBalance;
    }

    public void setWalletBalance(WalletBalance walletBalance) {
        this.walletBalance = walletBalance;
    }

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }
}
