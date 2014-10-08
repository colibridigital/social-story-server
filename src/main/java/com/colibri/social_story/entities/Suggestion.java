package com.colibri.social_story.entities;

import lombok.Data;
import lombok.NonNull;

@Data
public class Suggestion {

    @NonNull
    private User user;
    @NonNull
    private String word;


}
