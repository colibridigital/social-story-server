package com.colibri.social_story;

import com.firebase.client.DataSnapshot;

import java.util.*;
import java.util.concurrent.CountDownLatch;

public class StoryRoom {

    private String story = "My big story";
    private final StoryBase sb;
    private int users = 0;
    private int minUsers;

    private static final int SUGGEST_TIME = 30 * 1000;
    private static final int VOTE_TIME = 30 * 1000;

    public StoryRoom(int minUsers, StoryBase sb) {
        this.minUsers = minUsers;
        this.sb = sb;
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
        sb.addVoteListener("votes");

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
        sb.clearSuggestions();
        sb.setVotePhase();
    }

    private void voteEnd() throws InterruptedException {
        Map<String, Object> m = new HashMap<>();
        DataSnapshot ds = sb.getVotes().peek();
        if (ds != null) {
            story = story + ds.getValue();
            m.put("story", story);
            sb.syncSet("", m);
        }
        sb.clearVotes();
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




