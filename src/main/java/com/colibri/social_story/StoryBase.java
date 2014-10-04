package com.colibri.social_story;

import com.firebase.client.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class StoryBase {
    private final Firebase fb;

    public StoryBase(Firebase fb) {
        this.fb = fb;
    }

    public void syncClear() throws InterruptedException {
        final CountDownLatch done = new CountDownLatch(1);
        fb.removeValue(new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                done.countDown();
            }
        });
        done.await();
    }

    Firebase child(String path) {
        return fb.child(path);
    }

    Object syncReadMessage(String path)
            throws InterruptedException {
        final CountDownLatch done = new CountDownLatch(1);
        final List<Object> l = new ArrayList<>();
        System.out.println(fb.getRoot());
        fb.addChildEventListener(new FirebaseChildEventListenerAdapter() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                System.out.println("Something");
                done.countDown();
                l.add(dataSnapshot.getValue());
            }
        });
        done.await();
        return l.get(0);
    }

    void syncSet(String path, Map<String, Object> message) throws InterruptedException {
        final CountDownLatch done = new CountDownLatch(1);
        fb.child(path).setValue(message, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                System.out.println("Done");
                done.countDown();
            }
        });
        done.await();
        System.out.println("Done2");
    }

    void syncPush(Map<String, String> message) throws InterruptedException {
        final CountDownLatch done = new CountDownLatch(1);
        fb.push().setValue(message, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                done.countDown();
            }
        });
        done.await();
    }

    public long getServerOffsetMillis() throws InterruptedException {
        Date d = new Date();
        // return (Integer)syncReadMessage(".info/serverTimeOffset");
        return d.getTime();
    }
}
