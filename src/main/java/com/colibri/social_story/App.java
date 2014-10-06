package com.colibri.social_story;

import com.firebase.client.Firebase;

public class App {

    public static final String FB_URL = "https://colibristory.firebaseio.com/social-story/1";
    public static final int MESSAGE_COUNT = 3;

    public static void main(String[] args) throws InterruptedException {
        (new App()).run();
    }

    public void run() throws InterruptedException {
        StoryRoom story = new StoryRoom(1, new FirebaseStoryBase(new Firebase(FB_URL)));
        story.connect();
    }
}


