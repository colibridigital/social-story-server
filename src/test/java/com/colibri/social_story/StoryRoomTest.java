package com.colibri.social_story;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Query;
import junit.framework.TestCase;

import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

public class StoryRoomTest extends TestCase {

    public void testConnect() throws Exception {
        StoryRoom sr = new StoryRoom(0, new MockStoryBase(), 0, 0, 2);
        sr.connect();
    }
}

class MockStoryBase implements StoryBase {

    @Override
    public void syncClear(String path) throws InterruptedException {

    }

    @Override
    public long getServerOffsetMillis() throws InterruptedException {
        return 0;
    }

    @Override
    public void clearSuggestions() throws InterruptedException {

    }

    @Override
    public void clearVotes() throws InterruptedException {

    }

    @Override
    public void setVotePhase() throws InterruptedException {

    }

    @Override
    public void setSuggestPhase() throws InterruptedException {

    }

    @Override
    public ConcurrentLinkedQueue<DataSnapshot> getVotes() {
        return new ConcurrentLinkedQueue<>();
    }

    @Override
    public ConcurrentLinkedQueue<DataSnapshot> getSuggestions() {
        return new ConcurrentLinkedQueue<>();
    }

    @Override
    public void syncSet(String words, Map<String, Object> m) throws InterruptedException {

    }

    @Override
    public void onUserAdded(StoryBaseCallback storyBaseCallback) {

    }
}