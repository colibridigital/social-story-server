package com.colibri.social_story;

import com.colibri.social_story.entities.User;
import com.colibri.social_story.persistence.MongoPersister;
import com.colibri.social_story.transport.FBUserPersister;
import com.colibri.social_story.transport.Superbase;
import com.colibri.social_story.transport.UserID;
import com.colibri.social_story.utils.Utils;
import junit.framework.TestCase;

import java.util.*;

import static com.colibri.social_story.TestUtils.setUpProducerTwoConsumers;

public class MongoIntegrationTest extends TestCase {

    public void testTwoStories() throws InterruptedException {
        Superbase fb = new Superbase(App.FB_URL);
        fb.syncClear("");
        FBUserPersister fbUserPersister = new FBUserPersister(fb);
        Map<String, User> users = Utils.mapFromKeys(
                "megatron1",
                new User(new UserID("megatron1"), "megatron1@gmail.com", 0));
        users.put("megatron2",
                new User(new UserID("megatron2"), "megatron2@gmail.com", 0));
        users.put("megatron3",
                new User(new UserID("megatron3"), "megatron3@gmail.com", 0));
        fbUserPersister.setUsers(users);
        setUpProducerTwoConsumers(
                fb,
                users,
                1, "Story One", " one rulz!");
        setUpProducerTwoConsumers(
                fb,
                users,
                2, "Story Two", " two rulz!");

        // TODO clear the Mongo test instance

        // Use the mongo persister
        final App app = new App(2 * 1000 , 2 * 1000, 1, new MongoPersister());
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                app.stop();
            }
        }, 12 * 1000);
        app.run();

        // TODO read back stories and check as expected
    }
}
