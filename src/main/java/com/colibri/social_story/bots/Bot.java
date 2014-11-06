package com.colibri.social_story.bots;

import com.colibri.social_story.entities.User;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

public abstract class Bot extends TimerTask {

    protected final Firebase fb;
    protected final User user;

    public Bot(Firebase fb, User user) {
        this.fb = fb;
        this.user = user;
    }

    public void auth() {
        final CountDownLatch cd = new CountDownLatch(1);
        System.out.println("Authenticating user " + user.getUsername());
        fb.authWithPassword(user.getUsername(), "secret", new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                System.out.println("Auth successful");
                System.out.println(authData);
                cd.countDown();
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                System.out.println("Authentication failed " + firebaseError);
                cd.countDown();
            }
        });
        try {
            cd.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
