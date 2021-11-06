package edu.rice.comp504.model.user;


import java.util.ArrayList;
import java.util.Date;

public class User extends AUser {

    public User() {
    }

    public User(String name, Date birthdate, String school, String interest, String password, int id) {
        this.name = name;
        this.birthDate = birthdate;
        this.school = school;
        this.interest = interest;
        this.password = password;
        this.id = id;
    }

    @Override
    public boolean addChannel(int channelId) {
        boolean indicator = false;
        if (!this.channelList.contains(channelId)) {
            channelList.add(channelId);
            indicator = true;
        }
        return indicator;
    }

    @Override
    public ArrayList<Integer> removeChannel(int channelId) {
        for (int i = 0; i < channelList.size(); i++) {
            if (channelList.get(i) == channelId) {
                System.out.println("找到channel");
                channelList.remove(i);
            }
        }
        return this.channelList;
    }


}
