package com.colibri.social_story;

import com.colibri.social_story.persistence.StoryPersister;
import com.colibri.social_story.utils.Pair;
import junit.framework.TestCase;
import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Assert;

import java.util.*;

import static com.colibri.social_story.TestUtils.setUpProducerTwoConsumers;

public class AppIntegrationTest extends TestCase {

    public void testTwoStories() throws InterruptedException {

        setUpProducerTwoConsumers(1, "Story One", " one rulz!");
        setUpProducerTwoConsumers(2, "Story Two", " two rulz!");

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
