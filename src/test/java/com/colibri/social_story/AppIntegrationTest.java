package com.colibri.social_story;

import com.firebase.client.Firebase;
import junit.framework.TestCase;

import java.util.LinkedList;
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
        }, 10 * 1000);
        app.run();
        assertEquals(1, ss.size());
        Story s = ss.getFirst();
        //assertEquals(s.getTitle(), "My title");
        assertEquals(s.getStory(), "My big story some word");
    }
}
