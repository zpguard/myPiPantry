package com.pipantry.gui;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class RecipePanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private JTextArea resultsArea;
    private JButton searchButton;
    private JButton backButton;

    public RecipePanel() {

        // Layout setup
        setBorder(new EmptyBorder(20, 20, 20, 20));
        setLayout(new BorderLayout(15, 15));

        // Title label at top
        JLabel titleLabel = new JLabel("Search for Recipes", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        // Text area to display recipe results
        resultsArea = new JTextArea();
        resultsArea.setEditable(false);
        resultsArea.setLineWrap(true);
        resultsArea.setWrapStyleWord(true);

        add(new JScrollPane(resultsArea), BorderLayout.CENTER);

        // Bottom buttons
        JPanel bottomPanel = new JPanel();

        searchButton = new JButton("Find Recipes");
        backButton = new JButton("Back to Home");

        bottomPanel.add(searchButton);
        bottomPanel.add(backButton);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    // Getter methods so controller (GUI) can access components
    public JTextArea getResultsArea() {
        return resultsArea;
    }

    public JButton getSearchButton() {
        return searchButton;
    }

    public JButton getBackButton() {
        return backButton;
    }
}
