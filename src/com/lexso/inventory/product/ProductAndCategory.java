/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.lexso.inventory.product;

import java.awt.Color;
import java.awt.Image;
import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import com.lexso.connection.DatabaseConnection;

/**
 *
 * @author hesha
 */
public class ProductAndCategory extends javax.swing.JPanel {

    /**
     * Creates new form NewJPanel
     */
    public ProductAndCategory() {
        initComponents();
        jPanel4.setVisible(false);
        loadBrand();
        loadCategories();
        loadProductToTable();
        loadCategoryHasBrandToTable1();
        jfield3.setEditable(false);
        jfield4.setEditable(false);
        loadCategoryHasBrandToTable("");
        field3.setEditable(false);
        field4.setEditable(false);
        loadBrandToTable("");
        loadCategoryToTable("");
        loadCategoryHasBrandToTable("");
        disableFields();
        disableFields1();
    }
    private static HashMap<String, String> categoryMap = new HashMap<>();
    private static HashMap<String, String> brandMap = new HashMap<>();

//    Create Varible To Toggle Search
    private boolean toggle = true;

    private void resetFilters() {
        jTextField5.setText(""); // name
        jComboBox12.setSelectedIndex(0); // category
        jComboBox17.setSelectedIndex(0); // brand
        jComboBox16.setSelectedIndex(0); // unit
        jComboBox14.setSelectedIndex(0); // status
        jFormattedTextField5.setText(""); // warranty
        jDateChooser3.setDate(null); // createdAt
        jDateChooser4.setDate(null); // updatedAt
        jTextField1.setText("Search Product"); // fallbackSearch
        loadProductToTable();
    }

