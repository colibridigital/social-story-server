package com.colibri.social_story;

import com.colibri.social_story.persistence.StoryDAO;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;

import java.util.Map;
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
        fb.addChildEventListener(new StoryCreationController());
    }

    public void run() throws InterruptedException {
        connect();
        while (!es.isShutdown()) {
            Thread.sleep(5 * 1000);
        }
    }

    public void stop() {
        System.out.println("Shutting down app");
        es.shutdown();
    }

    /** Submits stories to the executor service. */
    private class StoryCreationController extends FirebaseChildEventListenerAdapter {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            // TODO read values from snapshot
            String storyId = dataSnapshot.getName();
            Map<String, String> attributes = getAttributesMap(dataSnapshot);
            String title = attributes.get("title");
            final Story newStory = new Story(
                    3,
                    new FirebaseStoryBase(new Firebase(FB_URL + storyId)),
                    suggestTime,
                    voteTime,
                    nRounds,
                    title
            );

            es.submit(new StoryRunner(newStory));
        }

        private Map<String, String> getAttributesMap(DataSnapshot dataSnapshot) {
            // XXX assumes there is exactly one child
            DataSnapshot attrDs = dataSnapshot.getChildren().iterator().next();
            return (Map <String, String>)attrDs.getValue();
        }
    }

    /** Runs a story. **/
    private class StoryRunner implements Runnable {
        private final Story newStory;

        public StoryRunner(Story newStory) {
            this.newStory = newStory;
        }

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
    }
}


