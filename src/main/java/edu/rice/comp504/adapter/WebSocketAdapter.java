package edu.rice.comp504.adapter;

import com.google.gson.JsonParser;
import edu.rice.comp504.model.ChatAppWorld;
import edu.rice.comp504.model.channel.Channel;
import edu.rice.comp504.model.message.Message;
import edu.rice.comp504.model.message.MessageFac;
import edu.rice.comp504.model.user.AUser;
import edu.rice.comp504.model.util.InfoToAMsg;
import edu.rice.comp504.model.util.ItemFinder;
import edu.rice.comp504.model.util.SpeechDetector;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.util.List;

/**
 * Create a web socket for the server.
 */
@WebSocket
public class WebSocketAdapter {
    private ChatAppWorld cw = ChatAppWorld.getOnlyWord();


    /**
     * Open user's session.
     *
     * @param session The user whose session is opened.
     */
    @OnWebSocketConnect
    public void onConnect(Session session) throws Exception {
        System.out.println("CONNECTION SUCCESS");
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------");
        List<String> userInfo = session.getUpgradeRequest().getParameterMap().get("userId");
        if (userInfo.size() == 1) {
            int userId = Integer.parseInt(userInfo.get(0));
            AUser user = ChatAppWorld.getUserById(userId);
            ChatAppWorld.setSession(user, session);
        }


//        InfoToAMsg helper = new InfoToAMsg(info);
//        MessageFac MsgFac = MessageFac.makeFac(helper.getSender(), helper.getReceiver(), helper.getChannel(), helper.getDate(), helper.getContent());
//        cw.getJoinedChannels(helper.getSender()).forEach(channel -> {
//                channel.broadcastNoti(MsgFac.makeNoti("join", helper.getSender()) );
//        });
    }

