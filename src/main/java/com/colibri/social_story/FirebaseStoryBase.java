package com.colibri.social_story;

import com.colibri.social_story.entities.User;
import com.firebase.client.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;

public class FirebaseStoryBase implements StoryBase {

    private final Firebase fb;
    final ConcurrentLinkedQueue<DataSnapshot> suggestions = new ConcurrentLinkedQueue<>();
    final ConcurrentLinkedQueue<DataSnapshot> votes = new ConcurrentLinkedQueue<>();
    private Long timeStarted = null;

    public FirebaseStoryBase(Firebase fb) {
        this.fb = fb;

        // add the listeners
        fb.child("votes").addChildEventListener(new FirebaseChildEventListenerAdapter() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                votes.add(dataSnapshot);
            }
        });

        fb.child("suggestions").addChildEventListener(new FirebaseChildEventListenerAdapter() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                suggestions.add(dataSnapshot);
            }
        });
    }

    public void syncClear(String path) throws InterruptedException {
        final CountDownLatch done = new CountDownLatch(1);
        fb.child(path).removeValue(new ReleaseLatchCompletionListener(done));
        done.await();
    }

    public void syncSet(String path, Map<String, Object> message) throws InterruptedException {
        final CountDownLatch done = new CountDownLatch(1);
        fb.child(path).setValue(message, new ReleaseLatchCompletionListener(done));
        done.await();
    }

    @Override
    public void onUserAdded(final StoryBaseCallback storyBaseCallback) {
        fb.child("users").addChildEventListener(new FirebaseChildEventListenerAdapter() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                storyBaseCallback.handle(new User(dataSnapshot.getName()));
            }});
    }

    void syncPush(Map<String, String> message) throws InterruptedException {
        final CountDownLatch done = new CountDownLatch(1);
        fb.push().setValue(message, new ReleaseLatchCompletionListener(done));
        done.await();
    }

    @Override
    public long getServerOffsetMillis() throws InterruptedException {
        Date d = new Date();
        // return (Integer)syncReadMessage(".info/serverTimeOffset");
        return d.getTime();
    }

    @Override
    public ConcurrentLinkedQueue<DataSnapshot> getSuggestions() {
        return suggestions;
    }

    @Override
    public void clearSuggestions() throws InterruptedException {
        suggestions.clear();
        syncClear("suggestions");
    }

    @Override
    public void clearVotes() throws InterruptedException {
        votes.clear();
        syncClear("votes");
    }

    public void writeStoryAttributes(StoryRoom story) {
        Map<String, Object> mp = new HashMap<>();
        mp.put("started", story.getStarted());
        mp.put("phase", story.getPhase());
        mp.put("time_story_started", story.getTimeStarted());
        mp.put("time_phase_started", story.getPhaseStarted());
        try {
            syncSet("attributes", mp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ConcurrentLinkedQueue<DataSnapshot> getVotes() {
        return votes;
    }

    private static class ReleaseLatchCompletionListener implements Firebase.CompletionListener {
        private final CountDownLatch done;

        public ReleaseLatchCompletionListener(CountDownLatch done) {
            this.done = done;
        }

        @Override
        public void onComplete(FirebaseError firebaseError, Firebase firebase) {
            done.countDown();
        }
    }


}