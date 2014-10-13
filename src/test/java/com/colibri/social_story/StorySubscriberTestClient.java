package com.colibri.social_story;

import com.colibri.social_story.utils.Utils;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;

import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

/** Simple client that connects to a story proposes and votes for the same word. */
class StorySubscriberTestClient extends TestClient {

    private final String word;

    StorySubscriberTestClient(Firebase fb, String username, String word) {
        super(fb, username);
        this.word = word;
    }

    @Override
    public void run() {
        fb.child("users").updateChildren(Utils.mapFromKeys(this.username, (Object) this.username));
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

        fb.child("suggestions").updateChildren(Utils.mapFromKeys(this.username, (Object)this.word));

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
        fb.child("votes").updateChildren(Utils.mapFromKeys(this.username, (Object)this.word));
    }
}
