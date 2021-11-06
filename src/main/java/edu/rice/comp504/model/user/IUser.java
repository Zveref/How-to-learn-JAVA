package edu.rice.comp504.model.user;

import java.util.ArrayList;

public interface IUser {

    /**
     * Get the user's name.
     *
     * @return user's name.
     */
    String getName();

    /**
     * Set the user's name.
     */
    void setName(String name);

    /**
     * Get all the channels' id that the user is in.
     *
     * @return list of channels' id
     */
    ArrayList<Integer> getChannelList();

    /**
     * Add channel id to the channel's list.
     *
     * @param channelId the channel's id that need to add.
     */
    boolean addChannel(int channelId);

    /**
     * Remove channel id from the channel's list.
     *
     * @param channelId the channel's id that need to remove.
     */
    ArrayList<Integer> removeChannel(int channelId);
}
