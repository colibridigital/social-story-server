package com.colibri.social_story;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class FirebaseUtils {
    public static void syncClear(final Firebase fb) throws InterruptedException {
        final CountDownLatch done = new CountDownLatch(1);
        fb.removeValue(new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                done.countDown();
            }
        });
        done.await();
    }

    static void syncReadMessages(Firebase fb, int messageCount)
            throws InterruptedException {
        final CountDownLatch done = new CountDownLatch(messageCount);
        fb.addChildEventListener(new FirebaseChildEventListenerAdapter() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                done.countDown();
                System.out.println(dataSnapshot.getValue());
            }
        });
        done.await();
    }

    static void syncWrite(final Firebase fb, Map<String, String> message) throws InterruptedException {
        final CountDownLatch done = new CountDownLatch(1);
        fb.push().setValue(message, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                done.countDown();
            }
        });
        done.await();
    }
}
