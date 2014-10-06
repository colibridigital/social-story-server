package com.colibri.social_story.entities;

import java.util.ArrayList;
import java.util.List;

public class StoryInstance {
    private String story;
    private List<User> userList = new ArrayList<>();

    public void addUser(String user) {
        userList.add(new User(user));
    }
}
