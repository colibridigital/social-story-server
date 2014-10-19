package com.colibri.social_story.entities;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Ranking {
    private List<User> topTenUsers = new ArrayList<>();

    public Ranking() {
    }

    public Ranking(List<User> topTen) {
        topTenUsers = new ArrayList<User>(topTen);
    }
}
