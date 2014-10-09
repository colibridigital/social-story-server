package com.colibri.social_story;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;

import java.util.concurrent.*;

public class App {

    public static final String FB_URL = "https://colibristory.firebaseio.com/social-story/live-stories/";

    private static final int DEFAULT_SUGGEST_TIME = 5 * 1000;
    private static final int DEFAULT_VOTE_TIME = 5 * 1000;
    private static final int N_ROUNDS = 10;
    private int nRounds = N_ROUNDS;
    private int voteTime = DEFAULT_VOTE_TIME;
    private int suggestTime = DEFAULT_SUGGEST_TIME;

    private final BlockingQueue<Story> storyCreationQueue = new LinkedBlockingQueue<>();
    private final ExecutorService es = Executors.newFixedThreadPool(2);

    private StoryPersister persister = new MongoPersister();
    private boolean stop = false;
    private int storyId = 1;

    public App(int suggestTime, int voteTime, int nRounds, StoryPersister storyPersister) {
        this.suggestTime = suggestTime;
        this.voteTime = voteTime;
        this.nRounds = nRounds;
        this.persister = storyPersister;
    }

    public static void main(String[] args) throws InterruptedException {
        (new App(DEFAULT_SUGGEST_TIME, DEFAULT_VOTE_TIME, N_ROUNDS, new MongoPersister())).run();
    }

    private void connect() {
        Firebase fb = new Firebase(FB_URL);
        fb.addChildEventListener(new FirebaseChildEventListenerAdapter() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                // TODO read values from snapshot
                System.out.println("Story added" + dataSnapshot.getName() + " " + dataSnapshot.getValue());
                String storyId = dataSnapshot.getName();
                storyCreationQueue.add(new Story(
                        3,
                        new FirebaseStoryBase(new Firebase(FB_URL + storyId)),
                        suggestTime,
                        voteTime,
                        nRounds
                ));
            }
        });
    }

    private String freshStoryId() {
        return Integer.toString(storyId++);
    }

    public void run() throws InterruptedException {
        connect();
        while (!stop) {
            final Story newStory = storyCreationQueue.poll(1, TimeUnit.SECONDS);
            if (newStory == null)
                continue;
            es.submit(new Runnable() {
                @Override
                public void run() {
                    System.out.println("Starting story.");
                    try {
                        newStory.connect();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    persister.save(newStory);
                }
            });
        }
    }

    public void stop() {
        System.out.println("Shutting down app");
        stop = true;
        es.shutdown();
    }
}


