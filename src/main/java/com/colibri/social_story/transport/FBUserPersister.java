package com.colibri.social_story.transport;

import com.colibri.social_story.entities.User;
import com.firebase.client.Firebase;

import java.util.concurrent.CountDownLatch;

import static com.colibri.social_story.utils.Utils.*;

public class FBUserPersister implements UserPersister {

    private final Firebase fb;

    public FBUserPersister(Firebase fb) {
        this.fb = fb;
    }

    @Override
    public void persistUser(User u) {
        CountDownLatch done = new CountDownLatch(1);
        fb.child("profiles/" + u.getUserName()).setValue(
                (Object) u, new ReleaseLatchCompletionListener(done));
        try {
            done.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadUser(UserID userId) {
        throw new UnsupportedOperationException("Load user not implemented!");
    }
}
