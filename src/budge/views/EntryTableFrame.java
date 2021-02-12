/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package budge.views;

import budge.Main;
import budge.model.Entry;
import budge.model.EntryKey;
import budge.model.ParsedEntry;
import budge.service.EntryService;
import budge.utils.FormUtils;
import budge.utils.StringUtils;
import budge.utils.Utils;
import budge.views.modals.EditModal;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author pat
 */
public class EntryTableFrame extends javax.swing.JFrame {
    
    EntryService entryService = Main.getEntryService();

    // table model used, with some customizations and overrides
    DefaultTableModel model = new DefaultTableModel() {
        @Override   // returns a certain type of class based on the column index
        public Class getColumnClass(int column) {
            if (column == 0) {
                return ImageIcon.class;
            } else {
                return Object.class;
            }
        }

        @Override
        public boolean isCellEditable(int row, int column){
            return false;
        }
    };

    /**
     * Creates new form TableFrame
     */
    public EntryTableFrame() {
        // init the components
        // checks if we're in the EDT to prevent NoSuchElementExceptions and ArrayIndexOutOfBoundsExceptions
        if (SwingUtilities.isEventDispatchThread()) {
            initComponents();
            init();
        } else {
            SwingUtilities.invokeLater(() -> {
                initComponents();
                init();
            });
        }
    }

    public void init() {
        table.setModel(model);

        model.addColumn(StringUtils.EMPTY);
        model.addColumn("Account");
        model.addColumn("Date");
        model.addColumn("Type");
        model.addColumn("Description");
        model.addColumn("Amount");
        model.addColumn("Category");
        model.addColumn("Index");

        FormUtils.setColumnWidth(0, 12, table);
        FormUtils.setColumnWidth(1, 120, table);
        FormUtils.setColumnWidth(2, 100, table);
        FormUtils.setColumnWidth(3, 170, table);
        // leave 3 to stretch
        FormUtils.setColumnWidth(5,100, table);
        FormUtils.setColumnWidth(6, 175, table);

        table.removeColumn(table.getColumnModel().getColumn(7));

        table.setAutoCreateRowSorter(true);

        addAllEntriesToTable(entryService.getEntries());
    }

    private void addAllEntriesToTable(List<ParsedEntry> entries) {

        // counter for non-parsed files
        int nonParsedEntries = 0;

        // sort by date first
        entries.sort(Comparator.comparing(Entry::getDate));

        for (int i = 0; i < entries.size(); i++) {
            ParsedEntry entry = entries.get(i);
            model.addRow(new Object[] {
                    null,
                    entry.getAccount(),
                    entry.getTransactionDate() == null ?
                            Utils.formatDate(entry.getDate()) : Utils.formatDate(entry.getTransactionDate()),
                    entry.getType() != null ? entry.getType().getType() : StringUtils.EMPTY,
                    entry.getParsedDescription(),
                    Utils.formatDoubleForCurrency(Double.valueOf(entry.getParsedAmount())),
                    entry.getCategory() != null ? entry.getCategory().getCategory() : StringUtils.EMPTY,
                    entry.getKey()
            });
            table.setValueAt(entry.isParsed() ? new ImageIcon(this.getClass().getResource("/resources/img/check-sm.png")) :
                    new ImageIcon(this.getClass().getResource("/resources/img/default-sm.png")), i, 0);
            if (!entry.isParsed()) {
                nonParsedEntries++;
            }
        }
        
        statusLabel.setText(entries.size() + " entries loaded! (" +
            (entries.size() - nonParsedEntries) + " entries parsed, " + nonParsedEntries + " non-parsed)");
    }

    private void editRows(int[] selectedRows) {
        List<ParsedEntry> entries = new ArrayList<>();
        for (int row : selectedRows) {
            EntryKey key = (EntryKey) model.getValueAt(row, 7);
            ParsedEntry entry = entryService.getEntryByKey(key);
            if (entry != null) {
                entries.add(entry);
            }
        }
        EditModal editModal = new EditModal(entries);
        editModal.setLocation(this.getX() + this.getWidth() + 10, this.getY());
        editModal.setVisible(true);
    }

    private void filter(
            String account, String dateFrom, String dateTo, String description, String parsedString, String category) {
        Boolean parsed;
        if (account.equals("All")) {
            account = StringUtils.EMPTY;
        }
        if (StringUtils.isEmpty(dateFrom)) {
            dateFrom = "01/01/1970";
        }
        if (StringUtils.isEmpty(dateTo)) {
            dateTo = "01/01/2070";
        }
        if (parsedString.equals("Any")) {
            parsed = null;
        } else {
            parsed = parsedString.equals("Parsed");
        }
        List<ParsedEntry> filteredEntries = entryService.filter(account, dateFrom, dateTo, description, parsed, category);
        filteredEntries.sort(Comparator.comparing(Entry::getDate));
        model.setRowCount(0);
        addAllEntriesToTable(filteredEntries);
    }

    private void clearFilter() {
        clear();
        model.setRowCount(0);
        addAllEntriesToTable(entryService.getEntries());
    }

