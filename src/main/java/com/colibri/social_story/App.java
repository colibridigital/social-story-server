package com.colibri.social_story;

import com.colibri.social_story.entities.*;
import com.colibri.social_story.persistence.MongoPersister;
import com.colibri.social_story.persistence.StoryPersister;
import com.colibri.social_story.transport.*;
import com.colibri.social_story.transport.FBRankingsPersister;
import com.colibri.social_story.transport.FBUserPersister;
import com.colibri.social_story.transport.RankingsPersister;
import com.colibri.social_story.transport.UserStore;
import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Logger;

public class App {

    private static final Logger log = Logger.getLogger(App.class.getName());

    public static final String FB_ROOT_URL = "https://sizzling-torch-6706.firebaseio.com/social-story/";
    public static final String FB_URL = FB_ROOT_URL + "/live-stories/";

    private final Vector<User> allUsers = new Vector<>();
    private final UserStore userStore = new FBUserPersister(new Firebase(FB_ROOT_URL));

    private static final int DEFAULT_SUGGEST_TIME = 30 * 1000;
    private static final int DEFAULT_VOTE_TIME = 30 * 1000;
    private static final int N_ROUNDS = 10;
    private int nRounds = N_ROUNDS;
    private int voteTime = DEFAULT_VOTE_TIME;
    private int suggestTime = DEFAULT_SUGGEST_TIME;

    private final ExecutorService es = Executors.newFixedThreadPool(2);

    private StoryPersister persister = new MongoPersister();

    private Ranking ranking;

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
        log.info("Connecting to firebase");
        Firebase fb = new Firebase(FB_URL);
        //syncAuth(fb);
        fb.addChildEventListener(new StoryCreationController());
    }

    public void run() throws InterruptedException {
        log.info("Getting user info...");
        userStore.syncLoadUsers(allUsers);
        log.info("Connecting to live stories...");
        connect();
        while (!es.isShutdown()) {
            Thread.sleep(5 * 1000);
        }
    }

    public void stop() {
        log.info("Shutting down app");
        es.shutdown();
    }

    private void syncAuth(Firebase ref) {
        if (ref.getAuth() == null) {
            log.info("Authenticating server..");
            final CountDownLatch cd = new CountDownLatch(1);
            ref.authWithPassword(
                    "megatron1@gmail.com", "secret",
                    new Firebase.AuthResultHandler() {
                        @Override
                        public void onAuthenticated(AuthData authData) {
                            log.info("Server authentication successful " + authData);
                            cd.countDown();
                        }

                        @Override
                        public void onAuthenticationError(FirebaseError firebaseError) {
                            log.severe("Server authentication failed");
                            cd.countDown();
                        }
                    });
            try {
                cd.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /** Submits stories to the executor service. */
    private class StoryCreationController extends FirebaseChildEventListenerAdapter {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            // TODO read values from snapshot
            String storyId = dataSnapshot.getName();
            final Map<String, Object> attributes = getAttributesMap(dataSnapshot);
            final Superbase ref = new Superbase(FB_URL + storyId);
            Object title = attributes.get("title");
            int minUsers = (int) (long) attributes.get("minUsers");
            final Story newStory = new Story(
                    minUsers,
                    new FirebaseStoryBase(ref),
                    suggestTime,
                    voteTime,
                    nRounds,
                    title.toString(),
                    userStore
            );
            es.submit(new StoryRunner(newStory));
        }


        private Map<String, Object> getAttributesMap(DataSnapshot dataSnapshot) {
            // XXX assumes there is exactly one child
            return (Map <String, Object>)dataSnapshot.getValue();
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
            log.info("Starting new story " + newStory);
            try {
                newStory.connect();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            postProcessStory(newStory);
            persister.save(newStory);
        }

        private void postProcessStory(Story newStory) {
            List<User> updatedUsers = updateUserScores(newStory);
            persistUsers(updatedUsers);
            Ranking newRanking = updateRankings();
            persistRanking(newRanking);
        }

        private List<User> updateUserScores(Story story) {
            log.info("Updating user score");
            List<User> users = new ArrayList<>();
            for (Votes votes : story.votesHistory()) {
                users.addAll(votes.rewardUsers());
            }
            return users;
        }

        private void persistUsers(List<User> users) {
            log.info("Updating user info");
            // TODO make this single instance (needs synchronization)
            com.colibri.social_story.transport.UserStore up = new FBUserPersister(new Firebase(FB_ROOT_URL));
            for (User u : users)
                up.persistUser(u);
        }

        private Ranking updateRankings() {
            synchronized(App.this) {
                Collections.sort(allUsers, new Comparator<User>() {
                    @Override
                    public int compare(User o1, User o2) {
                        return o2.getScore() - o1.getScore();
                    }
                });
            }
            ranking = new Ranking(allUsers.subList(0, Math.min(9, allUsers.size())));
            return ranking;
        }

        private void persistRanking(Ranking ranking) {
            RankingsPersister rp = new FBRankingsPersister(new Superbase(FB_ROOT_URL));
            rp.saveRanking(ranking);
        }
    }
}