package com.pipantry.ai;

import java.util.List;

// This class represents a single recipe returned from the Spoonacular API
public class SpoonacularRecipe {

    // Basic recipe info
    private int id;                     // unique recipe ID
    private String title;              // recipe name
    private String image;              // URL to recipe image

    // Info about ingredient matching
    private int usedIngredientCount;   // how many ingredients we already have
    private int missedIngredientCount; // how many ingredients are missing

    // List of ingredients we are missing for this recipe
    private List<SpoonacularIngredient> missedIngredients;

    // Getter for recipe ID
    public int getId() {
        return id;
    }

    // Getter for recipe title
    public String getTitle() {
        return title;
    }

    // Getter for image URL
    public String getImage() {
        return image;
    }

    // Getter for number of used ingredients
    public int getUsedIngredientCount() {
        return usedIngredientCount;
    }

    // Getter for number of missing ingredients
    public int getMissedIngredientCount() {
        return missedIngredientCount;
    }

    // Getter for list of missing ingredients
    public List<SpoonacularIngredient> getMissedIngredients() {
        return missedIngredients;
    }
}