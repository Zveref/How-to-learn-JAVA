package edu.rice.comp504.model.util;

import java.util.ArrayList;

public class SpeechDetector {
    private static ArrayList<String> badWords = new ArrayList<String>();


    public static boolean detectDir(InfoToAMsg src) {
        boolean indicator = false;
        badWords.add("fuck");
        badWords.add("shit");
        badWords.add("dumb");
        badWords.add("bitch");
        badWords.add("nigger");
        badWords.add("motherfucker");
        String str = src.getContent();
        String[] tokens = str.split(" ");
        String newStr = "";
        for (String token : tokens) {
            String t = token;
            for (String temp : badWords) {
                if (t.toLowerCase().equals(temp)) {
                    token = "**Dirty Word Warning**";
                    indicator = true;
                }
            }
            newStr += token + " ";
        }
        src.setContent(newStr);
        return indicator;
    }

    public static ArrayList<String> getRules() {
        return badWords;
    }
}