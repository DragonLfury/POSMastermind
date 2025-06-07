/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.lexso.sales;

import com.lexso.connection.DatabaseConnection;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import com.lexso.util.*;
import javax.swing.JDialog;
import javax.swing.*;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author NGD NISSHANKA
 */
public class InventoryManagement extends javax.swing.JPanel {

     private Main invoice;

    public void setInvoice(Main invoice) {
        this.invoice = invoice;
    }
    
    String query;

    private void loadInventory() {

        try {
            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
            dtm.setRowCount(0);
            query = """
                SELECT DISTINCT 
                    p.id, s.id, p.name, s.barcode, c.category_name, s.selling_price, 
                    s.mft, s.exp, s.buying_price, s.qty, s.discount_value, s.discount_type, s.discount_start_date, s.discount_end_date 
                FROM 
                    product p
                INNER JOIN stock s ON p.id = s.product_id
                INNER JOIN category_has_brand chb ON p.category = chb.category_has_brand_id
                INNER JOIN category c ON chb.category_category_id = c.id
                LEFT JOIN grn_item gi ON s.id = gi.stock_id WHERE 1=1
                """;

            ResultSet resultSet = DatabaseConnection.executeSearch(query);
            while (resultSet.next()) {
                Vector vector = new Vector();
                vector.add(resultSet.getString("p.id"));
                vector.add(resultSet.getString("s.id"));
                vector.add(resultSet.getString("name"));
                vector.add(resultSet.getString("barcode"));
                vector.add(resultSet.getString("category_name"));
                vector.add(resultSet.getString("mft"));
                vector.add(resultSet.getString("exp"));
                vector.add(resultSet.getString("selling_price"));
                vector.add(resultSet.getString("buying_price"));

                String discountType = resultSet.getString("s.discount_type");
                String discountValue = resultSet.getString("s.discount_value");
                java.sql.Timestamp startDate = resultSet.getTimestamp("s.discount_start_date");
                java.sql.Timestamp endDate = resultSet.getTimestamp("s.discount_end_date");

                LocalDateTime now = LocalDateTime.now();

                if (startDate == null || endDate == null) {

                    vector.add("0.0");
                } else {
                    LocalDateTime disStartJava = startDate.toLocalDateTime();
                    LocalDateTime disEndJava = endDate.toLocalDateTime();

                    if (now.isAfter(disEndJava) || now.isBefore(disStartJava)) {
                        vector.add("0.0");
                    } else if (discountType != null && !"no_discount".equals(discountType)) {
                        if ("percentage".equals(discountType) && discountValue != null) {
                            vector.add(discountValue + "%");
                        } else if ("fixed".equals(discountType) && discountValue != null) {
                            vector.add("LKR" + discountValue);
                        } else {
                            vector.add("0.0");
                        }
                    } else {
                        vector.add("0.0");
                    }
                }

                vector.add(resultSet.getString("qty"));
                dtm.addRow(vector);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean isValidPrice(String number) {
        try {
            Double.parseDouble(number);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void Search(String searchTerm, String sort, String startPrice, String endPrice, java.util.Date startDate, java.util.Date endDate) {
        try {

            query = """
                SELECT DISTINCT 
                    p.id, s.id, p.name, s.barcode, c.category_name, s.selling_price, 
                                        s.mft, s.exp, s.buying_price, s.qty, s.discount_value, s.discount_type, s.discount_start_date, s.discount_end_date 
                FROM 
                    product p
                INNER JOIN stock s ON p.id = s.product_id
                INNER JOIN category_has_brand chb ON p.category = chb.category_has_brand_id
                INNER JOIN category c ON chb.category_category_id = c.id
                LEFT JOIN grn_item gi ON s.id = gi.stock_id WHERE 1=1
                """;

            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                query += " AND (p.name LIKE '%" + searchTerm + "%' OR s.barcode LIKE '%" + searchTerm + "%' OR s.id LIKE '%" + searchTerm + "%')";

            }

            if (startPrice != null && !startPrice.trim().isEmpty() && endPrice != null && !endPrice.trim().isEmpty()) {

                if (isValidPrice(startPrice) && isValidPrice(endPrice)) {
                    query += " AND s.buying_price BETWEEN " + startPrice + " AND " + endPrice;
                } else {
                    JOptionPane.showMessageDialog(this, "Please enter A valid Price");
                    jTextField1.setText("");
                    jTextField3.setText("");
                }

            }

            if (startDate != null && endDate != null) {

                java.sql.Date sqlStartDate = new java.sql.Date(startDate.getTime());
                java.sql.Date sqlEndDate = new java.sql.Date(endDate.getTime());
                query += " AND s.exp BETWEEN '" + sqlStartDate + "' AND '" + sqlEndDate + "'";
            }

            if (sort != null) {
                sort = sort.trim();
                switch (sort) {
                    case "product (A to Z)":
                        query += " ORDER BY p.name ASC";
                        break;
                    case "product (Z to A)":
                        query += " ORDER BY p.name DESC";
                        break;
                    case "Buying Price Highest":
                        query += " ORDER BY s.buying_price DESC";
                        break;
                    case "Buying Price Lowest":
                        query += " ORDER BY s.buying_price ASC";
                        break;
                    case "EXP Date Nearest":
                        query += " ORDER BY s.exp ASC";
                        break;
                    case "EXP Date furthest":
                        query += " ORDER BY s.exp DESC";
                        break;
                    case "QTY Lowest":
                        query += " ORDER BY s.qty ASC";
                        break;
                    case "QTY Highest":
                        query += " ORDER BY s.qty DESC";
                        break;
                    case "Product ID Highest":
                        query += " ORDER BY p.id DESC";
                        break;
                    case "Product ID Lowest":
                        query += " ORDER BY p.id ASC";
                        break;
                    case "Stock ID Lowest":
                        query += " ORDER BY s.id ASC";
                        break;
                    case "Stock ID Highest":
                        query += " ORDER BY s.id DESC";
                        break;
                    case "Selling Price Lowest":
                        query += " ORDER BY selling_price ASC";
                        break;
                    case "Selling Price Highest":
                        query += " ORDER BY selling_price DESC";
                        break;
                    case "Discount Highest":
                        query += """
        ORDER BY
        CASE 
            WHEN s.discount_type = 'percentage' THEN s.selling_price * (s.discount_value / 100)
            WHEN s.discount_type = 'fixed' THEN s.discount_value
            ELSE 0
        END DESC
        """;
                        break;

                    case "Discount Lowest":
                        query += """
        ORDER BY
        CASE 
            WHEN s.discount_type = 'percentage' THEN s.selling_price * (s.discount_value / 100)
            WHEN s.discount_type = 'fixed' THEN s.discount_value
            ELSE 0
        END ASC
        """;
                        break;

                    case "MFD Furthest":
                        query += " ORDER BY mft DESC";
                        break;
                    case "MFD Nearest":
                        query += " ORDER BY mft ASC";
                        break;
                }
            }

            ResultSet resultSet = DatabaseConnection.executeSearch(query);
            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
            dtm.setRowCount(0);

            while (resultSet.next()) {
                Vector<String> vector = new Vector<>();
                vector.add(resultSet.getString("p.id"));
                vector.add(resultSet.getString("s.id"));
                vector.add(resultSet.getString("name"));
                vector.add(resultSet.getString("barcode"));
                vector.add(resultSet.getString("category_name"));
                vector.add(resultSet.getString("mft"));
                vector.add(resultSet.getString("exp"));
                vector.add(resultSet.getString("selling_price"));
                vector.add(resultSet.getString("buying_price"));
                String discountType = resultSet.getString("s.discount_type");
                String discountValue = resultSet.getString("s.discount_value");
                java.sql.Timestamp startDate1 = resultSet.getTimestamp("s.discount_start_date");
                java.sql.Timestamp endDate1 = resultSet.getTimestamp("s.discount_end_date");

                LocalDateTime now = LocalDateTime.now();

                if (startDate1 == null || endDate1 == null) {
                    vector.add("0.0");
                } else {
                    LocalDateTime disStartJava = startDate1.toLocalDateTime();
                    LocalDateTime disEndJava = endDate1.toLocalDateTime();

                    if (now.isAfter(disEndJava) || now.isBefore(disStartJava)) {
                        vector.add("0.0");
                    } else if (discountType != null && !"no_discount".equals(discountType)) {
                        if ("percentage".equals(discountType) && discountValue != null) {
                            vector.add(discountValue + "%");
                        } else if ("fixed".equals(discountType) && discountValue != null) {
                            vector.add("LKR" + discountValue);
                        } else {
                            vector.add("0.0");
                        }
                    } else {
                        vector.add("0.0");
                    }
                }

                vector.add(resultSet.getString("qty"));
                dtm.addRow(vector);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates new form InventoryManagement
     */
    public InventoryManagement() {
        initComponents();
        loadInventory();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jLabel8 = new javax.swing.JLabel();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jButton3 = new javax.swing.JButton();

        jPanel1.setPreferredSize(new java.awt.Dimension(1000, 700));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(51, 153, 255));
        jLabel3.setText("Inventory Management");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("Search");

        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField2KeyReleased(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setText("Sort By:");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select", "Product ID Highest", "Product ID Lowest", "Stock ID Highest", "Stock ID Lowest", "product (A to Z)", "product (Z to A)", "QTY Lowest", "QTY Highest", "EXP Date Furthest ", "EXP Date Nearest", "MFD Nearest", "MFD Furthest", "Selling Price Highest", "Selling Price Lowest", "Buying Price Highest", "Buying Price Lowest", "Discount Highest", "Discount Lowest" }));
        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Product ID", "Stock ID", "Product", "Barcode no", "product category", "MFD Date", "EXP Date", "Selling Price", "Buying price", "Discount ", "product qty"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTable1FocusGained(evt);
            }
        });
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jLabel4.setForeground(new java.awt.Color(0, 102, 102));
        jLabel4.setText("Track, manage, and update stock levels.");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setText("Price:");

        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setText("to");

        jTextField3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField3KeyReleased(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setText("EXP Date");

        jDateChooser1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser1PropertyChange(evt);
            }
        });
        jDateChooser1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jDateChooser1KeyReleased(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel8.setText("to");

        jDateChooser2.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser2PropertyChange(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(255, 51, 51));
        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton3.setText("Clear All");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 669, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(201, 201, 201)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 356, Short.MAX_VALUE)))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(12, 12, 12)
                                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 326, Short.MAX_VALUE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 135, Short.MAX_VALUE)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8)
                            .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(98, 98, 98)
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addComponent(jLabel3)
                                .addGap(0, 0, 0)
                                .addComponent(jLabel4)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel5)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel6)
                                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(0, 47, Short.MAX_VALUE)))
                .addGap(10, 10, 10)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 493, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(53, 53, 53))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 973, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 745, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyReleased
        String searchTerm = jTextField2.getText();
        String sort = String.valueOf(jComboBox1.getSelectedItem());
        String startPrice = jTextField1.getText();
        String endPrice = jTextField3.getText();
        java.util.Date startDate = jDateChooser1.getDate();
        java.util.Date endDate = jDateChooser2.getDate();

        Search(searchTerm, sort, startPrice, endPrice, startDate, endDate);
    }//GEN-LAST:event_jTextField2KeyReleased

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        String searchTerm = jTextField2.getText();
        String sort = String.valueOf(jComboBox1.getSelectedItem());
        String startPrice = jTextField1.getText();
        String endPrice = jTextField3.getText();
        java.util.Date startDate = jDateChooser1.getDate();
        java.util.Date endDate = jDateChooser2.getDate();

        Search(searchTerm, sort, startPrice, endPrice, startDate, endDate);
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jTable1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTable1FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable1FocusGained

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        String searchTerm = jTextField2.getText();
        String sort = String.valueOf(jComboBox1.getSelectedItem());
        String startPrice = jTextField1.getText();
        String endPrice = jTextField3.getText();
        java.util.Date startDate = jDateChooser1.getDate();
        java.util.Date endDate = jDateChooser2.getDate();

        Search(searchTerm, sort, startPrice, endPrice, startDate, endDate);
    }//GEN-LAST:event_jTextField1KeyReleased

    private void jTextField3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField3KeyReleased
        String searchTerm = jTextField2.getText();
        String sort = String.valueOf(jComboBox1.getSelectedItem());
        String startPrice = jTextField1.getText();
        String endPrice = jTextField3.getText();
        java.util.Date startDate = jDateChooser1.getDate();
        java.util.Date endDate = jDateChooser2.getDate();

        Search(searchTerm, sort, startPrice, endPrice, startDate, endDate);
    }//GEN-LAST:event_jTextField3KeyReleased

    private void jDateChooser1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser1PropertyChange
        String searchTerm = jTextField2.getText();
        String sort = String.valueOf(jComboBox1.getSelectedItem());
        String startPrice = jTextField1.getText();
        String endPrice = jTextField3.getText();
        java.util.Date startDate = jDateChooser1.getDate();
        java.util.Date endDate = jDateChooser2.getDate();

        Search(searchTerm, sort, startPrice, endPrice, startDate, endDate);
    }//GEN-LAST:event_jDateChooser1PropertyChange

    private void jDateChooser1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jDateChooser1KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jDateChooser1KeyReleased

    private void jDateChooser2PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser2PropertyChange
        String searchTerm = jTextField2.getText();
        String sort = String.valueOf(jComboBox1.getSelectedItem());
        String startPrice = jTextField1.getText();
        String endPrice = jTextField3.getText();
        java.util.Date startDate = jDateChooser1.getDate();
        java.util.Date endDate = jDateChooser2.getDate();

        Search(searchTerm, sort, startPrice, endPrice, startDate, endDate);
    }//GEN-LAST:event_jDateChooser2PropertyChange

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        jTextField2.setText("");
        jComboBox1.setSelectedIndex(0);
        jTextField1.setText("");
        jTextField3.setText("");
        jDateChooser1.setDate(null);
        jDateChooser2.setDate(null);

        loadInventory();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
       int row = jTable1.getSelectedRow();

        if (evt.getClickCount() == 2) {
            if (invoice != null) {
               
                String stockID = String.valueOf(jTable1.getValueAt(row, 1));
                String CategoryName = String.valueOf(jTable1.getValueAt(row, 4));
                String productID = String.valueOf(jTable1.getValueAt(row, 3));
                String productName = String.valueOf(jTable1.getValueAt(row, 2));
                String productPrice = String.valueOf(jTable1.getValueAt(row, 7));
                String productAvailableQTY = String.valueOf(jTable1.getValueAt(row, 10));
                String productmfd = String.valueOf(jTable1.getValueAt(row, 5));
                String productexp = String.valueOf(jTable1.getValueAt(row, 6));
                String discount = String.valueOf(jTable1.getValueAt(row, 9));
                invoice.getProductQTY().grabFocus();

                invoice.setStockID(stockID);
                invoice.setCategoryName(CategoryName);
                invoice.setProductID(productID);
                invoice.setDiscount(discount);
                invoice.setProductName(productName);
                invoice.setProductPrice(productPrice);
                invoice.setProductAvailableQTY(productAvailableQTY);
                invoice.setProductMFD(productmfd);
                invoice.setProductEXP(productexp);
                //                this.dispose();
                JDialog parentFrame = (JDialog) SwingUtilities.getWindowAncestor(this);
                if (parentFrame != null) {
                    parentFrame.dispose();  // Close the frame that holds customer.java
                }
            }
        } 
    }//GEN-LAST:event_jTable1MouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton3;
    private javax.swing.JComboBox<String> jComboBox1;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    // End of variables declaration//GEN-END:variables
}
