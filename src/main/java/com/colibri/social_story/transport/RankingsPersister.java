package com.colibri.social_story.transport;

import com.colibri.social_story.entities.Ranking;

public interface RankingsPersister {

    void saveRanking(Ranking ranking);

    Ranking getRanking();
}
