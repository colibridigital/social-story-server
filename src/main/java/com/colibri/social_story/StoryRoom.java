package com.colibri.social_story;

import com.colibri.social_story.entities.User;
import com.colibri.social_story.entities.Votes;
import com.firebase.client.DataSnapshot;

import java.util.*;
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
        start();
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

        Map<String, Object> m = new HashMap<>();
        for (DataSnapshot ds : sb.getSuggestions()) {
            System.out.println(ds.getValue() + " " + ds.getName());
            m.put((String)ds.getValue(), ds.getName());
        }
        sb.syncSet("words", m);
        sb.clearSuggestions();
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
        return (Phase) phase;
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




