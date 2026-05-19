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
import javax.swing.table.DefaultTableModel;
public class ViewPanel extends JPanel {
   private static final long serialVersionUID = 1L;
   private JTable viewTable;
   private DefaultTableModel viewTableModel;
   private JButton refreshButton;
   private JButton backButton;
   public ViewPanel() {
       setBorder(new EmptyBorder(20, 20, 20, 20));
       setLayout(new BorderLayout(15, 15));
       JLabel titleLabel = new JLabel("View Ingredients", SwingConstants.CENTER);
       titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
       add(titleLabel, BorderLayout.NORTH);
       String[] columns = {"ID", "Name", "Location", "Quantity", "Category"};
       viewTableModel = new DefaultTableModel(columns, 0) {
           private static final long serialVersionUID = 1L;
           @Override
           public boolean isCellEditable(int row, int column) {
               return false;
           }
       };
       viewTable = new JTable(viewTableModel);
       add(new JScrollPane(viewTable), BorderLayout.CENTER);
       JPanel bottomPanel = new JPanel();
       add(bottomPanel, BorderLayout.SOUTH);
       refreshButton = new JButton("Refresh");
       backButton = new JButton("Back to Home");
       bottomPanel.add(refreshButton);
       bottomPanel.add(backButton);
   }
   public JTable getViewTable() {
       return viewTable;
   }
   public DefaultTableModel getViewTableModel() {
       return viewTableModel;
   }
   public JButton getRefreshButton() {
       return refreshButton;
   }
   public JButton getBackButton() {
       return backButton;
   }
}
