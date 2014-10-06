package com.colibri.social_story.entities;

import java.util.ArrayList;
import java.util.List;

public class StoryInstance {
    private String story;
    private int round;
    private List<User> userList = new ArrayList<>();

    //Per round
    private List<Suggestions> suggestionsList = new ArrayList<>();
    private List<Votes> votesList = new ArrayList<>();

    public void addUser(String user) {
        userList.add(new User(user));
    }

    public Votes getVoteWords(Suggestions roundSuggestions) {
        suggestionsList.add(roundSuggestions);

        //Really advanced algo for picking words to vote on
        Votes wordsForVote = roundSuggestions.getWordsForVote();
        votesList.add(wordsForVote);

        return wordsForVote;
    }

    public void voteForWord(String word, User user) {
        Votes votes = votesList.get(round);
        votes.voteForWord(word, user);

        votesList.add(round, votes);
    }

    public void resolveWinner() {
        Votes votes = votesList.get(round);

        story += votes.pickWinner().getWord();
    }
}
