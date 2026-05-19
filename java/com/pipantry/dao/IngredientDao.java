package com.pipantry.dao;
//DAO is a class that simply talks to the database.
/*
This class is inititated by the IngreditnService.java class */
//No user input, just SQL and mapping. 
//Keeps SQL isolated from the rest of the "app" so it doesn't become "SQL everywhere"
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.pipantry.db.Database;
import com.pipantry.model.Ingredient;

public class IngredientDao {
//Builds an SQL insert command
//Opens a connection
//Safely fills those values
//Executes the insert and automatically closes eeverything
//Ingredient declaration
//Prepared statement prevent SQL injection; safe and efficient
//---------------ADD INGREDIENT METHOD----------------------
    public void add(String name, String location, int quantity, String category) throws SQLException {
        //questionmarks are placeholder values (where values will go later)
        String sql = "INSERT INTO ingredients(name, location, quantity, category) VALUES (?, ?, ?, ?)";
        //try statement makes it automatically close when finished
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            //Cleans up whitspace in inputs/values
            //Converts string to SQL VARCHAR
            //Converts double to SQL numeric
            ps.setString(1, name.trim());
            ps.setString(2, location.trim());
            ps.setInt(3, quantity);
            ps.setString(4, category.trim());
            //executeUpdate is used for inserting, updating, and deleting
            ps.executeUpdate();
        }
    }

    //------------------LIST ALL INGREDIENTS-----------------------
    //gets all the ingredients from the database (?) and return as a list of "Ingredient" objects
    public List<Ingredient> listAll() throws SQLException {
        String sql = "SELECT id, name, location, quantity, category FROM ingredients ORDER BY name ASC";
        List<Ingredient> list = new ArrayList<>();

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            //loop that walks through the database row by row (each row represents one ingredient)
            while (rs.next()) {
                list.add(new Ingredient(
            //actually reading the columns 
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("location"),
                        rs.getInt("quantity"),
                        rs.getString("category")
                ));
            }
        }
        return list;
    }

//------------REMOVE INGREDINT BY ID-------------------------    
    public boolean removeById(int id) throws SQLException {
        String sql = "DELETE FROM ingredients WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}