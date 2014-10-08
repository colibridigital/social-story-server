package com.colibri.social_story;

// TODO(paul-g): remove dependency on DS
import com.colibri.social_story.entities.Votes;
import com.firebase.client.DataSnapshot;

import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

public interface StoryBase {

    long getServerOffsetMillis() throws InterruptedException;

    void clearVotes() throws InterruptedException;

    ConcurrentLinkedQueue<DataSnapshot> getVotes();

    void writeStoryAttributes(StoryRoom story);

    void syncSet(String words, Map<String, Object> m) throws InterruptedException;

    void onUserAdded(final StoryBaseCallback storyBaseCallback);

    void onWordAdded(final StoryBaseCallback storyBaseCallback);

    void writeVotes(Votes v) throws InterruptedException;
}
