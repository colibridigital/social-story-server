package com.colibri.social_story.transport;

import com.colibri.social_story.entities.User;
import com.firebase.client.Firebase;

import static com.colibri.social_story.utils.Utils.*;

public class FBUserPersister implements UserPersister {

    private final Firebase fb;

    public FBUserPersister(Firebase fb) {
        this.fb = fb;
    }

    @Override
    public void persistUser(User u) {
        fb.child("profiles").updateChildren(
                mapFromKeys(u.getUserName(), (Object) u));
    }

    @Override
    public void loadUser(UserID userId) {
        throw new UnsupportedOperationException("Load user not implemented!");
    }
}
