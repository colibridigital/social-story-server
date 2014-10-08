package com.colibri.social_story;

import com.colibri.social_story.entities.*;
import com.firebase.client.DataSnapshot;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;

public class StoryRoom {

    private final int voteTime;
    private final int suggestTime;
    private final int nRounds;
    private String story = "My big story";
    private final StoryBase sb;
    private int minUsers;
    private int users = 0;
    private Phase phase;
    private long timeStarted;
    private long timePhaseStarted;
    private long phaseStarted;

    private Suggestions roundSuggestions;

    final ConcurrentLinkedQueue<Suggestions> suggestions = new ConcurrentLinkedQueue<>();
    final ConcurrentLinkedQueue<Votes> votes = new ConcurrentLinkedQueue<>();

    public StoryRoom(int minUsers, StoryBase sb,
                     int suggestTime, int voteTime,
                     int nRounds) {
        this.minUsers = minUsers;
        this.sb = sb;
        this.suggestTime = suggestTime;
        this.voteTime = voteTime;
        this.nRounds = nRounds;
    }

    void connect() throws InterruptedException {
        final CountDownLatch done = new CountDownLatch(this.minUsers);
        sb.onUserAdded(new StoryBaseCallback<User>() {
                           @Override
                           public void handle(User u) {
                               // TODO do something user
                               done.countDown();
                           }
                       }
        );
        done.await();

        // setup callback
        roundSuggestions = new Suggestions();
        sb.onWordAdded(new StoryBaseCallback<Suggestion>() {
            @Override
            public void handle(Suggestion s) {
                roundSuggestions.addSuggestion(s.getUser(), s.getValue());
                StoryRoom.this.addSuggestion(s);
            }
        });
        start();
    }

    private void addSuggestion(Suggestion s) {
        Suggestions ss = suggestions.peek();
        if (ss == null) {
            ss = new Suggestions();
            suggestions.add(ss);
        }
        ss.addSuggestion(s.getUser(), s.getValue());
    }

    private void start() throws InterruptedException {

        timeStarted = sb.getServerOffsetMillis();
        int r = 0;
        boolean finish = false;
        while (!finish && r < nRounds) {
            phase = Phase.SUGGEST;
            phaseStarted = sb.getServerOffsetMillis();
            sb.writeStoryAttributes(this);
            Thread.sleep(suggestTime);
            suggestionEnd();
            phase = Phase.VOTE;
            phaseStarted = sb.getServerOffsetMillis();
            sb.writeStoryAttributes(this);
            Thread.sleep(voteTime);
            finish = voteEnd();
            r++;
        }

        end();
    }

    private void suggestionEnd() throws InterruptedException {
        System.out.println("Suggestion end");
        sb.writeVotes(roundSuggestions.getWordsForVote());
        suggestions.add(roundSuggestions);
        roundSuggestions = new Suggestions();
    }

    private boolean voteEnd() throws InterruptedException {
        Map<String, Object> m = new HashMap<>();
        DataSnapshot ds = sb.getVotes().peek();
        if (ds != null) {
            story = story + ds.getValue();
            m.put("story", story);
            sb.syncSet("", m);
            return "End".equals(ds.getValue());
        }
        sb.clearVotes();
        return false;
    }

    private void end() {
        System.out.println("Story end");
    }

    public long getStarted() {
        return 0;
    }

    public Phase getPhase() {
        return phase;
    }

    public long getTimeStarted() {
        return timeStarted;
    }

    public long getPhaseStarted() {
        return phaseStarted;
    }

    public enum Phase {
        VOTE, SUGGEST
    }
}




