package com.colibri.social_story.entities;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class User {

    private String userName;

    // TODO fix memory leak
    private static Map<String, User> userMap = new HashMap<>();

    public static User newInstance(String username) {
        if (userMap.containsKey(username))
            return userMap.get(username);
        return new User(username);
    }

    private User(String userName) {
        this.userName = userName;
    }
}
