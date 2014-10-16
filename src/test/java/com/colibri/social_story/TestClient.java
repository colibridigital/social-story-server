package com.colibri.social_story;

import com.firebase.client.Firebase;

import java.util.TimerTask;

public abstract class TestClient extends TimerTask {

    protected final Firebase fb;
    protected String username;

    public TestClient(Firebase fb, String username) {
        this.fb = fb;
        this.username = username;
    }

    public void auth() {
//        final CountDownLatch cd = new CountDownLatch(1);
//        fb.authWithPassword(this.username, "secret", new Firebase.AuthResultHandler() {
//            @Override
//            public void onAuthenticated(AuthData authData) {
//                System.out.println("Auth successful");
//                cd.countDown();
//            }
//
//            @Override
//            public void onAuthenticationError(FirebaseError firebaseError) {
//                System.out.println("Authentication failed " + firebaseError);
//                cd.countDown();
//            }
//        });
//        try {
//            cd.await();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }
}
