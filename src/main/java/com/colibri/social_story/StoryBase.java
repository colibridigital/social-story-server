package com.colibri.social_story;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Query;

import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

public interface StoryBase {
    void syncClear(String path) throws InterruptedException;

    long getServerOffsetMillis() throws InterruptedException;

    void clearSuggestions() throws InterruptedException;

    void clearVotes() throws InterruptedException;

    void setVotePhase() throws InterruptedException;

    void setSuggestPhase() throws InterruptedException;

    ConcurrentLinkedQueue<DataSnapshot> getVotes();

    ConcurrentLinkedQueue<DataSnapshot> getSuggestions();

    void addSuggestionListener(String suggestions) throws InterruptedException;

    void addVoteListener(String votes) throws InterruptedException;

    Query child(String users);

    public void syncSet(String words, Map<String, Object> m) throws InterruptedException;
}
