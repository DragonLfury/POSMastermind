/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.lexso.inventory;

import com.formdev.flatlaf.themes.FlatMacLightLaf;
import javax.swing.JOptionPane;
import com.lexso.connection.DatabaseConnection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EditProduct extends javax.swing.JFrame {

    private String id;
    private static HashMap<String, String> categoryMap = new HashMap<>();
    private static HashMap<String, String> brandMap = new HashMap<>();

    public EditProduct() {
        initComponents();
        loadCategories();
        loadBrand();
        loadProductToTable("");
        disableFields();

    }

    private boolean fields;

    private void disableFields() {
        jTextField5.setEditable(false);
        jComboBox12.setEnabled(false);
        jComboBox17.setEnabled(false);
        jComboBox16.setEnabled(false);
        jComboBox14.setEnabled(false);
        jFormattedTextField5.setEditable(false);
        jTextField6.setEditable(false);
        jTextArea1.setEditable(false);
        jButton2.setEnabled(false);

        fields = false;

    }

    private void enableFields() {
        jTextField5.setEditable(true);
        jComboBox12.setEnabled(true);
        jComboBox17.setEnabled(true);
        jComboBox16.setEnabled(true);
        jComboBox14.setEnabled(true);
        jFormattedTextField5.setEditable(true);
        jTextArea1.setEditable(true);
        jButton2.setEnabled(true);

        fields = true;

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

////    Load Brands
//    private void loadBrand() {
//
//        try {
//
//            ResultSet rs = DatabaseConnection.executeSearch("Select * FROM `brand`");
//
//            Vector<String> vector = new Vector<>();
//            vector.add("Select");
//
//            while (rs.next()) {
//                vector.add(rs.getString("name"));
//                brandMap.put(rs.getString("name"), rs.getString("id"));
//
//            }
//            DefaultComboBoxModel model = new DefaultComboBoxModel(vector);
//            jComboBox17.setModel(model);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
    
    //    Load Brands
    private void loadBrand() {

        String selected = String.valueOf(jComboBox12.getSelectedItem());
         String catId="";
        
        
        
        try {
            ResultSet rs;

            if (selected.equals("Select")) {

                rs = DatabaseConnection.executeSearch("Select * FROM brand INNER JOIN category_has_brand "
                        + "ON category_has_brand.brand_id=brand.id");
            } else {
                
               ResultSet rs1=DatabaseConnection.executeSearch("SELECT * FROM `category` WHERE category_name='"+selected+"'");
               if(rs1.next()){
                    catId=rs1.getString("id");
               }
                rs = DatabaseConnection.executeSearch("Select * FROM brand INNER JOIN category_has_brand "
                        + "ON category_has_brand.brand_id=brand.id WHERE category_has_brand.category_category_id='" +catId+"' ");

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
//    LoadProduct to Table

    private void loadProductToTable(String name) {

        try {

            ResultSet rs = DatabaseConnection.executeSearch("SELECT * FROM `product` INNER JOIN  `category_has_brand` ON `product`.`category`=`category_has_brand`.`category_has_brand_id` "
                    + "INNER JOIN   `category` ON  `category`.`id`=`category_has_brand`.`category_category_id`"
                    + "INNER JOIN `brand` ON `brand`.`id`=`category_has_brand`.`brand_id` WHERE `product`.`name` LIKE '%" + name + "%' ORDER BY `product`.`id` ASC");

            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.setRowCount(0);

            while (rs.next()) {
                Vector<String> vector = new Vector<>();

                vector.add(rs.getString("product.id"));
                vector.add(rs.getString("product.name"));
                vector.add(rs.getString("category.category_name"));
                vector.add(rs.getString("brand.name"));
                vector.add(rs.getString("unit"));
                vector.add(rs.getString("updated_at"));
                vector.add(rs.getString("status"));

                model.addRow(vector);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

//    Uniq id generation
    public static String generateProductID(String prefix) {
        try {
            // Query to get the last inserted ID starting with the given prefix
            String query = "SELECT id FROM product WHERE id LIKE '" + prefix + "%' ORDER BY id DESC LIMIT 1";

            ResultSet rs = DatabaseConnection.executeSearch(query);

            if (rs.next()) {
                String lastID = rs.getString("id");  // e.g., "ROD00001"

                // Extract numeric part after the prefix
                String numericPart = lastID.substring(prefix.length());

                // Parse numeric part to integer and increment by 1
                int number = Integer.parseInt(numericPart);
                number++;

                // Format with leading zeros (length 5, change if you want)
                String newID = prefix + String.format("%05d", number);
                return newID;
            } else {
                // No records found for this prefix — start numbering at 1
                return prefix + "00001";
            }
        } catch (Exception e) {
            e.printStackTrace();
            // In case of error, fallback to prefix + 00001
            return prefix + "00001";
        }
    }

    private File selectedFile;

    private void selectImage() {
        JFileChooser fileChooser1 = new JFileChooser();
        int userSelection = fileChooser1.showOpenDialog(null);

        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png", "gif");
        fileChooser1.setFileFilter(filter);

        if (userSelection == fileChooser1.APPROVE_OPTION) {
            try {
                selectedFile = fileChooser1.getSelectedFile();
                jTextField6.setText(selectedFile.getName());

//             String  image= saveImage("Heshan");
//             JOptionPane.showMessageDialog(this, image);
            } catch (NullPointerException e) {
                e.printStackTrace();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

//    Save image
    private String saveImage(String title) {
        String currentImage = "";
        if (selectedFile != null && selectedFile.exists()) {
            File file = new File("src/product-image");
            if (!file.exists()) {
                file.mkdir();
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            String uniqId = sdf.format(new Date());
            String fileExtension = "";
            String originalFileName = selectedFile.getName();
            int dotIndex = originalFileName.lastIndexOf('.');

            if (dotIndex > 0) {
                fileExtension = originalFileName.substring(dotIndex);
            }

            String newFileName = title.replaceAll("\\s+", "_") + "_" + uniqId + fileExtension;
            File destinationFile = new File(file, newFileName);

            try {
                Files.copy(selectedFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                String relativePath =destinationFile.getName();
                currentImage = relativePath.replace("\\", "/");

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            currentImage = "default-image.jpg";

        }
        return currentImage;
    }

    private void reset() {
        jComboBox12.setSelectedIndex(0);
        jComboBox14.setSelectedIndex(0);
        jComboBox17.setSelectedIndex(0);
        jComboBox16.setSelectedIndex(0);
        jFormattedTextField5.setText("");
        jTextField5.setText("");
        jTextField6.setText("");
        jTextField7.setText("");
        jTextArea1.setText("");
        selectedFile=null;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jComboBox12 = new javax.swing.JComboBox<>();
        jLabel26 = new javax.swing.JLabel();
        jComboBox17 = new javax.swing.JComboBox<>();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jComboBox16 = new javax.swing.JComboBox<>();
        jLabel25 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jComboBox14 = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        jFormattedTextField5 = new javax.swing.JFormattedTextField();
        jTextField6 = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel22 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jButton1.setBackground(new java.awt.Color(0, 51, 255));
        jButton1.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Edit Product");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Product ID", "Name", "Category ", "Brand", "Unit ", "Updated At", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
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
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setPreferredWidth(150);
            jTable1.getColumnModel().getColumn(5).setPreferredWidth(120);
            jTable1.getColumnModel().getColumn(6).setPreferredWidth(50);
        }

        jLabel2.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabel2.setText("Search and Select a product");

        jLabel6.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabel6.setText("Update Peoducts");

        jTextField7.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jTextField7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField7ActionPerformed(evt);
            }
        });
        jTextField7.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField7KeyReleased(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        jLabel19.setText(" Product Name");

        jTextField5.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jTextField5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField5ActionPerformed(evt);
            }
        });

        jLabel21.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        jLabel21.setText("Select Category");

        jComboBox12.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jComboBox12.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox12.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox12ItemStateChanged(evt);
            }
        });

        jLabel26.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        jLabel26.setText("Select Brand");

        jComboBox17.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jComboBox17.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select", "kg", "liter", "piece", "pack", "gram", "ml" }));

        jLabel27.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(255, 0, 51));
        jLabel27.setText("*");

        jLabel28.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(255, 0, 51));
        jLabel28.setText("*");

        jLabel24.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(255, 0, 51));
        jLabel24.setText("*");

        jComboBox16.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jComboBox16.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select", "kg", "liter", "piece", "pack", "gram", "ml" }));
        jComboBox16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox16ActionPerformed(evt);
            }
        });

        jLabel25.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        jLabel25.setText("Unit");

        jLabel30.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(255, 0, 51));
        jLabel30.setText("*");

        jLabel23.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        jLabel23.setText("Status");

        jLabel29.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(255, 0, 51));
        jLabel29.setText("*");

        jComboBox14.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jComboBox14.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "active", "inactive" }));

        jLabel7.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        jLabel7.setText("Warranty Period ");

        jFormattedTextField5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jFormattedTextField5KeyReleased(evt);
            }
        });

        jTextField6.setFont(new java.awt.Font("Poppins", 0, 12)); // NOI18N
        jTextField6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField6ActionPerformed(evt);
            }
        });

        jLabel20.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        jLabel20.setText("Image");

        jButton2.setBackground(new java.awt.Color(0, 153, 255));
        jButton2.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Add");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jLabel22.setFont(new java.awt.Font("Poppins", 1, 12)); // NOI18N
        jLabel22.setText("Description");

        jButton3.setBackground(new java.awt.Color(255, 51, 51));
        jButton3.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Clear Fields");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(50, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel7)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addComponent(jLabel26)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addComponent(jLabel21)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addComponent(jLabel25)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jComboBox14, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jComboBox16, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jComboBox17, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jComboBox12, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jTextField5, javax.swing.GroupLayout.DEFAULT_SIZE, 425, Short.MAX_VALUE)
                                        .addComponent(jFormattedTextField5))
                                    .addGap(50, 50, 50))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(216, 216, 216)))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                    .addComponent(jTextField7)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jButton3))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel20, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 425, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(jButton1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                                .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jLabel22)
                                                .addGap(359, 359, 359)))
                                        .addGap(3, 3, 3))))))
                    .addComponent(jLabel6))
                .addGap(50, 50, 50))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel24))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel21)
                            .addComponent(jLabel28))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox12, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel26)
                        .addComponent(jLabel27))
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBox17, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(jLabel30))
                .addGap(7, 7, 7)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jComboBox16, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(jLabel29))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox14, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jFormattedTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField7ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        int row = jTable1.getSelectedRow();
        id = String.valueOf(jTable1.getValueAt(row, 0));
        try {
            ResultSet rs = DatabaseConnection.executeSearch("SELECT * FROM product WHERE id='" + id + "'");
            if (rs.next()) {

                if (evt.getClickCount() == 2) {
                    jTextField5.setText(String.valueOf(jTable1.getValueAt(row, 1)));
                    jComboBox12.setSelectedItem(String.valueOf(jTable1.getValueAt(row, 2)));
                    jComboBox17.setSelectedItem(String.valueOf(jTable1.getValueAt(row, 3)));
                    jComboBox16.setSelectedItem(jTable1.getValueAt(row, 4));
                    jComboBox14.setSelectedItem(jTable1.getValueAt(row, 6));
                    jFormattedTextField5.setText(rs.getString("warranty_period"));
                    jTextField6.setText(rs.getString("product_image"));
                    jTextArea1.setText(rs.getString("product_description"));
                    enableFields();

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }//GEN-LAST:event_jTable1MouseClicked

    private void jTextField7KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField7KeyReleased
        loadProductToTable(jTextField7.getText().trim());        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField7KeyReleased

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        if (!fields) {
            JOptionPane.showMessageDialog(null, "Please Select a product first !", "Validation Error", JOptionPane.ERROR_MESSAGE);

        } else {
            // Example usage
            String productID = id;

            // Assuming `productID` is known and selected
            String name = jTextField5.getText();
            String category = String.valueOf(jComboBox12.getSelectedItem());
            String brand = String.valueOf(jComboBox17.getSelectedItem());
            String unit = String.valueOf(jComboBox16.getSelectedItem());
            String status = String.valueOf(jComboBox14.getSelectedItem());
            String warranty = jFormattedTextField5.getText().trim();
            String newImagePath = "";
            String description=jTextArea1.getText();

            // Validate each field
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Name field cannot be empty!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                jTextField5.requestFocus();
                return;
            }

            if (name.length() > 60) {
                JOptionPane.showMessageDialog(null, "Name must be lower than 60 characters", "Validation Error", JOptionPane.ERROR_MESSAGE);
                jTextField5.requestFocus();
                return;
            }

            if (category.equals("Select")) {
                JOptionPane.showMessageDialog(null, "Please select a valid category!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                jComboBox12.requestFocus();
                return;
            }

            if (brand.equals("Select")) {
                JOptionPane.showMessageDialog(null, "Please select a valid brand!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                jComboBox17.requestFocus();
                return;
            }

            if (unit.equals("Select")) {
                JOptionPane.showMessageDialog(null, "Please select a valid unit!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                jComboBox16.requestFocus();
                return;
            }

            if (status.equals("Select")) {
                JOptionPane.showMessageDialog(null, "Please select a valid status!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                jComboBox14.requestFocus();
                return;
            }

            try {
                ResultSet rs = DatabaseConnection.executeSearch("SELECT * FROM `category_has_brand` WHERE `category_category_id`='" + categoryMap.get(category) + "' AND `brand_id`='" + brandMap.get(brand) + "'");

                if (rs.next()) {
                    String realCategory = rs.getString("category_has_brand_id");

                    // Fetch old product image
                    ResultSet rs2 = DatabaseConnection.executeSearch("SELECT `product_image` FROM `product` WHERE `id`='" + productID + "'");
                    String oldImagePath = "";
                    if (rs2.next()) {
                        oldImagePath = rs2.getString("product_image");
                    }

                    // Handle image update
                    if (selectedFile != null && selectedFile.exists()) {
                        // Save new image
                        newImagePath = saveImage(name);

                        // Delete old image if it's not the default
                        if (!oldImagePath.equals("product_image/default-image.jpg")) {
                            File oldImageFile = new File("src/product-image/" + new File(oldImagePath).getName());
                            if (oldImageFile.exists()) {
                                oldImageFile.delete();
                            }
                        }

                    } else {
                        // Keep old image if no new image selected
                        newImagePath = oldImagePath;
                    }

                    // Now update product details
                    DatabaseConnection.executeIUD("UPDATE `product` SET "
                        + "`name`='" + name + "', "
                        + "`category`='" + realCategory + "', "
                        + "`unit`='" + unit + "', "
                        + "`status`='" + status + "', "
                        + "`warranty_period`='" + warranty + "', "
                        + "`product_image`='" + newImagePath + "', "
                        + "`product_description`='" + description + "' "
                        + "WHERE `id`='" + productID + "'");

                    JOptionPane.showMessageDialog(null, "Product updated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    reset();
                    disableFields();
                    loadProductToTable("");

                } else {
                    JOptionPane.showMessageDialog(null, "Invalid category or brand selection", "Validation Error", JOptionPane.ERROR_MESSAGE);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextField5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField5ActionPerformed

    private void jComboBox12ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox12ItemStateChanged
        loadBrand();        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox12ItemStateChanged

    private void jComboBox16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox16ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox16ActionPerformed

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
        }        // TODO add your handling code here:
    }//GEN-LAST:event_jFormattedTextField5KeyReleased

    private void jTextField6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField6ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        selectImage();
        //        JFileChooser fileChooser = new JFileChooser();
        //
        //        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png", "gif");
        //        fileChooser.setFileFilter(filter);
        //
        //        int result = fileChooser.showOpenDialog(this);
        //        if (result == JFileChooser.APPROVE_OPTION) {
            //            File selectedFile = fileChooser.getSelectedFile();
            //            String filePath = selectedFile.getAbsolutePath();
            //            jTextField6.setText(filePath);
            //        }

        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        reset();
        disableFields();// TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        FlatMacLightLaf.setup();
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new EditProduct().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JComboBox<String> jComboBox12;
    private javax.swing.JComboBox<String> jComboBox14;
    private javax.swing.JComboBox<String> jComboBox16;
    private javax.swing.JComboBox<String> jComboBox17;
    private javax.swing.JFormattedTextField jFormattedTextField5;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
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
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    // End of variables declaration//GEN-END:variables
}
