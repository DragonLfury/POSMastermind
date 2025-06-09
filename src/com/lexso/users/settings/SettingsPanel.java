/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.lexso.users.settings;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.lexso.connection.DatabaseConnection;
import com.lexso.dashboard.main.DashboardWindow;
import com.lexso.inventory.stock.ManageGRN;
import com.lexso.settings.EditableReciept;
import com.lexso.settings.GiftCardManagement;
import com.lexso.settings.MarkAttendance;
import com.lexso.settings.location.main.LocationMainDashboard;
import com.lexso.users.bankaccounts.AllBankAccountsWindow;
import com.lexso.util.CurrentUser;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.TimeZone;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author User
 */
public class SettingsPanel extends javax.swing.JPanel {

    private static HashMap<String, String> countryMap = new HashMap<>();
    private static HashMap<String, String> provinceMap = new HashMap<>();
    private static HashMap<String, String> districtMap = new HashMap<>();
    private static HashMap<String, String> cityMap = new HashMap<>();
    private static HashMap<String, String> bankMap = new HashMap<>();

    public static Color hoverColor = Color.decode("#90EE90");

    private String loggedInUsername = CurrentUser.getUsername();
    private String loggedInEmail = CurrentUser.getEmail();
    private String loggedInRole = CurrentUser.getRole();
    private String loggedInProfilePic = CurrentUser.getProfilePic();

    private JFileChooser fileChooser;
    private File selectedFile;

