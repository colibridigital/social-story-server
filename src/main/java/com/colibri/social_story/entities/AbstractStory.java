package com.colibri.social_story.entities;


import com.google.code.morphia.annotations.Id;
import org.bson.types.ObjectId;

public class AbstractStory {
    @Id
    private ObjectId id;
}
