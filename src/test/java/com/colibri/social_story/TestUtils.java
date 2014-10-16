package com.colibri.social_story;

import com.firebase.client.Firebase;

import java.util.Timer;

public class TestUtils {

    /** Creates a story and adds two more subscribers to it. */
    public static void setUpProducerTwoConsumers(int storyId, String storyTitle, String word) {
        Firebase fb = new Firebase(App.FB_URL);
        StoryCreatorTestClient sc = new StoryCreatorTestClient(fb, "megatron1", storyId, storyTitle, word);
        Firebase storyFb = fb.child(Integer.toString(storyId));
        StorySubscriberTestClient sub1 = new StorySubscriberTestClient(storyFb, "megatron2", word);
        StorySubscriberTestClient sub2 = new StorySubscriberTestClient(storyFb, "megatron3", word);

        sc.auth();
        sub1.auth();
        sub2.auth();

        new Timer().schedule(sc, 1000);
        new Timer().schedule(sub1, 2 * 1000);
        new Timer().schedule(sub2, 2 * 1000);
    }
}
