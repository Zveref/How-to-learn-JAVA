package edu.rice.comp504.model.message;

import java.util.Date;

public class NullMessage extends AMessage {
    private static NullMessage ONLY;


    /**
     * constructor.
     */

    protected NullMessage(int messageId, int senderId, int receiverId, int channelId, String messageType, Date messageTime) {
        super(messageId, senderId, receiverId, channelId, messageType, messageTime);
        setSenderId(-100);
        setReceiverId(-100);
        setChannelId(-100);
        setMessageType("NULL");
        setMessageTime(null);
    }

    public static NullMessage make() {
        if (ONLY == null) {
            ONLY = new NullMessage(-100, -100, -100, -100, "null", null);
        }
        return ONLY;
    }

}
