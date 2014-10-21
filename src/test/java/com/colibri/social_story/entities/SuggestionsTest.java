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

    public void testRemovesDuplicates() {
        Suggestions s = new Suggestions();
        s.addSuggestion(new User("John"), "word1");
        s.addSuggestion(new User("Bob"), "word1");
        s.addSuggestion(new User("Tim"), "word2");
        Votes v = s.getWordsForVote();
        List<String> exp = Arrays.asList("word1", "word2");
        List<String> words = v.getWords().stream().map(ScoredWord::getWord).collect(toList());
        Assert.assertThat(exp, containsInAnyOrder(words.toArray()));
    }

}