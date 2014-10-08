package com.colibri.social_story;

// TODO(paul-g): remove dependency on DS
import com.firebase.client.DataSnapshot;

import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

public interface StoryBase {

    long getServerOffsetMillis() throws InterruptedException;

    void clearSuggestions() throws InterruptedException;

    void clearVotes() throws InterruptedException;

    ConcurrentLinkedQueue<DataSnapshot> getVotes();

    ConcurrentLinkedQueue<DataSnapshot> getSuggestions();

    void writeStoryAttributes(StoryRoom story);

    void syncSet(String words, Map<String, Object> m) throws InterruptedException;

    void onUserAdded(final StoryBaseCallback storyBaseCallback);
}
