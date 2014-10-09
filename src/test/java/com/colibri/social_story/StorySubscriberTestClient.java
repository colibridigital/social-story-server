package com.colibri.social_story;

import com.colibri.social_story.utils.Utils;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;

import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

class StorySubscriberTestClient extends TimerTask {
    private final Firebase fb;
    private String username;

    StorySubscriberTestClient(Firebase fb, String username) {
        this.fb = fb;
        this.username = username;
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
