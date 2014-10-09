package com.colibri.social_story;

import com.firebase.client.Firebase;

import java.util.TimerTask;

public class StoryCreatorTestClient extends TimerTask {

    private final Firebase fb;
    private final String title;
    private final String word;
    private int id = 1;

    public StoryCreatorTestClient(Firebase fb, int id, String title, String word) {
        this.fb = fb;
        this.id = id;
        this.title = title;
        this.word = word;
    }

    @Override
    public void run() {
        // push a new story
        fb.child(id + "/attributes/title").setValue(title);

        // subscribe to it as usual
        StorySubscriberTestClient sub = new StorySubscriberTestClient(fb.child(Integer.toString(id)), "cuser", word);
        sub.run();
    }
}
