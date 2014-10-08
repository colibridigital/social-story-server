package com.colibri.social_story;

import com.colibri.social_story.entities.Votes;

public interface StoryBase {

    long getServerOffsetMillis() throws InterruptedException;

    void onUserAdded(final StoryBaseCallback storyBaseCallback);

    void onWordAdded(final StoryBaseCallback storyBaseCallback);

    void onVotesAdded(final StoryBaseCallback storyBaseCallback);

    void writeVotes(Votes v) throws InterruptedException;

    void writeStoryAttributes(Story story);

    void removeStory();
}