    /**
     * Creates new form NewJPanel
     */
    public SettingsPanel() {
        initComponents(); // Line 62
        loadSVGIcons();

        loadCountry();
        loadProfilePic(loggedInEmail);
        loadGender();
        loadBank();
        loadProvice(countryMap.get(String.valueOf(countryComboBox1.getSelectedItem()))); // Line 68 - Another potential NullPointerException here if countryComboBox is null
        loadDistrict(provinceMap.get(String.valueOf(provinceComboBox1.getSelectedItem())));
        loadCity(districtMap.get(String.valueOf(districtComboBox1.getSelectedItem())));

        loadDetails();
        loadBankCountry(bankMap.get(String.valueOf(bankComboBox.getSelectedItem())));
        loadBankDetails();
        // Gether User Type
        try {

            ResultSet resultSet = DatabaseConnection.executeSearch("SELECT * FROM `user` WHERE `email` = '" + loggedInEmail + "'");
            if (resultSet.next()) {
                if (resultSet.getString("role_id").equals("1")) {
                    jPanel20.setVisible(true);
                    adminProPanel.setVisible(true);
                } else {
                    jPanel20.setVisible(false);
                    adminProPanel.setVisible(false);
                }
            } else {

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loadCountry() {
        try {
            ResultSet resultSet = DatabaseConnection.executeSearch("SELECT * FROM `country`");

            Vector vector = new Vector();
            vector.add("Select Country");

            while (resultSet.next()) {
                vector.add(resultSet.getString("country_name"));
                countryMap.put(resultSet.getString("country_name"), resultSet.getString("id"));
            }

            DefaultComboBoxModel model = new DefaultComboBoxModel(vector);
            countryComboBox1.setModel(model); // Use countryComboBox1 instead of countryComboBox

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadGender() {
        try {
            ResultSet resultSet = DatabaseConnection.executeSearch("SELECT * FROM `gender`");
            Vector<String> vector = new Vector<>();
            while (resultSet.next()) {
                vector.add(resultSet.getString("name"));
            }
            DefaultComboBoxModel model = new DefaultComboBoxModel(vector);
            jComboBox2.setModel(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadProvice(String id) {

        try {
            ResultSet resultSet = DatabaseConnection.executeSearch("SELECT * FROM `province` WHERE `country_id` = '" + id + "'");

            Vector vector = new Vector();

            while (resultSet.next()) {
                vector.add(resultSet.getString("province_name"));
                provinceMap.put(resultSet.getString("province_name"), resultSet.getString("id"));
            }

            DefaultComboBoxModel model = new DefaultComboBoxModel(vector);
            provinceComboBox1.setModel(model);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loadDistrict(String provinceID) {

        try {

            ResultSet resultSet = DatabaseConnection.executeSearch("SELECT * FROM `district` WHERE `province_id` = '" + provinceID + "'");

            Vector vector = new Vector();

            while (resultSet.next()) {
                vector.add(resultSet.getString("district_name"));
                districtMap.put(resultSet.getString("district_name"), resultSet.getString("id"));
            }

            DefaultComboBoxModel model = new DefaultComboBoxModel(vector);
            districtComboBox1.setModel(model);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loadCity(String did) {

        try {
            ResultSet resultSet = DatabaseConnection.executeSearch("SELECT * FROM `city` WHERE `district_id` = '" + did + "'");

            Vector vector = new Vector();

            while (resultSet.next()) {
                vector.add(resultSet.getString("city_name"));
                cityMap.put(resultSet.getString("city_name"), resultSet.getString("id"));
            }

            DefaultComboBoxModel model = new DefaultComboBoxModel(vector);
            cityComboBox1.setModel(model);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loadProfilePic(String userEmail) {
        try {
            ResultSet resultSet = DatabaseConnection.executeSearch("SELECT * FROM `user` WHERE `email` = '" + userEmail + "'");
            if (resultSet.next()) {
                ImageIcon profileIcon;
                ImageIcon signatureIcon;
                if (resultSet.getString("profile_picture").contains(".")) {
                    profileIcon = new ImageIcon(getClass().getResource(resultSet.getString("profile_picture")));
                } else {
                    profileIcon = new ImageIcon(getClass().getResource("/com/lexso/users/profilepics/man.png"));
                }
                if (resultSet.getString("signature").contains(".")) {
                    signatureIcon = new ImageIcon(getClass().getResource(resultSet.getString("signature")));
                } else {
                    signatureIcon = new ImageIcon(getClass().getResource("/com/lexso/users/signatures/nullsignature.png"));
                }
                Image profileImg = profileIcon.getImage();
                Image signatureImg = signatureIcon.getImage();
                Image resizedProfileImg = profileImg.getScaledInstance(140, 140, Image.SCALE_SMOOTH);
                Image resizedSignatureImg = signatureImg.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                ImageIcon resizedProfileIcon = new ImageIcon(resizedProfileImg);
                ImageIcon resizedSignatureIcon = new ImageIcon(resizedSignatureImg);
                jLabel49.setIcon(resizedProfileIcon);
                jLabel54.setIcon(resizedSignatureIcon);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadSVGIcons() {

        FlatSVGIcon profileImage = new FlatSVGIcon("com/lexso/users/icon/profile.svg", 20, 20);
        jButton4.setIcon(profileImage);

        FlatSVGIcon signImage = new FlatSVGIcon("com/lexso/users/icon/sign.svg", 25, 20);
        jButton8.setIcon(signImage);

        FlatSVGIcon removeImage = new FlatSVGIcon("com/lexso/users/icon/remove.svg", 20, 20);
        jButton5.setIcon(removeImage);
        jButton9.setIcon(removeImage);
        
        FlatSVGIcon reciptIcon = new FlatSVGIcon("com/lexso/users/icon/Setting_Recipt_Icon.svg", 25, 20);
        jLabel65.setIcon(reciptIcon);

        FlatSVGIcon bankIcon = new FlatSVGIcon("com/lexso/users/icon/Setting_Location_Icon.svg", 23, 20);
        jLabel63.setIcon(bankIcon);

        FlatSVGIcon giftsIcon = new FlatSVGIcon("com/lexso/users/icon/Setting_Gift_Icon.svg", 25, 20);
        jLabel73.setIcon(giftsIcon);

        FlatSVGIcon attendanceIcon = new FlatSVGIcon("com/lexso/users/icon/Setting_Atendance_Icon.svg", 25, 20);
        jLabel68.setIcon(attendanceIcon);

        FlatSVGIcon supplierIocn = new FlatSVGIcon("com/lexso/users/icon/Setting_Supplier_Icon.svg", 25, 20);
        jLabel70.setIcon(supplierIocn);
    }

    private void loadDetails() {
        try {
            ResultSet resultSet = DatabaseConnection.executeSearch(
                    "SELECT `user`.*, `user_address`.`line1`, `user_address`.`line2`, `city`.`city_name`, `district`.`district_name`, `province`.`province_name`, `country`.`country_name`, `gender`.`name` AS gender_name "
                    + "FROM `user` "
                    + "LEFT JOIN `user_address` ON `user`.`email` = `user_address`.`user_email` "
                    + "LEFT JOIN `city` ON `user_address`.`city_id` = `city`.`id` "
                    + "LEFT JOIN `district` ON `city`.`district_id` = `district`.`id` "
                    + "LEFT JOIN `province` ON `district`.`province_id` = `province`.`id` "
                    + "LEFT JOIN `country` ON `province`.`country_id` = `country`.`id` "
                    + "LEFT JOIN `gender` ON `user`.`gender_id` = `gender`.`id` "
                    + "WHERE `user`.`email` = '" + loggedInEmail + "'"
            );
            if (resultSet.next()) {
                jTextField7.setText(resultSet.getString("first_name"));
                jTextField8.setText(resultSet.getString("last_name"));
                jTextField10.setText(resultSet.getString("nic"));
                jTextField11.setText(resultSet.getString("mobile"));
                jTextField14.setText(resultSet.getString("whatsapp_number"));
                jTextField12.setText(resultSet.getString("line1"));
                jTextField13.setText(resultSet.getString("line2"));
                countryComboBox1.setSelectedItem(resultSet.getString("country_name"));
                provinceComboBox1.setSelectedItem(resultSet.getString("province_name"));
                districtComboBox1.setSelectedItem(resultSet.getString("district_name"));
                cityComboBox1.setSelectedItem(resultSet.getString("city_name"));
                jComboBox2.setSelectedItem(resultSet.getString("gender_name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadBank() {

        try {

            ResultSet resultSet = DatabaseConnection.executeSearch("SELECT * FROM `bank`");

            Vector vector = new Vector();

            while (resultSet.next()) {
                vector.add(resultSet.getString("bank_name"));
                bankMap.put(resultSet.getString("bank_name"), resultSet.getString("id"));
            }

            DefaultComboBoxModel model = new DefaultComboBoxModel(vector);
            bankComboBox.setModel(model);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loadBankCountry(String bankId) {
        try {
            ResultSet resultSet = DatabaseConnection.executeSearch(
                    "SELECT country.country_name FROM `bank` "
                    + "INNER JOIN `country` ON `bank`.`country_id` = `country`.`id` "
                    + "WHERE `bank`.`id` = '" + bankId + "'"
            );
            Vector<String> vector = new Vector<>();
            while (resultSet.next()) {
                vector.add(resultSet.getString("country_name"));
            }
            DefaultComboBoxModel model = new DefaultComboBoxModel(vector);
            bankCountryComboBox.setModel(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadBankDetails() {
        try {
            ResultSet bankresultSet = DatabaseConnection.executeSearch(
                    "SELECT * FROM `bank_details` "
                    + "INNER JOIN `bank` ON `bank_details`.`bank_id` = `bank`.`id` "
                    + "WHERE `bank_details`.`user_email` = '" + loggedInEmail + "'"
            );
            if (bankresultSet.next()) {
                jTextField15.setText(bankresultSet.getString("name"));
                jTextField16.setText(bankresultSet.getString("acnumber"));
                jTextField17.setText(bankresultSet.getString("branch"));
                jTextArea1.setText(bankresultSet.getString("description"));
                bankComboBox.setSelectedItem(bankresultSet.getString("bank_name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void changePassword() {
        String currentPassword = String.valueOf(jPasswordField1.getPassword());
        String newPassword = String.valueOf(jPasswordField2.getPassword());
        String confirmPassword = String.valueOf(jPasswordField3.getPassword());

        // Validate current password
        if (currentPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Current password cannot be empty.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validate new password
        if (newPassword.isEmpty() || newPassword.length() < 8) {
            JOptionPane.showMessageDialog(this, "New password must be at least 8 characters long.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validate confirm password
        if (!newPassword.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "New password and confirm password do not match.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Check if the current password is correct
            ResultSet resultSet = DatabaseConnection.executeSearch("SELECT * FROM `user` WHERE `email` = '" + loggedInEmail + "' AND `password` = '" + currentPassword + "'");

            if (!resultSet.next()) {
                JOptionPane.showMessageDialog(this, "Current password is incorrect.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int response = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to update your password?",
                    "Confirm Update",
                    JOptionPane.YES_NO_OPTION);

            if (response == JOptionPane.YES_OPTION) {
                try {
                    // Proceed with updating the password
                    DatabaseConnection.executeIUD("UPDATE `user` SET `password` = '" + newPassword + "' WHERE `email` = '" + loggedInEmail + "'");

                    // Inform the user that the update was successful
                    JOptionPane.showMessageDialog(this, "Password updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    // Handle any exceptions and inform the user of an error
                    JOptionPane.showMessageDialog(this, "An error occurred while updating the password: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }

                // Clear the password fields
                jPasswordField1.setText("");
                jPasswordField2.setText("");
                jPasswordField3.setText("");
            } else {
                // Inform the user that the update was canceled
                JOptionPane.showMessageDialog(this, "Update canceled.", "Canceled", JOptionPane.INFORMATION_MESSAGE);

                // Clear the password fields
                jPasswordField1.setText("");
                jPasswordField2.setText("");
                jPasswordField3.setText("");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred while updating the password.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void changeUserDetails() {
        try {
            String first_name = jTextField7.getText().trim(); // Use jTextField7
            String last_name = jTextField8.getText().trim(); // Use jTextField8
            String nic = jTextField10.getText().trim(); // Use jTextField10
            String mobile = jTextField11.getText().trim(); // Use jTextField11
            String whatsapp = jTextField14.getText().trim(); // Already correct
            String line1 = jTextField12.getText().trim(); // Use jTextField12
            String line2 = jTextField13.getText().trim(); // Use jTextField13
            String country_name = String.valueOf(countryComboBox1.getSelectedItem()); // Use countryComboBox1
            String province_name = String.valueOf(provinceComboBox1.getSelectedItem()); // Use provinceComboBox1
            String district_name = String.valueOf(districtComboBox1.getSelectedItem()); // Use districtComboBox1
            String city_name = String.valueOf(cityComboBox1.getSelectedItem()); // Use cityComboBox1
            String gender = String.valueOf(jComboBox2.getSelectedItem());

            if (first_name.isEmpty() || !first_name.matches("[a-zA-Z]+")) {
                JOptionPane.showMessageDialog(this, "Invalid first name. It must only contain letters and cannot be empty.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (last_name.isEmpty() || !last_name.matches("[a-zA-Z]+")) {
                JOptionPane.showMessageDialog(this, "Invalid last name. It must only contain letters and cannot be empty.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!nic.matches("[a-zA-Z0-9\\-\\s]+")) {
                JOptionPane.showMessageDialog(this, "Invalid NIC format. It must contain only letters, numbers, spaces, or hyphens.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!mobile.matches("\\+?[0-9\\s\\-]+") || mobile.length() < 7 || mobile.length() > 15) {
                JOptionPane.showMessageDialog(this, "Invalid mobile number. It must contain only digits, spaces, hyphens, and may start with +.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!whatsapp.isEmpty() && (!whatsapp.matches("\\+?[0-9\\s\\-]+") || whatsapp.length() < 7 || whatsapp.length() > 15)) {
                JOptionPane.showMessageDialog(this, "Invalid WhatsApp number. It must contain only digits, spaces, hyphens, and may start with +.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            ResultSet genderResultSet = DatabaseConnection.executeSearch("SELECT id FROM `gender` WHERE `name` = '" + gender + "'");
            String genderId = genderResultSet.next() ? genderResultSet.getString("id") : "1";

            ResultSet resultSet = DatabaseConnection.executeSearch("SELECT * FROM `user` WHERE (`nic` = '" + nic + "' OR `mobile` = '" + mobile + "' OR `whatsapp_number` = '" + whatsapp + "') AND `email` != '" + loggedInEmail + "'");
            if (resultSet.next()) {
                JOptionPane.showMessageDialog(this, "This NIC, mobile number, or WhatsApp number is already registered.", "Warning", JOptionPane.WARNING_MESSAGE);
            } else {
                int response = JOptionPane.showConfirmDialog(this,
                        "Are you sure you want to update your information?",
                        "Confirm Update",
                        JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    ResultSet addressCheck = DatabaseConnection.executeSearch("SELECT COUNT(*) FROM user_address WHERE user_email = '" + loggedInEmail + "'");
                    if (addressCheck.next() && addressCheck.getInt(1) > 0) {
                        DatabaseConnection.executeIUD("UPDATE user SET first_name = '" + first_name + "', last_name = '" + last_name + "', nic = '" + nic + "', mobile = '" + mobile + "', whatsapp_number = '" + whatsapp + "', gender_id = '" + genderId + "' WHERE email = '" + loggedInEmail + "'");
                        DatabaseConnection.executeIUD("UPDATE user_address SET line1 = '" + line1 + "', line2 = '" + line2 + "', city_id = '" + cityMap.get(city_name) + "' WHERE user_email = '" + loggedInEmail + "'");
                        JOptionPane.showMessageDialog(this, "Information updated successfully!");
                    } else {
                        DatabaseConnection.executeIUD("INSERT INTO user_address (user_email, line1, line2, city_id) VALUES ('" + loggedInEmail + "', '" + line1 + "', '" + line2 + "', '" + cityMap.get(city_name) + "')");
                        JOptionPane.showMessageDialog(this, "Address added successfully!");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Update canceled.");
                    loadDetails();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addOrUpdateBankDetails() {
        try {
            ResultSet bankresultSet = DatabaseConnection.executeSearch("SELECT * FROM `bank_details` "
                    + "INNER JOIN `bank` ON `bank_details`.`bank_id` = `bank`.`id` "
                    + "WHERE `bank_details`.`user_email` = '" + loggedInEmail + "'");

            String name = jTextField15.getText();
            String acnumber = jTextField16.getText();
            String branch = jTextField17.getText();
            String description = jTextArea1.getText();
            String bank_name = String.valueOf(bankComboBox.getSelectedItem());

            if (name.isEmpty() || acnumber.isEmpty() || branch.isEmpty() || description.isEmpty() || bank_name == null) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.");
                return;
            }

            if (!bankresultSet.next()) {
                DatabaseConnection.executeIUD(
                        "INSERT INTO `bank_details`(`name`, `acnumber`, `branch`, `description`, `bank_id`, `user_email`) VALUES ('"
                        + name + "', '" + acnumber + "', '" + branch + "', '" + description + "', " + bankMap.get(bank_name) + ", '" + loggedInEmail + "')"
                );
                JOptionPane.showMessageDialog(this, "Bank details added successfully.");
            } else {
                int response = JOptionPane.showConfirmDialog(this,
                        "Are you sure you want to update the bank details?",
                        "Confirm Update",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                if (response == JOptionPane.YES_OPTION) {
                    DatabaseConnection.executeIUD(
                            "UPDATE `bank_details` SET `name` = '" + name + "', `acnumber` = '" + acnumber + "', `branch` = '" + branch + "', `description` = '" + description + "', `bank_id` = " + bankMap.get(bank_name) + " WHERE `user_email` = '" + loggedInEmail + "'"
                    );
                    JOptionPane.showMessageDialog(this, "Bank details updated successfully.");
                } else {
                    JOptionPane.showMessageDialog(this, "Update canceled.");
                }
            }
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
        settingsPanel1 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jTextField8 = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jTextField10 = new javax.swing.JTextField();
        jTextField11 = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        jTextField12 = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jTextField13 = new javax.swing.JTextField();
        countryComboBox1 = new javax.swing.JComboBox<>();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        provinceComboBox1 = new javax.swing.JComboBox<>();
        cityComboBox1 = new javax.swing.JComboBox<>();
        jLabel39 = new javax.swing.JLabel();
        districtComboBox1 = new javax.swing.JComboBox<>();
        jLabel40 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jLabel41 = new javax.swing.JLabel();
        jTextField14 = new javax.swing.JTextField();
        jLabel42 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jPanel18 = new javax.swing.JPanel();
        jLabel43 = new javax.swing.JLabel();
        jPasswordField1 = new javax.swing.JPasswordField();
        jLabel44 = new javax.swing.JLabel();
        jPasswordField2 = new javax.swing.JPasswordField();
        jLabel45 = new javax.swing.JLabel();
        jPasswordField3 = new javax.swing.JPasswordField();
        jButton3 = new javax.swing.JButton();
        jLabel46 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        jLabel49 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jLabel50 = new javax.swing.JLabel();
        jPanel20 = new javax.swing.JPanel();
        jLabel51 = new javax.swing.JLabel();
        jPanel23 = new javax.swing.JPanel();
        jLabel53 = new javax.swing.JLabel();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jLabel54 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        jPanel24 = new javax.swing.JPanel();
        jLabel57 = new javax.swing.JLabel();
        jTextField15 = new javax.swing.JTextField();
        jLabel58 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel59 = new javax.swing.JLabel();
        jLabel60 = new javax.swing.JLabel();
        jTextField16 = new javax.swing.JTextField();
        jLabel61 = new javax.swing.JLabel();
        jTextField17 = new javax.swing.JTextField();
        bankComboBox = new javax.swing.JComboBox<>();
        bankCountryComboBox = new javax.swing.JComboBox<>();
        jLabel62 = new javax.swing.JLabel();
        jButton11 = new javax.swing.JButton();
        adminProPanel = new javax.swing.JPanel();
        jLabel63 = new javax.swing.JLabel();
        jLabel64 = new javax.swing.JLabel();
        jLabel65 = new javax.swing.JLabel();
        jLabel66 = new javax.swing.JLabel();
        jLabel67 = new javax.swing.JLabel();
        jLabel68 = new javax.swing.JLabel();
        jLabel69 = new javax.swing.JLabel();
        jLabel70 = new javax.swing.JLabel();
        jLabel71 = new javax.swing.JLabel();
        jLabel72 = new javax.swing.JLabel();
        jLabel73 = new javax.swing.JLabel();
        jLabel74 = new javax.swing.JLabel();

        setLayout(new java.awt.BorderLayout());

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        jLabel5.setText("Settings");

        jLabel21.setText("Manage your account settings and preferences");

        jPanel16.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 153, 255), 3));
        jPanel16.setToolTipText("");
        jPanel16.setPreferredSize(new java.awt.Dimension(609, 649));

        jLabel22.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel22.setText("Edit Profile");

        jPanel17.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204), 2));

        jLabel23.setText("First Name");

        jTextField7.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTextField7.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField7jTextField2KeyPressed(evt);
            }
        });

        jTextField8.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTextField8.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField8KeyPressed(evt);
            }
        });

        jLabel30.setText("Last Name");

        jLabel31.setText("NIC");

        jTextField10.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTextField10.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField10jTextField2KeyPressed(evt);
            }
        });

        jTextField11.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTextField11.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField11jTextField2KeyPressed(evt);
            }
        });

        jLabel32.setText("Mobile");

        jTextField12.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTextField12.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField12jTextField2KeyPressed(evt);
            }
        });

        jLabel33.setText("Address Line 01");

        jLabel36.setText("Address Line 02");

        jTextField13.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTextField13.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField13jTextField2KeyPressed(evt);
            }
        });

        countryComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                countryComboBox1ItemStateChanged(evt);
            }
        });

        jLabel37.setText("Country");

        jLabel38.setText("Province");

        provinceComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                provinceComboBox1ItemStateChanged(evt);
            }
        });

        jLabel39.setText("City");

        districtComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                districtComboBox1ItemStateChanged(evt);
            }
        });

        jLabel40.setText("District");

        jButton2.setBackground(new java.awt.Color(0, 153, 255));
        jButton2.setFont(new java.awt.Font("Segoe UI Black", 1, 12)); // NOI18N
        jButton2.setText("Save");
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel41.setText("Whatsapp");

        jTextField14.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTextField14.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField14jTextField2KeyPressed(evt);
            }
        });

        jLabel42.setText("Gender");

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                        .addComponent(jLabel42)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addComponent(jLabel23)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton2))
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel37)
                            .addComponent(countryComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel38)
                            .addGroup(jPanel17Layout.createSequentialGroup()
                                .addComponent(provinceComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(19, 19, 19))))
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField13)
                            .addComponent(jTextField12)
                            .addComponent(jComboBox2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTextField7, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jTextField8, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jTextField10, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextField11, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
                                    .addGroup(jPanel17Layout.createSequentialGroup()
                                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel30)
                                            .addComponent(jLabel31)
                                            .addComponent(jLabel32))
                                        .addGap(0, 0, Short.MAX_VALUE)))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel41)
                                    .addComponent(jTextField14, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel17Layout.createSequentialGroup()
                                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel40)
                                    .addComponent(districtComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel39)
                                    .addComponent(cityComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addComponent(jLabel33)
                            .addComponent(jLabel36))
                        .addGap(19, 19, 19))))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel23)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jLabel30)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jLabel31)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32)
                    .addComponent(jLabel41))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField14, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel42)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel33)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jLabel36)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel38)
                    .addComponent(jLabel37))
                .addGap(6, 6, 6)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(provinceComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(countryComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                        .addComponent(jLabel40)
                        .addGap(6, 6, 6)
                        .addComponent(districtComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                        .addComponent(jLabel39)
                        .addGap(6, 6, 6)
                        .addComponent(cityComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(23, 23, 23))
        );

        jPanel18.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204), 2));

        jLabel43.setText("Current Password");

        jPasswordField1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPasswordField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jPasswordField1jPasswordField3KeyPressed(evt);
            }
        });

        jLabel44.setText("New Password");

        jPasswordField2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPasswordField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jPasswordField2jPasswordField3KeyPressed(evt);
            }
        });

        jLabel45.setText("Confirm Password");

        jPasswordField3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPasswordField3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jPasswordField3KeyPressed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(0, 153, 255));
        jButton3.setFont(new java.awt.Font("Segoe UI Black", 1, 12)); // NOI18N
        jButton3.setText("Save");
        jButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel46.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/lexso/users/icon/close-eyes.png"))); // NOI18N
        jLabel46.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel46.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel46MouseClicked(evt);
            }
        });

        jLabel47.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/lexso/users/icon/close-eyes.png"))); // NOI18N
        jLabel47.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel47.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel47MouseClicked(evt);
            }
        });

        jLabel48.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/lexso/users/icon/close-eyes.png"))); // NOI18N
        jLabel48.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel48.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel48MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel45)
                            .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel44, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jPasswordField2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE)
                                .addComponent(jPasswordField1, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jPasswordField3, javax.swing.GroupLayout.Alignment.LEADING)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel47, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel48, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addComponent(jLabel43)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton3))))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jLabel43))
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15)
                        .addComponent(jLabel44)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPasswordField2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15)
                        .addComponent(jLabel45)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPasswordField3, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36)
                        .addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36)
                        .addComponent(jLabel48, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jPanel19.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204), 2));

        jLabel49.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel49.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jButton4.setText("Select Profile Pic");
        jButton4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("Clear");
        jButton5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setBackground(new java.awt.Color(0, 153, 255));
        jButton6.setFont(new java.awt.Font("Segoe UI Black", 1, 12)); // NOI18N
        jButton6.setText("Save");
        jButton6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jLabel50.setText("Set/Change Profile Picture");

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel50)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton6))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel49, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(78, 78, 78))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel50))
                .addGap(27, 27, 27)
                .addComponent(jLabel49, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(77, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addComponent(jLabel22)
                        .addContainerGap())
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                        .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(20, 20, 20))))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel22)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel16Layout.createSequentialGroup()
                        .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel17, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20))
        );

        jPanel20.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 153, 255), 3));

        jLabel51.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel51.setText("Others");

        jPanel23.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 2, true));

        jLabel53.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel53.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jButton8.setText("Select Signature");
        jButton8.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton9.setText("Clear");
        jButton9.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton10.setBackground(new java.awt.Color(0, 153, 255));
        jButton10.setFont(new java.awt.Font("Segoe UI Black", 1, 12)); // NOI18N
        jButton10.setText("Save");
        jButton10.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jLabel54.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel54.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel55.setText("To");

        jLabel56.setText("Set/Change Signature");

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel23Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel56)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton10))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel23Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel54, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel55)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                .addComponent(jLabel53, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel23Layout.createSequentialGroup()
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel56))
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel53, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel54, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel23Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel55)
                        .addGap(57, 57, 57)))
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14))
        );

        jPanel24.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 2, true));

        jLabel57.setText("AC Holder Name");

        jTextField15.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField15jTextField11KeyPressed(evt);
            }
        });

        jLabel58.setText("Account Number");

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextArea1jTextField11KeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(jTextArea1);

        jLabel59.setText("Description");

        jLabel60.setText("Bank Name");

        jTextField16.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField16jTextField11KeyPressed(evt);
            }
        });

        jLabel61.setText("Branch");

        jTextField17.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField17KeyPressed(evt);
            }
        });

        bankComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        bankComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                bankComboBoxItemStateChanged(evt);
            }
        });

        bankCountryComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel62.setText("Country");

        jButton11.setBackground(new java.awt.Color(0, 153, 255));
        jButton11.setFont(new java.awt.Font("Segoe UI Black", 1, 12)); // NOI18N
        jButton11.setText("Save");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel57)
                    .addComponent(jTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel58)
                    .addComponent(jTextField16, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(14, Short.MAX_VALUE))
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bankComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel60))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel24Layout.createSequentialGroup()
                        .addComponent(jLabel62)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel24Layout.createSequentialGroup()
                        .addComponent(bankCountryComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel24Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton11))
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel61)
                    .addComponent(jTextField17, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel24Layout.createSequentialGroup()
                        .addComponent(jLabel59)
                        .addGap(0, 91, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel57)
                    .addComponent(jLabel58))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextField15)
                    .addComponent(jTextField16, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel62)
                    .addComponent(jLabel60))
                .addGap(18, 18, 18)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bankCountryComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bankComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel24Layout.createSequentialGroup()
                        .addComponent(jLabel61)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField17, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(11, 11, 11))
                    .addGroup(jPanel24Layout.createSequentialGroup()
                        .addComponent(jLabel59)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel51))
                .addGap(20, 20, 20))
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel51)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel63.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel63.setToolTipText("Bank Accounts");
        jLabel63.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel63.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel63MouseClicked(evt);
            }
        });

        jLabel64.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel64.setText("Reciept");

        jLabel65.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel65.setToolTipText("Bank Accounts");
        jLabel65.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel65.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel65MouseClicked(evt);
            }
        });

        jLabel66.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel66.setText("Location");

        jLabel67.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        jLabel67.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel67.setText("Attendance");

        jLabel68.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel68.setToolTipText("Bank Accounts");
        jLabel68.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel68.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel68MouseClicked(evt);
            }
        });

        jLabel69.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel69.setText("GRN Mng.");

        jLabel70.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel70.setToolTipText("Bank Accounts");
        jLabel70.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel70.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel70MouseClicked(evt);
            }
        });

        jLabel71.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel71.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel71.setText("Admin");

        jLabel72.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel72.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel72.setText("Settings");

        jLabel73.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel73.setToolTipText("Bank Accounts");
        jLabel73.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel73.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel73MouseClicked(evt);
            }
        });

        jLabel74.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel74.setText("Gifts");

        javax.swing.GroupLayout adminProPanelLayout = new javax.swing.GroupLayout(adminProPanel);
        adminProPanel.setLayout(adminProPanelLayout);
        adminProPanelLayout.setHorizontalGroup(
            adminProPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel64, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel66, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel69, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel71, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel72, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(adminProPanelLayout.createSequentialGroup()
                .addGroup(adminProPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel67, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, adminProPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel74, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(adminProPanelLayout.createSequentialGroup()
                .addGroup(adminProPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(adminProPanelLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(adminProPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel68, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel70, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel63, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel65, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(adminProPanelLayout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jLabel73, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        adminProPanelLayout.setVerticalGroup(
            adminProPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(adminProPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel71, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jLabel72, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(jLabel65, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel64)
                .addGap(20, 20, 20)
                .addComponent(jLabel63, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel66)
                .addGap(20, 20, 20)
                .addComponent(jLabel68, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel67)
                .addGap(20, 20, 20)
                .addComponent(jLabel70, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel69)
                .addGap(20, 20, 20)
                .addComponent(jLabel73, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel74)
                .addContainerGap(187, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout settingsPanel1Layout = new javax.swing.GroupLayout(settingsPanel1);
        settingsPanel1.setLayout(settingsPanel1Layout);
        settingsPanel1Layout.setHorizontalGroup(
            settingsPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(settingsPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(settingsPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jLabel21)
                    .addGroup(settingsPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, 640, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(adminProPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        settingsPanel1Layout.setVerticalGroup(
            settingsPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(settingsPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addGap(0, 0, 0)
                .addComponent(jLabel21)
                .addGap(15, 15, 15)
                .addGroup(settingsPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(adminProPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, 691, Short.MAX_VALUE)
                    .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1127, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(settingsPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1115, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 834, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(settingsPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField7jTextField2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField7jTextField2KeyPressed
        if (evt.getKeyCode() == evt.VK_ENTER) {
            changeUserDetails();
        }
    }//GEN-LAST:event_jTextField7jTextField2KeyPressed

    private void jTextField8KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField8KeyPressed
        if (evt.getKeyCode() == evt.VK_ENTER) {
            changeUserDetails();
        }
    }//GEN-LAST:event_jTextField8KeyPressed

    private void jTextField10jTextField2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField10jTextField2KeyPressed
        if (evt.getKeyCode() == evt.VK_ENTER) {
            changeUserDetails();
        }
    }//GEN-LAST:event_jTextField10jTextField2KeyPressed

    private void jTextField11jTextField2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField11jTextField2KeyPressed
        if (evt.getKeyCode() == evt.VK_ENTER) {
            changeUserDetails();
        }
    }//GEN-LAST:event_jTextField11jTextField2KeyPressed

    private void jTextField12jTextField2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField12jTextField2KeyPressed
        if (evt.getKeyCode() == evt.VK_ENTER) {
            changeUserDetails();
        }
    }//GEN-LAST:event_jTextField12jTextField2KeyPressed

    private void jTextField13jTextField2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField13jTextField2KeyPressed
        if (evt.getKeyCode() == evt.VK_ENTER) {
            changeUserDetails();
        }
    }//GEN-LAST:event_jTextField13jTextField2KeyPressed

    private void countryComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_countryComboBox1ItemStateChanged
        loadProvice(countryMap.get(String.valueOf(countryComboBox1.getSelectedItem())));
        loadDistrict(provinceMap.get(String.valueOf(provinceComboBox1.getSelectedItem())));
        loadCity(districtMap.get(String.valueOf(districtComboBox1.getSelectedItem())));
    }//GEN-LAST:event_countryComboBox1ItemStateChanged

    private void provinceComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_provinceComboBox1ItemStateChanged
        loadDistrict(provinceMap.get(String.valueOf(provinceComboBox1.getSelectedItem())));
        loadCity(districtMap.get(String.valueOf(districtComboBox1.getSelectedItem())));
    }//GEN-LAST:event_provinceComboBox1ItemStateChanged

    private void districtComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_districtComboBox1ItemStateChanged
        loadCity(districtMap.get(String.valueOf(districtComboBox1.getSelectedItem())));
    }//GEN-LAST:event_districtComboBox1ItemStateChanged

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        changeUserDetails();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTextField14jTextField2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField14jTextField2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField14jTextField2KeyPressed

    private void jPasswordField1jPasswordField3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jPasswordField1jPasswordField3KeyPressed
        if (evt.getKeyCode() == evt.VK_ENTER) {
            changePassword();
        }
    }//GEN-LAST:event_jPasswordField1jPasswordField3KeyPressed

    private void jPasswordField2jPasswordField3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jPasswordField2jPasswordField3KeyPressed
        if (evt.getKeyCode() == evt.VK_ENTER) {
            changePassword();
        }
    }//GEN-LAST:event_jPasswordField2jPasswordField3KeyPressed

    private void jPasswordField3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jPasswordField3KeyPressed
        if (evt.getKeyCode() == evt.VK_ENTER) {
            changePassword();
        }
    }//GEN-LAST:event_jPasswordField3KeyPressed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        changePassword();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jLabel46MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel46MouseClicked
        if (jPasswordField1.getEchoChar() == '•') {
            ImageIcon eye = new ImageIcon(getClass().getResource("/com/lexso/users/icon/view.png"));
            jLabel46.setIcon(eye);
            jPasswordField1.setEchoChar((char) 0); // Show password
        } else {
            ImageIcon eye = new ImageIcon(getClass().getResource("/com/lexso/users/icon/close-eyes.png"));
            jLabel46.setIcon(eye);
            jPasswordField1.setEchoChar('•'); // Hide password
        }
    }//GEN-LAST:event_jLabel46MouseClicked

    private void jLabel47MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel47MouseClicked

        if (jPasswordField2.getEchoChar() == '•') {
            ImageIcon eye = new ImageIcon(getClass().getResource("/com/lexso/users/icon/view.png"));
            jLabel47.setIcon(eye);
            jPasswordField2.setEchoChar((char) 0); // show password
        } else if (evt.getClickCount() == 1) {
            ImageIcon eye = new ImageIcon(getClass().getResource("/com/lexso/users/icon/close-eyes.png"));
            jLabel47.setIcon(eye);
            jPasswordField2.setEchoChar('•'); //hide password
        }
    }//GEN-LAST:event_jLabel47MouseClicked

    private void jLabel48MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel48MouseClicked

        if (jPasswordField3.getEchoChar() == '•') {
            ImageIcon eye = new ImageIcon(getClass().getResource("/com/lexso/users/icon/view.png"));
            jLabel48.setIcon(eye);
            jPasswordField3.setEchoChar((char) 0); // show password
        } else if (evt.getClickCount() == 1) {
            ImageIcon eye = new ImageIcon(getClass().getResource("/com/lexso/users/icon/close-eyes.png"));
            jLabel48.setIcon(eye);
            jPasswordField3.setEchoChar('•'); //hide password
        }
    }//GEN-LAST:event_jLabel48MouseClicked

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Image files", "jpg", "jpeg", "png"));
        int returnValue = fileChooser.showOpenDialog(this);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            String filePath = selectedFile.getPath();

            if (filePath.endsWith(".jpg") || filePath.endsWith(".jpeg") || filePath.endsWith(".png")) {
                ImageIcon icon = new ImageIcon(filePath);
                Image img = icon.getImage();
                Image resizedImg = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                ImageIcon resizedIcon = new ImageIcon(resizedImg);
                jLabel49.setIcon(resizedIcon);
                jButton5.grabFocus();
            } else {
                JOptionPane.showMessageDialog(this, "Please select a valid image file (.png, .jpg, .jpeg).");
            }
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        selectedFile = null;
        ImageIcon icon = new ImageIcon(getClass().getResource("/com/lexso/users/profilepics/man.png"));
        jLabel49.setIcon(icon);
        jButton5.grabFocus();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed

        Icon icon = jLabel49.getIcon(); // Corrected from jLabel33

        if (selectedFile != null || icon != null) {
            if (selectedFile != null) {
                try {
                    // Generate a random number for the file name prefix
                    Random random = new Random();
                    int randomNumber = random.nextInt(100000);
                    String originalFileName = selectedFile.getName();
                    String newFileName = randomNumber + "_" + originalFileName;

                    // Specify the target directory and file name
                    String targetDir = "src/com/lexso/users/profilepics"; // Align with new schema
                    File dir = new File(targetDir);

                    // Create directory if it does not exist
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }

                    // Specify the target file path with the new file name
                    File targetFile = new File(dir, newFileName);

                    // Copy the file to the target directory
                    Files.copy(selectedFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                    try {
                        String path = "/com/lexso/users/profilepics/" + newFileName; // Store relative path
                        DatabaseConnection.executeIUD("UPDATE `user` SET `profile_picture` = '" + path + "' WHERE `email`= '" + loggedInEmail + "'");
                        loadProfilePic(loggedInEmail);
                        jLabel49.setIcon(null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    JOptionPane.showMessageDialog(this, "Profile picture saved successfully to " + targetFile.getAbsolutePath());

                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error saving profile picture.");
                }
            } else {
                try {
                    DatabaseConnection.executeIUD("UPDATE `user` SET `profile_picture` = '/com/lexso/users/profilepics/man.png' WHERE `email`= '" + loggedInEmail + "'");
                    loadProfilePic(loggedInEmail);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "No file selected.");
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Image files", "jpg", "jpeg", "png"));
        int returnValue = fileChooser.showOpenDialog(this);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            String filePath = selectedFile.getPath();
            if (filePath.endsWith(".jpg") || filePath.endsWith(".jpeg") || filePath.endsWith(".png")) {
                ImageIcon icon = new ImageIcon(filePath);
                Image img = icon.getImage();
                Image resizedImg = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                ImageIcon resizedIcon = new ImageIcon(resizedImg);
                jLabel53.setIcon(resizedIcon);
                jButton9.grabFocus();
            } else {
                JOptionPane.showMessageDialog(this, "Please select a valid image file (.png, .jpg, .jpeg).");
            }
        }
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        selectedFile = null;
        ImageIcon icon = new ImageIcon(getClass().getResource("/com/lexso/users/signatures/nullsignature.png"));
        jLabel53.setIcon(icon);
        jButton9.grabFocus();

    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        Icon icon = jLabel53.getIcon(); // Corrected from jLabel40

        if (selectedFile != null || icon != null) {
            if (selectedFile != null) {
                try {
                    // Generate a random number for the file name prefix
                    Random random = new Random();
                    int randomNumber = random.nextInt(100000);
                    String originalFileName = selectedFile.getName();
                    String newFileName = randomNumber + "_" + originalFileName;

                    // Specify the target directory and file name
                    String targetDir = "src/com/lexso/users/signatures"; // Align with new schema
                    File dir = new File(targetDir);

                    // Create directory if it does not exist
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }

                    // Specify the target file path with the new file name
                    File targetFile = new File(dir, newFileName);

                    // Copy the file to the target directory
                    Files.copy(selectedFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                    try {
                        String path = "/com/lexso/users/signatures/" + newFileName; // Store relative path
                        DatabaseConnection.executeIUD("UPDATE `user` SET `signature` = '" + path + "' WHERE `email`= '" + loggedInEmail + "'");
                        loadProfilePic(loggedInEmail);
                        jLabel53.setIcon(null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    JOptionPane.showMessageDialog(this, "Signature saved successfully to " + targetFile.getAbsolutePath());

                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error saving signature.");
                }
            } else {
                try {
                    DatabaseConnection.executeIUD("UPDATE `user` SET `signature` = '/com/lexso/users/signatures/nullsignature.png' WHERE `email`= '" + loggedInEmail + "'");
                    loadProfilePic(loggedInEmail);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "No file selected.");

        }
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jTextField15jTextField11KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField15jTextField11KeyPressed
        if (evt.getKeyCode() == evt.VK_ENTER) {
            addOrUpdateBankDetails();
        }
    }//GEN-LAST:event_jTextField15jTextField11KeyPressed

    private void jTextArea1jTextField11KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextArea1jTextField11KeyPressed
        if (evt.getKeyCode() == evt.VK_ENTER) {
            addOrUpdateBankDetails();
        }
    }//GEN-LAST:event_jTextArea1jTextField11KeyPressed

    private void jTextField16jTextField11KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField16jTextField11KeyPressed
        if (evt.getKeyCode() == evt.VK_ENTER) {
            addOrUpdateBankDetails();
        }
    }//GEN-LAST:event_jTextField16jTextField11KeyPressed

    private void jTextField17KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField17KeyPressed
        if (evt.getKeyCode() == evt.VK_ENTER) {
            addOrUpdateBankDetails();
        }
    }//GEN-LAST:event_jTextField17KeyPressed

    private void bankComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_bankComboBoxItemStateChanged

        loadBankCountry(bankMap.get(String.valueOf(bankComboBox.getSelectedItem())));
    }//GEN-LAST:event_bankComboBoxItemStateChanged

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        addOrUpdateBankDetails();
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jLabel63MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel63MouseClicked
        LocationMainDashboard locationMainDashboard = new LocationMainDashboard(new DashboardWindow(), true);
        locationMainDashboard.setVisible(true);
    }//GEN-LAST:event_jLabel63MouseClicked

    private void jLabel65MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel65MouseClicked
        EditableReciept editableReciept = new EditableReciept();
        editableReciept.setVisible(true);
    }//GEN-LAST:event_jLabel65MouseClicked

    private void jLabel68MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel68MouseClicked
        MarkAttendance markAttendance = new MarkAttendance();
        markAttendance.setVisible(true);
    }//GEN-LAST:event_jLabel68MouseClicked

    private void jLabel73MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel73MouseClicked
        GiftCardManagement giftCardManagement = new GiftCardManagement();
        giftCardManagement.setVisible(true);
    }//GEN-LAST:event_jLabel73MouseClicked

    private void jLabel70MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel70MouseClicked
        ManageGRN manageGRN = new ManageGRN();
        manageGRN.setVisible(true);
    }//GEN-LAST:event_jLabel70MouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel adminProPanel;
    private javax.swing.JComboBox<String> bankComboBox;
    private javax.swing.JComboBox<String> bankCountryComboBox;
    private javax.swing.JComboBox<String> cityComboBox1;
    private javax.swing.JComboBox<String> countryComboBox1;
    private javax.swing.JComboBox<String> districtComboBox1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JPasswordField jPasswordField2;
    private javax.swing.JPasswordField jPasswordField3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField13;
    private javax.swing.JTextField jTextField14;
    private javax.swing.JTextField jTextField15;
    private javax.swing.JTextField jTextField16;
    private javax.swing.JTextField jTextField17;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JComboBox<String> provinceComboBox1;
    private javax.swing.JPanel settingsPanel1;
    // End of variables declaration//GEN-END:variables
}
