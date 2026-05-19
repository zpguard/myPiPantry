package com.pipantry.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
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

public class ScanPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private JTextField nameField;
    private JComboBox<String> locationBox;
    private JSpinner quantitySpinner;
    private JComboBox<String> categoryBox;
    private JButton addButton;
    private JButton backButton;
    private JButton scanButton;

    private map cache = map.getInstance();

    public ScanPanel() {
        setBackground(new Color(255, 199, 214));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        setLayout(new BorderLayout(15, 15));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(255, 199, 214));

        backButton = new JButton("← Back");
        backButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        backButton.setFocusPainted(false);
        topPanel.add(backButton, BorderLayout.WEST);

        JLabel titleLabel = new JLabel("Scan or Add Ingredient", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        topPanel.add(titleLabel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel();
        formPanel.setBorder(BorderFactory.createTitledBorder("Ingredient Details"));
        formPanel.setLayout(new GridLayout(8, 1, 8, 8));
        add(formPanel, BorderLayout.CENTER);

        nameField = new JTextField();

        categoryBox = new JComboBox<>(new String[] {
                "Produce", "Dairy", "Meat", "Grains", "Snacks", "Beverages", "Frozen", "Other"
        });

        locationBox = new JComboBox<>(new String[] {
                "Pantry", "Fridge", "Freezer"
        });

        quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));

        addButton = new JButton("Add Ingredient");
        addButton.setEnabled(false);

        scanButton = new JButton("Scan Item");

        nameField.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                updateFormFromName();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateFormFromName();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateFormFromName();
            }
        });

        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);

        formPanel.add(new JLabel("Category:"));
        formPanel.add(categoryBox);

        formPanel.add(new JLabel("Location:"));
        formPanel.add(locationBox);

        formPanel.add(new JLabel("Quantity:"));
        formPanel.add(quantitySpinner);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(255, 199, 214));

        scanButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        scanButton.setFocusPainted(false);

        addButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        addButton.setFocusPainted(false);

        bottomPanel.add(scanButton);
        bottomPanel.add(addButton);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void updateFormFromName() {
        String name = nameField.getText().trim();

        addButton.setEnabled(!name.isEmpty());

        if (name.isEmpty()) {
            return;
        }

        if (cache.checkCache(name)) {
            Ingredient savedItem = cache.getFromCache(name);

            categoryBox.setSelectedItem(savedItem.getCategory());
            locationBox.setSelectedItem(savedItem.getLocation());
        }
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

    public void setScannedName(String name) {
        nameField.setText(name);
        updateFormFromName();
    }

    public void clearForm() {
        nameField.setText("");
        locationBox.setSelectedIndex(0);
        quantitySpinner.setValue(1);
        categoryBox.setSelectedIndex(0);
        addButton.setEnabled(false);
    }
}
