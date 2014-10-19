package com.colibri.social_story.transport;

import com.colibri.social_story.entities.User;

public interface UserPersister {

    void persistUser(User u);

    void loadUser(UserID userId);
}
