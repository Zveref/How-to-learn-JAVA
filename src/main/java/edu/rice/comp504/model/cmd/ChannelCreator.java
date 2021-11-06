package edu.rice.comp504.model.cmd;


import edu.rice.comp504.model.channel.Channel;

public abstract class ChannelCreator {
    private int adminId;
    private int serial;

    //serial can be used to identify Channel type, odd --> private, even --> public

    public ChannelCreator() {
    }

    public static Channel createChannel() {
        return null;
    }

}