package com.colibri.social_story;

import com.firebase.client.Firebase;

import java.util.TimerTask;

public class StoryCreatorTestClient extends TimerTask {

    private final Firebase fb;
    private int id = 1;

    public StoryCreatorTestClient(Firebase fb, int id) {
        this.fb = fb;
        this.id = id;
    }

    @Override
    public void run() {
        // push a new story
        fb.child(id + "/attributes/title").setValue("Something");

        // subscribe to it as usual
        StorySubscriberTestClient sub = new StorySubscriberTestClient(fb.child(Integer.toString(id)), "cuser");
        sub.run();
    }
}
