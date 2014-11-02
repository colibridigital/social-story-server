package com.colibri.social_story;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

public abstract class TestClient extends TimerTask {

    protected final Firebase fb;
    protected String username;
    protected String uid;

    public TestClient(Firebase fb, String username, String uid) {
        this.fb = fb;
        this.uid = uid;
        this.username = username;
    }

    public void auth() {
        final CountDownLatch cd = new CountDownLatch(1);
        fb.authWithPassword(this.username, "secret", new Firebase.AuthResultHandler() {
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
