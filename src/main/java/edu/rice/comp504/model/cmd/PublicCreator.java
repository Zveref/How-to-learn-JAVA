package edu.rice.comp504.model.cmd;

import edu.rice.comp504.model.channel.Channel;
import edu.rice.comp504.model.user.AUser;

import java.util.HashSet;


public class PublicCreator extends ChannelCreator {
    private static PublicCreator ONLY;
    private static AUser admin;
    private static int unlockSerial;

    private PublicCreator() {
        unlockSerial = -2;
    }

    public static PublicCreator make(AUser user) {
        if (ONLY == null) {
            ONLY = new PublicCreator();
        }
        admin = user;
        unlockSerial += 2;
        return ONLY;
    }


    public Channel create(int capacity, String channelName) {
        HashSet<AUser> users = new HashSet<>();
        users.add(admin);
        Channel publicChannel = Channel.makeChannel(admin, users, unlockSerial, false, capacity, channelName);
        publicChannel.addUser(admin);
        return publicChannel;
    }
}
