package com.colibri.social_story;

import com.colibri.social_story.entities.*;
import com.colibri.social_story.transport.StoryBase;
import com.colibri.social_story.transport.StoryBaseCallback;
import com.colibri.social_story.transport.UserID;
import com.colibri.social_story.transport.UserStore;
import com.colibri.social_story.utils.Pair;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.mongodb.morphia.annotations.Transient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
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
    @Getter(AccessLevel.NONE)
    transient private final StoryBase sb;
    private int minUsers;
    private Phase phase;
    private long timeStarted;
    private long phaseStarted;
    private String story = "My big story";

    private final Map<String, User> users = new HashMap<>();

    @Getter(AccessLevel.NONE)
    final private List<Suggestions> suggestions = new Vector<>();
    final private List<Votes> votes = new Vector<>();

    private Suggestions roundSuggestions;
    @Getter(AccessLevel.NONE) @Setter(AccessLevel.NONE)
    private Votes roundVotes;
    @Getter(AccessLevel.NONE)
    private UserStore userStore;

    public Story(int minUsers, StoryBase sb,
                 int suggestTime, int voteTime,
                 int nRounds, String title, UserStore userStore) {
        this.minUsers = minUsers;
        this.sb = sb;
        this.suggestTime = suggestTime;
        this.voteTime = voteTime;
        this.nRounds = nRounds;
        this.title = title;
        this.userStore = userStore;
    }

    public boolean connect() throws InterruptedException {
        final CountDownLatch done = new CountDownLatch(this.minUsers);
        sb.onUserAdded(new StoryBaseCallback<String>() {
                           @Override
                           public void handle(String username) {
                               // TODO do something user
                               User u = userStore.getUserByID(new UserID(username));
                               users.put(username, u);
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
                roundSuggestions.addSuggestion(users.get(s.fst), s.snd);
            }
        });

        sb.onVotesAdded(new StoryBaseCallback<Pair<String, String>>() {
            @Override
            public void handle(Pair<String, String> vote) {
                log.info("Add " + vote + " to " + Story.this);
                roundVotes.voteForWord(vote.snd, users.get(vote.fst));
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
            sb.saveStory(this);
            Thread.sleep(suggestTime);
            suggestionEnd();
            phase = Phase.VOTE;
            phaseStarted = sb.getServerOffsetMillis();
            sb.saveStory(this);
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
        //roundVotes.addWord(new User(""), "End");
        log.info("Round votes: " + roundVotes);
        suggestions.add(roundSuggestions);
        roundSuggestions = new Suggestions();
    }

    private boolean voteEnd() throws InterruptedException {
        log.info("Vote end");
        ScoredWord sw = roundVotes.pickWinner();
        if (sw == null)
            return false;
        log.info("Adding word to story " + sw);
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

    public Suggestions getSuggestions() {
        return roundSuggestions;
    }

    public Map<String, Object> getWords() {
        Map<String, Object> m = new HashMap<>();
        if (roundVotes != null) {
            for (ScoredWord sw : roundVotes.getWords()) {
                m.put(sw.getWord(), sw.getUser());
            }
        }
        return m;
    }

    public List<Suggestions> suggestionsHistory() {
        return this.suggestions;
    }

    public List<Votes> votesHistory() {
        return this.votes;
    }
}




