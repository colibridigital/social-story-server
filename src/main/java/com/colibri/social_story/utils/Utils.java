package com.colibri.social_story.utils;

import java.util.HashMap;
import java.util.Map;

public abstract class Utils {

    public static <K, V> Map<K, V> mapFromKeys(K key, V value) {
        Map<K, V> map = new HashMap<>();
        map.put(key, value);
        return map;
    }
}
