package com.colibri.social_story;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class Story {

    private final StoryBase sb;
    private int users = 0;
    private int minUsers;
    final CountDownLatch done = new CountDownLatch(1);

    public Story(int minUsers, Firebase fb) {
        this.minUsers = minUsers;
        this.sb = new StoryBase(fb);
    }

    // wait for min users and start
    void connect() throws InterruptedException {
        sb.child("users").addChildEventListener(new FirebaseChildEventListenerAdapter() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                try {
                    addUser();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        done.await();
    }

    private void start() throws InterruptedException {
        System.out.println("Story started");
        Map<String, Object> mp = new HashMap<>();
        mp.put("started", "true");
        mp.put("phase", "suggestion");
        System.out.println(sb.getServerOffsetMillis());
        mp.put("time_started", Long.toString(sb.getServerOffsetMillis()));
        sb.syncSet("attributes", mp);

        end();
    }

    private void suggestionEnd() {

    }

    private void voteEnd() {

    }

    private void roundEnd() {
        // round == suggest + vote
    }

    private void end() {
        System.out.println("Story end");
        done.countDown();
    }

    private void addUser() throws InterruptedException {
        System.out.println("User registered");
        users++;
        if (users >= this.minUsers) {
            start();
        }
    }

}




