package com.colibri.social_story;

import com.firebase.client.Firebase;

public class App {

    public static final String FB_URL = "https://colibristory.firebaseio.com/social-story/1";

    private static final int DEFAULT_SUGGEST_TIME = 30 * 1000;
    private static final int DEFAULT_VOTE_TIME = 30 * 1000;
    private static final int N_ROUNDS = 30;

    public static void main(String[] args) throws InterruptedException {
        (new App()).run();
    }

    public void run() throws InterruptedException {
        Story story = new Story(1,
                new FirebaseStoryBase(new Firebase(FB_URL)),
                DEFAULT_SUGGEST_TIME,
                DEFAULT_VOTE_TIME,
                N_ROUNDS
                );
        story.connect();
    }
}


