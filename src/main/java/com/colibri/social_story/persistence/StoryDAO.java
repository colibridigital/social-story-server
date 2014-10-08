package com.colibri.social_story.persistence;

import com.colibri.social_story.Story;
import com.google.code.morphia.dao.BasicDAO;
import org.bson.types.ObjectId;

public class StoryDAO extends BasicDAO<Story, ObjectId> {
    public StoryDAO() {
        super(Story.class, MongoConnectionManager.instance().getDb());
    }
}