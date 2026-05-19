package com.pipantry.gui;

import java.awt.CardLayout;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

import com.pipantry.ai.SpoonacularRecipe;
import com.pipantry.ai.SpoonacularService;
import com.pipantry.map.map;
import com.pipantry.model.Ingredient;
import com.pipantry.service.IngredientService;

public class PiPantryGUI extends JFrame {

    private static final long serialVersionUID = 1L;

    private final IngredientService service;

    private CardLayout cardLayout;
    private JPanel cardPanel;

    private HomePanel homePanel;
    private ScanPanel addPanel;
    private ScanPanel scanPanel;
    private RemovePanel removePanel;
    private ViewPanel viewPanel;
    private RecipePanel recipePanel;

    public PiPantryGUI(IngredientService service) {
        this.service = service;

        setTitle("PiPantry");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        buildGUI();
        registerListeners();

        refreshHomeTable();
        refreshRemoveTable();
        refreshViewTable();
    }

    private void buildGUI() {
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        homePanel = new HomePanel();
        addPanel = new ScanPanel();
        scanPanel = new ScanPanel();
        removePanel = new RemovePanel();
        viewPanel = new ViewPanel();
        recipePanel = new RecipePanel();

        cardPanel.add(homePanel, "HOME");
        cardPanel.add(addPanel, "ADD");
        cardPanel.add(scanPanel, "SCAN");
        cardPanel.add(removePanel, "REMOVE");
        cardPanel.add(viewPanel, "VIEW");
        cardPanel.add(recipePanel, "RECIPES");

        setContentPane(cardPanel);
        cardLayout.show(cardPanel, "HOME");
    }

    private void registerListeners() {
        homePanel.getAddButton().addActionListener(e -> cardLayout.show(cardPanel, "ADD"));

        homePanel.getRemoveButton().addActionListener(e -> {
            refreshRemoveTable();
            cardLayout.show(cardPanel, "REMOVE");
        });

        homePanel.getRecipesButton().addActionListener(e -> cardLayout.show(cardPanel, "RECIPES"));

        addPanel.getAddButton().addActionListener(e -> addIngredient());

        addPanel.getBackButton().addActionListener(e -> {
            refreshHomeTable();
            cardLayout.show(cardPanel, "HOME");
        });

        addPanel.getScanButton().addActionListener(e -> {
            scanPanel.clearForm();
            cardLayout.show(cardPanel, "SCAN");
        });

        scanPanel.getScanButton().addActionListener(e -> scanIngredient());

        scanPanel.getAddButton().addActionListener(e -> addScannedOrTypedIngredient());

        scanPanel.getBackButton().addActionListener(e -> {
            refreshHomeTable();
            cardLayout.show(cardPanel, "HOME");
        });

        removePanel.getDeleteButton().addActionListener(e -> deleteSelectedIngredients());

        removePanel.getBackButton().addActionListener(e -> {
            refreshHomeTable();
            cardLayout.show(cardPanel, "HOME");
        });

        recipePanel.getSearchButton().addActionListener(e -> searchRecipes());

        recipePanel.getBackButton().addActionListener(e -> {
            refreshHomeTable();
            cardLayout.show(cardPanel, "HOME");
        });
    }

    private void addIngredient() {
        try {
            String name = addPanel.getNameField().getText().trim();
            String location = (String) addPanel.getLocationBox().getSelectedItem();
            int quantity = (Integer) addPanel.getQuantitySpinner().getValue();
            String category = (String) addPanel.getCategoryBox().getSelectedItem();

            addOrUpdateIngredient(name, location, quantity, category);

            JOptionPane.showMessageDialog(this, "Ingredient added successfully.");

            addPanel.clearForm();

            refreshHomeTable();
            refreshViewTable();
            refreshRemoveTable();

            cardLayout.show(cardPanel, "HOME");

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Input Error", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error adding ingredient: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void scanIngredient() {
        try {
            URL url = new URL("http://192.168.96.166:5000/scan");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream())
            );

            StringBuilder response = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                response.append(line);
            }

            in.close();

            String scannedName = response.toString().trim();

            if (scannedName.contains("result")) {
                scannedName = scannedName.replace("{\"result\":\"", "")
                        .replace("\"}", "")
                        .trim();
            }

            scanPanel.setScannedName(scannedName);

