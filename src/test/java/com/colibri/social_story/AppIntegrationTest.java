package com.colibri.social_story;

import com.firebase.client.Firebase;
import junit.framework.TestCase;

import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

public class AppIntegrationTest extends TestCase {

    private static final String STORY_TITLE = "Story title";
    private static final int STORY_ID = 1;

    public void testApp() throws InterruptedException {
        Firebase fb = new Firebase(App.FB_URL);

        Timer t0 = new Timer();
        t0.schedule(new StoryCreatorTestClient(fb, STORY_ID, STORY_TITLE), 1000);

        Timer t = new Timer();
        Firebase storyFb = fb.child(Integer.toString(STORY_ID));
        t.schedule(new StorySubscriberTestClient(storyFb, "user1"), 2 * 1000);
        t = new Timer();
        t.schedule(new StorySubscriberTestClient(storyFb, "user2"), 2 * 1000);

        final LinkedList<Story> ss = new LinkedList<>();
        final App app = new App(2 * 1000 , 2 * 1000, 1, new StoryPersister() {
            @Override
            public void save(Story s) {
                ss.add(s);
            }
        });
        t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                app.stop();
            }
        }, 8 * 1000);
        app.run();
        assertEquals(1, ss.size());
        Story s = ss.getFirst();
        assertEquals(s.getTitle(), STORY_TITLE);
        assertEquals(s.getStory(), "My big story some word");
    }
}
