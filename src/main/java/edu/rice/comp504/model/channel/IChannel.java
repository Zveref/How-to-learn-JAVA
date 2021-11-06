//package edu.rice.comp504.model.channel;
//
//import edu.rice.comp504.model.user.AUser;
//import org.eclipse.jetty.websocket.api.Session;
//
///**
// * An interface for channel class.
// */
//public interface IChannel {
//
//    /**
//     * Add a user to the channel.
//     * @param session user's session
//     * @param user the user need to add to channel
//     */
//    void addUser(Session session, AUser user);
//
//    /**
//     * Remove a user from the channel.
//     * @param session user's session
//     * @return removed user
//     */
//    AUser removeUser(Session session);
//
//    /**
//     * Send message to all users in this channel.
//     * @param session sender's session
//     * @param message message need to be sent
//     */
//    void broadcastMessage(Session session, String message);
//
//}
