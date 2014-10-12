package com.colibri.social_story.entities;

import lombok.Data;
import lombok.NonNull;

@Data
@SuppressWarnings("PMD.UnusedPrivateField")
public class ScoredWord implements  Comparable<ScoredWord> {

    @NonNull
    private User user;
    @NonNull
    private String word;
    @NonNull
    private int score;

    public void incrementScore() {
        score++;
    }

    @Override
    public int compareTo(ScoredWord o) {
        return Integer.compare(this.score, o.score);
    }
}
