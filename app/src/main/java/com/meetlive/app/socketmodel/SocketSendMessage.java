package com.meetlive.app.socketmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SocketSendMessage {
    @SerializedName("from")
    @Expose
    private String from;

    @SerializedName("to")
    @Expose
    private String to;

    @SerializedName("conversationId")
    @Expose
    private String conversationId;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("senderProfilePic")
    @Expose
    private String senderProfilePic;

    @SerializedName("friendId")
    @Expose
    private String friendId;

    @SerializedName("userId")
    @Expose
    private String userid;
    @SerializedName("mimeType")
    @Expose
    private String mimetype;
    @SerializedName("senderType")
    @Expose
    private String sendertype;

    @SerializedName("isBotMessage")
    @Expose
    private String isBotMessage;

    @SerializedName("action_name")
    @Expose
    private String actionName;
    @SerializedName("action_id")
    @Expose
    private String actionId;
    @SerializedName("bot_message_id")
    @Expose
    private String botmessageid;
    @SerializedName("audio_duration")
    @Expose
    private String audioDuration;


    public SocketSendMessage(String from, String userid) {
        this.friendId = from;
        this.userid = userid;
    }

    public SocketSendMessage(String from, String to, String conversationId) {
        this.from = from;
        this.to = to;
        this.conversationId = conversationId;
    }

    public SocketSendMessage(String from, String to, String conversationId, String name, String message, String senderProfilePic, String mimetype) {
        this.from = from;
        this.to = to;
        this.conversationId = conversationId;
        this.name = name;
        this.message = message;
        this.senderProfilePic = senderProfilePic;
        this.mimetype = mimetype;
    }

    public SocketSendMessage(String from, String to, String conversationId, String name, String message, String senderProfilePic, String mimetype,
                             String sendertype) {
        this.from = from;
        this.to = to;
        this.conversationId = conversationId;
        this.name = name;
        this.message = message;
        this.senderProfilePic = senderProfilePic;
        this.mimetype = mimetype;
        this.sendertype = sendertype;
    }

    public SocketSendMessage(String from, String to, String conversationId, String name, String message, String senderProfilePic, String mimetype,
                             String sendertype, String audioDuration) {
        this.from = from;
        this.to = to;
        this.conversationId = conversationId;
        this.name = name;
        this.message = message;
        this.senderProfilePic = senderProfilePic;
        this.mimetype = mimetype;
        this.sendertype = sendertype;
        this.audioDuration = audioDuration;
    }

    public SocketSendMessage(String from, String to, String conversationId, String name, String message, String senderProfilePic, String mimetype,
                             String sendertype, String isBotMessage, String actionId, String actionname, String botmessageid) {
        this.from = from;
        this.to = to;
        this.conversationId = conversationId;
        this.name = name;
        this.message = message;
        this.senderProfilePic = senderProfilePic;
        this.mimetype = mimetype;
        this.sendertype = sendertype;
        this.isBotMessage = isBotMessage;
        this.actionId = actionId;
        this.actionName = actionname;
        this.botmessageid = botmessageid;
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

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderProfilePic() {
        return senderProfilePic;
    }

    public void setSenderProfilePic(String senderProfilePic) {
        this.senderProfilePic = senderProfilePic;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getMimetype() {
        return mimetype;
    }

    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }

    public String getSendertype() {
        return sendertype;
    }

    public void setSendertype(String sendertype) {
        this.sendertype = sendertype;
    }

    public String getIsBotMessage() {
        return isBotMessage;
    }

    public void setIsBotMessage(String isBotMessage) {
        this.isBotMessage = isBotMessage;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }
}
