package edu.rice.comp504.model.message;

import java.util.Date;

public abstract class AMessage {
    private int messageId;
    private int senderId;
    private int receiverId;
    private int channelId;
    private String messageType;
    private Date messageTime;

    /**
     * constructor.
     */

    protected AMessage(int messageId, int senderId, int receiverId, int channelId, String messageType, Date messageTime) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.channelId = channelId;
        this.messageType = messageType;
        this.messageTime = messageTime;

    }

    /**
     * get message id.
     *
     * @return the id int
     */
    public int getMessageId() {
        return messageId;
    }

    /**
     * get sender id.
     *
     * @return an int id
     */
    public Integer getSenderId() {
        return senderId;
    }

//    /**
//     * get receiver id (can be channel id or user id)
//     * @return an int id
//     */
//    public Integer getReceiverID() {
//        return receiverID;
//    }

    /**
     * get id of channel where the message is being sent
     *
     * @return an int id
     */
    public Integer getChannelID() {
        return channelId;
    }

//    /**
//     * get message content.
//     * @return the string
//     */
//    public String getMessageContent() {
//        return messageContent;
//    }
//
//    /**
//     * change massage content
//     * @param messageContent
//     */
//    public void setMessageContent(String messageContent) {
//        this.messageContent = messageContent;
//    }

    /**
     * get the message type.
     * it can be messages between users
     * it can also be a notification, such as XX leaves/join/request
     *
     * @return message type
     */
    public String getMessageType() {
        return messageType;
    }

    /**
     * set the message type.
     *
     * @param messageType type
     */
    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public void setMessageTime(Date messageTime) {
        this.messageTime = messageTime;
    }

    public int getReceiverId() {
        return receiverId;
    }

    //    /**
//     * get the message action type.
//     * @return message action type
//     */
//    public String getMessageActionType() {
//        return messageActionType;
//    }
//
//    /**
//     * set the message action type.
//     *  default would be send, can be set to recall/delete/edit
//     * @param messageActionType type
//     * @return
//     */
//    public void setMessageActionType(String messageActionType) {
//        this.messageActionType = messageActionType;
//    }

    /**
     * record the time the current message being sent.
     *
     * @return the time.
     */
    public Date getMessageTime() {
        return messageTime;
    }


}













