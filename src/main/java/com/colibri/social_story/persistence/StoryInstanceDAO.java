package com.colibri.social_story.persistence;

import com.colibri.social_story.entities.StoryInstance;
import com.google.code.morphia.dao.BasicDAO;
import org.bson.types.ObjectId;

public class StoryInstanceDAO extends BasicDAO<StoryInstance, ObjectId> {
    public StoryInstanceDAO() {
        super(StoryInstance.class, MongoConnectionManager.instance().getDb());
    }
}
