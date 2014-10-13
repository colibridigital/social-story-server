package com.colibri.social_story;

import com.colibri.social_story.entities.*;
import com.colibri.social_story.utils.Pair;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.mongodb.morphia.annotations.Transient;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

@Data
@SuppressWarnings("PMD.UnusedPrivateField")
public class Story extends AbstractStory {

    private static final Logger log = Logger.getLogger(Story.class.getName());

    private String title;
    private final int voteTime;
    private final int suggestTime;
    private final int nRounds;

    @Transient
    private final StoryBase sb;
    private int minUsers;
    private Phase phase;
    private long timeStarted;
    private long phaseStarted;
    private String story = "My big story";

    private static Map<String, User> userMap = new HashMap<>();

    final private ConcurrentLinkedQueue<Suggestions> suggestions = new ConcurrentLinkedQueue<>();
    final private ConcurrentLinkedQueue<Votes> votes = new ConcurrentLinkedQueue<>();

    @Getter(AccessLevel.NONE) @Setter(AccessLevel.NONE)
    private Suggestions roundSuggestions;
    @Getter(AccessLevel.NONE) @Setter(AccessLevel.NONE)
    private Votes roundVotes;

    public Story(int minUsers, StoryBase sb,
                 int suggestTime, int voteTime,
                 int nRounds, String title) {
        this.minUsers = minUsers;
        this.sb = sb;
        this.suggestTime = suggestTime;
        this.voteTime = voteTime;
        this.nRounds = nRounds;
        this.title = title;
    }

    public boolean connect() throws InterruptedException {
        final CountDownLatch done = new CountDownLatch(this.minUsers);
        sb.onUserAdded(new StoryBaseCallback<String>() {
                           @Override
                           public void handle(String username) {
                               // TODO do something user
                               User u = new User(username);
                               userMap.put(username, u);
                               log.info(u + " joined " + Story.this);
                               done.countDown();
                           }
                       }
        );
        done.await();

        // setup callback
        roundSuggestions = new Suggestions();
        sb.onWordAdded(new StoryBaseCallback<Pair<String, String>>() {
            @Override
            public void handle(Pair<String, String> s) {
                log.info("Add " + s + " to " + Story.this);
                roundSuggestions.addSuggestion(userMap.get(s.fst), s.snd);
            }
        });

        sb.onVotesAdded(new StoryBaseCallback<Pair<String, String>>() {
            @Override
            public void handle(Pair<String, String> vote) {
                log.info("Add " + vote + " to " + Story.this);
                roundVotes.voteForWord(vote.snd, userMap.get(vote.fst));
            }
        });
        start();

        return true;
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
            sb.removeStory();
            r++;
        }

        end();
    }

    private void suggestionEnd() throws InterruptedException {
        log.info("Suggestion end");
        roundVotes = roundSuggestions.getWordsForVote();
        roundVotes.addWord(null, "End");
        log.info("Round votes: " + roundVotes);
        sb.writeVotes(roundVotes);
        suggestions.add(roundSuggestions);
        roundSuggestions = new Suggestions();
    }

    private boolean voteEnd() throws InterruptedException {
        log.info("Vote end");
        ScoredWord sw = roundVotes.pickWinner();
        if (sw == null)
            return false;
        story = story + sw.getWord();
        votes.add(roundVotes);
        roundVotes = new Votes();
        return "End".equals(sw.getWord());
    }

    private void end() {
        log.info("Story end");
        sb.removeStory();
    }

    public enum Phase {
        VOTE, SUGGEST
    }
}




