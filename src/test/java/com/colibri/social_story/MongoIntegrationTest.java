package com.colibri.social_story;

import junit.framework.TestCase;

import java.util.Timer;
import java.util.TimerTask;

import static com.colibri.social_story.TestUtils.setUpProducerTwoConsumers;

public class MongoIntegrationTest extends TestCase {

    public void testTwoStories() throws InterruptedException {

        setUpProducerTwoConsumers(1, "Story One", " one rulz!");
        setUpProducerTwoConsumers(2, "Story Two", " two rulz!");

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
