package com.colibri.social_story.entities;

import lombok.Data;

@Data
public class ScoredWord {
    private String word;
    private User user;
    private int score;

    public void incrementScore() {
        score++;
    }

    public User getUser() {
        return user;
    }

    public String getWord() {
        return word;
    }
}
