package com.colibri.social_story;

import com.colibri.social_story.utils.Utils;
import com.firebase.client.Firebase;
import junit.framework.TestCase;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {

    private static class SimpleClient extends TimerTask {
        private String username;

        private SimpleClient(String username) {
            this.username = username;
        }

        @Override
        public void run() {
            Firebase fb = new Firebase(App.FB_URL);
            fb.child("users").updateChildren(Utils.mapFromKeys(this.username, (Object)this.username));
        }
    }

    public void testApp() throws InterruptedException {
        System.out.println("Test");
        Timer t = new Timer();
        t.schedule(new SimpleClient("user1"), 2 * 1000);
        t.schedule(new SimpleClient("user2"), 2 * 1000);

        App app = new App(2 * 1000 , 2 * 1000, 1, new TestPersister());
        app.run();
    }

    private class TestPersister implements StoryPersister {
        @Override
        public void save(Story s) {
            assertEquals(s.getStory(), "My big story");
        }
    }
}
