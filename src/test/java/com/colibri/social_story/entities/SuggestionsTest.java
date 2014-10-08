package com.colibri.social_story.entities;

import junit.framework.TestCase;

import java.util.HashSet;
import java.util.Set;

public class SuggestionsTest extends TestCase {

    public void testRemovesDuplicates() {
        Suggestions s = new Suggestions();
        s.addSuggestion(new User("John"), "word1");
        s.addSuggestion(new User("Bob"), "word1");
        s.addSuggestion(new User("Tim"), "word2");
        Votes v = s.getWordsForVote();
        Set<ScoredWord> exp = new HashSet<>();
        exp.add(new ScoredWord(new User("John"), "word1", 0));
        exp.add(new ScoredWord(new User("Tim"), "word2", 0));
        assertEquals(exp, v.getWords());
    }

}