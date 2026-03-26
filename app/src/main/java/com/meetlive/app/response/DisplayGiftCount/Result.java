package com.meetlive.app.response.DisplayGiftCount;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {
    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("gift_id")
    @Expose
    private Integer giftId;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("transaction_des")
    @Expose
    private String transactionDes;
    @SerializedName("gift_details")
    @Expose
    private GiftDetails giftDetails;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getGiftId() {
        return giftId;
    }

    public void setGiftId(Integer giftId) {
        this.giftId = giftId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTransactionDes() {
        return transactionDes;
    }

    public void setTransactionDes(String transactionDes) {
        this.transactionDes = transactionDes;
    }

    public GiftDetails getGiftDetails() {
        return giftDetails;
    }

    public void setGiftDetails(GiftDetails giftDetails) {
        this.giftDetails = giftDetails;
    }

}
