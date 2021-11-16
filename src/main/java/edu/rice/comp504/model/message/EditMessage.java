package edu.rice.comp504.model.message;

import com.google.gson.JsonObject;

import java.util.Date;

public class EditMessage extends AMessage {
    private int trackId;
    private String editContent;

    protected EditMessage(int messageId, int senderId, int receiverId, int channelId, String messageType, Date messageTime, int trackId, String editContent) {
        super(messageId, senderId, receiverId, channelId, messageType, messageTime);
        this.trackId = trackId;
        this.editContent = editContent;
    }

    public int getTrackId() {
        return trackId;
    }

    public String getEditContent() {
        return editContent;
    }

    public JsonObject toObject() {
        JsonObject js = new JsonObject();
        js.addProperty("messageId", getMessageId());
        js.addProperty("senderId", getSenderId());
        js.addProperty("receiverId", getReceiverId());
        js.addProperty("messageType", "editMessage");
        js.addProperty("messageTime", getMessageType());
        js.addProperty("channelId", getChannelID());
        js.addProperty("trackId", getTrackId());
        js.addProperty("editContent", getEditContent());
        return js;
    }
}
