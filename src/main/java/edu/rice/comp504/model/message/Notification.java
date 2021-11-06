package edu.rice.comp504.model.message;

import com.google.gson.JsonObject;
import edu.rice.comp504.model.ChatAppWorld;

import java.util.Date;
import java.util.UUID;

public class Notification extends AMessage {
    private String notiType;
    private int subjectId;

    protected Notification(int messageId, int senderId, int receiverId, int channelId, String messageType, Date messageTime, String notiType, int subjectId) {
        super(messageId, senderId, receiverId, channelId, messageType, messageTime);
        setSenderId(0);
        this.notiType = notiType;
        this.subjectId = subjectId;
    }


    /**
     * get notification type.
     *
     * @return a string notification type
     */
    public String getNotiType() {
        return notiType;
    }

    /**
     * get subjectId type.
     *
     * @return a int subject's ID
     */
    public int getSubjectId() {
        return subjectId;
    }

    public JsonObject toObject() {
        JsonObject js = new JsonObject();
        js.addProperty("messageId", getMessageId());
        js.addProperty("senderId", getSenderId());
        js.addProperty("receiverId", getReceiverId());
        js.addProperty("messageType", "directMessage");
        js.addProperty("messageTime", getMessageType());
        js.addProperty("notiType", getNotiType());
        js.addProperty("subjectId", getSubjectId());
        return js;

    }

    public String toString() {
        String subjectName = ChatAppWorld.getUserById(getSubjectId()).getName();
        switch (notiType) {
            case "connectionLost":
                return subjectName + "lost connection";
            case "invite":
                return "channelAdmin" + " invites " + subjectName + " to join this room";
            case "join":
                return subjectName + " joined the room";
            case "leave":
                return subjectName + " left the room";
            case "block":
                return subjectName + " has been blocked by channelAdmin";
            case "mute":
                return subjectName + " is muted in all channels due to hate speeches";
            case "warn":
                return subjectName + " is warned in all channels due to hate speeches";
            case "delete":
                return subjectName + " as a admin deleted a message";
            case "recall":
                return subjectName + " recalled a message";
            case "edit":
                return subjectName + " edited a message";
            case "report":
                return subjectName + " is reported because of inappropriate behaviour";
            default:
                return null;
        }
    }

}
