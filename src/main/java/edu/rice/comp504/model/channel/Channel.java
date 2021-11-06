package edu.rice.comp504.model.channel;

import edu.rice.comp504.model.ChatAppWorld;
import edu.rice.comp504.model.message.AMessage;
import edu.rice.comp504.model.message.DirectMessage;
import edu.rice.comp504.model.message.EditMessage;
import edu.rice.comp504.model.message.Message;
import edu.rice.comp504.model.user.AUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.jetty.websocket.api.Session;

import javax.naming.Binding;
import javax.script.Bindings;

/**
 * Abstract channel class that all concrete channel classes will implement.
 */
public class Channel {

    //name is in initiated in the Achat, and put in a ChanelMap
    private AUser admin;
    //    private ConcurrentHashMap<AUser, Session> roommates;  //thread-safe
    private HashSet<AUser> roommates;
    private ArrayList<AUser> blockList;
    private int channelId;
    private boolean ifLocked;
    private int capacity;
    private String channelName;
    //private String password;
    private HashMap<Integer, Message> history;

    private Channel(AUser admin, HashSet<AUser> roommates, int channelId, boolean ifLocked, int capacity, String channelName) {
        this.admin = admin;
        this.roommates = roommates;
        this.channelId = channelId;
        this.ifLocked = ifLocked;
        this.capacity = capacity;
        blockList = new ArrayList<>();
        this.history = new HashMap<>();
        this.channelName = channelName;
    }

    public static Channel makeChannel(AUser admin, HashSet<AUser> roommates, int channelId, boolean ifPrivate, int capacity, String channelName) {
        return new Channel(admin, roommates, channelId, ifPrivate, capacity, channelName);
    }

    /**
     * when someone join or rejoin the channel
     *
     * @param user the user to join
     */
    public boolean addUser(AUser user) {
        boolean indicator = false;
        if (!roommates.contains(user)) {
            roommates.add(user);
            indicator = true;
        }
        return indicator;
    }


    /**
     * When someone quit the channel
     *
     * @param quitUser the user who's to quit NOTE the admin cannot quit the room he's created
     */
    public boolean deleteUser(AUser quitUser) {
        boolean indicator = false;
        AUser target = null;
        for (AUser user : roommates) {
            if (user == quitUser && user != admin) {
                target = user;
                indicator = true;
            }
        }
        roommates.remove(target);
        return indicator;
    }

    public HashSet<AUser> getRoommates() {
        return this.roommates;
    }


    /**
     * get the number of current channel members
     */
    public int getUserNum() {
        return roommates.size();
    }

    /**
     * get the channel capacity
     */
    public int getCapacity() {
        return capacity;
    }

    public void blockUser(AUser user) {
        blockList.add(user);
    }


    /**
     * See if someone's blocked in this channel
     *
     * @param user the user which is suspected of being blocked in this channel
     */
    public boolean ifBlocked(AUser user) {
        return blockList.contains(user);
    }

    /**
     * get the channel ID
     */
    public int getChannelId() {
        return channelId;
    }

    /**
     * get the admin
     */
    public AUser getAdmin() {
        return admin;
    }


    /**
     * broadcast the information, the info would be displayed in chat window
     */
//    filter(Session::isOpen)
    public void broadcastMessage(Message msg) {
        roommates.forEach(roommate -> {
            Session userSession = ChatAppWorld.getSession(roommate);
            if (userSession.isOpen()) {
                try {
                    userSession.getRemote().sendString(String.valueOf(msg.toObject()));
                } catch (Exception e) {
                    System.out.println("ERROR IN CHANNEL__BROADCAST");
                    e.printStackTrace();
                }
            }

        });
    }

    /**
     * broadcast the information, the info would be displayed in chat window
     */
    public void broadcastNoti(AMessage noti) {
        roommates.forEach(roommate -> {
            Session userSession = ChatAppWorld.getSession(roommate);
            if (userSession.isOpen()) {
                try {
                    userSession.getRemote().sendString(String.valueOf(noti.toString()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * DM the information, the info would be displayed in chat window
     */
    public void directMessage(DirectMessage dm) {
        AUser sender = ChatAppWorld.getUserById(dm.getSenderId());
        AUser receiver = ChatAppWorld.getUserById(dm.getReceiverId());

        if (roommates.contains(sender) && roommates.contains(receiver)) {

            Session senderSession = ChatAppWorld.getSession(sender);
            Session receiverSession = ChatAppWorld.getSession(receiver);
            try {
                senderSession.getRemote().sendString(String.valueOf(dm.toObject()));
                receiverSession.getRemote().sendString(String.valueOf(dm.toObject()));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }


    /**
     * edit/recall or delete the history message
     */
    public void editMessage(EditMessage Emsg) {
        System.out.println(history);
        int editId = Emsg.getTrackId();
        System.out.println(editId);


        Integer messageRecallId = null;
        Integer messageEditId = null;



        for (int id : history.keySet() ) {
            if (id == editId) {
                if (Emsg.getEditContent().isEmpty()) {
                    messageRecallId = id;
                } else {
                    messageEditId = id;
                }

            }
        }
        if (messageRecallId != null) {
            history.remove(messageRecallId);
        } else if (
                messageEditId != null) {
            history.get(messageEditId).setMessageContent(Emsg.getEditContent());

        }


//        history.keySet().forEach(MsgId -> {
//            System.out.println(MsgId);
//            if (MsgId == editId) {
//                if (Emsg.getEditContent().isEmpty()) {
//                    history.remove(MsgId);
//                    System.out.println(history);
//                }
//                System.out.println("MESSAGE ID EQUALS TO EDIT ID");
////                history.replace(MsgId, Emsg);
//            }
//        });
        roommates.forEach(roommate -> {
            Session userSession = ChatAppWorld.getSession(roommate);
            if (userSession.isOpen()) {
                try {
                    userSession.getRemote().sendString(String.valueOf(Emsg.toObject()));
                } catch (Exception e) {
                    System.out.println("ERROR IN CHANNEL__BROADCAST");
                    e.printStackTrace();}
            }

        });
    }

    public String getChannelName() {
        return channelName;
    }


    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public HashMap<Integer, Message> getHistory() {
        return history;
    }

    public boolean getIfLocked() {
        return ifLocked;
    }

    public boolean addHistory(Integer amsId, Message ams) {
        if (history.keySet().contains(amsId)) {
            return false;
        }
        history.put(amsId, ams);
        return true;
    }

}