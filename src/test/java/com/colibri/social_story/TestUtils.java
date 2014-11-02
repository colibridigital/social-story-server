package com.colibri.social_story;

import com.colibri.social_story.entities.User;
import com.firebase.client.Firebase;

import java.util.Iterator;
import java.util.Map;
import java.util.Timer;

public class TestUtils {

    /** Creates a story and adds two more subscribers to it. */
    public static void setUpProducerTwoConsumers(Firebase fb, Map<String, User> users, int storyId, String storyTitle, String word) {
        Iterator<User> it = users.values().iterator();
        StoryCreatorTestClient sc = new StoryCreatorTestClient(fb, it.next(), storyId, storyTitle, word);
        Firebase storyFb = fb.child(Integer.toString(storyId));
        StorySubscriberTestClient sub1 = new StorySubscriberTestClient(storyFb, it.next(), word);
        StorySubscriberTestClient sub2 = new StorySubscriberTestClient(storyFb, it.next(), word);

        sc.auth();
        sub1.auth();
        sub2.auth();

        new Timer().schedule(sc, 1000);
        new Timer().schedule(sub1, 2 * 1000);
        new Timer().schedule(sub2, 2 * 1000);
    }

}
