package com.amora.app.response.RemainingGiftCard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RemainingGiftCardData {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("total_gift_cards")
    @Expose
    private Integer totalGiftCards;
    @SerializedName("rem_gift_cards")
    @Expose
    private Integer remGiftCards;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("free_seconds")
    @Expose
    private String freeSeconds;

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

    public Integer getTotalGiftCards() {
        return totalGiftCards;
    }

    public void setTotalGiftCards(Integer totalGiftCards) {
        this.totalGiftCards = totalGiftCards;
    }

    public Integer getRemGiftCards() {
        return remGiftCards;
    }

    public void setRemGiftCards(Integer remGiftCards) {
        this.remGiftCards = remGiftCards;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getFreeSeconds() {
        return freeSeconds;
    }

    public void setFreeSeconds(String freeSeconds) {
        this.freeSeconds = freeSeconds;
    }

}
