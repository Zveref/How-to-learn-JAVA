package edu.rice.comp504.model.util;

import edu.rice.comp504.model.channel.Channel;
import edu.rice.comp504.model.user.AUser;
import edu.rice.comp504.model.user.User;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

public class SpeechDetectorTest extends TestCase {

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