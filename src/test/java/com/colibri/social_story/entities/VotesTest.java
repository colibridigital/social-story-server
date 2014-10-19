package com.colibri.social_story.entities;

import junit.framework.TestCase;
import org.junit.Before;

import java.util.ArrayList;
import java.util.List;

public class VotesTest extends TestCase {
    private User john = new User("John");
    private User tim = new User("Tim");
    private Votes votes;

    public void setUp() {
        Suggestions s = new Suggestions();
        s.addSuggestion(john, "word1");
        s.addSuggestion(tim, "word2");
        votes = s.getWordsForVote();
    }

    public void testPickWinner() {
        votes.voteForWord("word2", john);
        votes.voteForWord("word1", tim);
        votes.voteForWord("word1", new User("Bob"));
        ScoredWord sw = votes.pickWinner();
        assertEquals(sw.getWord(), "word1");
    }

    public void testRewardUsers() {
        votes.voteForWord("word2", john);
        votes.voteForWord("word1", tim);
        votes.voteForWord("word1", new User("Bob"));
        votes.pickWinner();

        List<User> expectedRewarded = new ArrayList<>();
        expectedRewarded.add(john);

        List<User> rewardedUsers = votes.rewardUsers();
        assertEquals(expectedRewarded, rewardedUsers);
    }
}