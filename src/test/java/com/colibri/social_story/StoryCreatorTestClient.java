package com.colibri.social_story;

import com.colibri.social_story.utils.Utils;
import com.firebase.client.Firebase;

import java.util.Map;

public class StoryCreatorTestClient extends TestClient {

    private final String title;
    private final String word;
    private int id = 1;

    public StoryCreatorTestClient(Firebase fb, String username, int id, String title, String word) {
        super(fb, username);
        this.id = id;
        this.title = title;
        this.word = word;
    }

    @Override
    public void run() {
        // push a new story
        Map<String, Object> mp = Utils.mapFromKeys("title", (Object)title);
        mp.put("minUsers", 2);
        fb.child(id + "/").setValue(mp);

        // subscribe to it as usual
        StorySubscriberTestClient sub = new StorySubscriberTestClient(fb.child(Integer.toString(id)), this.username, word);
        sub.run();
    }
}
