package com.pipantry.model;
public class Ingredient {
   private final int id;
   private final String name;
   private final String location;
   private final int quantity;
   private final String category;
   public Ingredient(int id, String name, String location, int quantity, String category) {
       this.id = id;
       this.name = name;
       this.location = location;
       this.quantity = quantity;
       this.category = category;
   }
   public Ingredient( String name, String location, int quantity, String category){
       this(0,"None", location, quantity, category);
   }
   public int getId() { return id; }
   public String getName() { return name; }
   public String getLocation() { return location; }
   public int getQuantity() { return quantity; }
   public String getCategory() { return category; }
   @Override
   public String toString() {
       return String.format("#%d |%s | %d | %s | %s", id, name, quantity, category, location);
   }
}

