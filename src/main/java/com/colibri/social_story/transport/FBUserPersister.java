package com.colibri.social_story.transport;

import com.colibri.social_story.FirebaseValueEventListenerAdapter;
import com.colibri.social_story.entities.User;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;

import java.util.Vector;
import java.util.concurrent.CountDownLatch;

public class FBUserPersister implements UserStore {

    private static final String USERS = "users/";
    private final Firebase fb;

    public FBUserPersister(Firebase fb) {
        this.fb = fb;
    }

    @Override
    public void persistUser(User u) {
        CountDownLatch done = new CountDownLatch(1);
        fb.child(USERS + u.getUserName()).setValue(
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

    /** A vector to update with the user information */
    @Override
    public void syncLoadUsers(final Vector<User> users) {
        final CountDownLatch done = new CountDownLatch(1);
        fb.child(USERS).addValueEventListener(new FirebaseValueEventListenerAdapter() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    users.add((User) ds.getValue());
                    done.countDown();
                }
            }
        });
        try {
            done.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
