package com.amora.app.response.UserListResponseNew;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FemaleWallet {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("razorpay_id")
    @Expose
    private Object razorpayId;
    @SerializedName("gift_id")
    @Expose
    private Integer giftId;
    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("transaction_des")
    @Expose
    private String transactionDes;
    @SerializedName("gift_details")
    @Expose
    private GiftDetails giftDetails;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public Integer getGiftId() {
        return giftId;
    }

    public void setGiftId(Integer giftId) {
        this.giftId = giftId;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
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
