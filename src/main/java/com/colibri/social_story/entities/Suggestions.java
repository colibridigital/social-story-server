package com.colibri.social_story.entities;

import java.util.HashMap;
import java.util.Map;

public class Suggestions {
    private Map<User, String> suggestedWords = new HashMap<>();

    public void addSuggestion(User user, String suggestion) {
        suggestedWords.put(user, suggestion);
    }
}
