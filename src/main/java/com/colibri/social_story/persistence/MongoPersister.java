package com.colibri.social_story.persistence;

import com.colibri.social_story.Story;
import com.colibri.social_story.persistence.StoryDAO;
import com.colibri.social_story.persistence.StoryPersister;

public class MongoPersister implements StoryPersister {

    public void save(Story s) {
        //TODO Fix class mappings
        StoryDAO storyDAO = new StoryDAO();
        storyDAO.save(s);
    }
}