            if (scannedName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Scanner did not return an item name.");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error scanning ingredient: " + ex.getMessage(),
                    "Scan Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addScannedOrTypedIngredient() {
        try {
            String name = scanPanel.getNameField().getText().trim();
            String location = (String) scanPanel.getLocationBox().getSelectedItem();
            int quantity = (Integer) scanPanel.getQuantitySpinner().getValue();
            String category = (String) scanPanel.getCategoryBox().getSelectedItem();

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please type an ingredient name or scan an item first.",
                        "Input Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            addOrUpdateIngredient(name, location, quantity, category);

            JOptionPane.showMessageDialog(this, "Ingredient added successfully.");

            scanPanel.clearForm();

            refreshHomeTable();
            refreshViewTable();
            refreshRemoveTable();

            cardLayout.show(cardPanel, "HOME");

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Input Error", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error adding ingredient: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addOrUpdateIngredient(String name, String location, int quantity, String category) {
        List<Ingredient> ingredients = service.listIngredients();

        for (Ingredient ingredient : ingredients) {
            if (ingredient.getName().equalsIgnoreCase(name)) {

                int newQuantity = ingredient.getQuantity() + quantity;

                service.removeIngredient(ingredient.getId());
                service.addIngredient(name, location, newQuantity, category);

                saveIngredientToCache(name, location, newQuantity, category);
                return;
            }
        }

        service.addIngredient(name, location, quantity, category);
        saveIngredientToCache(name, location, quantity, category);
    }

    private void saveIngredientToCache(String name, String location, int quantity, String category) {
        Ingredient ingredient = new Ingredient(name, location, quantity, category);
        map.getInstance().addToCache(name, ingredient);
    }

    private void refreshHomeTable() {
        DefaultTableModel model = homePanel.getIngredientsTableModel();
        model.setRowCount(0);

        List<Ingredient> ingredients = service.listIngredients();

        for (Ingredient ingredient : ingredients) {
            model.addRow(new Object[] {
                    ingredient.getId(),
                    ingredient.getName(),
                    ingredient.getLocation(),
                    ingredient.getQuantity(),
                    ingredient.getCategory()
            });
        }
    }

    private void refreshRemoveTable() {
        DefaultTableModel model = removePanel.getRemoveTableModel();
        model.setRowCount(0);

        List<Ingredient> ingredients = service.listIngredients();

        for (Ingredient ingredient : ingredients) {
            model.addRow(new Object[] {
                    false,
                    ingredient.getId(),
                    ingredient.getName(),
                    ingredient.getLocation(),
                    ingredient.getQuantity(),
                    ingredient.getCategory()
            });
        }
    }

    private void refreshViewTable() {
        DefaultTableModel model = viewPanel.getViewTableModel();
        model.setRowCount(0);

        List<Ingredient> ingredients = service.listIngredients();

        for (Ingredient ingredient : ingredients) {
            model.addRow(new Object[] {
                    ingredient.getId(),
                    ingredient.getName(),
                    ingredient.getLocation(),
                    ingredient.getQuantity(),
                    ingredient.getCategory()
            });
        }
    }

    private void deleteSelectedIngredients() {
        DefaultTableModel model = removePanel.getRemoveTableModel();
        List<Integer> idsToDelete = new ArrayList<>();

        for (int i = 0; i < model.getRowCount(); i++) {
            Boolean selected = (Boolean) model.getValueAt(i, 0);

            if (selected != null && selected) {
                idsToDelete.add((Integer) model.getValueAt(i, 1));
            }
        }

        if (idsToDelete.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select at least one ingredient to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete the selected items?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            for (int id : idsToDelete) {
                service.removeIngredient(id);
            }

            JOptionPane.showMessageDialog(this, "Selected ingredient(s) deleted.");

            refreshHomeTable();
            refreshRemoveTable();
            refreshViewTable();
        }
    }

    private void searchRecipes() {
        try {
            List<String> ingredientNames = service.listIngredients().stream()
                    .map(i -> i.getName().toLowerCase())
                    .toList();

            if (ingredientNames.isEmpty()) {
                recipePanel.getResultsArea().setText("No ingredients available. Add some ingredients first.");
                return;
            }

            SpoonacularService api = new SpoonacularService();
            List<SpoonacularRecipe> recipes = api.getRecipes(ingredientNames);

            if (recipes == null || recipes.isEmpty()) {
                recipePanel.getResultsArea().setText("No recipes found for the current ingredients.");
                return;
            }

            StringBuilder result = new StringBuilder();

            for (SpoonacularRecipe recipe : recipes) {
                result.append(recipe.getTitle())
                        .append(" (")
                        .append(recipe.getUsedIngredientCount())
                        .append(" used, ")
                        .append(recipe.getMissedIngredientCount())
                        .append(" missing)\n");

                if (recipe.getMissedIngredients() != null && !recipe.getMissedIngredients().isEmpty()) {
                    result.append("Missing: ");

                    for (int i = 0; i < recipe.getMissedIngredients().size(); i++) {
                        result.append(recipe.getMissedIngredients().get(i).getName());

                        if (i < recipe.getMissedIngredients().size() - 1) {
                            result.append(", ");
                        }
                    }

                    result.append("\n");
                }

                result.append("\n");
            }

            recipePanel.getResultsArea().setText(result.toString());

        } catch (Exception ex) {
            recipePanel.getResultsArea().setText("Error retrieving recipes: " + ex.getMessage());
        }
    }
}
