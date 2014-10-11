package com.colibri.social_story;

import com.colibri.social_story.utils.Pair;
import junit.framework.TestCase;
import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Assert;

import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import static com.colibri.social_story.TestUtils.*;

public class AppIntegrationTest extends TestCase {

    public void testTwoStories() throws InterruptedException {

        setUpProducerTwoConsumers(1, "Story One", " one rulz!");
        setUpProducerTwoConsumers(2, "Story Two", " two rulz!");

        final LinkedList<Pair<String, String>> ss = new LinkedList<>();
        final App app = new App(2 * 1000 , 2 * 1000, 1, new StoryPersister() {
            @Override
            public void save(Story s) {
                ss.add(new Pair<>(s.getTitle(), s.getStory()));
            }
        });
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                app.stop();
            }
        }, 12 * 1000);
        app.run();
        assertEquals(ss.size(), 2);
        LinkedList<Pair<String, String>> exp = new LinkedList<>();
        exp.add(new Pair<>("Story One", "My big story one rulz!"));
        exp.add(new Pair<>("Story Two", "My big story two rulz!"));
        Assert.assertThat(ss, IsIterableContainingInAnyOrder.containsInAnyOrder(exp.toArray()));
    }
}
