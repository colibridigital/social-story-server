package com.colibri.social_story;

import com.firebase.client.Firebase;

public class App {

    public static final String FB_URL = "https://colibristory.firebaseio.com/social-story/1";

    private static final int DEFAULT_SUGGEST_TIME = 5 * 1000;
    private static final int DEFAULT_VOTE_TIME = 5 * 1000;
    private static final int N_ROUNDS = 10;
    private int nRounds = N_ROUNDS;
    private int voteTime = DEFAULT_VOTE_TIME;
    private int suggestTime = DEFAULT_SUGGEST_TIME;

    private StoryPersister persister = new MongoPersister();

    public App(int suggestTime, int voteTime, int nRounds, StoryPersister storyPersister) {
        this.suggestTime = suggestTime;
        this.voteTime = voteTime;
        this.nRounds = nRounds;
        this.persister = storyPersister;
    }

    public static void main(String[] args) throws InterruptedException {
        (new App(DEFAULT_SUGGEST_TIME, DEFAULT_VOTE_TIME, N_ROUNDS, new MongoPersister())).run();
    }

    public void run() throws InterruptedException {
        Story story = new Story(1,
                new FirebaseStoryBase(new Firebase(FB_URL)),
                suggestTime,
                voteTime,
                nRounds
                );

        story.connect();
        persister.save(story);
    }
}


