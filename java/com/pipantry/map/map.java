package com.pipantry.map;

import java.util.HashMap;
import java.util.Map;

import com.pipantry.model.Ingredient;

public class map {

    private static map instance = new map();

    private Map<String, Ingredient> cacheMap;

    private map() {
        cacheMap = new HashMap<>();
    }

    public static map getInstance() {
        return instance;
    }

    public Map<String, Ingredient> getCacheMap() {
        return cacheMap;
    }

    private String normalizeKey(String key) {
        return key.trim().toLowerCase();
    }

    public void addToCache(String key, Ingredient value) {
        cacheMap.put(normalizeKey(key), value);
    }

    public boolean checkCache(String key) {
        return cacheMap.containsKey(normalizeKey(key));
    }

    public Ingredient getFromCache(String key) {
        return cacheMap.get(normalizeKey(key));
    }
}