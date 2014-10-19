package com.colibri.social_story.persistence;

import com.colibri.social_story.Story;

// Some interface through which stories are persisted
public interface StoryPersister {

    void save(Story s);

}
