package com.meetlive.app.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ActionButtonValue {
    @SerializedName("action_name")
    @Expose
    private String actionName;
    @SerializedName("action_id")
    @Expose
    private int actionId;
    @SerializedName("conversationId")
    @Expose
    private String conversationId;
    @SerializedName("UserId")
    @Expose
    private int userId;
    @SerializedName("_id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("senderProfilePic")
    @Expose
    private String senderProfilePic;
    @SerializedName("senderType")
    @Expose
    private int senderType;
    @SerializedName("receiverId")
    @Expose
    private int receiverId;
    @SerializedName("receiverName")
    @Expose
    private Object receiverName;
    @SerializedName("receiverImageUrl")
    @Expose
    private String receiverImageUrl;
    @SerializedName("body")
    @Expose
    private String body;
    @SerializedName("isFriendAccept")
    @Expose
    private int isFriendAccept;
    @SerializedName("mimeType")
    @Expose
    private String mimeType;
    @SerializedName("isBotMessage")
    @Expose
    private int isBotMessage;
    @SerializedName("from")
    @Expose
    private String from;
    @SerializedName("to")
    @Expose
    private String to;

    public ActionButtonValue() {
    }

    public ActionButtonValue(String actionName, int actionId, String conversationId, int userId, int id, String name, String senderProfilePic, int senderType, int receiverId, Object receiverName, String receiverImageUrl, String body, int isFriendAccept, String mimeType, int isBotMessage, String from, String to) {
        this.actionName = actionName;
        this.actionId = actionId;
        this.conversationId = conversationId;
        this.userId = userId;
        this.id = id;
        this.name = name;
        this.senderProfilePic = senderProfilePic;
        this.senderType = senderType;
        this.receiverId = receiverId;
        this.receiverName = receiverName;
        this.receiverImageUrl = receiverImageUrl;
        this.body = body;
        this.isFriendAccept = isFriendAccept;
        this.mimeType = mimeType;
        this.isBotMessage = isBotMessage;
        this.from = from;
        this.to = to;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public int getActionId() {
        return actionId;
    }

    public void setActionId(int actionId) {
        this.actionId = actionId;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSenderProfilePic() {
        return senderProfilePic;
    }

    public void setSenderProfilePic(String senderProfilePic) {
        this.senderProfilePic = senderProfilePic;
    }

    public int getSenderType() {
        return senderType;
    }

    public void setSenderType(int senderType) {
        this.senderType = senderType;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public Object getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(Object receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverImageUrl() {
        return receiverImageUrl;
    }

    public void setReceiverImageUrl(String receiverImageUrl) {
        this.receiverImageUrl = receiverImageUrl;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getIsFriendAccept() {
        return isFriendAccept;
    }

    public void setIsFriendAccept(int isFriendAccept) {
        this.isFriendAccept = isFriendAccept;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public int getIsBotMessage() {
        return isBotMessage;
    }

    public void setIsBotMessage(int isBotMessage) {
        this.isBotMessage = isBotMessage;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

}
