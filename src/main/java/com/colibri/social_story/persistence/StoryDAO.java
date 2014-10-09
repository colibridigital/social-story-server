package com.colibri.social_story.persistence;

import com.colibri.social_story.Story;
import org.bson.types.ObjectId;
import org.mongodb.morphia.dao.BasicDAO;

public class StoryDAO extends BasicDAO<Story, ObjectId> {
    public StoryDAO() {
        super(Story.class, MongoConnectionManager.instance().getDb());
    }
}