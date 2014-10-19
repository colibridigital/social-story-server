package com.colibri.social_story.transport;

import com.colibri.social_story.Story;

public interface StoryBase {

    long getServerOffsetMillis() throws InterruptedException;

    void onUserAdded(final StoryBaseCallback storyBaseCallback);

    void onWordAdded(final StoryBaseCallback storyBaseCallback);

    void onVotesAdded(final StoryBaseCallback storyBaseCallback);

    void saveStory(Story story);

    void removeStory();
}
