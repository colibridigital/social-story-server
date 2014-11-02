package com.colibri.social_story.entities;

import com.colibri.social_story.transport.UserID;
import lombok.Data;

@Data
@SuppressWarnings("PMD.UnusedPrivateField")
public class User {

    private UserID uid;
    private String username;
    private int score = 0;
    private String email;

    public User() {}
    public User(String userName) {
        this.username = userName;
    }
    public User(UserID uid, String userName, int score) {
        this.uid = uid;
        this.username = userName;
        this.score = score;
    }

    public synchronized void addScore(int extraScore) {
        score += extraScore;
    }
}
