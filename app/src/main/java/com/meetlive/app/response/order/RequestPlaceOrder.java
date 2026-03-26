package com.meetlive.app.response.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestPlaceOrder {
    @SerializedName("subscription_plan_id")
    @Expose
    private String subscriptionPlanId;
    @SerializedName("original_price")
    @Expose
    private double originalPrice;
    @SerializedName("discounted_price")
    @Expose
    private int discountedPrice;
    @SerializedName("transaction_id")
    @Expose
    private String transactionId;
    @SerializedName("payment_method")
    @Expose
    private int paymentMethod;
    @SerializedName("order_id")
    @Expose
    private String orderId;

    public RequestPlaceOrder(String subscriptionPlanId, double originalPrice, int discountedPrice, String transactionId, int paymentMethod,
                             String orderId) {
        this.subscriptionPlanId = subscriptionPlanId;
        this.originalPrice = originalPrice;
        this.discountedPrice = discountedPrice;
        this.transactionId = transactionId;
        this.paymentMethod = paymentMethod;
        this.orderId = orderId;
    }

    public String getSubscriptionPlanId() {
        return subscriptionPlanId;
    }

    public void setSubscriptionPlanId(String subscriptionPlanId) {
        this.subscriptionPlanId = subscriptionPlanId;
    }

    public double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public int getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(int discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public int getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(int paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
