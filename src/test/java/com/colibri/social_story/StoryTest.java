package com.colibri.social_story;

import junit.framework.TestCase;
import org.mockito.InOrder;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;


public class StoryTest extends TestCase {

    public void testConnect() throws Exception {

        StoryBase storyBase = mock(StoryBase.class);
        int nRounds = 2;
        Story sr = new Story(0, storyBase, 0, 0, 2, "Some title");
        sr.connect();

        InOrder inOrder = inOrder(storyBase);
        // check callbacks are set
        inOrder.verify(storyBase).onUserAdded(any(StoryBaseCallback.class));
        inOrder.verify(storyBase).onWordAdded(any(StoryBaseCallback.class));
        inOrder.verify(storyBase).onVotesAdded(any(StoryBaseCallback.class));

        // all rounds are played
        inOrder.verify(storyBase, calls(1)).getServerOffsetMillis();
        for (int i = 0; i < nRounds; i++) {
            inOrder.verify(storyBase).saveStory(any(Story.class));
            inOrder.verify(storyBase).getServerOffsetMillis();
            inOrder.verify(storyBase).saveStory(any(Story.class));
            inOrder.verify(storyBase, calls(1)).removeStory();
        }
        inOrder.verify(storyBase, calls(1)).removeStory();
        inOrder.verifyNoMoreInteractions();
    }
}
