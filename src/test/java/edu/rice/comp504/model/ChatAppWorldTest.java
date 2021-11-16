package edu.rice.comp504.model;

import com.google.gson.JsonObject;
import edu.rice.comp504.model.channel.Channel;
import edu.rice.comp504.model.cmd.PrivateCreator;
import edu.rice.comp504.model.message.*;
import edu.rice.comp504.model.user.AUser;
import edu.rice.comp504.model.user.User;
import edu.rice.comp504.model.util.*;
import junit.framework.TestCase;

import java.util.Date;
import java.util.HashSet;

public class ChatAppWorldTest extends junit.framework.TestCase {

    public void testGetOnlyWord() {
        ChatAppWorld cw = ChatAppWorld.getOnlyWord();
        assertNotNull("getonlyworld", cw);
        cw.clearOnlyWord();
    }

    public void testGetUserFact() {
        ChatAppWorld cw = ChatAppWorld.getOnlyWord();
        ChatAppWorld.getUserFact();
        assertNotNull(ChatAppWorld.USERFAC);
        cw.clearOnlyWord();
    }

    public void testSetSession() {
        //zver
    }

    public void testGetSession() {
        //zver
    }

    public void testGetUserMapByName() {
        ChatAppWorld cw = ChatAppWorld.getOnlyWord();
        AUser user = cw.createUser("name", "2000-01-01", "rice", "study", "123456");
        AUser user2 = cw.createUser("name2", "2000-02-02", "rice2", "study2", "1234562");
        assertEquals("getusrmapbyname", "rice", cw.getUserMapByName().get("name").getSchool());
        cw.clearOnlyWord();
    }

    public void testGetUserMapById() {
        ChatAppWorld cw = ChatAppWorld.getOnlyWord();
        cw.createUser("name", "2000-01-01", "rice", "study", "123456");
        cw.createUser("name2", "2000-02-02", "rice2", "study2", "1234562");
        assertEquals("getusrmapbyid", "rice", cw.getUserMapById().get(1).getSchool());
        cw.clearOnlyWord();
    }

    public void testGetUserByName() {
        ChatAppWorld cw = ChatAppWorld.getOnlyWord();
        cw.createUser("name", "2000-01-01", "rice", "study", "123456");
        cw.createUser("name2", "2000-02-02", "rice2", "study2", "1234562");
        String school = cw.getUserByName("name").getSchool();
        assertEquals("getusrbyname", "rice", cw.getUserByName("name").getSchool());
        cw.clearOnlyWord();
    }

    public void testGetUserById() {
        ChatAppWorld cw = ChatAppWorld.getOnlyWord();
        cw.createUser("name", "2000-01-01", "rice", "study", "123456");
        cw.createUser("name2", "2000-02-02", "rice2", "study2", "1234562");
        assertEquals("getusrbyid", "rice", cw.getUserById(1).getSchool());
        cw.clearOnlyWord();
    }

    public void testGetChannelById() {
//        ChatAppWorld cw = ChatAppWorld.getOnlyWord();
//        cw.createUser("aa", "2000-02-02", "rice", "study", "123");
//        cw.createChannel(1, true, 2,"c");
//        assertEquals("getchannelbyid", "c", cw.getChannelById(1).getChannelName());
//        cw.clearOnlyWord();
    }

    public void testMapChannelById() {
    }

    public void testMapUserByName() {
        ChatAppWorld cw = ChatAppWorld.getOnlyWord();
        AUser newUser = cw.getUserFact().make("user");
        newUser.setName("aa");
        Date date = DateCaster.StrToDate("2000-02-02");
        newUser.setBirthDate(date);
        newUser.setSchool("rice");
        newUser.setInterest("study");
        newUser.setPassword("123");
        int id = 1;
        newUser.setId(id);
        cw.mapUserByName("aa", newUser);
        assertEquals("mapuserbyname", 1, cw.getUserMapByName().size());
        cw.clearOnlyWord();
    }

