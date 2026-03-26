package com.meetlive.app.response.message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SendMessage {
    @SerializedName("__v")
    @Expose
    private int v;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("sender")
    @Expose
    private String sender;
    @SerializedName("conversationId")
    @Expose
    private String conversationId;
    @SerializedName("body")
    @Expose
    private String body;
  /*  @SerializedName("attachment")
    @Expose
    private String attachment;
  */  @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;
    @SerializedName("isEdited")
    @Expose
    private boolean isEdited;
    @SerializedName("trash")
    @Expose
    private boolean trash;
    @SerializedName("mimeType")
    @Expose
    private String mimeType;
    @SerializedName("deleteBy")
    @Expose
    private List<Object> deleteBy = null;
    @SerializedName("adminSpecific")
    @Expose
    private List<Object> adminSpecific = null;
    @SerializedName("members")
    @Expose
    private List<Object> members = null;
    @SerializedName("read")
    @Expose
    private List<String> read = null;

    public int getV() {
        return v;
    }

    public void setV(int v) {
        this.v = v;
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

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

  /*  public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }*/

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isIsEdited() {
        return isEdited;
    }

    public void setIsEdited(boolean isEdited) {
        this.isEdited = isEdited;
    }

    public boolean isTrash() {
        return trash;
    }

    public void setTrash(boolean trash) {
        this.trash = trash;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public List<Object> getDeleteBy() {
        return deleteBy;
    }

    public void setDeleteBy(List<Object> deleteBy) {
        this.deleteBy = deleteBy;
    }

    public List<Object> getAdminSpecific() {
        return adminSpecific;
    }

    public void setAdminSpecific(List<Object> adminSpecific) {
        this.adminSpecific = adminSpecific;
    }

    public List<Object> getMembers() {
        return members;
    }

    public void setMembers(List<Object> members) {
        this.members = members;
    }

    public List<String> getRead() {
        return read;
    }

    public void setRead(List<String> read) {
        this.read = read;
    }

}
