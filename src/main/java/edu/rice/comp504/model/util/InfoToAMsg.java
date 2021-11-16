package edu.rice.comp504.model.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Calendar;
import java.util.Date;

/*
 * This class is to convert information from js to create AMessage
 * information should include: "senderId", "channelId", "receiverId", "messageContent"
 * */


public class InfoToAMsg {
    private int senderId;
    private int receiverId;
    private int channelId;
    private Date time;
    private String content;


    public InfoToAMsg(String info) {
        JsonObject obj = new JsonParser().parse(info).getAsJsonObject();
        this.senderId = obj.get("senderId").getAsInt();
        this.receiverId = obj.get("receiverId").getAsInt();
        this.channelId = obj.get("channelId").getAsInt();
        this.time = DateCaster.StringToTime(obj.get("time").getAsString());
//        Date date = Calendar.getInstance().getTime();
        this.content = obj.get("content").getAsString();
    }


    public int getSender() {
        return senderId;
    }

    public int getReceiver() {
        return receiverId;
    }

    public int getChannel() {
        return channelId;
    }

    public Date getTime() {
        return time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String str) {
        this.content = str;
    }


}
