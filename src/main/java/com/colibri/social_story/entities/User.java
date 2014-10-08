package com.colibri.social_story.entities;

import lombok.Data;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;

@Data
public class User {

    @NonNull
    private String userName;

    // TODO fix memory leak
    private static Map<String, User> userMap = new HashMap<>();

    public static User newInstance(String username) {
        if (userMap.containsKey(username))
            return userMap.get(username);
        return new User(username);
    }
}
