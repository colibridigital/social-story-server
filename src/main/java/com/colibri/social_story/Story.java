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

    private static final int SUGGEST_TIME = 10 * 1000;
    private static final int VOTE_TIME = 10 * 1000;

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
        sb.setSuggestPhase();
        sb.addSuggestionListener("suggestions");

        while (true) {
            Thread.sleep(SUGGEST_TIME);
            suggestionEnd();
            Thread.sleep(VOTE_TIME);
            voteEnd();
            roundEnd();
        }

        // end();
    }

    private void suggestionEnd() throws InterruptedException {
        System.out.println("Suggestion end");

        Map<String, Object> m = new HashMap<>();
        for (DataSnapshot ds : sb.getSuggestions()) {
            System.out.println(ds.getValue() + " " + ds.getName());
            m.put((String)ds.getValue(), ds.getName());
        }
        sb.syncSet("words", m);

        sb.setVotePhase();

        sb.clearSuggestions();
    }

    private void voteEnd() {
        // TODO process votes and add to story

    }

    private void roundEnd() throws InterruptedException {
        sb.setSuggestPhase();
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




