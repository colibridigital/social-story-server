package com.colibri.social_story.entities;

import lombok.Data;
import lombok.NonNull;

@Data
@SuppressWarnings("PMD.UnusedPrivateField")
public class User {
    @NonNull
    private String userName;

    public User() {}

    public User(String userName) {this.userName = userName;}
}
