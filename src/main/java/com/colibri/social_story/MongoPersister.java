package com.colibri.social_story;

import com.colibri.social_story.persistence.StoryDAO;

public class MongoPersister implements StoryPersister {

    public void save(Story s) {
        //TODO Fix class mappings
        StoryDAO storyDAO = new StoryDAO();
        storyDAO.save(s);
    }
}
