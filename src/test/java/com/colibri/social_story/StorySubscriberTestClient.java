package com.colibri.social_story;

import com.colibri.social_story.entities.User;
import com.colibri.social_story.transport.FirebaseValueEventListenerAdapter;
import com.colibri.social_story.utils.Utils;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;

import java.util.concurrent.CountDownLatch;

/** Simple client that connects to a story proposes and votes for the same word. */
class StorySubscriberTestClient extends TestClient {

    private final String word;

    public StorySubscriberTestClient(Firebase storyFb, User user, String word) {
        super(storyFb, user);
        this.word = word;
    }

    @Override
    public void run() {
        String uid = user.getUid().getUid();
        fb.child("users").updateChildren(Utils.mapFromKeys( uid, (Object)uid));
        final CountDownLatch done = new CountDownLatch(1);
        fb.child("/phase").addValueEventListener(
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
        fb.child("suggestions").updateChildren(Utils.mapFromKeys(uid, (Object)this.word));

        final CountDownLatch done2 = new CountDownLatch(1);
        fb.child("phase").addValueEventListener(
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
        fb.child("votes").updateChildren(Utils.mapFromKeys(uid, (Object)this.word));
    }
}
