package com.pipantry.ai;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import com.google.gson.Gson;

public class SpoonacularService {

    // API key provided by Spoonacular (used to authenticate requests)
    private static final String API_KEY = "e698d054ceeb4c8b9d85be215eeff38f";

    /*
     * This method connects to the Spoonacular API and retrieves recipes
     * based on a list of ingredients from the user's pantry.
     *
     * This is considered the "AI" part of the project because:
     * - We send user data (ingredients) to an external API
     * - The API returns intelligent results (recipes based on what we have)
     */
    public List<SpoonacularRecipe> getRecipes(List<String> ingredients) {
        try {
            // Convert ingredient list into comma-separated string for API
            String ingredientString = String.join(",", ingredients);

            /*
             * Build the API request URL
             * - ingredients = user pantry items
             * - number = limit results
             * - ranking = prioritize best matches
             * - ignorePantry = avoids common pantry assumptions
             */
            String urlString = "https://api.spoonacular.com/recipes/findByIngredients"
                    + "?ingredients=" + ingredientString
                    + "&number=5"
                    + "&ranking=1"
                    + "&ignorePantry=true"
                    + "&apiKey=" + API_KEY;

            // Create connection to the API
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Read response from API
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream())
            );

            String line;
            StringBuilder response = new StringBuilder();

            // Store full JSON response
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();

            /*
             * Convert JSON response into Java objects using Gson
             * This allows us to easily access recipe data like title, image, etc.
             */
            Gson gson = new Gson();
            SpoonacularRecipe[] recipes =
                    gson.fromJson(response.toString(), SpoonacularRecipe[].class);

            return List.of(recipes);

        } catch (Exception e) {
            // If anything fails, throw error
            throw new RuntimeException("Failed to get recipes from Spoonacular", e);
        }
    }
}