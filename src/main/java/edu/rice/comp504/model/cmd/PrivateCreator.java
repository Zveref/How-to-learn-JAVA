package edu.rice.comp504.model.cmd;

import edu.rice.comp504.model.channel.Channel;
import edu.rice.comp504.model.user.AUser;

import java.util.HashSet;

public class PrivateCreator extends ChannelCreator {
    private static PrivateCreator ONLY;
    private static AUser admin;
    private static int lockSerial;

    private PrivateCreator() {
        lockSerial = -1;
    }

    public static PrivateCreator make(AUser user) {
        if (ONLY == null) {
            ONLY = new PrivateCreator();
        }
        admin = user;
        lockSerial += 2;
        return ONLY;
    }


    public Channel create(int capacity, String channelName) {

        HashSet<AUser> users = new HashSet<>();
        users.add(admin);
        Channel privateChannel = Channel.makeChannel(admin, users, lockSerial, true, capacity, channelName);
        privateChannel.addUser(admin);
        return privateChannel;
    }
}
