package com.colibri.social_story;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class Story {

    private final Firebase fb;
    private int users = 0;
    private int minUsers;
    final CountDownLatch done = new CountDownLatch(1);

    public Story(int minUsers, Firebase fb) {
        this.minUsers = minUsers;
        this.fb = fb;
    }

    // wait for min users and start
    void connect() throws InterruptedException {
        fb.child("users").addChildEventListener(new FirebaseChildEventListenerAdapter() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                addUser();
            }
        });
        done.await();
    }

    private void start() {
        System.out.println("Story started");
    }

    private void suggestionEnd() {

    }

    private void voteEnd() {

    }

    private void roundEnd() {
        // round == suggest + vote
    }

    private void end() {
        done.countDown();
    }

    private void addUser() {
        System.out.println("User registered");
        users++;
        if (users > this.minUsers) {
            start();
        }
    }

}



