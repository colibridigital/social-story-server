package com.colibri.social_story.transport;

import com.colibri.social_story.entities.User;

import java.util.Vector;

public interface UserStore {

    void persistUser(User u);

    void loadUser(UserID userId);

    void syncLoadUsers(Vector<User> users);
}
