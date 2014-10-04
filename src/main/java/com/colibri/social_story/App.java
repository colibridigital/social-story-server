package com.colibri.social_story;

import com.firebase.client.Firebase;

import java.util.HashMap;
import java.util.Map;

public class App {

    public static final String FB_URL = "https://colibristory.firebaseio.com/social-story/1";
    public static final int MESSAGE_COUNT = 3;

    public static void main(String[] args) throws InterruptedException {
        (new App()).run();
    }

    // Waits for a few messages, prints IDK, removes all messages and exits
    public void run() throws InterruptedException {
        final Firebase fb = new Firebase(FB_URL);

        Story story = new Story(2, fb);
        story.connect();

    }
}


