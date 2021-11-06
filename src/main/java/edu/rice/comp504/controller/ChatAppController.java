package edu.rice.comp504.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import edu.rice.comp504.adapter.DispatchAdapter;
import edu.rice.comp504.adapter.WebSocketAdapter;
import edu.rice.comp504.model.ChatAppWorld;
import edu.rice.comp504.model.channel.Channel;
import edu.rice.comp504.model.message.AMessage;
import edu.rice.comp504.model.user.AUser;
import edu.rice.comp504.model.util.SwitchHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static spark.Spark.*;

/**
 * The chat app controller communicates with all the clients on the web socket.
 */
public class ChatAppController {

    /**
     * Chat App entry point.
     *
     * @param args The command line arguments
     */
    public static void main(String[] args) {
        port(getHerokuAssignedPort());
        staticFiles.location("/public");
        Gson gson = new Gson();
        ChatAppWorld cw = ChatAppWorld.getOnlyWord();
        DispatchAdapter da = new DispatchAdapter(cw);
        webSocket("/chatapp", WebSocketAdapter.class);
        init();

        // return AUser
        post("/signUp", (request, response) -> {
            String userName = request.queryParams("userName");
            String birthDate = request.queryParams("birthDate");
            String school = request.queryParams("school");
            String interest = request.queryParams("interest");
            String password = request.queryParams("password");
            //da.signUp(userName, birthDate, school, interest, password)
//            System.out.println(userName + " " + birthDate + " " + school + " " + interest + " " + password);
//            return gson.toJson("login");
            return gson.toJson(da.signUp(userName, birthDate, school, interest, password));
        });


        /**
         * login
         * pass in: userName and password
         * return: new user_json
         */
        post("/login", (request, response) -> {
            String userName = request.queryParams("userName");
            String password = request.queryParams("password");
            JsonObject js = new JsonObject();
            if (da.login(userName, password)) {
                js.addProperty("isLogin", true);
                js.addProperty("userName", da.getUserByName(userName).getName());
                js.addProperty("birthDate", da.getUserBirthStr(userName));
                js.addProperty("school", da.getUserByName(userName).getSchool());
                js.addProperty("interest", da.getUserByName(userName).getInterest());
                js.addProperty("userId", da.getUserByName(userName).getId());

//                System.out.println("ID "+da.getUserByName(userName).getId());
            } else {
                js.addProperty("isLogin", false);
            }
            return js;
        });

        /**
         * createChannel
         * pass in: adminId, capacity, channelName, and isPrivate
         * return: new channel_json
         */
        post("/createChannel", (request, response) -> {
            //String sessionInfo = request.params(":session");
            int adminId = Integer.parseInt(request.queryParams("adminId"));
            int capacity = Integer.parseInt(request.queryParams("capacity"));
            String channelName = request.queryParams("channelName");
            Boolean isPrivate = Boolean.parseBoolean(request.queryParams("isPrivate"));
            Channel channel = da.createChannel(adminId, isPrivate, capacity, channelName);
//            AUser user = ChatAppWorld.getUserById(adminId);
//            System.out.println(user);
            return gson.toJson(channel);
//            return gson.toJson("user can create channel");
        });

        /**
         * quitChannel
         * pass in: userId, channelId
         * return: boolean_json if quitSuccess
         */
        post("/quitChannel", (request, response) -> {
            int userId = Integer.parseInt(request.queryParams("userId"));
            int channelId = Integer.parseInt(request.queryParams("channelId"));
            System.out.println("----------------------------------------------------------------------");
            System.out.println("EndPoint: leave channel");
            return gson.toJson("SingleQuitSuccess ? :" + da.quitChannel(userId, channelId));
        });

        /**
         * quitAllChannels
         * pass in: userId
         * return: boolean_json if quitAll success
         */
        get("/quitAllChannels", (request, response) -> {
            boolean quitIndicator = true;
            int userId = Integer.parseInt(request.queryParams("userId"));
            ArrayList<Channel> list = da.getJoinedChannels(userId);
            for (Channel temp : list) {
                quitIndicator = da.quitChannel(userId, temp.getChannelId());
            }
            return gson.toJson("QuitAllSuccess ? :" + quitIndicator);
        });


        /**
         * When click at one channel, return its information
         * pass in: channelId
         * return: this channel_json
         */
        get("/switchChannel/:channelId", (request, response) -> {
            int channelId = Integer.parseInt(request.params(":channelId"));
//            Channel historyChannel = da.getChannelById(channelId);
//            SwitchHelper helper = new SwitchHelper(historyChannel);
            return gson.toJson(da.switchHelper(da.getChannelById(channelId)));
        });


        /**
         * When login, a user could see all channels available(private & public) in this app
         * pass in: No value
         * return: ArrayList<Channel>_json
         */
        post("/viewAllChannels", (request, response) -> {
            Map<Integer, Channel> allChannels = da.getAllChannels();
            ArrayList<Channel> allChannelList = new ArrayList<>(allChannels.values());
            return gson.toJson(allChannelList);
        });


        /**
         * search channel, to find all channels whose name contains keywords
         * pass in: key searching words
         * return: ArrayList<Channel>_json
         */
        post("/searchChannel", (request, response) -> {
            String keywords = request.queryParams("keyWords");
            Map<Integer, Channel> allChannels = da.getAllChannels();
            ArrayList<Channel> targetChannels = new ArrayList<>();
            for (Channel temp : allChannels.values()) {
                if (temp.getChannelName().contains(keywords) || keywords.contains(temp.getChannelName())) {
                    targetChannels.add(temp);
                }
            }
            return gson.toJson(targetChannels);
        });


        /**
         * user join the channel
         * pass in: userId and channelId
         * return: boolean_json if join success
         */
        post("/joinChannel", (request, response) -> {
            int userId = Integer.parseInt(request.queryParams("userId"));
            int channelId = Integer.parseInt(request.queryParams("channelId"));
            System.out.println(userId);
            System.out.println(channelId);
            Channel thisChannel = da.getChannelById(channelId);
            AUser thiUser = da.getUserById(userId);
            boolean joinIndicator = thisChannel.addUser(thiUser) && thiUser.addChannel(channelId);// To see if joining success
            return gson.toJson(thisChannel);
        });


        /**
         * channel admin invites another user to join the channel
         * pass in: invite and channelId
         * return: boolean_json if invite success
         */
        get("/invite/:channelId/:userId", (request, response) -> {
            int channelId = Integer.parseInt(request.params(":channelId"));
            int userId = Integer.parseInt(request.params(":userId"));
            Channel thisChannel = da.getChannelById(channelId);
            AUser user = da.getUserById(userId);
            boolean inviteIndicator = thisChannel.addUser(user) && user.addChannel(thisChannel.getChannelId());
            return gson.toJson("InviteSuccess" + inviteIndicator);
        });


        /**
         * admin could block someone
         * pass in: userId and channelId
         * return: boolean_json if block success
         */
        get("/block/:channelId/:userId", (request, response) -> {
            boolean blockIndicator = false;
            int channelId = Integer.parseInt(request.params(":channelId"));
            int userId = Integer.parseInt(request.params(":userId"));
            AUser user = da.getUserById(userId);
            Channel channel = da.getChannelById(channelId);
            System.out.println("block user " + userId + "from channel " + channelId);
            if (user != channel.getAdmin() && !channel.ifBlocked(user)) {
                channel.blockUser(user);
                blockIndicator = true;
            }
            return gson.toJson("BlockSuccess" + blockIndicator);
        });


//        /**
//         * mute would be carried out by backend systematically
//         */
//        get("/mute/:userId", (request, response) -> {
//            int userId = Integer.parseInt(request.params(":userId"));
//            AUser user = da.getUserById(userId);
//            user.setMuteStatus(true);
//            return gson.toJson("MuteSuccess");
//        });

        /**
         * view all channels a user has joined
         * pass in: userId
         * return: ArrayList<Channel>_json if block success
         */
        get("/joinedChannels/:userId", (request, response) -> {
            String userId = request.params(":userId");
            da.getJoinedChannels(Integer.parseInt(userId));
            return gson.toJson(da.getJoinedChannels(Integer.parseInt(userId)));
        });

//        /**
//         * get history for a certain channel ----->see "switchChannel" Endpoint
//         * pass in: channelId
//         * return: ArrayList<AMessage>_json
//         */
//        get("/getHistory", (request, response) -> {
//            int channelId = Integer.parseInt(request.params("channelId"));
//            Channel historyChannel = da.getChannelById(channelId);
//            ArrayList<AMessage> history = new ArrayList<>(historyChannel.getHistory().values());
//            return gson.toJson(history);
//        });

        /**
         * get all users registered
         * return: ArrayList<AUser>_json
         */
        get("/getAllUsers", (request, response) -> {
            return gson.toJson(da.getAllUsers());
        });


        /**
         * get all roommates
         * return: HashSet<AUser>_json
         */
        get("/getRoommates/:channelId", (request, response) -> {
            int channelId = Integer.parseInt(request.params(":channelId"));
            Channel curChannel = da.getChannelById(channelId);
            HashSet<AUser> roommates = curChannel.getRoommates();
            return gson.toJson(roommates);
        });


    }


    /**
     * Get the heroku assigned port number.
     *
     * @return The heroku assigned port number
     */
    private static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; // return default port if heroku-port isn't set.
    }
}
