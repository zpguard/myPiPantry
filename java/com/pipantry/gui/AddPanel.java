package com.pipantry.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.pipantry.map.map;
import com.pipantry.model.Ingredient;

public class AddPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private JTextField nameField;
    private JComboBox<String> locationBox;
    private JSpinner quantitySpinner;
    private JComboBox<String> categoryBox;
    private JButton addButton;
    private JButton backButton;
    private JButton scanButton;

    public AddPanel() {
        setBackground(new Color(255, 199, 214));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        setLayout(new BorderLayout(15, 15));

        // Top panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(255, 199, 214));

        backButton = new JButton("← Back");
        backButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        backButton.setFocusPainted(false);
        topPanel.add(backButton, BorderLayout.WEST);

        JLabel titleLabel = new JLabel("Add Ingredient", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        topPanel.add(titleLabel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setBorder(BorderFactory.createTitledBorder("Enter Ingredient Details"));
        formPanel.setLayout(new GridLayout(8, 1, 8, 8));
        add(formPanel, BorderLayout.CENTER);

        // Use singleton cache
        map cache = map.getInstance();

        // Initialize fields
        nameField = new JTextField();
        addButton = new JButton("Add Ingredient");
        scanButton = new JButton("Scan Item");

        categoryBox = new JComboBox<>(new String[] {
            "Produce", "Dairy", "Meat", "Grains", "Snacks", "Beverages", "Frozen", "Other"
        });

        locationBox = new JComboBox<>(new String[] {
            "Pantry", "Fridge", "Freezer"
        });

        quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));

        nameField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                handleUpdate();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                handleUpdate();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                handleUpdate();
            }

            private void handleUpdate() {
                toggleAddButton();
                updateField();
            }

            private void toggleAddButton() {
                addButton.setEnabled(!nameField.getText().trim().isEmpty());
            }

            private void updateField() {
                String input = nameField.getText().trim();
                if (cache.checkCache(input)) {
                    Ingredient item = cache.getCacheMap().get(input);
                    categoryBox.setSelectedItem(item.getCategory());
                    locationBox.setSelectedItem(item.getLocation());
                    quantitySpinner.setValue(item.getQuantity());
                }
            }
        });

        // Scan button action: opens a blank new window
        scanButton.addActionListener(e -> {
            JFrame scanFrame = new JFrame("Scanner");
            scanFrame.setSize(500, 400);
            scanFrame.setLocationRelativeTo(null);
            scanFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            scanFrame.setVisible(true);
        });

        // Add components to form panel
        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);

        formPanel.add(new JLabel("Category:"));
        formPanel.add(categoryBox);

        formPanel.add(new JLabel("Location:"));
        formPanel.add(locationBox);

        formPanel.add(new JLabel("Quantity:"));
        formPanel.add(quantitySpinner);

        // Bottom panel
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(255, 199, 214));

        scanButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        scanButton.setFocusPainted(false);

        addButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        addButton.setFocusPainted(false);
        addButton.setEnabled(false);

        bottomPanel.add(scanButton);
        bottomPanel.add(addButton);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    public JTextField getNameField() {
        return nameField;
    }

    public JComboBox<String> getLocationBox() {
        return locationBox;
    }

    public JSpinner getQuantitySpinner() {
        return quantitySpinner;
    }

    public JComboBox<String> getCategoryBox() {
        return categoryBox;
    }

    public JButton getAddButton() {
        return addButton;
    }

    public JButton getBackButton() {
        return backButton;
    }

    public JButton getScanButton() {
        return scanButton;
    }

    public void clearForm() {
        nameField.setText("");
        locationBox.setSelectedIndex(0);
        quantitySpinner.setValue(1);
        categoryBox.setSelectedIndex(0);
    }
}