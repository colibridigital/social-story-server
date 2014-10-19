package com.colibri.social_story.transport;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.concurrent.CountDownLatch;

public class ReleaseLatchCompletionListener implements Firebase.CompletionListener {
    private final CountDownLatch done;

    public ReleaseLatchCompletionListener(CountDownLatch done) {
        this.done = done;
    }

    @Override
    public void onComplete(FirebaseError firebaseError, Firebase firebase) {
        done.countDown();
    }
}
