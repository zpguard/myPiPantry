package com.pipantry.gui;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class RemovePanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private JTable removeTable;
    private DefaultTableModel removeTableModel;
    private JButton deleteButton;
    private JButton backButton;

    public RemovePanel() {

        setBorder(new EmptyBorder(20, 20, 20, 20));
        setLayout(new BorderLayout(15, 15));

        JLabel titleLabel = new JLabel("Remove Ingredients", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));

        add(titleLabel, BorderLayout.NORTH);

        String[] columns = {
                "Select",
                "ID",
                "Name",
                "Location",
                "Quantity",
                "Category"
        };

        removeTableModel = new DefaultTableModel(columns, 0) {

            private static final long serialVersionUID = 1L;

            @Override
            public Class<?> getColumnClass(int columnIndex) {

                if (columnIndex == 0) {
                    return Boolean.class;
                }

                if (columnIndex == 1) {
                    return Integer.class;
                }

                if (columnIndex == 4) {
                    return Double.class;
                }

                return String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0;
            }
        };

        removeTable = new JTable(removeTableModel);

        removeTable.setFillsViewportHeight(true);

        // Keep checkbox column as real checkboxes
        removeTable.getColumnModel().getColumn(0)
                .setCellRenderer(removeTable.getDefaultRenderer(Boolean.class));

        removeTable.getColumnModel().getColumn(0)
                .setCellEditor(removeTable.getDefaultEditor(Boolean.class));

        // Left align all OTHER columns
        leftAlignTableColumns(removeTable);

        // Make checkbox column smaller
        TableColumn selectColumn = removeTable.getColumnModel().getColumn(0);
        selectColumn.setPreferredWidth(60);
        selectColumn.setMaxWidth(70);

        add(new JScrollPane(removeTable), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();

        add(bottomPanel, BorderLayout.SOUTH);

        deleteButton = new JButton("Delete Selected");
        backButton = new JButton("Back to Home");

        bottomPanel.add(deleteButton);
        bottomPanel.add(backButton);
    }

    private void leftAlignTableColumns(JTable table) {

        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();

        leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);

        // Start at column 1 so checkbox column stays checkbox
        for (int i = 1; i < table.getColumnCount(); i++) {

            table.getColumnModel()
                    .getColumn(i)
                    .setCellRenderer(leftRenderer);
        }
    }

    public JTable getRemoveTable() {
        return removeTable;
    }

    public DefaultTableModel getRemoveTableModel() {
        return removeTableModel;
    }

    public JButton getDeleteButton() {
        return deleteButton;
    }

    public JButton getBackButton() {
        return backButton;
    }
}
