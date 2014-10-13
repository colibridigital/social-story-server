package com.colibri.social_story.entities;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Votes {
    //Word: Username (who suggested the word): Num Votes
    private Set<ScoredWord> words = new HashSet<>();

    public void addWord(User user, String word) {
        ScoredWord thisWord = new ScoredWord(user, word, 0);
        words.add(thisWord);
    }

    public void voteForWord(String word, User user) {
        synchronized (word) {
            System.out.println(user.getUserName() + " is voting for " + word);
            //Maybe we want to change this to a map to make it faster, maybe we dont care
            for(ScoredWord thisWord : words) {
                if(thisWord.getWord().equals(word)) {
                    thisWord.incrementScore();
                }
            }
        }

        System.out.println(words);
    }

    public ScoredWord pickWinner() {
        return words.size() > 0 ? Collections.max(words) : null;
    }

    public Set<ScoredWord> getWords() {
        return new HashSet<>(words);
    }
}
