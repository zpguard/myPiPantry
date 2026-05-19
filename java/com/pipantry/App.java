package com.pipantry;

import javax.swing.SwingUtilities;

import com.pipantry.db.Database;
import com.pipantry.gui.PiPantryGUI;
import com.pipantry.service.IngredientService;

public class App {

    public static void main(String[] args) {
        Database.init();

        SwingUtilities.invokeLater(() -> {
            PiPantryGUI gui = new PiPantryGUI(new IngredientService());
            gui.setVisible(true);
        });
    }
}