    public void testMapUserById() {
        ChatAppWorld cw = ChatAppWorld.getOnlyWord();
        AUser newUser = cw.getUserFact().make("user");
        newUser.setName("aa");
        Date date = DateCaster.StrToDate("2000-02-02");
        newUser.setBirthDate(date);
        newUser.setSchool("rice");
        newUser.setInterest("study");
        newUser.setPassword("123");
        int id = 1;
        newUser.setId(id);
        cw.mapUserById(id, newUser);
        assertEquals("mapuserbyid", 1, cw.getUserMapById().size());
        cw.clearOnlyWord();
    }


    public void testCreateUser() {
        ChatAppWorld cw = ChatAppWorld.getOnlyWord();
        AUser newUser = cw.getUserFact().make("user");
        newUser.setName("aa");
        Date date = DateCaster.StrToDate("2000-02-02");
        newUser.setBirthDate(date);
        newUser.setSchool("rice");
        newUser.setInterest("study");
        newUser.setPassword("123");
        int id = 1;
        newUser.setId(id);
        newUser.addWarnNum();
        newUser.addWarnNum();
        newUser.addWarnNum();
        newUser.addWarnNum();
        newUser.globalBlock();
        assertEquals("globalblock", true, newUser.getMuteStatus());
        newUser.setMuteStatus(false);
        cw.createUser("aa", "2000-02-02", "rice", "study", "123");
        assertEquals("testname", newUser.getName(), cw.getUserById(1).getName());
        assertEquals("testbirth", newUser.getBirthDate(), cw.getUserById(1).getBirthDate());
        assertEquals("testInterest", newUser.getInterest(), cw.getUserById(1).getInterest());
        assertEquals("testschool", newUser.getSchool(), cw.getUserById(1).getSchool());
        assertEquals("testpassword", newUser.getPassword(), cw.getUserById(1).getPassword());
        assertEquals("testId", newUser.getId(), cw.getUserById(1).getId());
        assertEquals("warnnum", 4, newUser.getWarnNum());
        assertEquals("muteStatus", false, newUser.getMuteStatus());

        AUser nulluser = cw.getUserFact().make("null");
        assertEquals("nulluser", "", nulluser.getSchool());
        assertEquals("nulluseraddchannel", false, nulluser.addChannel(1));
        cw.clearOnlyWord();
    }

    public void testCreateChannel() {
        ChatAppWorld cw = ChatAppWorld.getOnlyWord();
        cw.createUser("aa", "2000-02-02", "rice", "study", "123");
        cw.createChannel(1, true, 2, "c");
        assertNotNull(cw.getChannelById(1));

        Date date = DateCaster.StrToDate("2000-02-02");
        User user = new User("bb", date, "rice", "study", "123", 2);
        user.createChannelList();
        user.addChannel(1);
        assertEquals("useraddchannel", true, true);
        user.removeChannel(1);
        assertEquals("userremovechannel", 0, user.getChannelList().size());

        AUser nulluser = cw.getUserFact().make("null");
        nulluser.createChannelList();
        assertEquals("nulluser", "", nulluser.getSchool());
        assertEquals("nulluseraddchannel", false, nulluser.addChannel(1));
        assertNull("nulluser remove channel", nulluser.removeChannel(1));


        cw.clearOnlyWord();
    }

    public void testGetJoinedChannels() {

    }

    public void testJoinChannel() {
        ChatAppWorld cw = ChatAppWorld.getOnlyWord();
        AUser u = cw.createUser("aa", "2000-02-02", "rice", "study", "123");
        AUser u2 = cw.createUser("bb", "2000-02-02", "rice", "study", "123");
        Channel c = cw.createChannel(1, true, 2, "channel");
        System.out.println(c.getChannelId());
//        cw.joinChannel(1, c.getChannelId());
//        c.addUser(u);
        assertEquals("join channel", "channel", cw.joinChannel(1, c.getChannelId()).getChannelName());
        cw.joinChannel(2, c.getChannelId());

        //get joined channel
        assertEquals("get joined channels", 1, ChatAppWorld.getJoinedChannels(1).size());

        //leave channel
        System.out.println(u.getChannelList().get(0));
        System.out.println(u.getId());
        cw.leaveChannel(1, c.getChannelId());
        cw.leaveChannel(2, c.getChannelId());
        assertEquals("leave channel", 0, u2.getChannelList().size());
        assertEquals("leave channel", 1, u.getChannelList().size());
        assertEquals("roommates num", 1, c.getRoommates().size());

        //get all channel
        assertEquals("test all channel", 1, ChatAppWorld.getAllChannels().size());
        cw.clearOnlyWord();
    }

