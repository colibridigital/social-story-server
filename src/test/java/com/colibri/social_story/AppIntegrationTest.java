package com.colibri.social_story;

import com.colibri.social_story.entities.User;
import com.colibri.social_story.transport.FBUserPersister;
import com.colibri.social_story.transport.Superbase;
import com.colibri.social_story.transport.UserID;
import com.colibri.social_story.utils.Pair;
import com.colibri.social_story.utils.Utils;
import junit.framework.TestCase;
import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Assert;

import java.util.*;

import static com.colibri.social_story.TestUtils.setUpProducerTwoConsumers;

public class AppIntegrationTest extends TestCase {

    public void testTwoStories() throws InterruptedException {

        Superbase fb = new Superbase(App.FB_URL);
        Superbase root = new  Superbase(App.FB_ROOT_URL);
        root.syncClear("");
        FBUserPersister fbUserPersister = new FBUserPersister(root);
        System.out.println("Clearing users..");

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
                users, 1, "Story One", " one rulz!");
        setUpProducerTwoConsumers(
                fb,
                users, 2, "Story Two", " two rulz!");

        final LinkedList<Pair<String, String>> ss = new LinkedList<>();
        final App app = new App( 2 * 1000 , 2 * 1000, 1,
                s -> ss.add(new Pair<>(s.getTitle(), s.getStory())));
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                app.stop();
            }
        }, 12 * 1000);
        app.run();
        assertEquals(2, ss.size());
        List<Pair<String, String>> exp = Arrays.asList(
                new Pair<>("Story One", "My big story one rulz!"),
                new Pair<>("Story Two", "My big story two rulz!"));
        Assert.assertThat(ss, IsIterableContainingInAnyOrder.containsInAnyOrder(exp.toArray()));
    }
}
