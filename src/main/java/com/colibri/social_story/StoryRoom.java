package com.colibri.social_story;

import com.colibri.social_story.entities.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;

public class StoryRoom {

    private final String title = "My title";
    private final int voteTime;
    private final int suggestTime;
    private final int nRounds;
    private String story = "My big story";
    private final StoryBase sb;
    private int minUsers;
    private Phase phase;
    private long timeStarted;
    private long phaseStarted;

    private Suggestions roundSuggestions;

    private List<User> users = new ArrayList<>();
    final ConcurrentLinkedQueue<Suggestions> suggestions = new ConcurrentLinkedQueue<>();
    final ConcurrentLinkedQueue<Votes> votes = new ConcurrentLinkedQueue<>();
    private Votes roundVotes;

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
                               users.add(u);
                               System.out.println("Added users");
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
                System.out.println("Add suggestion");
                roundSuggestions.addSuggestion(s.getUser(), s.getWord());
            }
        });

        sb.onVotesAdded( new StoryBaseCallback<Vote>() {
            @Override
            public void handle(Vote vote) {
                System.out.println("Add vote");
                roundVotes.voteForWord(vote.getWord(), vote.getUser());
            }
        });
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
            sb.removeStory();
        }

        end();
    }

    private void suggestionEnd() throws InterruptedException {
        System.out.println("Suggestion end");
        roundVotes = roundSuggestions.getWordsForVote();
        sb.writeVotes(roundVotes);
        suggestions.add(roundSuggestions);
        roundSuggestions = new Suggestions();
    }

    private boolean voteEnd() throws InterruptedException {
        System.out.println("Vote end");
        ScoredWord sw = roundVotes.pickWinner();
        if (sw == null)
            return false;
        story = story + sw.getWord();
        votes.add(roundVotes);
        roundVotes = new Votes();
        return "End".equals(sw.getWord());
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

    public String getStory() {
        return story;
    }

    public String getTitle() {
        return title;
    }

    public enum Phase {
        VOTE, SUGGEST
    }
}




