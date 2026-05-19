package com.pipantry.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class HomePanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private JButton addButton;
    private JButton removeButton;
    private JButton searchButton;

    private JTable ingredientsTable;
    private DefaultTableModel ingredientsTableModel;

    public HomePanel() {
        setBackground(new Color(255, 199, 214));
        setBorder(new EmptyBorder(40, 40, 40, 40));
        setLayout(new BorderLayout(20, 20));

        JLabel titleLabel = new JLabel("Welcome to PiPantry!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));
        titleLabel.setForeground(Color.BLACK);
        add(titleLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(255, 199, 214));
        buttonPanel.setBorder(new EmptyBorder(20, 60, 20, 60));
        buttonPanel.setLayout(new GridLayout(4, 1, 15, 15));
        add(buttonPanel, BorderLayout.WEST);

        addButton = createStyledButton("Add Ingredient");
        ImageIcon addIcon = loadIcon("/icons/add_ingredient_icon.png", 40);
        JPanel addWrapper = createButtonRow(addButton, addIcon);

        removeButton = createStyledButton("Remove Ingredient");
        ImageIcon removeIcon = loadIcon("/icons/remove_item_icon1.png", 40);
        JPanel removeWrapper = createButtonRow(removeButton, removeIcon);

        searchButton = createStyledButton("Search for Recipes");
        ImageIcon searchIcon = loadIcon("/icons/search_icon.png", 40);
        JPanel searchWrapper = createButtonRow(searchButton, searchIcon);

        buttonPanel.add(addWrapper);
        buttonPanel.add(removeWrapper);
        buttonPanel.add(searchWrapper);

        String[] columns = {"ID", "Name", "Location", "Quantity", "Category"};

        ingredientsTableModel = new DefaultTableModel(columns, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) return Integer.class;
                if (columnIndex == 3) return Double.class;
                return String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        ingredientsTable = new JTable(ingredientsTableModel);
        ingredientsTable.setFillsViewportHeight(true);
        leftAlignTableColumns(ingredientsTable);

        JScrollPane scrollPane = new JScrollPane(ingredientsTable);
        scrollPane.setPreferredSize(new Dimension(425, 450));

        add(scrollPane, BorderLayout.CENTER);
    }

    private void leftAlignTableColumns(JTable table) {
        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(leftRenderer);
        }
    }

    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 20));
        btn.setBackground(new Color(197, 26, 74));
        btn.setForeground(Color.BLACK);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setHorizontalAlignment(SwingConstants.CENTER);
        return btn;
    }

    private JPanel createButtonRow(JButton button, ImageIcon icon) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(197, 26, 74));
        panel.setBorder(new EmptyBorder(10, 20, 10, 20));

        JLabel leftIcon = new JLabel();
        JLabel rightIcon = new JLabel();

        if (icon != null) {
            leftIcon.setIcon(icon);
            rightIcon.setIcon(icon);
        }

        panel.add(leftIcon, BorderLayout.WEST);
        panel.add(button, BorderLayout.CENTER);
        panel.add(rightIcon, BorderLayout.EAST);

        return panel;
    }

    private ImageIcon loadIcon(String path, int size) {
        URL url = getClass().getResource(path);

        if (url == null) {
            System.out.println("Image not found: " + path);
            return null;
        }

        ImageIcon icon = new ImageIcon(url);
        Image img = icon.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);

        return new ImageIcon(img);
    }

    public JTable getIngredientsTable() {
        return ingredientsTable;
    }

    public DefaultTableModel getIngredientsTableModel() {
        return ingredientsTableModel;
    }

    public JButton getAddButton() {
        return addButton;
    }

    public JButton getRemoveButton() {
        return removeButton;
    }

    public JButton getRecipesButton() {
        return searchButton;
    }
}
