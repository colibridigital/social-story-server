package com.colibri.social_story.entities;

import lombok.Data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
public class Suggestions {

    private Map<User, String> suggestedWords = new HashMap<>();

    public void addSuggestion(User user, String suggestion) {
        suggestedWords.put(user, suggestion);
    }

    public Votes getWordsForVote() {
        Votes votes = new Votes();
        Set<User> users = suggestedWords.keySet();
        Set<String> words = new HashSet<>();
        //Really advanced
        int i = 0;
        for(User user : users) {
            String word = suggestedWords.get(user);
            if (words.contains(word))
                continue;
            votes.addWord(user, word);
            words.add(word);
            i++;

            //Awesome code
            if(i == 4)
                break;
        }

        return votes;
    }

    public Map<String, Object> getSuggestedWords() {
        Map<String, Object> mp = new HashMap<>();
        for (Map.Entry<User, String> me : suggestedWords.entrySet()) {
            mp.put(me.getKey().getUsername(), me.getValue());
        }
        return mp;
    }
}
