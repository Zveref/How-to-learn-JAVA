package edu.rice.comp504.model.message;

import com.google.gson.JsonObject;

import java.util.Date;

public class DirectMessage extends Message {

    protected DirectMessage(int messageId, int senderId, int receiverId, int channelId, String messageType, Date messageTime, String messageContent, Boolean receivable) {

        super(messageId, senderId, receiverId, channelId, messageType, messageTime, messageContent, receivable);
    }

    public JsonObject toObject() {
        JsonObject js = new JsonObject();
        js.addProperty("messageId", getMessageId());
        js.addProperty("senderId", getSenderId());
        js.addProperty("receiverId", getReceiverId());
        js.addProperty("channelId", getChannelID());
        js.addProperty("messageType", "directMessage");
        js.addProperty("messageTime", getMessageType());
        js.addProperty("messageContent", getMessageContent());
        js.addProperty("receivable", getReceivable());
        return js;
    }

}
