package com.colibri.social_story.transport;

import com.colibri.social_story.Superbase;
import com.colibri.social_story.entities.Ranking;

import java.util.concurrent.CountDownLatch;

public class FBRankingsPersister implements RankingsPersister {

    private final Superbase sb;

    public FBRankingsPersister(Superbase sb) {
        this.sb = sb;
    }

    @Override
    public void saveRanking(Ranking ranking) {
        sb.syncWrite("ranking", ranking);
    }

    @Override
    public Ranking getRanking() {
        throw new UnsupportedOperationException("Load user not implemented!");
    }
}