    /**
     * Close the user's session.
     *
     * @param session The use whose session is closed.
     */
    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String info) {
        InfoToAMsg helper = new InfoToAMsg(info);
        MessageFac MsgFac = MessageFac.makeFac(helper.getSender(), helper.getReceiver(), helper.getChannel(), helper.getTime(), helper.getContent());
        cw.getJoinedChannels(helper.getSender()).forEach(channel -> {
            channel.broadcastNoti(MsgFac.makeNoti("leave", helper.getSender()));
        });

    }

    /**
     * Send a message.
     *
     * @param session The session user sending the message.
     * @param info    The message from JS.
     */
    @OnWebSocketMessage
    public void onMessage(Session session, String info) throws Exception {
        System.out.print("FROM FRONTEND: ");
        System.out.println(info);
        InfoToAMsg helper = new InfoToAMsg(info);
        System.out.println("SENDER_ID: " + helper.getSender() + "  RECEIVER_ID: " + helper.getReceiver() + "  CONTENT: " + helper.getContent() + " CHANNEL_ID: " + helper.getChannel() + " MESSAGE_TIME: " + helper.getTime());
        MessageFac MsgFac = MessageFac.makeFac(helper.getSender(), helper.getReceiver(), helper.getChannel(), helper.getTime(), helper.getContent());
        Channel currentChannel = ChatAppWorld.getChannelById(helper.getChannel());

        if (helper.getSender() == 0) {          //senderId_0 means sent by system -->  Notification
            System.out.println("senderId + notfication type");
            System.out.println(ItemFinder.findNum(helper.getContent()));
            System.out.println(ItemFinder.findString(helper.getContent()));

            System.out.println("NOTIFICATION CONTENT");
            System.out.println(helper.getContent());
            System.out.println("------------------------------------");
            currentChannel.broadcastNoti(MsgFac.makeNoti(ItemFinder.findString(helper.getContent()), ItemFinder.findNum(helper.getContent())));
//
//            System.out.println("notification created");
//            System.out.println(MsgFac.makeNoti(ItemFinder.findString(helper.getContent()), ItemFinder.findNum(helper.getContent())));
//            currentChannel.broadcastNoti(MsgFac.makeNoti(ItemFinder.findString(helper.getContent()), ItemFinder.findNum(helper.getContent())));
//            cw.getJoinedChannels(ItemFinder.findNum(helper.getContent())).forEach(channel -> {
//                channel.broadcastNoti(MsgFac.makeNoti(ItemFinder.findString(helper.getContent()), ItemFinder.findNum(helper.getContent())));
//            });
            return;
        }
        if (helper.getSender() > 0 && helper.getReceiver() == 0) {       // receiverId_0 means send to all roommates --->Message
            System.out.println("AMessage_Class identified ---->  Message");
            int senderId = helper.getSender();
            AUser sender = cw.getUserById(senderId);


            System.out.println("channelid same");
            if (currentChannel.ifBlocked(sender)) {                                  //if is blocked
                currentChannel.broadcastNoti(MsgFac.makeNoti("block", helper.getSender()));
                currentChannel.broadcastMessage(MsgFac.makeMsg(false, helper.getContent()));
                System.out.println("BLOCKED");
                return;
            } else if (sender.getMuteStatus()) {                               //if is muted
                currentChannel.broadcastNoti(MsgFac.makeNoti("mute", helper.getSender()));
                currentChannel.broadcastMessage(MsgFac.makeMsg(false, helper.getContent()));
                System.out.println("MUTED");
                return;
            } else if (SpeechDetector.detectDir(helper)) {         //if contains hate words
                currentChannel.broadcastNoti(MsgFac.makeNoti("warn", helper.getSender()));
                currentChannel.broadcastMessage(MsgFac.makeMsg(true, helper.getContent()));
                sender.addWarnNum();
                if (sender.getWarnNum() > 1) {
                    sender.setMuteStatus(true);
                    System.out.println("MUTED NOW!");
                }
                System.out.println("WARNED");
                return;
            }
            System.out.println("BAD message_tag determine: DONE");
            Message message = MsgFac.makeMsg(true, helper.getContent());
            System.out.println("TO FRONTEND: ");
            System.out.println("SENDER_ID: " + message.getSenderId() + "  RECEIVER: " + message.getReceiverId() + "  CONTENT: " + message.getMessageContent() + " CHANNEL_ID: " + message.getChannelID());
//                String test2 = String.valueOf(message.toObject());
            currentChannel.broadcastMessage(message);
            currentChannel.addHistory(message.getMessageId(), message);
            System.out.println("HISTORY SIZE: " + currentChannel.getHistory().size());
            System.out.println("--------------------------------------------------------------------------------------------------------------------------------------");
            return;


//                });
//                if(channel.ifBlocked(sender)){                                  //if is blocked
//                    channel.broadcastNoti(MsgFac.makeNoti("block", helper.getSender()));
//                    channel.broadcastMessage(MsgFac.makeMsg(false, helper.getContent()));
//                    System.out.println("BLOCKED");
//                    return;
//                }else if(sender.getMuteStatus()){                               //if is muted
//                    channel.broadcastNoti(MsgFac.makeNoti("mute", helper.getSender()));
//                    channel.broadcastMessage(MsgFac.makeMsg(false, helper.getContent()));
//                    System.out.println("MUTED");
//                    return;
//                }else if(SpeechDetector.detectDir(helper)){         //if contains hate words
//                    channel.broadcastNoti(MsgFac.makeNoti("warn", helper.getSender()));
//                    channel.broadcastMessage(MsgFac.makeMsg(true, helper.getContent()));
//                    sender.addWarnNum();
//                    if(sender.getWarnNum() > 1 ){
//                        sender.setMuteStatus(true);
//                        System.out.println("MUTED NOW!");
//                    }
//                    System.out.println("WARNED");
//                    return;
//                }
//                System.out.println("BAD message_tag determine: DONE");
//                Message message= MsgFac.makeMsg(true, helper.getContent());
//                System.out.println("TO FRONTEND: " );
//                System.out.println("SENDER_ID: " + message.getSenderId() + "  RECEIVER: " + message.getReceiverId() + "  CONTENT: " + message.getMessageContent() + " CHANNEL_ID: " + message.getChannelID() );
////                String test2 = String.valueOf(message.toObject());
//                channel.broadcastMessage(message);
//                channel.addHistory(message.getMessageId(), message);
//                System.out.println("HISTORY SIZE: " + channel.getHistory().size());
//                System.out.println("--------------------------------------------------------------------------------------------------------------------------------------");
//                return;
//            });
        }

        if (helper.getSender() > 0 && helper.getReceiver() > 0 && helper.getSender() != helper.getReceiver()) {       // --->DM
            AUser dmSender = cw.getUserById(helper.getSender());
//            cw.getJoinedChannels(helper.getSender()).forEach(channel -> {
            if (currentChannel.ifBlocked(dmSender)) {                                  //if is blocked
                currentChannel.broadcastNoti(MsgFac.makeNoti("block", helper.getSender()));
                currentChannel.directMessage(MsgFac.makeDm(false, helper.getContent()));
                return;
            } else if (dmSender.getMuteStatus()) {                               //if is muted
                currentChannel.broadcastNoti(MsgFac.makeNoti("mute", helper.getSender()));
                currentChannel.directMessage(MsgFac.makeDm(false, helper.getContent()));
                return;
            } else if (SpeechDetector.detectDir(helper)) {         //if contains hate words
                currentChannel.broadcastNoti(MsgFac.makeNoti("warn", helper.getSender()));
                currentChannel.directMessage(MsgFac.makeDm(true, helper.getContent()));
                dmSender.addWarnNum();
                if (dmSender.getWarnNum() > 1) {
                    dmSender.setMuteStatus(true);
                }
                return;
            }
            currentChannel.directMessage(MsgFac.makeDm(true, helper.getContent()));
            return;
//            });
        }

        if(helper.getSender() == helper.getReceiver()){         //senderId equals receiverId means to edit or recall someone's own message ----> recall/edit
            System.out.println("HERE IN EDIT/RECALL MESSAGE");
            Channel editChannel = cw.getChannelById(helper.getChannel());
            String notiType;
            if(editChannel != null){
                if (ItemFinder.findString(helper.getContent()).isEmpty()) {
                    System.out.println("recall here");

                    editChannel.editMessage(MsgFac.makeEmsg(ItemFinder.findNum(helper.getContent()), ""));
                    notiType = "recall";
                    System.out.println(notiType);

                } else {
                    editChannel.editMessage(MsgFac.makeEmsg(ItemFinder.findNum(helper.getContent()), ItemFinder.findString(helper.getContent())));
                    notiType ="edit";
                }


                editChannel.broadcastNoti(MsgFac.makeNoti(notiType,helper.getSender()));
                return;
            }
        }

        if (helper.getSender() == -1) {                                           //senderId_-1 means current channel admin is to delete one AMessage by tracking messageId
            Channel editChannel = cw.getChannelById(helper.getChannel());
            editChannel.editMessage(MsgFac.makeEmsg(ItemFinder.findNum(helper.getContent()), null));
            editChannel.broadcastNoti(MsgFac.makeNoti("delete", editChannel.getAdmin().getId()));
            return;
        }


//        if(cw.getUserById(helper.getSender()) == cw.getChannelById(helper.getChannel()).getAdmin() ){        //delete message -----> wasted!
//            Channel editChannel = cw.getChannelById(helper.getChannel());
//            editChannel.editMessage(MsgFac.makeEmsg(ItemFinder.findNum(helper.getContent()), null));
//            editChannel.broadcastNoti(MsgFac.makeNoti("delete",helper.getSender()));
//            return;
//        }


    }


}
