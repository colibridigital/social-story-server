package com.colibri.social_story.entities;

import java.util.*;

public class Votes {
    private static final int winnerReward = 2;

    //Word: Username (who suggested the word): Num Votes
    private Set<ScoredWord> words = new HashSet<>();

    private ScoredWord winner;

    public void addWord(User user, String word) {
        this.addWord(new ScoredWord(user, word, 0));
    }

    public void addWord(ScoredWord scoredWord) {
        words.add(scoredWord);
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
        winner = words.size() > 0 ? Collections.max(words) : null;
        return winner;
    }

    public Set<ScoredWord> getWords() {
        return new HashSet<>(words);
    }

    public ScoredWord getWinner() {
        return winner;
    }

    public List<User> rewardUsers() {
        List<User> rewardedUsers = new ArrayList<>();

        // winner gets rewarded
        User user = winner.getUser();
        user.addScore(winnerReward);
        rewardedUsers.add(user);

        return rewardedUsers;
    }
}
