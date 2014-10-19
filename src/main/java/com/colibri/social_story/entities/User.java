package com.colibri.social_story.entities;

import com.colibri.social_story.transport.UserID;
import lombok.Data;
import lombok.NonNull;

@Data
@SuppressWarnings("PMD.UnusedPrivateField")
public class User {

    private UserID uid;
    private String userName;
    private int score = 0;

    public User() {}
    public User(UserID uid, String userName, int score) {
        this.uid = uid;
        this.userName = userName;
        this.score = score;
    }

    public synchronized void addScore(int extraScore) {
        score += extraScore;
    }
}
