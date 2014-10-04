package com.colibri.social_story;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;

public class Story {

    private final StoryBase sb;
    private int users = 0;
    private int minUsers;


    public Story(int minUsers, Firebase fb) {
        this.minUsers = minUsers;
        this.sb = new StoryBase(fb);
    }

    // wait for min users and start
    void connect() throws InterruptedException {
        final CountDownLatch done = new CountDownLatch(this.minUsers);
        sb.child("users").addChildEventListener(new FirebaseChildEventListenerAdapter() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                done.countDown();
        }});

        done.await();

        addUser();
    }

    private void start() throws InterruptedException {
        System.out.println("Story started");
        Map<String, Object> mp = new HashMap<>();
        mp.put("started", "true");
        mp.put("phase", "suggestion");
        mp.put("time_started", Long.toString(sb.getServerOffsetMillis()));
        sb.syncSet("attributes", mp);

        // add the handleers
        sb.addSuggestionListener("suggestions");


        Timer timer = new Timer();
        while (true) {
            Thread.sleep(2000);
            suggestionEnd();
            Thread.sleep(2000);
            voteEnd();
            roundEnd();
        }

        // end();
    }

    private void suggestionEnd() throws InterruptedException {
        System.out.println("Suggestion end");
        ConcurrentLinkedQueue<DataSnapshot> suggestions = sb.getSuggestions();

        for (DataSnapshot ds : suggestions) {
            System.out.println(ds.getValue() + " " + ds.getName());
            // TODO do something useful
        }

        sb.clearSuggestions();
    }

    private void voteEnd() {

    }

    private void roundEnd() {
        // round == suggest + vote
    }

    private void end() {
        System.out.println("Story end");
    }

    private void addUser() throws InterruptedException {
        System.out.println("User registered");
        users++;
        if (users >= this.minUsers) {
            start();
        }
    }

}




