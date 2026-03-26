package com.meetlive.app.response.Chat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LastMessage {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("sender")
    @Expose
    private String sender;
    @SerializedName("body")
    @Expose
    private String body;
  /*  @SerializedName("attachment")
    @Expose
    private String attachment;*/
    @SerializedName("timestamp")
    @Expose
    private long timestamp;
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
    @SerializedName("read")
    @Expose
    private List<String> read = null;
    @SerializedName("index")
    @Expose
    private int index;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
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
    }
*/
    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
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

    public List<String> getRead() {
        return read;
    }

    public void setRead(List<String> read) {
        this.read = read;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

}
