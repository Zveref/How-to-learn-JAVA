package edu.rice.comp504.model.message;

import com.google.gson.JsonObject;
import edu.rice.comp504.model.ChatAppWorld;
import edu.rice.comp504.model.user.User;
import junit.framework.TestCase;

import java.util.Date;

public class MessageFacTest extends TestCase {

    public void testMakeFac() {
        int senderId = 1;
        int receiverId = 2;
        int channelId = 3;
        Date messageTime = new Date();
        String messageContent = "message";
        MessageFac factory = MessageFac.makeFac(senderId, receiverId, channelId, messageTime, messageContent);
    }

    public void testMakeMsg() {
        int senderId = 1;
        int receiverId = 2;
        int channelId = 3;
        Date messageTime = new Date();
        String messageContent = "message";
        MessageFac factory = MessageFac.makeFac(senderId, receiverId, channelId, messageTime, messageContent);

        Message m = factory.makeMsg(true, "message");
        assertEquals(m.getReceivable(), new Boolean("true"));
        assertEquals(m.getMessageContent(), "message");

        m.setMessageContent("NewMessage");
        assertEquals(m.getMessageContent(), "NewMessage");

        assertEquals(m.checkMuted(), new Boolean("true"));
        JsonObject json = m.toObject();
    }

    public void testMakeDm() {
        int senderId = 1;
        int receiverId = 2;
        int channelId = 3;
        Date messageTime = new Date();
        String messageContent = "message";
        MessageFac factory = MessageFac.makeFac(senderId, receiverId, channelId, messageTime, messageContent);

        DirectMessage dm = factory.makeDm(true, "message");

        JsonObject json = dm.toObject();
    }

    public void testMakeEmsg() {
        int senderId = 1;
        int receiverId = 2;
        int channelId = 3;
        Date messageTime = new Date();
        String messageContent = "message";
        MessageFac factory = MessageFac.makeFac(senderId, receiverId, channelId, messageTime, messageContent);

        EditMessage Emsg = factory.makeEmsg(5, "message");

        assertEquals(Emsg.getTrackId(), 5);
        assertEquals(Emsg.getEditContent(), "message");

        JsonObject json = Emsg.toObject();
    }

    public void testMakeNoti() {
        int senderId = 1;
        int receiverId = 2;
        int channelId = 3;
        Date messageTime = new Date();
        String messageContent = "message";
        MessageFac factory = MessageFac.makeFac(senderId, receiverId, channelId, messageTime, messageContent);

        Notification invite = (Notification) factory.makeNoti("invite", 0);
        Notification join = (Notification) factory.makeNoti("join", 0);
        Notification leave = (Notification) factory.makeNoti("leave", 0);
        Notification connectionLost = (Notification) factory.makeNoti("connectionLost", 0);
        Notification block = (Notification) factory.makeNoti("block", 0);
        Notification warn = (Notification) factory.makeNoti("warn", 0);
        Notification mute = (Notification) factory.makeNoti("mute", 0);
        Notification report = (Notification) factory.makeNoti("report", 0);
        Notification recall = (Notification) factory.makeNoti("recall", 0);
        Notification delete = (Notification) factory.makeNoti("delete", 0);
        Notification edit = (Notification) factory.makeNoti("edit", 0);
        NullMessage none = (NullMessage) factory.makeNoti("null", 0);

        assertEquals(invite.getNotiType(), "invite");
        assertEquals(invite.getSubjectId(), 0);
        JsonObject inviteObject = invite.toObject();
        assertEquals(inviteObject.get("messageType").getAsString(), "directMessage");
        assertEquals(inviteObject.get("subjectId").getAsInt(), 0);
        ChatAppWorld caw = ChatAppWorld.getOnlyWord();
        User user = new User("test", new Date(), "school", "interest", "password", 0);
        caw.mapUserById(0, user);

        String inviteString = invite.toString();
        join.toString();
        leave.toString();
        connectionLost.toString();
        block.toString();
        warn.toString();
        mute.toString();
        report.toString();
        recall.toString();
        delete.toString();
        edit.toString();

        assertEquals(inviteString, "channelAdmin invites test to join this room");
    }

    public void testSetSenderId() {
        int senderId = 1;
        int receiverId = 2;
        int channelId = 3;
        Date messageTime = new Date();
        String messageContent = "message";
        MessageFac factory = MessageFac.makeFac(senderId, receiverId, channelId, messageTime, messageContent);
        factory.setSenderId(2);
    }

    public void testSetChannelId() {
        int senderId = 1;
        int receiverId = 2;
        int channelId = 3;
        Date messageTime = new Date();
        String messageContent = "message";
        MessageFac factory = MessageFac.makeFac(senderId, receiverId, channelId, messageTime, messageContent);
        factory.setChannelId(3);
    }

    public void testSetReceiverId() {
        int senderId = 1;
        int receiverId = 2;
        int channelId = 3;
        Date messageTime = new Date();
        String messageContent = "message";
        MessageFac factory = MessageFac.makeFac(senderId, receiverId, channelId, messageTime, messageContent);
        factory.setReceiverId(4);
    }

    public void testSetMessageTime() {
        int senderId = 1;
        int receiverId = 2;
        int channelId = 3;
        Date messageTime = new Date();
        String messageContent = "message";
        MessageFac factory = MessageFac.makeFac(senderId, receiverId, channelId, messageTime, messageContent);
        factory.setMessageTime(new Date());
    }

    public void testSetMessageContent() {
        int senderId = 1;
        int receiverId = 2;
        int channelId = 3;
        Date messageTime = new Date();
        String messageContent = "message";
        MessageFac factory = MessageFac.makeFac(senderId, receiverId, channelId, messageTime, messageContent);
        factory.setMessageContent("newContent");
    }
}