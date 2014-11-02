package com.colibri.social_story.transport;

import com.colibri.social_story.entities.User;
import com.colibri.social_story.transport.ReleaseLatchCompletionListener;
import com.firebase.client.Firebase;

import java.util.concurrent.CountDownLatch;

/** A Firebase that sucks less... */
public class Superbase extends Firebase {

    public Superbase(String url) {
        super(url);
    }

    public void syncClear(String path) throws InterruptedException {
        final CountDownLatch done = new CountDownLatch(1);
        child(path).removeValue(new ReleaseLatchCompletionListener(done));
        done.await();
    }

    public void syncWrite(String pathFromRoot, Object object) {
        try {
            final CountDownLatch done = new CountDownLatch(1);
            if (pathFromRoot == null)
                setValue(object, new ReleaseLatchCompletionListener(done));
            else
                getRoot().child(pathFromRoot).setValue(object, new ReleaseLatchCompletionListener(done));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
