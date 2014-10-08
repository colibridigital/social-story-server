package com.colibri.social_story;

import com.colibri.social_story.entities.*;
import com.colibri.social_story.utils.Pair;
import com.firebase.client.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class FirebaseStoryBase implements StoryBase {

    private final Firebase fb;

    public FirebaseStoryBase(Firebase fb) {
        this.fb = fb;
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
                storyBaseCallback.handle(dataSnapshot.getName());
            }});
    }

    @Override
    public void onWordAdded(final StoryBaseCallback storyBaseCallback) {
        fb.child("suggestions").addChildEventListener(new FirebaseChildEventListenerAdapter() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                storyBaseCallback.handle(
                        new Pair<>(dataSnapshot.getName(),
                                   dataSnapshot.getValue()));
            }
        });
    }

    @Override
    public void onVotesAdded(final StoryBaseCallback storyBaseCallback) {
        fb.child("votes").addChildEventListener(new FirebaseChildEventListenerAdapter() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                storyBaseCallback.handle(
                        new Pair<>(dataSnapshot.getName(),
                                   dataSnapshot.getValue()));
            }
        });
    }

    @Override
    public void writeVotes(Votes v) throws InterruptedException {
        Map<String, Object> m = new HashMap<>();
        for (ScoredWord sw : v.getWords()) {
            m.put(sw.getWord(), sw.getUser());
        }
        syncSet("words", m);
    }

    @Override
    public long getServerOffsetMillis() throws InterruptedException {
        Date d = new Date();
        // return (Integer)syncReadMessage(".info/serverTimeOffset");
        return d.getTime();
    }

    public void writeStoryAttributes(Story story) {
        Map<String, Object> mp = new HashMap<>();
        mp.put("started", story.getTimeStarted());
        mp.put("phase", story.getPhase());
        mp.put("time_story_started", story.getTimeStarted());
        mp.put("time_phase_started", story.getPhaseStarted());
        mp.put("story", story.getStory());
        mp.put("title", story.getTitle());
        try {
            syncSet("attributes", mp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeStory() {
        try {
            syncClear("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void syncClear(String path) throws InterruptedException {
        final CountDownLatch done = new CountDownLatch(1);
        fb.child(path).removeValue(new ReleaseLatchCompletionListener(done));
        done.await();
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