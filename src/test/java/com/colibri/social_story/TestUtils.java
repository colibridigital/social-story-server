package com.colibri.social_story;

import com.firebase.client.Firebase;

import java.util.Timer;

public class TestUtils {

    /** Creates a story and adds two more subscribers to it. */
    public static void setUpProducerTwoConsumers(int storyId, String storyTitle, String word) {
        Firebase fb = new Firebase(App.FB_URL);
        new Timer().schedule(new StoryCreatorTestClient(fb, storyId, storyTitle, word), 1000);
        Firebase storyFb = fb.child(Integer.toString(storyId));
        new Timer().schedule(new StorySubscriberTestClient(storyFb, "user1", word), 2 * 1000);
        new Timer().schedule(new StorySubscriberTestClient(storyFb, "user2", word), 2 * 1000);
    }
}
