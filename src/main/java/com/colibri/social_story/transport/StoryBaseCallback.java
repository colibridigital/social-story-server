package com.colibri.social_story.transport;

public interface StoryBaseCallback<T> {
    // TODO this will probably need to provide some data
    void handle(T t);
}
