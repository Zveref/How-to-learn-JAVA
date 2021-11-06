//package edu.rice.comp504.model.channel;
//
//import edu.rice.comp504.model.user.AUser;
//import org.eclipse.jetty.websocket.api.Session;
//
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
///**
// * Abstract channel class that all concrete channel classes will implement.
// */
//public abstract class AChannel implements IChannel {
//    protected String channelName;
//    protected int channelId;
//    protected int capacity;
//    protected Boolean isPrivate;
//    protected Session admin;
//    protected Map<Session, AUser> users = new ConcurrentHashMap<>();
//
//    /**
//     * Get the channel name.
//     * @return channel name
//     */
//    public String getChannelName() {
//        return channelName;
//    }
//
//    /**
//     * Set the channel name.
//     */
//    public void setChannelName(String channelName) {
//        this.channelName = channelName;
//    }
//
//    /**
//     * Get the channel's capacity.
//     * @return channel's capacity
//     */
//    public int getCapacity() {
//        return capacity;
//    }
//
//    /**
//     * Set the channel's capacity.
//     */
//    public int setCapacity() {
//        return capacity;
//    }
//
//    /**
//     * Check if the channel is a private channel.
//     * @return if the channel is private
//     */
//    public Boolean checkIsPrivate() {
//        return isPrivate;
//    }
//
//
//    /**
//     * Check if the channel is a private channel.
//     */
//    public void setIsPrivate(boolean isPrivate) {
//        this.isPrivate = isPrivate;
//    }
//
//    /**
//     * Return channel's id.
//     * @return channel's id
//     */
//    public int getChannelId() {
//        return channelId;
//    }
//
//    /**
//     * Return all users in channel.
//     * @return all users in channel
//     */
//    public Map<Session, AUser> getUsers() {
//        return users;
//    }
//
//    /**
//     * Add a user to the channel.
//     * @param session user's session
//     * @param user the user need to add to channel
//     */
//    public abstract void addUser(Session session, AUser user);
//
//    /**
//     * Remove a user from the channel.
//     * @param session user's session
//     * @return removed user
//     */
//    public abstract AUser removeUser(Session session);
//
//    /**
//     * Send message to all users in this channel.
//     * @param session sender's session
//     * @param message message need to be sent
//     */
//    public abstract void broadcastMessage(Session session, String message);
//
//
//}
