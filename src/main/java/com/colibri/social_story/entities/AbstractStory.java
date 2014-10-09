package com.colibri.social_story.entities;


import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Id;

public class AbstractStory {
    @Id
    private ObjectId id;
}
