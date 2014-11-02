package com.colibri.social_story.transport;

import com.colibri.social_story.entities.User;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

public class FBUserPersister implements UserStore {

    private static final Logger log = Logger.getLogger(FBUserPersister.class.getName());

    private static final String USERS = "users";
    private final Firebase fb;
    private final Vector<User> userCache = new Vector<>();
    private List<User> users;

    public FBUserPersister(Firebase fb) {
        this.fb = fb;
    }

    @Override
    public void persistUser(User u) {
        CountDownLatch done = new CountDownLatch(1);
        fb.child(USERS + u.getUsername()).setValue(
                (Object) u, new ReleaseLatchCompletionListener(done));
        try {
            done.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /** A vector to update with the user information */
    @Override
    public void syncLoadUsers(final Vector<User> users) {
        log.info("Loading all users");
        final CountDownLatch done = new CountDownLatch(1);
        fb.child(USERS).addValueEventListener(new FirebaseValueEventListenerAdapter() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    System.out.println(dataSnapshot.getValue());
                    User u = ds.getValue(User.class);
                    u.setUid(new UserID(ds.getName()));
                    users.add(u);
                    userCache.add(u);
                }
                done.countDown();
            }
        });
        try {
            done.await();
            log.info("Finished loading users");
            log.info(userCache.toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User getUserByID(UserID userID) {
        log.info("Getting user by ID " + userID);
        for (User u : userCache) {
            if (u.getUid().equals(userID))
                return u;
        }
        return null;
    }

    public void setUsers(Map<String, User> users) throws InterruptedException {
        final CountDownLatch cd = new CountDownLatch(1);
        fb.child("users").setValue(users, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                log.info("Done setting users, error: "  + firebaseError);
                cd.countDown();
            }
        });
        cd.await();
    }

    public List<User> getUsers() {
        return users;
    }
}
