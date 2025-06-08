/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.lexso.settings.location.city;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import com.lexso.connection.DatabaseConnection;

/**
 *
 * @author sithi
 */
public class CityManagement extends javax.swing.JPanel {

    private static HashMap<String, String> countryMap = new HashMap<>();
    private static HashMap<String, String> provinceMap = new HashMap<>();
    private static HashMap<String, String> districtMap = new HashMap<>();
    /**
     * Creates new form CityManagement
     */
    public CityManagement() {
        initComponents();
        loadCountry();
        loadCity();
        
        jComboBox2.setEnabled(false);
        jComboBox3.setEnabled(false);
        jButton2.setEnabled(false);
        jButton3.setEnabled(false);
        jButton4.setEnabled(false);
    }
    
    public void loadCity() {
        loadCity(""); // Call the overloaded method with empty search term
    }
    
   

    public void loadCity(String searchTerm) {
        try {
            String query;
            if (searchTerm.isEmpty()) {
                query = "SELECT * FROM `city` INNER JOIN `status` ON `city`.`status_Id` = `status`.`id` INNER JOIN `district` ON `city`.`district_id` = `district`.`id` INNER JOIN `province` ON `district`.`province_id` = `province`.`id` INNER JOIN `country` ON `province`.`country_id` = `country`.`id` ORDER BY `city`.`id` ASC ";
            } else {
                query = "SELECT * FROM `city` INNER JOIN `status` ON `city`.`status_Id` = `status`.`id` INNER JOIN `district` ON `city`.`district_id` = `district`.`id` INNER JOIN `province` ON `district`.`province_id` = `province`.`id` INNER JOIN `country` ON `province`.`country_id` = `country`.`id`  "
                        + "WHERE `city_name` LIKE '%" + searchTerm + "%' ORDER BY `city`.`id` ASC ";
            }

            ResultSet resultset = DatabaseConnection.executeSearch(query);
            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.setRowCount(0);

            while (resultset.next()) {
                Vector<String> vector = new Vector<>();
                vector.add(resultset.getString("id"));
                vector.add(resultset.getString("city_name"));
                vector.add(resultset.getString("district_name"));
                vector.add(resultset.getString("province_name"));
                vector.add(resultset.getString("country_name"));
                vector.add(resultset.getString("status.name"));

                model.addRow(vector);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void loadProvince(String country) {
        try {
            ResultSet resultset = DatabaseConnection.executeSearch("SELECT * FROM `province` WHERE `country_id` = '" + countryMap.get(country) + "' AND `status_Id` = '1' ORDER BY `province_name` ASC");
            Vector<String> vector = new Vector<>();
            vector.add("Select Province");

            provinceMap.clear();

            while (resultset.next()) {
                vector.add(resultset.getString("province_name"));
                provinceMap.put(resultset.getString("province_name"), resultset.getString("id"));
            }

            DefaultComboBoxModel dcm = new DefaultComboBoxModel(vector);
            jComboBox2.setModel(dcm);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void loadDistrict(String province) {
        try {
            ResultSet resultset = DatabaseConnection.executeSearch("SELECT * FROM `district` WHERE `province_id` = '" + provinceMap.get(province) + "' AND `status_Id` = '1' ORDER BY `district_name` ASC");
            Vector<String> vector = new Vector<>();
            vector.add("Select District");

            districtMap.clear();

            while (resultset.next()) {
                vector.add(resultset.getString("district_name"));
                districtMap.put(resultset.getString("district_name"), resultset.getString("id"));
            }

            DefaultComboBoxModel dcm = new DefaultComboBoxModel(vector);
            jComboBox3.setModel(dcm);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadCountry() {
        try {
            ResultSet resultset = DatabaseConnection.executeSearch("SELECT * FROM `country` WHERE `status_Id` = '1' ORDER BY `country_name` ASC");
            Vector<String> vector = new Vector<>();
            vector.add("Select Country");

            while (resultset.next()) {
                vector.add(resultset.getString("country_name"));
                countryMap.put(resultset.getString("country_name"), resultset.getString("id"));
            }

            DefaultComboBoxModel dcm = new DefaultComboBoxModel(vector);
            jComboBox1.setModel(dcm);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        jPanel2 = new javax.swing.JPanel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jComboBox2 = new javax.swing.JComboBox<>();
        jComboBox3 = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        jPanel1.setPreferredSize(new java.awt.Dimension(1000, 700));

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Your Country" }));
        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Province" }));
        jComboBox2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox2ItemStateChanged(evt);
            }
        });

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select District" }));

        jButton1.setBackground(new java.awt.Color(255, 193, 7));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton1.setText("Add");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(76, 175, 80));
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Update");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(244, 67, 54));
        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Change Status");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(33, 150, 243));
        jButton4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("Reset");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel1.setText("City Name");

        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField2KeyReleased(evt);
            }
        });

        jLabel2.setText("Search City");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(54, 54, 54)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)
                        .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(194, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField1)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(17, 17, 17))
        );

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "City", "District", "Province", "Country", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true, false
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
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 985, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 385, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1030, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 581, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        try {
            int row = jTable1.getSelectedRow();

            jButton1.setEnabled(false);
            jButton2.setEnabled(true);
            jButton3.setEnabled(true);
            jButton4.setEnabled(true);
            String city = String.valueOf(jTable1.getValueAt(row, 1));
            String district = String.valueOf(jTable1.getValueAt(row, 2));
            String province = String.valueOf(jTable1.getValueAt(row, 3));
            String country = String.valueOf(jTable1.getValueAt(row, 4));
            jComboBox1.setSelectedItem(country);
            jComboBox2.setSelectedItem(province);
            jComboBox3.setSelectedItem(district);
            jTextField1.setText(city);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jTextField2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyReleased
        String searchTerm = jTextField2.getText().trim();
        loadCity(searchTerm);
    }//GEN-LAST:event_jTextField2KeyReleased

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        reset();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        try {
            int row = jTable1.getSelectedRow();
            String id = String.valueOf(jTable1.getValueAt(row, 0));
            ResultSet resultset1 = DatabaseConnection.executeSearch("SELECT * FROM `city` WHERE `id` = '" + id + "' ");

            if (resultset1.next()) {
                int status = resultset1.getInt("status_Id");

                if (status == 1) {
                    DatabaseConnection.executeIUD("UPDATE `city` SET `status_Id` = '2' WHERE `id` = '" + id + "'");

                } else {
                    DatabaseConnection.executeIUD("UPDATE `city` SET `status_Id` = '1' WHERE `id` = '" + id + "'");

                }

                loadCity();
                reset();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {
            String city = jTextField1.getText();
            String country = String.valueOf(jComboBox1.getSelectedItem());
            String province = String.valueOf(jComboBox2.getSelectedItem());
            String district = String.valueOf(jComboBox3.getSelectedItem());
            int row = jTable1.getSelectedRow();
            String id = String.valueOf(jTable1.getValueAt(row, 0));

            ResultSet resultset = DatabaseConnection.executeSearch("SELECT * FROM `city` WHERE `city_name` = '" + city + "' AND  `district_id` = '" + districtMap.get(district) + "'");

            if (country == "Select Country") {
                JOptionPane.showMessageDialog(this, "Please Select Country", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (province == "Select Province") {
                JOptionPane.showMessageDialog(this, "Please Select Province Name", "Warning", JOptionPane.WARNING_MESSAGE);
            }if (district == "Select District") {
                JOptionPane.showMessageDialog(this, "Please Select District", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (city.isBlank()) {
                JOptionPane.showMessageDialog(this, "Please Enter City Name", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (resultset.next()) {
                JOptionPane.showMessageDialog(this, "This City Already added", "Warning", JOptionPane.WARNING_MESSAGE);
            } else {
                DatabaseConnection.executeIUD("Update `city` SET `city_name` = '" + city + "',`district_id`= '" + districtMap.get(district) + "'  WHERE `id` = '" + id + "'");
                reset();
                loadCity();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            String city = jTextField1.getText();
            String country = String.valueOf(jComboBox1.getSelectedItem());
            String province = String.valueOf(jComboBox2.getSelectedItem());
            String district = String.valueOf(jComboBox3.getSelectedItem());

            ResultSet resultset = DatabaseConnection.executeSearch("SELECT * FROM `city` WHERE `city_name` = '" + city + "' AND  `district_id` = '" + districtMap.get(district) + "'");

            if (country == "Select Country") {
                JOptionPane.showMessageDialog(this, "Please Select Country", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (province == "Select Province") {
                JOptionPane.showMessageDialog(this, "Please Select Province Name", "Warning", JOptionPane.WARNING_MESSAGE);
            }if (district == "Select District") {
                JOptionPane.showMessageDialog(this, "Please Select District", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (city.isBlank()) {
                JOptionPane.showMessageDialog(this, "Please Enter City Name", "Warning", JOptionPane.WARNING_MESSAGE);
            } else if (resultset.next()) {
                JOptionPane.showMessageDialog(this, "This City Already added", "Warning", JOptionPane.WARNING_MESSAGE);
            } else {
                DatabaseConnection.executeIUD("INSERT INTO `city` (`city_name`,`district_id`,`status_Id`)VALUES('" + city + "','" + districtMap.get(district) + "','1')");
                reset();
                loadCity();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jComboBox2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox2ItemStateChanged
        if (jComboBox2.getSelectedIndex() == 0) {
            jComboBox3.setEnabled(false);
            //        jComboBox2.setModel(new DefaultComboBoxModel(new Vector())); // Clear the combo box
            jComboBox3.setSelectedIndex(0);
        } else {
            jComboBox3.setEnabled(true);
            String province= String.valueOf(jComboBox2.getSelectedItem());
            loadDistrict(province);
        }
    }//GEN-LAST:event_jComboBox2ItemStateChanged

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        if (jComboBox1.getSelectedIndex() == 0) {
            jComboBox2.setEnabled(false);
            //        jComboBox2.setModel(new DefaultComboBoxModel(new Vector())); // Clear the combo box
            jComboBox2.setSelectedIndex(0);
        } else {
            jComboBox2.setEnabled(true);
            String country = String.valueOf(jComboBox1.getSelectedItem());
            loadProvince(country);
        }
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    
    
    private void reset() {
        jButton1.setEnabled(true);
        jButton2.setEnabled(false);
        jButton3.setEnabled(false);
        jButton4.setEnabled(false);
        jTextField1.setText("");
        jTextField2.setText("");
        jComboBox1.setSelectedIndex(0);

        jComboBox2.setSelectedIndex(0);
        jComboBox2.setEnabled(false);
        
        jComboBox3.setSelectedIndex(0);
        jComboBox3.setEnabled(false);

        loadCity();

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables
}
