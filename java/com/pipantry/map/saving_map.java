package com.pipantry.map;

import com.pipantry.model.Ingredient;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Scanner;

public class saving_map {

    public void save_to_file(map cache, String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {

            for (Map.Entry<String, Ingredient> entry : cache.getCacheMap().entrySet()) {
                String key = entry.getKey();
                Ingredient value = entry.getValue();

                writer.println(
                        key + "," +
                        value.getLocation() + "," +
                        value.getCategory() + "," +
                        value.getQuantity()
                );
            }

        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }

    public void get_from_file(String filename, map cache) {
        try (Scanner scanner = new Scanner(new File(filename))) {

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                String[] parts = line.split(",");

                if (parts.length < 4) {
                    continue;
                }

                String name = parts[0].trim();
                String location = parts[1].trim();
                String category = parts[2].trim();

                int quantity = (int) Double.parseDouble(parts[3].trim());

                Ingredient item = new Ingredient(name, location, quantity, category);

                cache.addToCache(name, item);
            }

        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
}
    

