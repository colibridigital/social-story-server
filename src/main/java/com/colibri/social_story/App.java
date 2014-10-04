package com.colibri.social_story;

import com.firebase.client.Firebase;

import java.util.HashMap;
import java.util.Map;

public class App {

    public static final String FB_URL = "https://colibristory.firebaseio.com/messages";
    public static final int MESSAGE_COUNT = 3;

    public static void main(String[] args) throws InterruptedException {
        (new App()).run();
    }

    // Waits for a few messages, prints IDK, removes all messages and exits
    public void run() throws InterruptedException {
        final Firebase fb = new Firebase(FB_URL);
        FirebaseUtils.syncReadMessages(fb, 0);
        Map<String, String> mp = new HashMap<>();
        mp.put("name", "paul");
        mp.put("text", "I don't care!");
        FirebaseUtils.syncWrite(fb, mp);
        FirebaseUtils.syncClear(fb);
    }
}


