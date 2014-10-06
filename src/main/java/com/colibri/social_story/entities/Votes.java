package com.colibri.social_story.entities;

import java.util.HashSet;
import java.util.Set;

public class Votes {
    //Word: Username (who suggested the word): Num Votes
    private Set<ScoredWord> words = new HashSet<>();

    public void addWord(User user, String word) {
        ScoredWord thisWord = new ScoredWord();
        thisWord.setUser(user);
        thisWord.setWord(word);
    }

    public void voteForWord(String word, User user) {
        synchronized (word) {
            //Maybe we want to change this to a map to make it faster, maybe we dont care
            for(ScoredWord thisWord : words) {
                if(thisWord.getWord().equals(word)) {
                    thisWord.incrementScore();
                }
            }
        }
    }

    public ScoredWord pickWinner() {
        int maxScore = 0;
        ScoredWord highScore = null;

        for(ScoredWord word : words) {
            if(word.getScore() >= maxScore) {
                highScore = word;
            }
        }

        return highScore;
    }
}
