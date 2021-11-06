package edu.rice.comp504.model.message;

import com.google.gson.JsonObject;

import java.util.Date;
import java.util.UUID;

public class Message extends AMessage {
    private Boolean receivable;
    private String messageContent;

    public Boolean getReceivable() {
        return receivable;
    }

    protected Message(int messageId, int senderId, int receiverId, int channelId, String messageType, Date messageTime, String messageContent, Boolean receivable) {
        super(messageId, senderId, receiverId, channelId, messageType, messageTime);
        this.receivable = receivable;
        this.messageContent = messageContent;
        setMessageType("message");
    }


    public String getMessageContent() {
        return messageContent;
    }

    /**
     * change massage content
     *
     * @param messageContent
     */
    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }


    /**
     * get the message action type.
     *
     * @return message action type
     */
    public Boolean checkMuted() {
        return receivable;
    }


    public JsonObject toObject() {
        JsonObject js = new JsonObject();
        js.addProperty("messageId", getMessageId());
        js.addProperty("senderId", getSenderId());
        js.addProperty("receiverId", getReceiverId());
        js.addProperty("messageType", "message");
        js.addProperty("messageTime", getMessageType());
        js.addProperty("messageContent", getMessageContent());
        js.addProperty("messageContent", getMessageContent());
        js.addProperty("receivable", getReceivable());
        return js;
    }
}
