package com.meetlive.app.response.message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Message_ {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("body")
    @Expose
    private String body;
    @SerializedName("messageType")
    @Expose
    private String messageType;
    /* @SerializedName("attachment")
     @Expose
     private String attachment;
    */
    @SerializedName("conversationId")
    @Expose
    private String conversationId;
    @SerializedName("sender")
    @Expose
    private String sender;
    @SerializedName("read")
    @Expose
    private List<String> read = null;
    @SerializedName("mimeType")
    @Expose
    private String mimeType;
    @SerializedName("trash")
    @Expose
    private boolean trash;
    @SerializedName("isEdited")
    @Expose
    private boolean isEdited;
    @SerializedName("timestamp")
    @Expose
    private long timestamp;
    @SerializedName("deleteBy")
    @Expose
    private List<Object> deleteBy = null;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("index")
    @Expose
    private int index;
    @SerializedName("readSize")
    @Expose
    private int readSize;
    @SerializedName("audio_duration")
    @Expose
    private String audioDuration;


    private boolean isplaying = false;


    public Message_(String id, String body, String mimeType) {
        this.sender = id;
        this.body = body;
        this.mimeType = mimeType;
    }

    public Message_(String id, String body, String mimeType, String audioDuration) {
        this.sender = id;
        this.body = body;
        this.mimeType = mimeType;
        this.audioDuration = audioDuration;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    /*public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }
*/
    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public List<String> getRead() {
        return read;
    }

    public void setRead(List<String> read) {
        this.read = read;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public boolean isTrash() {
        return trash;
    }

    public void setTrash(boolean trash) {
        this.trash = trash;
    }

    public boolean isIsEdited() {
        return isEdited;
    }

    public void setIsEdited(boolean isEdited) {
        this.isEdited = isEdited;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public List<Object> getDeleteBy() {
        return deleteBy;
    }

    public void setDeleteBy(List<Object> deleteBy) {
        this.deleteBy = deleteBy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getReadSize() {
        return readSize;
    }

    public void setReadSize(int readSize) {
        this.readSize = readSize;
    }

    public boolean isIsplaying() {
        return isplaying;
    }

    public void setIsplaying(boolean isplaying) {
        this.isplaying = isplaying;
    }

    public String getAudioDuration() {
        return audioDuration;
    }

    public void setAudioDuration(String audioDuration) {
        this.audioDuration = audioDuration;
    }
}
