package com.colibri.social_story.bots;

import com.colibri.social_story.entities.User;
import com.colibri.social_story.utils.Utils;
import com.firebase.client.Firebase;

import java.util.Map;

public class CreateSubscribeBot extends Bot {

    private final String title;
    private final String word;
    private int id = 1;

    public CreateSubscribeBot(Firebase fb, User user, int id, String title, String word) {
        super(fb, user);
        this.id = id;
        this.title = title;
        this.word = word;
    }

    @Override
    public void run() {
        // push a new story
        Map<String, Object> mp = Utils.mapFromKeys("title", title);
        mp.put("minUsers", 2);
        fb.child(id + "/").setValue(mp);

        // subscribe to it as usual
        SubscriberBot sub = new SubscriberBot(fb.child(Integer.toString(id)), this.user, word);
        sub.run();
    }
}
