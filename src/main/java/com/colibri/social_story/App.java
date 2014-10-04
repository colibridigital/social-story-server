package com.colibri.social_story;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class App {
    public static final String FB_URL = "https://sizzling-torch-6706.firebaseio.com/messages";
    public static final int MESSAGE_COUNT = 3;

    public static void main(String[] args) throws InterruptedException {
        (new App()).run();
    }

    // Waits for a few messages, prints IDK, removes all messages and exits
    public void run() throws InterruptedException {
        final Firebase fb = new Firebase(FB_URL);
        syncReadMessages(fb, MESSAGE_COUNT);
        syncWrite(fb);
        syncClear(fb);
    }

    private void syncReadMessages(Firebase fb, int messageCount)
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

    private void syncWrite(final Firebase fb) throws InterruptedException {
        Map<String, String> mp = new HashMap<>();
        mp.put("name", "paul");
        mp.put("text", "I don't care!");
        final CountDownLatch done = new CountDownLatch(1);
        fb.push().setValue(mp, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                done.countDown();
            }
        });
        done.await();
    }

    private void syncClear(final Firebase fb) throws InterruptedException {
        final CountDownLatch done = new CountDownLatch(1);
        fb.removeValue(new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                done.countDown();
            }
        });
        done.await();
    }
}


