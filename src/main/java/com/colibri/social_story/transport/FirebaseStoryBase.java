package com.colibri.social_story.transport;

import com.colibri.social_story.*;
import com.colibri.social_story.utils.Pair;
import com.firebase.client.DataSnapshot;

import java.util.Date;
import java.util.concurrent.CountDownLatch;

public class FirebaseStoryBase implements StoryBase {

    private final Superbase sb;

    public FirebaseStoryBase(Superbase sb) {
        this.sb = sb;
    }

    private void syncClear(String path) throws InterruptedException {
        final CountDownLatch done = new CountDownLatch(1);
        sb.child(path).removeValue(new ReleaseLatchCompletionListener(done));
        done.await();
    }

    private Pair<String, Object> syncGetLeafFromRoot(String path) throws InterruptedException {
        final CountDownLatch done = new CountDownLatch(1);
        final Pair<String, Object> value = new Pair<>();
        sb.getRoot().child(path).addValueEventListener( new FirebaseValueEventListenerAdapter() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                value.fst = dataSnapshot.getName();
                value.snd = dataSnapshot.getValue();
                done.countDown();
            }
        });
        done.await();
        return value;
    }

    @Override
    public void onUserAdded(final StoryBaseCallback storyBaseCallback) {
        sb.child("users").addChildEventListener(new FirebaseChildEventListenerAdapter() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                storyBaseCallback.handle(dataSnapshot.getName());
            }});
    }

    @Override
    public void onWordAdded(final StoryBaseCallback storyBaseCallback) {
        sb.child("suggestions").addChildEventListener(new FirebaseChildEventListenerAdapter() {
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
        sb.child("votes").addChildEventListener(new FirebaseChildEventListenerAdapter() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                storyBaseCallback.handle(
                        new Pair<>(dataSnapshot.getName(),
                                   dataSnapshot.getValue()));
            }
        });
    }

    @Override
    public void saveStory(Story story) {
        sb.syncWrite(null, story);
    }

    @Override
    public long getServerOffsetMillis() throws InterruptedException {
        Pair<String, Object> offset = syncGetLeafFromRoot(".info/serverTimeOffset");
        return new Date().getTime() + (Long)offset.snd;
    }

    @Override
    public void removeStory() {
        try {
            syncClear("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}