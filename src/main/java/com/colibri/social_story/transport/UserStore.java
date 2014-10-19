package com.colibri.social_story.transport;

import com.colibri.social_story.entities.User;

import java.util.Vector;

public interface UserStore {

    void persistUser(User u);

    void syncLoadUsers(Vector<User> users);

    User getUserByID(UserID userID);
}
