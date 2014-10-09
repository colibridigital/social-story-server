package com.colibri.social_story;

import com.firebase.client.Firebase;
import junit.framework.TestCase;

import java.util.Timer;
import java.util.TimerTask;

public class AppIntegrationTest extends TestCase {

    public void testApp() throws InterruptedException {
        Firebase fb = new Firebase(App.FB_URL);

        Timer t0 = new Timer();
        t0.schedule(new StoryCreatorTestClient(fb, 1), 2 * 1000);

        Timer t = new Timer();
        t.schedule(new StorySubscriberTestClient(fb.child("1"), "user1"), 3 * 1000);
        t = new Timer();
        t.schedule(new StorySubscriberTestClient(fb.child("1"), "user2"), 3 * 1000);

        final App app = new App(2 * 1000 , 2 * 1000, 1, new TestPersister());
        t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                app.stop();
            }
        }, 9 * 1000);
        app.run();
    }

    private class TestPersister implements StoryPersister {
        @Override
        public void save(Story s) {
            // TODO check story title is as proposed
            assertEquals(s.getStory(), "My big story some word");
        }
    }
}
