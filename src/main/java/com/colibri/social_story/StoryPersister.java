package com.colibri.social_story;

// Some interface through which stories are persisted
public interface StoryPersister {

    void save(Story s);

}
