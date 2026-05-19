package com.pipantry.service;

import java.sql.SQLException;
import java.util.List;

import com.pipantry.dao.IngredientDao;
import com.pipantry.map.map;
import com.pipantry.model.Ingredient;

public class IngredientService {
    private final IngredientDao dao = new IngredientDao();
    private final map cache = map.getInstance();

    public IngredientService() {
        // Load all existing DB ingredients into cache on startup
        try {
            for (Ingredient i : dao.listAll()) {
                cache.addToCache(i.getName(), i);
            }
        } catch (SQLException e) {
            System.out.println("Error loading cache: " + e.getMessage());
        }
    }

    public void addIngredient(String name, String location, int quantity, String category) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name required");
        if (location == null || location.isBlank()) throw new IllegalArgumentException("Location required");
        if (category == null || category.isBlank()) throw new IllegalArgumentException("Category required");
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be > 0");

        try {
            dao.add(name, location, quantity, category);
            // Also update cache
            cache.addToCache(name, new Ingredient(0, name, location, quantity, category));
        } catch (SQLException e) {
            throw new RuntimeException("DB error while adding ingredient", e);
        }
    }

    public List<Ingredient> listIngredients() {
        try {
            return dao.listAll();
        } catch (SQLException e) {
            throw new RuntimeException("DB error while listing ingredients", e);
        }
    }

    public boolean removeIngredient(int id) {
        try {
            // Also remove from cache
            listIngredients().stream()
                .filter(i -> i.getId() == id)
                .findFirst()
                .ifPresent(i -> cache.getCacheMap().remove(i.getName()));
            return dao.removeById(id);
        } catch (SQLException e) {
            throw new RuntimeException("DB error while removing ingredient", e);
        }
    }
}