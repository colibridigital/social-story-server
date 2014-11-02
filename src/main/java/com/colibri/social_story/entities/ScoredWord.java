package com.colibri.social_story.entities;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;

@Data
@SuppressWarnings("PMD.UnusedPrivateField")
public class ScoredWord implements  Comparable<ScoredWord> {

    @NonNull
    private User user;

    @NonNull
    private String word;
    @Getter(AccessLevel.NONE)
    private boolean endStory;
    @NonNull
    private int score;
    public ScoredWord(User user, String word, int score) {
        this.user = user;
        this.word = word;
        this.score = score;
        this.endStory = false;
    }

    public void incrementScore() {
        score++;
    }

    @Override
    public int compareTo(ScoredWord o) {
        return Integer.compare(this.score, o.score);
    }

    public User getUser() {
        return user;
    }

    public static ScoredWord endStoryWord() {
        ScoredWord endWord = new ScoredWord(new User(""), "End Story", 0);
        endWord.endStory = true;
        return endWord;
    }
}