    private void clear() {
        accountComboBox.setSelectedItem("All");
        dateToField.setText(StringUtils.EMPTY);
        dateFromField.setText(StringUtils.EMPTY);;
        descriptionTextField.setText(StringUtils.EMPTY);
        parsedComboBox.setSelectedItem("Any");
        categoryComboBox.setSelectedItem(StringUtils.EMPTY);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tableScrollPane = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        filterLbl = new javax.swing.JLabel();
        accountLbl = new javax.swing.JLabel();
        dateLbl = new javax.swing.JLabel();
        toLbl = new javax.swing.JLabel();
        descriptionLbl = new javax.swing.JLabel();
        categoryLbl = new javax.swing.JLabel();
        accountComboBox = new javax.swing.JComboBox<>();
        dateFromField = new javax.swing.JTextField();
        dateToField = new javax.swing.JTextField();
        descriptionTextField = new javax.swing.JTextField();
        categoryComboBox = new javax.swing.JComboBox<>();
        filterButton = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();
        statusLbl = new javax.swing.JLabel();
        statusLabel = new javax.swing.JLabel();
        clearFilterButton = new javax.swing.JButton();
        parsedComboBox = new javax.swing.JComboBox<>();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        exitMenuItem = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        editRowsMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Budge - Table View");

        table.setFont(new java.awt.Font("Lucida Sans Typewriter", 0, 12)); // NOI18N
        tableScrollPane.setViewportView(table);

        filterLbl.setFont(new java.awt.Font("Lucida Grande", 0, 16)); // NOI18N
        filterLbl.setText("Filters");

        accountLbl.setText("Account:");

        dateLbl.setText("Date Range:");

        toLbl.setText("to");

        descriptionLbl.setText("Description (Contains):");

        categoryLbl.setText("Category:");

        accountComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "Pat's Checking", "Pat's Savings", "Aimee's Checking", "Aimee's Savings", "Joint Checking", "Joint Savings" }));

        categoryComboBox.setModel(FormUtils.initCategoryComboBox());

        filterButton.setText("Filter");
        filterButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filterButtonActionPerformed(evt);
            }
        });

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jSeparator4.setOrientation(javax.swing.SwingConstants.VERTICAL);

        statusLbl.setText("Status:");

        clearFilterButton.setText("Clear Filter");
        clearFilterButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearFilterButtonActionPerformed(evt);
            }
        });

        parsedComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Any", "Parsed", "Not Parsed" }));

        fileMenu.setText("File");

        exitMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.META_DOWN_MASK));
        exitMenuItem.setText("Exit Budge");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        editMenu.setText("Edit");

        editRowsMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.META_DOWN_MASK));
        editRowsMenuItem.setText("Edit Selected Rows...");
        editRowsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editRowsMenuItemActionPerformed(evt);
            }
        });
        editMenu.add(editRowsMenuItem);

        menuBar.add(editMenu);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tableScrollPane)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(filterLbl)
                                    .addComponent(accountComboBox, 0, 140, Short.MAX_VALUE)
                                    .addComponent(accountLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(dateFromField, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(toLbl)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(dateToField, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(dateLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(descriptionLbl, javax.swing.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE)
                                    .addComponent(descriptionTextField)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(statusLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(statusLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(parsedComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(categoryComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(categoryLbl, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(clearFilterButton, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(filterButton, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(parsedComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(filterLbl)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jSeparator3, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(accountLbl)
                                                    .addComponent(dateLbl)
                                                    .addComponent(descriptionLbl))
                                                .addGap(0, 0, Short.MAX_VALUE))
                                            .addComponent(categoryLbl, javax.swing.GroupLayout.Alignment.TRAILING))
                                        .addComponent(statusLbl))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(accountComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(dateToField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(toLbl)
                                            .addComponent(dateFromField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(descriptionTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(categoryComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(filterButton)
                        .addComponent(clearFilterButton))
                    .addComponent(statusLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tableScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void editRowsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editRowsMenuItemActionPerformed
        editRows(table.getSelectedRows());
    }//GEN-LAST:event_editRowsMenuItemActionPerformed

    private void filterButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filterButtonActionPerformed
        filter(
                accountComboBox.getSelectedItem() != null ? accountComboBox.getSelectedItem().toString() : null,
                dateFromField.getText(),
                dateToField.getText(),
                descriptionTextField.getText(),
                parsedComboBox.getSelectedItem() != null ? parsedComboBox.getSelectedItem().toString() : null,
                categoryComboBox.getSelectedItem() != null ? categoryComboBox.getSelectedItem().toString() : null
                );
    }//GEN-LAST:event_filterButtonActionPerformed

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        this.dispose();
    }//GEN-LAST:event_exitMenuItemActionPerformed

    private void clearFilterButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearFilterButtonActionPerformed
        clearFilter();
    }//GEN-LAST:event_clearFilterButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> accountComboBox;
    private javax.swing.JLabel accountLbl;
    private javax.swing.JComboBox<String> categoryComboBox;
    private javax.swing.JLabel categoryLbl;
    private javax.swing.JButton clearFilterButton;
    private javax.swing.JTextField dateFromField;
    private javax.swing.JLabel dateLbl;
    private javax.swing.JTextField dateToField;
    private javax.swing.JLabel descriptionLbl;
    private javax.swing.JTextField descriptionTextField;
    private javax.swing.JMenu editMenu;
    private javax.swing.JMenuItem editRowsMenuItem;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JButton filterButton;
    private javax.swing.JLabel filterLbl;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JComboBox<String> parsedComboBox;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JLabel statusLbl;
    private javax.swing.JTable table;
    private javax.swing.JScrollPane tableScrollPane;
    private javax.swing.JLabel toLbl;
    // End of variables declaration//GEN-END:variables
}
