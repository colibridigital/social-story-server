package com.colibri.social_story;

import com.colibri.social_story.entities.Votes;
import junit.framework.TestCase;

public class StoryRoomTest extends TestCase {

    public void testConnect() throws Exception {
        StoryRoom sr = new StoryRoom(0, new MockStoryBase(), 0, 0, 2);
        sr.connect();
    }
}

class MockStoryBase implements StoryBase {

    @Override
    public long getServerOffsetMillis() throws InterruptedException {
        return 0;
    }

    @Override
    public void writeStoryAttributes(StoryRoom story) {

    }

    @Override
    public void removeStory() {

    }

    @Override
    public void onUserAdded(StoryBaseCallback storyBaseCallback) {

    }

    @Override
    public void onWordAdded(StoryBaseCallback storyBaseCallback) {

    }

    @Override
    public void onVotesAdded(StoryBaseCallback storyBaseCallback) {

    }

    @Override
    public void writeVotes(Votes v) throws InterruptedException {

    }
}