package com.colibri.social_story.entities;

import junit.framework.TestCase;

public class VotesTest extends TestCase {

    public void testPickWinner() {
        Suggestions s = new Suggestions();
        s.addSuggestion(new User("John"), "word1");
        s.addSuggestion(new User("Tim"), "word2");
        Votes v = s.getWordsForVote();
        v.voteForWord("word2", new User("John"));
        v.voteForWord("word1", new User("Tim"));
        v.voteForWord("word1", new User("Bob"));
        ScoredWord sw = v.pickWinner();
        assertEquals(sw.getWord(), "word1");
    }
}