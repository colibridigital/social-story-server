package com.colibri.social_story.entities;

import junit.framework.TestCase;
import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Assert;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;

public class SuggestionsTest extends TestCase {

    public static final String WORD2 = "word2";
    public static final String WORD1 = "word1";

    public void testRemovesDuplicates() {
        Suggestions s = new Suggestions();
        s.addSuggestion(new User("John"), WORD1);
        s.addSuggestion(new User("Bob"), WORD1);
        s.addSuggestion(new User("Tim"), WORD2);
        Votes v = s.getWordsForVote();
        List<String> exp = Arrays.asList(WORD1, WORD2);
        List<String> words = v.getWords().stream().map(ScoredWord::getWord).collect(toList());
        Assert.assertThat(exp, containsInAnyOrder(words.toArray()));
    }

}
