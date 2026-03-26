package com.meetlive.app.response.ChatRoom;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChatRoom {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("__v")
    @Expose
    private int v;
    @SerializedName("hidden")
    @Expose
    private List<Object> hidden = null;
    @SerializedName("deleted_by")
    @Expose
    private List<Object> deletedBy = null;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("currentUsers")
    @Expose
    private List<Integer> currentUsers = null;
    @SerializedName("chatUsers")
    @Expose
    private List<ChatUser> chatUsers = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getV() {
        return v;
    }

    public void setV(int v) {
        this.v = v;
    }

    public List<Object> getHidden() {
        return hidden;
    }

    public void setHidden(List<Object> hidden) {
        this.hidden = hidden;
    }

    public List<Object> getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(List<Object> deletedBy) {
        this.deletedBy = deletedBy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Integer> getCurrentUsers() {
        return currentUsers;
    }

    public void setCurrentUsers(List<Integer> currentUsers) {
        this.currentUsers = currentUsers;
    }

    public List<ChatUser> getChatUsers() {
        return chatUsers;
    }

    public void setChatUsers(List<ChatUser> chatUsers) {
        this.chatUsers = chatUsers;
    }

}
