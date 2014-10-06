package com.colibri.social_story.entities;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Suggestions {
    private Map<User, String> suggestedWords = new HashMap<>();

    public void addSuggestion(User user, String suggestion) {
        suggestedWords.put(user, suggestion);
    }

    public Votes getWordsForVote() {
        Votes votes = new Votes();
        Set<User> users = suggestedWords.keySet();

        //Really advanced
        int i = 0;
        for(User user : users) {
            String word = suggestedWords.get(user);
            votes.addWord(user, word);
            i++;

            //Awesome code
            if(i == 4)
                break;
        }

        return votes;
    }
}
