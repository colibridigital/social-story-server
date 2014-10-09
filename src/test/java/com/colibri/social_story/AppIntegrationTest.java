package com.colibri.social_story;

import com.colibri.social_story.*;
import com.colibri.social_story.utils.Utils;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import junit.framework.TestCase;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

public class AppIntegrationTest extends TestCase {

    private static class SimpleTestClient extends TimerTask {
        private String username;

        private SimpleTestClient(String username) {
            this.username = username;
        }

        @Override
        public void run() {
            Firebase fb = new Firebase(App.FB_URL);
            fb.child("users").updateChildren(Utils.mapFromKeys(this.username, (Object)this.username));
            final CountDownLatch done = new CountDownLatch(1);
            fb.child("attributes/phase").addValueEventListener(
                    new FirebaseValueEventListenerAdapter() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if ("SUGGEST".equals(dataSnapshot.getValue()))
                                done.countDown();
                        }});
            try {
                done.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            fb.child("suggestions").updateChildren(Utils.mapFromKeys(" some word", (Object)this.username));

            final CountDownLatch done2 = new CountDownLatch(1);
            fb.child("attributes").child("phase").addValueEventListener(
                    new FirebaseValueEventListenerAdapter() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if ("VOTE".equals(dataSnapshot.getValue()))
                                done2.countDown();
                        }});
            try {
                done2.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            fb.child("votes").updateChildren(Utils.mapFromKeys(this.username, (Object)" some word"));
        }
    }

    public void testApp() throws InterruptedException {
        Timer t = new Timer();
        t.schedule(new SimpleTestClient("user1"), 1 * 1000);
        Timer t2 = new Timer();
        t2.schedule(new SimpleTestClient("user2"), 1 * 1000);

        App app = new App(2 * 1000 , 2 * 1000, 1, new TestPersister());
        app.run();
    }

    private class TestPersister implements StoryPersister {
        @Override
        public void save(Story s) {
            assertEquals(s.getStory(), "My big story some word");
        }
    }
}
