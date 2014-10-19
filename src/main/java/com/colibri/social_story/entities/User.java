package com.colibri.social_story.entities;

import lombok.Data;
import lombok.NonNull;

@Data
@SuppressWarnings("PMD.UnusedPrivateField")
public class User {
    @NonNull
    private String userName;
    private int score = 0;

    public User() {}

    public User(String userName) {this.userName = userName;}

    public synchronized void addScore(int extraScore) {
        score += extraScore;
    }
}