    //    Load categories
    private void loadCategories() {

        try {

            ResultSet rs = DatabaseConnection.executeSearch("Select * FROM `category`");

            Vector<String> vector = new Vector<>();
            vector.add("Select");

            while (rs.next()) {
                vector.add(rs.getString("category_name"));
                categoryMap.put(rs.getString("category_name"), rs.getString("id"));

            }
            DefaultComboBoxModel model = new DefaultComboBoxModel(vector);
            jComboBox12.setModel(model);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

//    Load Brands
    private void loadBrand() {

        String selected = String.valueOf(jComboBox12.getSelectedItem());
        String catId = "";

        try {
            ResultSet rs;

            if (selected.equals("Select")) {

                rs = DatabaseConnection.executeSearch("Select * FROM brand INNER JOIN category_has_brand "
                        + "ON category_has_brand.brand_id=brand.id");
            } else {

                ResultSet rs1 = DatabaseConnection.executeSearch("SELECT * FROM `category` WHERE category_name='" + selected + "'");
                if (rs1.next()) {
                    catId = rs1.getString("id");
                }
                rs = DatabaseConnection.executeSearch("Select * FROM brand INNER JOIN category_has_brand "
                        + "ON category_has_brand.brand_id=brand.id WHERE category_has_brand.category_category_id='" + catId + "' ");

            }
            Vector<String> vector = new Vector<>();
            vector.add("Select");

            while (rs.next()) {
                vector.add(rs.getString("name"));
                brandMap.put(rs.getString("name"), rs.getString("id"));

            }
            DefaultComboBoxModel model = new DefaultComboBoxModel(vector);
            jComboBox17.setModel(model);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void loadProductToTable() {
        String fallbackSearch = jTextField1.getText().trim();
        boolean isFallback = !fallbackSearch.equals("") && !fallbackSearch.equals("Search Product");

        String name = jTextField5.getText().trim();
        String category = String.valueOf(jComboBox12.getSelectedItem());
        String brand = String.valueOf(jComboBox17.getSelectedItem());
        String unit = String.valueOf(jComboBox16.getSelectedItem());
        String status = String.valueOf(jComboBox14.getSelectedItem());
        String warranty = jFormattedTextField5.getText().trim();
        Date createdAtDate = jDateChooser3.getDate();
        Date updatedAtDate = jDateChooser4.getDate();

        String selectedSort = jComboBox2.getSelectedItem().toString();
        String orderBy = getOrderByClause(selectedSort);

        try {
            ResultSet rs;

            if (isFallback) {
                // Run fallback query
                rs = DatabaseConnection.executeSearch("SELECT * FROM `product` "
                        + "INNER JOIN `category_has_brand` ON `product`.`category` = `category_has_brand`.`category_has_brand_id` "
                        + "INNER JOIN `category` ON `category`.`id` = `category_has_brand`.`category_category_id` "
                        + "INNER JOIN `brand` ON `brand`.`id` = `category_has_brand`.`brand_id` "
                        + "WHERE `product`.`name` LIKE '%" + fallbackSearch + "%' ORDER BY " + orderBy);
            } else {
                // Build your filter query here using values like name, category, brand, etc.
                String query = "SELECT * FROM `product` "
                        + "INNER JOIN `category_has_brand` ON `product`.`category` = `category_has_brand`.`category_has_brand_id` "
                        + "INNER JOIN `category` ON `category`.`id` = `category_has_brand`.`category_category_id` "
                        + "INNER JOIN `brand` ON `brand`.`id` = `category_has_brand`.`brand_id` "
                        + "WHERE 1=1";

                if (!name.isEmpty()) {
                    query += " AND `product`.`name` LIKE '%" + name + "%'";
                }
                if (!category.equals("Select")) {
                    query += " AND `category`.`category_name` = '" + category + "'";
                }
                if (!brand.equals("Select")) {
                    query += " AND `brand`.`name` = '" + brand + "'";
                }
                if (!unit.equals("Select")) {
                    query += " AND `unit` = '" + unit + "'";
                }
                if (!status.equals("Select")) {
                    query += " AND `status` = '" + status + "'";
                }
                if (!warranty.isEmpty()) {
                    query += " AND `warranty` = '" + warranty + "'";
                }
                if (createdAtDate != null) {
                    query += " AND DATE(`product`.`created_at`) = '" + new java.sql.Date(createdAtDate.getTime()) + "'";
                }
                if (updatedAtDate != null) {
                    query += " AND DATE(`product`.`updated_at`) = '" + new java.sql.Date(updatedAtDate.getTime()) + "'";
                }

                query += " ORDER BY " + orderBy;

                rs = DatabaseConnection.executeSearch(query);
            }

            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.setRowCount(0);

            while (rs.next()) {
                Vector<String> vector = new Vector<>();
                vector.add(rs.getString("product.id"));
                vector.add(rs.getString("product.name"));
                vector.add(rs.getString("category.category_name"));
                vector.add(rs.getString("brand.name"));
                vector.add(rs.getString("unit"));
                vector.add(rs.getString("created_at"));
                vector.add(rs.getString("updated_at"));
                vector.add(rs.getString("status"));
                model.addRow(vector);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getOrderByClause(String sortOption) {
        switch (sortOption) {
            case "Id ASC":
                return "product.id ASC";
            case "Id DESC":
                return "product.id DESC";
            case "Name ASC":
                return "product.name ASC";
            case "Name DESC":
                return "product.name DESC";
            case "Category ASC":
                return "category.category_name ASC";
            case "Category DESC":
                return "category.category_name DESC";
            case "Brand ASC":
                return "brand.name ASC";
            case "Brand DESC":
                return "brand.name DESC";
            case "Unit ASC":
                return "product.unit ASC";
            case "Unit DESC":
                return "product.unit DESC";
            case "Created at ASC":
                return "product.created_at ASC";
            case "Created at DESC":
                return "product.created_at DESC";
            case "Updated at ASC":
                return "product.updated_at ASC";
            case "Updated at DESC":
                return "product.updated_at DESC";
            case "Status ASC":
                return "product.status ASC";
            case "Status DESC":
                return "product.status DESC";
            default:
                return "product.id ASC"; // default sorting
        }
    }

    private String id;
    private String brandId;
    private String catId;

    private void enableFields() {
        field1.setEditable(true);

    }

    private void disableFields() {
        field1.setEditable(false);

    }

    private void enableFields1() {
        field2.setEditable(true);

    }

    private void disableFields1() {
        field2.setEditable(false);

    }

    private void loadBrandToTable(String name) {

        try {

            ResultSet rs = DatabaseConnection.executeSearch("SELECT * FROM `brand` WHERE `name` LIKE '%" + name + "%' ORDER BY `id` ASC");

            DefaultTableModel model = (DefaultTableModel) jTable3.getModel();
            model.setRowCount(0);

            while (rs.next()) {
                Vector<String> vector = new Vector<>();

                vector.add(rs.getString("id"));
                vector.add(rs.getString("name"));

                model.addRow(vector);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void reset() {
        field3.setText("");
        field4.setText("");
        id = null;

    }

    public JTextField getField3() {
        return field3;
    }

    public JTextField getField4() {
        return field4;
    }

//   
    private void loadCategoryHasBrandToTable(String searchText) {
        try {
            ResultSet rs = DatabaseConnection.executeSearch("SELECT * FROM `category_has_brand`");

            DefaultTableModel model = (DefaultTableModel) jTable4.getModel();
            model.setRowCount(0);

            while (rs.next()) {
                String categoryId = rs.getString("category_category_id");
                String brandId = rs.getString("brand_id");

                ResultSet rs2 = DatabaseConnection.executeSearch("SELECT * FROM `category` WHERE `id`='" + categoryId + "'");
                if (rs2.next()) {
                    String categoryName = rs2.getString("category_name");

                    ResultSet rs3 = DatabaseConnection.executeSearch("SELECT * FROM `brand` WHERE `id`='" + brandId + "'");
                    if (rs3.next()) {
                        String brandName = rs3.getString("name");

                        // Check if either category or brand name contains the search text
                        if (categoryName.toLowerCase().contains(searchText.toLowerCase())
                                || brandName.toLowerCase().contains(searchText.toLowerCase())
                                || searchText.trim().isEmpty()) {

                            Vector<String> vector = new Vector<>();
                            vector.add(rs.getString("category_has_brand_id"));
                            vector.add(categoryName);
                            vector.add(brandName);

                            model.addRow(vector);
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadCategoryToTable(String name) {

        try {

            ResultSet rs = DatabaseConnection.executeSearch("SELECT * FROM `category` WHERE `category_name` LIKE '%" + name + "%' ORDER BY `id` ASC");

            DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
            model.setRowCount(0);

            while (rs.next()) {
                Vector<String> vector = new Vector<>();

                vector.add(rs.getString("id"));
                vector.add(rs.getString("category_name"));

                model.addRow(vector);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void brandReset() {
        field2.setText("");
        disableFields1();

    }

    private void catReset() {
        field1.setText("");
        disableFields();
    }

    private void reset1() {
        jfield1.setText("");
        jfield2.setText("");
        jfield3.setText("");
        jfield4.setText("");

    }

    public JTextField getJField3() {
        return jfield3;
    }

    public JTextField getJField4() {
        return jfield4;
    }

    private void loadCategoryHasBrandToTable1() {

        try {

            ResultSet rs = DatabaseConnection.executeSearch("SELECT * FROM `category_has_brand`");
//                    + "INNER JOIN `category` ON  `category`.`id`=`categorie_has_brand`.`categories_category_id` "
//                    + "INNER JOIN `brand` ON `brand`.`id`=`categorie_has_brand`.`brand_id`");

            DefaultTableModel model = (DefaultTableModel) jtable1.getModel();
            model.setRowCount(0);

            while (rs.next()) {

                String category = rs.getString("category_category_id");
                String brand = rs.getString("brand_id");

                ResultSet rs2 = DatabaseConnection.executeSearch("SELECT * FROM `category` WHERE `id`='" + category + "'");
                if (rs2.next()) {
                    String categoryName = rs2.getString("category_name");

                    ResultSet rs3 = DatabaseConnection.executeSearch("SELECT * FROM `brand` WHERE `id`='" + brand + "'");
                    if (rs3.next()) {
                        String brandName = rs3.getString("name");

                        Vector<String> vector = new Vector<>();

                        vector.add(rs.getString("category_has_brand_id"));
                        vector.add(categoryName);
                        vector.add(brandName);

                        model.addRow(vector);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jComboBox2 = new javax.swing.JComboBox<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jDateChooser3 = new com.toedter.calendar.JDateChooser();
        jLabel24 = new javax.swing.JLabel();
        jDateChooser4 = new com.toedter.calendar.JDateChooser();
        jLabel30 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jComboBox16 = new javax.swing.JComboBox<>();
        jLabel25 = new javax.swing.JLabel();
        jComboBox14 = new javax.swing.JComboBox<>();
        jLabel23 = new javax.swing.JLabel();
        jComboBox12 = new javax.swing.JComboBox<>();
        jLabel21 = new javax.swing.JLabel();
        jFormattedTextField5 = new javax.swing.JFormattedTextField();
        jLabel7 = new javax.swing.JLabel();
        jComboBox17 = new javax.swing.JComboBox<>();
        jLabel26 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel6 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jfield1 = new javax.swing.JTextField();
        jbutton1 = new javax.swing.JButton();
        jbutton = new javax.swing.JButton();
        jfield2 = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jbutton2 = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jfield3 = new javax.swing.JTextField();
        jbutton3 = new javax.swing.JButton();
        jLabel32 = new javax.swing.JLabel();
        jfield4 = new javax.swing.JTextField();
        jbutton4 = new javax.swing.JButton();
        jbutton5 = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        jtable1 = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        Search1 = new javax.swing.JTextField();
        field1 = new javax.swing.JTextField();
        btn1 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jLabel11 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        search3 = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        field3 = new javax.swing.JTextField();
        btn3 = new javax.swing.JButton();
        field4 = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        btn4 = new javax.swing.JButton();
        btn5 = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        btn = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        search2 = new javax.swing.JTextField();
        field2 = new javax.swing.JTextField();
        btn2 = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setLayout(new java.awt.BorderLayout());

        jTabbedPane1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jButton1.setBackground(new java.awt.Color(0, 51, 255));
        jButton1.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Add New Products");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(255, 153, 51));
        jButton2.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Edit Products");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jTextField1.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jTextField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField1FocusLost(evt);
            }
        });
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(0, 51, 255));
        jButton3.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Advanced");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jComboBox2.setFont(new java.awt.Font("Poppins", 0, 14)); // NOI18N
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Id ASC", "Id DESC", "Name ASC", "Name DESC", "Category ASC", "Category DESC", "Brand ASC", "Brand DESC", "Unit ASC", "Unit DESC", "Created at ASC", "Created at DESC", "Updated at ASC", "Updated at DESC", "Status ASC", "Status DESC" }));
        jComboBox2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox2ItemStateChanged(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Product ID", "Name", "Category ", "Brand", "Unit ", "Created At", "Updated At", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTable1);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jDateChooser3.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser3PropertyChange(evt);
            }
        });

        jLabel24.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        jLabel24.setText("Add Date");

        jDateChooser4.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser4PropertyChange(evt);
            }
        });

        jLabel30.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        jLabel30.setText("Update Date");

        jTextField5.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jTextField5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField5ActionPerformed(evt);
            }
        });
        jTextField5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField5KeyReleased(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        jLabel19.setText("Name");

        jComboBox16.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jComboBox16.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select", "kg", "liter", "piece", "pack", "gram", "ml" }));
        jComboBox16.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox16ItemStateChanged(evt);
            }
        });
        jComboBox16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox16ActionPerformed(evt);
            }
        });

        jLabel25.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        jLabel25.setText("Unit");

        jComboBox14.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jComboBox14.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select", "active", "inactive" }));
        jComboBox14.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox14ItemStateChanged(evt);
            }
        });

        jLabel23.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        jLabel23.setText("Status");

        jComboBox12.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jComboBox12.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox12.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox12ItemStateChanged(evt);
            }
        });

        jLabel21.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        jLabel21.setText("Category");

        jFormattedTextField5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jFormattedTextField5KeyReleased(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        jLabel7.setText("Warranty Period (Months)");

        jComboBox17.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jComboBox17.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select", "kg", "liter", "piece", "pack", "gram", "ml" }));
        jComboBox17.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox17ItemStateChanged(evt);
            }
        });

        jLabel26.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        jLabel26.setText("Brand");

        jButton4.setBackground(new java.awt.Color(0, 51, 255));
        jButton4.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("Close");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel24)
                    .addComponent(jTextField5)
                    .addComponent(jComboBox16, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jDateChooser3, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE))
                .addGap(55, 55, 55)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel30)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jDateChooser4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                            .addComponent(jComboBox14, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBox12, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel21, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel23, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(55, 55, 55)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel26)
                                    .addComponent(jComboBox17, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel7)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addGap(56, 56, 56)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jFormattedTextField5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton4, javax.swing.GroupLayout.Alignment.TRAILING))))))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel26))
                    .addComponent(jLabel21, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox17, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel25)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel23)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jComboBox14, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jComboBox16, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel24)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jDateChooser3, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel30)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addComponent(jDateChooser4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addGap(14, 14, 14))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(jFormattedTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 590, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jButton3)
                                .addGap(18, 18, 18)
                                .addComponent(jComboBox2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(25, 25, 25))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(89, 89, 89))
        );

        jTabbedPane1.addTab("Proudcts", jPanel2);

        jLabel9.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabel9.setText("Add Category");

        jLabel28.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        jLabel28.setText("Name");

        jfield1.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jfield1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jfield1ActionPerformed(evt);
            }
        });

        jbutton1.setBackground(new java.awt.Color(0, 0, 255));
        jbutton1.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        jbutton1.setForeground(new java.awt.Color(255, 255, 255));
        jbutton1.setText("Add");
        jbutton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbutton1ActionPerformed(evt);
            }
        });

        jbutton.setBackground(new java.awt.Color(0, 255, 0));
        jbutton.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        jbutton.setForeground(new java.awt.Color(255, 255, 255));
        jbutton.setText("Clear");
        jbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbuttonActionPerformed(evt);
            }
        });

        jfield2.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jfield2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jfield2ActionPerformed(evt);
            }
        });

        jLabel29.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        jLabel29.setText("Name");

        jLabel10.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabel10.setText("Add Brand");

        jbutton2.setBackground(new java.awt.Color(0, 0, 255));
        jbutton2.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        jbutton2.setForeground(new java.awt.Color(255, 255, 255));
        jbutton2.setText("Add");
        jbutton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbutton2ActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabel12.setText("Set Brand to category");

        jLabel31.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        jLabel31.setText("Category");

        jfield3.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jfield3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jfield3ActionPerformed(evt);
            }
        });

        jbutton3.setBackground(new java.awt.Color(0, 0, 255));
        jbutton3.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        jbutton3.setForeground(new java.awt.Color(255, 255, 255));
        jbutton3.setText("Add");
        jbutton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbutton3ActionPerformed(evt);
            }
        });

        jLabel32.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        jLabel32.setText("Brand");

        jfield4.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jfield4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jfield4ActionPerformed(evt);
            }
        });

        jbutton4.setBackground(new java.awt.Color(0, 0, 255));
        jbutton4.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        jbutton4.setForeground(new java.awt.Color(255, 255, 255));
        jbutton4.setText("Add");
        jbutton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbutton4ActionPerformed(evt);
            }
        });

        jbutton5.setBackground(new java.awt.Color(0, 0, 255));
        jbutton5.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        jbutton5.setForeground(new java.awt.Color(255, 255, 255));
        jbutton5.setText("Save");
        jbutton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbutton5ActionPerformed(evt);
            }
        });

        jtable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Id", "Category", "Brand"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane5.setViewportView(jtable1);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jfield2)
                    .addComponent(jfield1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jbutton1, javax.swing.GroupLayout.DEFAULT_SIZE, 392, Short.MAX_VALUE)
                    .addComponent(jbutton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jfield3, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbutton3))
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel31)
                        .addGap(497, 497, 497)
                        .addComponent(jLabel32)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jbutton4))
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jLabel29)
                .addContainerGap(908, Short.MAX_VALUE))
            .addComponent(jbutton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jfield4, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel28)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jbutton, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane5)))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbutton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jfield1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbutton1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jfield2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbutton2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jbutton3, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jfield3, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jfield4, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jbutton4, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jbutton5, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane2.addTab("Add", jPanel6);

        jLabel13.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabel13.setText("Update Category");

        jLabel14.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        jLabel14.setText("Search");

        Search1.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        Search1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Search1ActionPerformed(evt);
            }
        });
        Search1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                Search1KeyReleased(evt);
            }
        });

        field1.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        field1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                field1ActionPerformed(evt);
            }
        });
        field1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                field1KeyReleased(evt);
            }
        });

        btn1.setBackground(new java.awt.Color(0, 0, 255));
        btn1.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btn1.setForeground(new java.awt.Color(255, 255, 255));
        btn1.setText("Update");
        btn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn1ActionPerformed(evt);
            }
        });

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Id", "Category Name"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable2MouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(jTable2);

        jLabel11.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabel11.setText("Update Brand to category");

        jLabel22.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        jLabel22.setText("Search");

        search3.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        search3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                search3ActionPerformed(evt);
            }
        });
        search3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                search3KeyPressed(evt);
            }
        });

        jLabel20.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        jLabel20.setText("Category");

        field3.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        field3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                field3ActionPerformed(evt);
            }
        });

        btn3.setBackground(new java.awt.Color(0, 0, 255));
        btn3.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btn3.setForeground(new java.awt.Color(255, 255, 255));
        btn3.setText("Add");
        btn3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn3ActionPerformed(evt);
            }
        });

        field4.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        field4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                field4ActionPerformed(evt);
            }
        });

        jLabel27.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        jLabel27.setText("Brand");

        btn4.setBackground(new java.awt.Color(0, 0, 255));
        btn4.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btn4.setForeground(new java.awt.Color(255, 255, 255));
        btn4.setText("Add");
        btn4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn4ActionPerformed(evt);
            }
        });

        btn5.setBackground(new java.awt.Color(0, 0, 255));
        btn5.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btn5.setForeground(new java.awt.Color(255, 255, 255));
        btn5.setText("Save");
        btn5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn5ActionPerformed(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabel16.setText("Update Brand");

        btn.setBackground(new java.awt.Color(0, 0, 255));
        btn.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btn.setForeground(new java.awt.Color(255, 255, 255));
        btn.setText("Clear All");
        btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActionPerformed(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        jLabel15.setText("Search");

        search2.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        search2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                search2ActionPerformed(evt);
            }
        });
        search2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                search2KeyReleased(evt);
            }
        });

        field2.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        field2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                field2ActionPerformed(evt);
            }
        });
        field2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                field2KeyReleased(evt);
            }
        });

        btn2.setBackground(new java.awt.Color(0, 0, 255));
        btn2.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        btn2.setForeground(new java.awt.Color(255, 255, 255));
        btn2.setText("Update");
        btn2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn2ActionPerformed(evt);
            }
        });

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Id", "Brand Name"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable3MouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(jTable3);

        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Id", "Category", "Brand"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable4MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable4);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel20)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel22))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(field4, javax.swing.GroupLayout.PREFERRED_SIZE, 352, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(12, 12, 12)
                                .addComponent(btn4, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel27)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                                .addComponent(field3, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btn3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(search3, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                        .addComponent(field1, javax.swing.GroupLayout.PREFERRED_SIZE, 354, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(btn1))
                                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(Search1, javax.swing.GroupLayout.PREFERRED_SIZE, 354, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btn, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(search2, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(field2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btn2, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))))))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(search2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Search1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(field1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btn1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(field2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btn2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                .addComponent(btn)
                                .addGap(149, 149, 149))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(105, 105, 105)))))
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(search3, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(field3, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn3, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(field4, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn4, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btn5, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(51, 51, 51))
        );

        jTabbedPane2.addTab("Edit", jPanel5);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 940, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(30, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 686, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(10, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Category And Brands ", jPanel3);

        jLabel6.setFont(new java.awt.Font("Poppins", 1, 24)); // NOI18N
        jLabel6.setText("Product Management");

        jLabel1.setText("Controlling All Product Data Operations");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(24, 24, 24)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 745, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox17ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox17ItemStateChanged
        loadProductToTable();        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox17ItemStateChanged

    private void jFormattedTextField5KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextField5KeyReleased
        if (!jFormattedTextField5.getText().matches("\\d*")) { // Allow only digits
            JOptionPane.showMessageDialog(
                    null,
                    "Please enter a valid number.",
                    "Invalid Input",
                    JOptionPane.WARNING_MESSAGE
            );

            // Clear the text field
            jFormattedTextField5.setText("");

            loadProductToTable();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_jFormattedTextField5KeyReleased

    private void jComboBox12ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox12ItemStateChanged
        loadProductToTable();
        loadBrand();
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox12ItemStateChanged

    private void jComboBox14ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox14ItemStateChanged
        loadProductToTable();        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox14ItemStateChanged

    private void jComboBox16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox16ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox16ActionPerformed

    private void jComboBox16ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox16ItemStateChanged
        loadProductToTable();        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox16ItemStateChanged

    private void jTextField5KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField5KeyReleased
        loadProductToTable();        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField5KeyReleased

    private void jTextField5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField5ActionPerformed

    private void jDateChooser4PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser4PropertyChange
        loadProductToTable();        // TODO add your handling code here:
    }//GEN-LAST:event_jDateChooser4PropertyChange

    private void jDateChooser3PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser3PropertyChange
        loadProductToTable();        // TODO add your handling code here:
    }//GEN-LAST:event_jDateChooser3PropertyChange

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked

        int row = jTable1.getSelectedRow();
        String id = String.valueOf(jTable1.getValueAt(row, 0));
        try {
            ResultSet rs = DatabaseConnection.executeSearch("SELECT * FROM product WHERE id='" + id + "'");
            if (rs.next()) {
                if (evt.getClickCount() == 2) {

                    String productName = String.valueOf(jTable1.getValueAt(row, 1));
                    String category = String.valueOf(jTable1.getValueAt(row, 2));
                    String brand = String.valueOf(jTable1.getValueAt(row, 3));
                    String unit = String.valueOf(jTable1.getValueAt(row, 4));
                    String status = String.valueOf(jTable1.getValueAt(row, 7));
                    String warrntyPeriod = rs.getString("warranty_period");
                    String image = rs.getString("product_image");
                    String description = rs.getString("product_description");
                    String createdAt = String.valueOf(jTable1.getValueAt(row, 5));
                    String updatedAt = String.valueOf(jTable1.getValueAt(row, 6));

                    SingleProduct.getInstance(this).getJLabel6().setText(id);
                    SingleProduct.getInstance(this).getJLabel3().setText(productName);
                    SingleProduct.getInstance(this).getJLabel25().setText(category);
                    SingleProduct.getInstance(this).getJLabel27().setText(brand);
                    SingleProduct.getInstance(this).getJLabel30().setText(unit);
                    SingleProduct.getInstance(this).getJLabel32().setText(status);
                    SingleProduct.getInstance(this).getJLabel33().setText(warrntyPeriod);
                    SingleProduct.getInstance(this).getJLabel36().setText(createdAt);
                    SingleProduct.getInstance(this).getJLabel38().setText(updatedAt);
                    SingleProduct.getInstance(this).getJLabel39().setText(description);

                    System.out.println(image);
                    ImageIcon icon = new ImageIcon("src/product-image/" + image);
                    System.out.println("src/product-image/" + image);

                    // Resize the image to fit JLabel size
                    Image img = icon.getImage();
                    int width = SingleProduct.getInstance(this).getJLabel1().getWidth();
                    int height = SingleProduct.getInstance(this).getJLabel1().getHeight();

                    Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                    ImageIcon scaledIcon = new ImageIcon(scaledImg);

                    if (status.equalsIgnoreCase("active")) {

                        SingleProduct.getInstance(this).getJButton1().setEnabled(false);
                        SingleProduct.getInstance(this).getJButton2().setEnabled(true);
                        SingleProduct.getInstance(this).getJLabel32().setForeground(Color.GREEN);

                    } else {
                        SingleProduct.getInstance(this).getJButton1().setEnabled(true);
                        SingleProduct.getInstance(this).getJButton2().setEnabled(false);
                        SingleProduct.getInstance(this).getJLabel32().setForeground(Color.RED);

                    }
                    // Set icon to JLabel
                    SingleProduct.getInstance(this).getJLabel1().setIcon(scaledIcon);
                    SingleProduct.getInstance(this).setVisible(true);

                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jComboBox2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox2ItemStateChanged
        loadProductToTable();        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox2ItemStateChanged

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        if (toggle) {
            jPanel4.setVisible(true);
            jTextField1.setEnabled(false);
            resetFilters();

        }
        toggle = !toggle;
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        loadProductToTable();        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1KeyReleased

    private void jTextField1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField1FocusLost
        if (jTextField1.getText().trim().isEmpty()) {
            jTextField1.setText("Search Product");
            jTextField1.setForeground(Color.GRAY);
        }
    }//GEN-LAST:event_jTextField1FocusLost

    private void jTextField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField1FocusGained
        if (jTextField1.getText().equals("Search Product")) {
            jTextField1.setText("");
            jTextField1.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_jTextField1FocusGained

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        new EditProduct().setVisible(true);        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        new AddProduct().setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        jPanel4.setVisible(false);
        jTextField1.setEnabled(true);
        resetFilters();
        toggle = !toggle;

        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

    private void Search1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Search1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Search1ActionPerformed

    private void Search1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Search1KeyReleased
        loadCategoryToTable(Search1.getText());        // TODO add your handling code here:
    }//GEN-LAST:event_Search1KeyReleased

    private void field1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_field1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_field1ActionPerformed

    private void field1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_field1KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_field1KeyReleased

    private void btn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn1ActionPerformed
        String category = field1.getText();
        if (category.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please select category to update", "Warning", JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                DatabaseConnection.executeIUD("UPDATE category SET category_name='" + category + "' WHERE id='" + catId + "'");
                JOptionPane.showMessageDialog(null, "Update Successfully", "Warning", JOptionPane.INFORMATION_MESSAGE);
                disableFields();
                catReset();
                loadCategoryToTable("");

            } catch (Exception ex) {

                ex.printStackTrace();
            }
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_btn1ActionPerformed

    private void jTable2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable2MouseClicked
        int row = jTable2.getSelectedRow();

        try {
            catId = String.valueOf(jTable2.getValueAt(row, 0));
            String category = String.valueOf(jTable2.getValueAt(row, 1));
            enableFields();
            field1.setText(category);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jTable2MouseClicked

    private void search3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_search3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_search3ActionPerformed

    private void search3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_search3KeyPressed
        loadCategoryHasBrandToTable(search3.getText());        // TODO add your handling code here:
    }//GEN-LAST:event_search3KeyPressed

    private void field3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_field3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_field3ActionPerformed

    private void btn3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn3ActionPerformed
        CategoryTable2 ct = new CategoryTable2(this);
        ct.setVisible(true);

        // TODO add your handling code here:
    }//GEN-LAST:event_btn3ActionPerformed

    private void field4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_field4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_field4ActionPerformed

    private void btn4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn4ActionPerformed
        BrandTable2 bt = new BrandTable2(this);
        bt.setVisible(true);
    }//GEN-LAST:event_btn4ActionPerformed

    private void btn5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn5ActionPerformed
        String category = field3.getText();
        String brand = field4.getText();

        if (id == null) {
            JOptionPane.showMessageDialog(null, "Please select both category and brand to Update", "Warning", JOptionPane.WARNING_MESSAGE);
            return;

        }

        if (category.isEmpty() || brand.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please select both category and brand to Update", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            ResultSet rs = DatabaseConnection.executeSearch("SELECT * FROM category WHERE category_name='" + category + "'");
            if (rs.next()) {
                category = rs.getString("id");
            }
            ResultSet rs1 = DatabaseConnection.executeSearch("SELECT * FROM brand WHERE name='" + brand + "'");
            if (rs1.next()) {
                brand = rs1.getString("id");
            }

            ResultSet rs2 = DatabaseConnection.executeSearch("SELECT * FROM category_has_brand WHERE "
                    + "category_category_id='" + category + "' AND brand_id='" + brand + "'");

            if (rs2.next()) {
                JOptionPane.showMessageDialog(null, "Already Registered !", "Infomation", JOptionPane.WARNING_MESSAGE);
                reset();
            } else {
                DatabaseConnection.executeIUD("UPDATE category_has_brand SET category_category_id=" + category + " , brand_id='" + brand + "' WHERE"
                        + " category_has_brand_id='" + id + "'");
                JOptionPane.showMessageDialog(null, "Category Successfully set to the Brand !", "Infomation", JOptionPane.INFORMATION_MESSAGE);
                reset();
                loadCategoryHasBrandToTable("");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btn5ActionPerformed

    private void btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActionPerformed
        jTable1.clearSelection();
        jTable2.clearSelection();
        jTable3.clearSelection();
        field1.setText("");
        field2.setText("");
        field2.setText("");
        search2.setText("");
        field3.setText("");

        field4.setText("");
        Search1.setText("");
        field2.setEditable(false);
        field1.setEditable(false);
        search3.setText("");
        id = null;
        catId = null;
        brandId = null;

        // TODO add your handling code here:
    }//GEN-LAST:event_btnActionPerformed

    private void search2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_search2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_search2ActionPerformed

    private void search2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_search2KeyReleased
        loadBrandToTable(search2.getText());        // TODO add your handling code here:
        // TODO add your handling code here:
    }//GEN-LAST:event_search2KeyReleased

    private void field2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_field2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_field2ActionPerformed

    private void field2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_field2KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_field2KeyReleased

    private void btn2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn2ActionPerformed
        String brand = field2.getText();
        if (brand.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please select brand to update", "Warning", JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                DatabaseConnection.executeIUD("UPDATE brand SET name='" + brand + "' WHERE id='" + brandId + "'");
                JOptionPane.showMessageDialog(null, "Update Successfully", "Warning", JOptionPane.INFORMATION_MESSAGE);
                disableFields1();
                brandReset();
                loadBrandToTable("");

            } catch (Exception ex) {

                ex.printStackTrace();
            }
        }        // TODO add your handling code here:
    }//GEN-LAST:event_btn2ActionPerformed

    private void jTable3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable3MouseClicked
        int row = jTable3.getSelectedRow();

        try {
            brandId = String.valueOf(jTable3.getValueAt(row, 0));
            String brand = String.valueOf(jTable3.getValueAt(row, 1));
            enableFields1();

            field2.setText(brand);
        } catch (Exception e) {
            e.printStackTrace();
        }    // TODO add your handling code here:
    }//GEN-LAST:event_jTable3MouseClicked

    private void jTable4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable4MouseClicked

        int row = jTable4.getSelectedRow();
        if (evt.getClickCount() == 2) {
            String category = String.valueOf(jTable4.getValueAt(row, 1));
            String brand = String.valueOf(jTable4.getValueAt(row, 2));
            String categoryId = "";
            String brandId = "";

            field3.setText(category);
            field4.setText(brand);
            try {
                ResultSet rs = DatabaseConnection.executeSearch("SELECT * FROM category WHERE category_name='" + category + "'");
                if (rs.next()) {
                    categoryId = rs.getString("id");
                }
                ResultSet rs1 = DatabaseConnection.executeSearch("SELECT * FROM brand WHERE name='" + brand + "'");
                if (rs1.next()) {
                    brandId = rs1.getString("id");
                }

                ResultSet rs3 = DatabaseConnection.executeSearch("SELECT * FROM category_has_brand WHERE category_category_id='" + categoryId + "' AND "
                        + "brand_id='" + brandId + "'");

                if (rs3.next()) {
                    id = rs3.getString("category_has_brand_id");
                    System.out.println(id);
                } else {
                    JOptionPane.showMessageDialog(null, "Something went wrong", "Warning", JOptionPane.ERROR_MESSAGE);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            field3.setText(category);
            field4.setText(brand);

        }// TODO add your handling code here:
    }//GEN-LAST:event_jTable4MouseClicked

    private void jfield1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jfield1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jfield1ActionPerformed

    private void jbutton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbutton1ActionPerformed
        try {

            String category = jfield1.getText().trim();

            if (category.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter a category name", "Validation Error", JOptionPane.ERROR_MESSAGE);
            } else if (category.length() > 25) {
                JOptionPane.showMessageDialog(null, "Category name must be less than 25 chracters", "Validation Error", JOptionPane.ERROR_MESSAGE);
            } else {

                ResultSet rs = DatabaseConnection.executeSearch("SELECT * FROM `category` WHERE `category_name`='" + category + "'");

                if (rs.next()) {

                    JOptionPane.showMessageDialog(null, "Category already added!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    reset1();

                } else {
                    DatabaseConnection.executeIUD("INSERT INTO `category`(category_name) VALUE('" + category + "')");
                    JOptionPane.showMessageDialog(null, "New Category Added Successfully!", "Validation Error", JOptionPane.INFORMATION_MESSAGE);
                    reset1();

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // TODO add your handling code here:
    }//GEN-LAST:event_jbutton1ActionPerformed

    private void jbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbuttonActionPerformed
        reset1();        // TODO add your handling code here:
    }//GEN-LAST:event_jbuttonActionPerformed

    private void jfield2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jfield2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jfield2ActionPerformed

    private void jbutton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbutton2ActionPerformed
        try {

            String brand = jfield2.getText().trim();

            if (brand.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter a brand name", "Validation Error", JOptionPane.ERROR_MESSAGE);
            } else if (brand.length() > 25) {
                JOptionPane.showMessageDialog(null, "Brand name must be less than 25 chracters", "Validation Error", JOptionPane.ERROR_MESSAGE);
            } else {

                ResultSet rs = DatabaseConnection.executeSearch("SELECT * FROM `brand` WHERE `name`='" + brand + "'");

                if (rs.next()) {

                    JOptionPane.showMessageDialog(null, "Brand already added!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    reset1();

                } else {
                    DatabaseConnection.executeIUD("INSERT INTO `brand`(`name`) VALUE('" + brand + "')");
                    JOptionPane.showMessageDialog(null, "New  Brand Added Successfully!", "Validation Error", JOptionPane.INFORMATION_MESSAGE);
                    reset1();

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_jbutton2ActionPerformed

    private void jfield3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jfield3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jfield3ActionPerformed

    private void jbutton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbutton3ActionPerformed
        CategoryTable ct = new CategoryTable(this);
        ct.setVisible(true);

        // TODO add your handling code here:
    }//GEN-LAST:event_jbutton3ActionPerformed

    private void jfield4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jfield4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jfield4ActionPerformed

    private void jbutton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbutton4ActionPerformed
        BrandTable bt = new BrandTable(this);
        bt.setVisible(true);
    }//GEN-LAST:event_jbutton4ActionPerformed

    private void jbutton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbutton5ActionPerformed
        String category = jfield3.getText();
        String brand = jfield4.getText();

        if (category.isEmpty() || brand.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please select both category and brand to set", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            ResultSet rs = DatabaseConnection.executeSearch("SELECT * FROM category WHERE category_name='" + category + "'");
            if (rs.next()) {
                category = rs.getString("id");
            }
            ResultSet rs1 = DatabaseConnection.executeSearch("SELECT * FROM brand WHERE name='" + brand + "'");
            if (rs1.next()) {
                brand = rs1.getString("id");
            }

            ResultSet rs2 = DatabaseConnection.executeSearch("SELECT * FROM category_has_brand WHERE "
                    + "category_category_id='" + category + "' AND brand_id='" + brand + "'");

            if (rs2.next()) {
                JOptionPane.showMessageDialog(null, "Already Registered !", "Infomation", JOptionPane.WARNING_MESSAGE);
                reset1();
            } else {
                DatabaseConnection.executeIUD("INSERT INTO category_has_brand(`category_category_id`,`brand_id`) "
                        + "VALUES('" + category + "','" + brand + "')");
                JOptionPane.showMessageDialog(null, "Category Successfully set to the Brand !", "Infomation", JOptionPane.INFORMATION_MESSAGE);
                reset1();
                loadCategoryHasBrandToTable1();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jbutton5ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField Search1;
    private javax.swing.JButton btn;
    private javax.swing.JButton btn1;
    private javax.swing.JButton btn2;
    private javax.swing.JButton btn3;
    private javax.swing.JButton btn4;
    private javax.swing.JButton btn5;
    private javax.swing.JTextField field1;
    private javax.swing.JTextField field2;
    private javax.swing.JTextField field3;
    private javax.swing.JTextField field4;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JComboBox<String> jComboBox12;
    private javax.swing.JComboBox<String> jComboBox14;
    private javax.swing.JComboBox<String> jComboBox16;
    private javax.swing.JComboBox<String> jComboBox17;
    private javax.swing.JComboBox<String> jComboBox2;
    private com.toedter.calendar.JDateChooser jDateChooser3;
    private com.toedter.calendar.JDateChooser jDateChooser4;
    private javax.swing.JFormattedTextField jFormattedTextField5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JButton jbutton;
    private javax.swing.JButton jbutton1;
    private javax.swing.JButton jbutton2;
    private javax.swing.JButton jbutton3;
    private javax.swing.JButton jbutton4;
    private javax.swing.JButton jbutton5;
    private javax.swing.JTextField jfield1;
    private javax.swing.JTextField jfield2;
    private javax.swing.JTextField jfield3;
    private javax.swing.JTextField jfield4;
    private javax.swing.JTable jtable1;
    private javax.swing.JTextField search2;
    private javax.swing.JTextField search3;
    // End of variables declaration//GEN-END:variables
}