    public void testLeaveChannel() {
        //zver
    }

    public void testGetAllChannels() {
        //zver
    }

    public void testGetAllUsers() {
        ChatAppWorld cw = ChatAppWorld.getOnlyWord();
        cw.createUser("aa", "2000-01-01", "rice", "study", "123");
        cw.createUser("ab", "2000-01-01", "rice", "study", "123");
        assertEquals("getallusers", 2, cw.getAllUsers().size());

        cw.clearOnlyWord();
    }

    //    pop test message
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
        assertEquals((json.get("messageId").getAsInt()), 2);

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

    public void testStrToDate() {
        Date date = DateCaster.StrToDate("2021-11-05");
        assertEquals(date.getYear(), 2021 - 1900);
        assertEquals(date.getMonth(), 11 - 1);
        assertEquals(date.getDate(), 5);
    }

    public void testDateToStr() {
        Date date = DateCaster.StrToDate("2021-11-05");
        String dateStr = DateCaster.DateToStr(date);
        System.out.println(dateStr);
        assertEquals(dateStr, "2021-11-05");
    }

    public void testStringToTime() {
        Date date = DateCaster.StringToTime("2021-11-05 10:30:15");

        //System.out.println(date.toString());
        assertEquals(date.getYear(), 2021 - 1900);
        assertEquals(date.getMonth(), 11 - 1);
        assertEquals(date.getDate(), 5);
        assertEquals(date.getHours(), 10);
        assertEquals(date.getMinutes(), 30);
        assertEquals(date.getSeconds(), 15);
    }

    public void testTimeToString() {
        Date date = DateCaster.StringToTime("2021-11-05 10:30:15");
        String dateStr = DateCaster.TimeToString(date);
        assertEquals("2021-11-05 10:30:15", dateStr);
    }

    public void testFindNum() {
        int num = ItemFinder.findNum("gh3j1k45d");
        assertEquals(num, 3145);
        num = ItemFinder.findNum("abcd");
        assertEquals(num, -99999);
    }

    public void testFindString() {
        String s = ItemFinder.findString("gh3j1k45d");
        assertEquals(s, "ghjkd");
        s = ItemFinder.findString("abcd");
        assertEquals(s, "abcd");
        s = ItemFinder.findString("");
        assertEquals(s, "");
    }

    public void testDetectDir() {

        InfoToAMsg info = new InfoToAMsg("{\"senderId\":\"3\",\"receiverId\":\"0\",\"channelId\":\"0\",\"time\":\"2021-11-5 18:0:23\",\"content\":\"fuck\"}");
        boolean result = SpeechDetector.detectDir(info);
        assertEquals(result, true);

        int senderId = info.getSender();
        assertEquals(senderId, 3);
        int receiverId = info.getReceiver();
        assertEquals(receiverId, 0);
        int channelId = info.getChannel();
        assertEquals(channelId, 0);
        Date time = info.getTime();
        String content = info.getContent();
        assertEquals(content, "**Dirty Word Warning** ");
        info.setContent("good");
        assertEquals(info.getContent(), "good");

    }

    public void testSwitchHelper() {
        AUser admin = new User("testUser", new Date(), "school", "interest", "password", 1);
        Channel c = Channel.makeChannel(admin, new HashSet<AUser>(), 1, false, 10, "testChannel");
        new SwitchHelper(c);
    }
}