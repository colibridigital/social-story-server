package com.colibri.social_story.entities;

public class Vote {
    private String word;
    private User user;

    public Vote(User user, String word) {
        this.word = word;
    }

    public String getWord() {
        return word;
    }

    public User getUser() {
        return user;
    }
}
