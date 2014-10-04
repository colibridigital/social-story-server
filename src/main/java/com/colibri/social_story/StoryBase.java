package com.colibri.social_story;

import com.firebase.client.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;

public class StoryBase {
    private final Firebase fb;
    final ConcurrentLinkedQueue<DataSnapshot> suggestions = new ConcurrentLinkedQueue<>();
    private Long timeStarted;

    public StoryBase(Firebase fb) {
        this.fb = fb;
    }

    public void syncClear(String path) throws InterruptedException {
        final CountDownLatch done = new CountDownLatch(1);
        fb.child(path).removeValue(new Firebase.CompletionListener() {
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

    void addSuggestionListener(String path)
            throws InterruptedException {
        fb.child(path).addChildEventListener(new FirebaseChildEventListenerAdapter() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                suggestions.add(dataSnapshot);
            }
        });
    }

    void syncSet(String path, Map<String, Object> message) throws InterruptedException {
        final CountDownLatch done = new CountDownLatch(1);
        fb.child(path).setValue(message, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                done.countDown();
            }
        });
        done.await();
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

    public ConcurrentLinkedQueue<DataSnapshot> getSuggestions() {
        return suggestions;
    }

    public void clearSuggestions() throws InterruptedException {
        suggestions.clear();
        syncClear("suggestions");
    }

    public void setVotePhase() throws InterruptedException {
        setPhase("suggest");
    }

    public void setSuggestPhase() throws InterruptedException {
        setPhase("vote");
    }

    private void setPhase(String phase) throws InterruptedException {
        Map<String, Object> mp = new HashMap<>();
        mp.put("started", "true");
        mp.put("phase", phase);
        mp.put("time_started",
                timeStarted == null ? getServerOffsetMillis() : timeStarted);
        syncSet("attributes", mp);
    }
}