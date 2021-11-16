package edu.rice.comp504.model.util;

import edu.rice.comp504.model.channel.Channel;

public class ItemFinder {

    public static int findNum(String str) {
        String num = "";
        int i = 0;
        while (i < str.length()) {
            if (Character.isDigit(str.charAt(i))) {
                num += str.charAt(i);
            }
            i++;
        }
        if (num != "") {
            return Integer.parseInt(num);
        }
        return -99999;
    }

    public static String findString(String str) {
        String string = "";
        int i = 0;
        while (i < str.length()) {
            if (Character.isLetter(str.charAt(i))) {
                string += str.charAt(i);
            }
            i++;
        }
        return string;
    }


}
