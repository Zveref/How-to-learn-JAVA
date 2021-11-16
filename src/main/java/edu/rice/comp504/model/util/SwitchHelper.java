package edu.rice.comp504.model.util;

import edu.rice.comp504.model.channel.Channel;
import edu.rice.comp504.model.message.AMessage;
import edu.rice.comp504.model.user.AUser;

import java.util.ArrayList;
import java.util.HashSet;

public class SwitchHelper {

    private ArrayList<AMessage> history;
    private AUser admin;
    private HashSet<AUser> roommates;
    private String channelName;
    private int channelId;
    private boolean ifLocked;
    private int capacity;

    public SwitchHelper(Channel channel) {
        this.history = new ArrayList<AMessage>(channel.getHistory().values());
        this.admin = channel.getAdmin();
        this.roommates = channel.getRoommates();
        this.channelName = channel.getChannelName();
        this.channelId = channel.getChannelId();
        this.ifLocked = channel.getIfLocked();
        this.capacity = channel.getCapacity();
    }

}
