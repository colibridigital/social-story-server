package com.colibri.social_story.entities;

import lombok.Data;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;

@Data
public class User {
    @NonNull
    private String userName;
}
