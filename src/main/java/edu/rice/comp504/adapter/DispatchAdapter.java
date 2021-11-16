package edu.rice.comp504.adapter;

import edu.rice.comp504.model.ChatAppWorld;
import edu.rice.comp504.model.channel.Channel;
import edu.rice.comp504.model.user.AUser;
import edu.rice.comp504.model.util.DateCaster;
import edu.rice.comp504.model.util.SwitchHelper;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class DispatchAdapter {
    private ChatAppWorld cw;

    public DispatchAdapter(ChatAppWorld cw) {
        this.cw = cw;
    }


    public boolean signUp(String userName, String birthDate, String school, String interest, String password) {
        //userName has been used
        if (ChatAppWorld.getUserMapByName().containsKey(userName)) {
            return false;
        }
        AUser newUser = cw.createUser(userName, birthDate, school, interest, password);
        return true;
    }

    public boolean login(String userName, String password) {
        if (!ChatAppWorld.getUserMapByName().containsKey(userName)) {
            return false;
        }
        if (Objects.equals(ChatAppWorld.getUserByName(userName).getPassword(), password)) {
            return true;
        }
        return false;
    }

    public AUser getUserByName(String userName) {
        return ChatAppWorld.getUserByName(userName);
    }

    public AUser getUserById(int userId) {
        return ChatAppWorld.getUserById(userId);
    }

    public String getUserBirthStr(String userName) {
        AUser user = getUserByName(userName);
        return DateCaster.DateToStr(user.getBirthDate());
    }

    public ArrayList<Channel> getJoinedChannels(int userId) {
//        System.out.println(ChatAppWorld.getJoinedChannels(userId));
        return ChatAppWorld.getJoinedChannels(userId);
    }

    public Map<Integer, Channel> getAllChannels() {
        return ChatAppWorld.getAllChannels();
    }

    public Channel createChannel(int userId, boolean isPrivate, int capacity, String channelName) {

        return cw.createChannel(userId, isPrivate, capacity, channelName);
    }

    public Channel joinChannel(int userId, int channelId) {

        return cw.joinChannel(userId, channelId);
    }

    public boolean quitChannel(int userId, int channelId) {
        return cw.leaveChannel(userId, channelId);
    }

    public Channel getChannelById(int channelId) {
        return cw.getChannelById(channelId);
    }


    public ArrayList<AUser> getAllUsers() {
        return cw.getAllUsers();
    }

    public SwitchHelper switchHelper(Channel historyChannel) {
        return new SwitchHelper(historyChannel);
    }
}
