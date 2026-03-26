package com.meetlive.app.response.NewWallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WalletHistoryData {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("credit")
    @Expose
    private Integer credit;
    @SerializedName("debit")
    @Expose
    private Integer debit;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("razorpay_id")
    @Expose
    private Object razorpayId;
    @SerializedName("transaction_des")
    @Expose
    private String transactionDes;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    public Integer getDebit() {
        return debit;
    }

    public void setDebit(Integer debit) {
        this.debit = debit;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Object getRazorpayId() {
        return razorpayId;
    }

    public void setRazorpayId(Object razorpayId) {
        this.razorpayId = razorpayId;
    }

    public String getTransactionDes() {
        return transactionDes;
    }

    public void setTransactionDes(String transactionDes) {
        this.transactionDes = transactionDes;
    }
}
