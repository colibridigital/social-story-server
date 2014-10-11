package com.colibri.social_story;

import com.colibri.social_story.*;
import com.colibri.social_story.utils.Pair;
import com.firebase.client.Firebase;
import junit.framework.TestCase;
import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Assert;

import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

public class MongoIntegrationTest extends TestCase {

    private static final String STORY_TITLE = "Story title";
    private static final int STORY_ID = 1;

    /** Creates a story and adds two more subscribers to it. */
    private void setUpClients(int storyId, String storyTitle, String word) {
        Firebase fb = new Firebase(App.FB_URL);
        Timer t0 = new Timer();
        t0.schedule(new StoryCreatorTestClient(fb, storyId, storyTitle, word), 1000);
        Timer t = new Timer();
        Firebase storyFb = fb.child(Integer.toString(storyId));
        t.schedule(new StorySubscriberTestClient(storyFb, "user1", word), 2 * 1000);
        t = new Timer();
        t.schedule(new StorySubscriberTestClient(storyFb, "user2", word), 2 * 1000);
    }

    public void testTwoStories() throws InterruptedException {

        setUpClients(1, "Story One", " one rulz!");
        setUpClients(2, "Story Two", " two rulz!");

        final LinkedList<Pair<String, String>> ss = new LinkedList<>();
        final App app = new App(2 * 1000 , 2 * 1000, 1, new MongoPersister());
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                app.stop();
            }
        }, 8 * 1000);
        app.run();

        // TODO check it worked
    }
}
