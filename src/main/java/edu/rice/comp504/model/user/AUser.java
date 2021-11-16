package edu.rice.comp504.model.user;

import java.util.ArrayList;
import java.util.Date;

/**
 * Abstract user class that all user classes will implement.
 */
public abstract class AUser implements IUser {

    protected int id;
    protected String name;
    protected Date birthDate;
    protected String school;
    protected String interest;
    protected String password;
    protected ArrayList<Integer> channelList;
    protected boolean muteStatus = false;
    protected int warnNum = 0;


    /**
     * Add when the user's message was blocked because of hate speech.
     */
    public void addWarnNum() {
        this.warnNum += 1;
    }


    /**
     * Block a user if blockNum = 3.
     */
    public void globalBlock() {
        if (warnNum >= 3) {
            muteStatus = true;
        }
    }

    /**
     * see if it's muted in all channels
     */
    public boolean getMuteStatus() {
        return muteStatus;
    }


    /**
     * Get the user's name.
     *
     * @return user's name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Set the user's name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the user's birthdate.
     *
     * @return User's birthdate
     */
    public Date getBirthDate() {
        return this.birthDate;
    }

    /**
     * Set the user's birthDate.
     */
    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    /**
     * Get the user's school.
     *
     * @return User's school
     */
    public String getSchool() {
        return this.school;
    }

    /**
     * Set the user's school.
     */
    public void setSchool(String school) {
        this.school = school;
    }

    /**
     * Get the user's interest.
     *
     * @return User's interest.
     */
    public String getInterest() {
        return this.interest;
    }

    /**
     * Set the user's interest.
     */
    public void setInterest(String interest) {
        this.interest = interest;
    }

    /**
     * Get the user's password.
     *
     * @return User's password.
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Set the user's password.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Get all the channels' id that the user is in.
     *
     * @return list of channels' id
     */
    public ArrayList<Integer> getChannelList() {
        return this.channelList;
    }

    /**
     * Add channel id to the channel's list.
     *
     * @param channelId the channel's id that need to add.
     */
    public abstract boolean addChannel(int channelId);

    /**
     * Remove channel id from the channel's list.
     *
     * @param channelId the channel's id that need to remove.
     */
    public abstract ArrayList<Integer> removeChannel(int channelId);

    /**
     * Get the user's school.
     *
     * @return User's school
     */
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public void createChannelList() {
        this.channelList = new ArrayList<>();
    }

    public void setMuteStatus(boolean muteStatus) {
        this.muteStatus = muteStatus;
    }

    public int getWarnNum() {
        return warnNum;
    }
}
