/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package com.lexso.users.bankaccounts;

import com.formdev.flatlaf.themes.FlatMacLightLaf;
import com.toedter.calendar.JDateChooser;
import java.awt.Image;
import java.awt.TextField;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import com.lexso.connection.DatabaseConnection;

/**
 *
 * @author User
 */
public class AddOrUpdateUsers extends javax.swing.JDialog {

    private static HashMap<String, String> roleMap = new HashMap<>();
    private static HashMap<String, String> genderMap = new HashMap<>();
    private static HashMap<String, String> themeMap = new HashMap<>();

    private AllUsersWindow parentFrame;
    private static String recentUser;

    /**
     * Creates new form AddOrUpdateUsers
     */
    public AddOrUpdateUsers(java.awt.Frame parent, boolean modal, String recent) {
        super(parent, modal);
        this.parentFrame = (AllUsersWindow) parent;
        initComponents();
        recentUser = recent;
        timePicker1.set24HourView(true);
        timePicker2.set24HourView(true);
        timePicker3.set24HourView(true);
        timePicker1.setOrientation(SwingConstants.HORIZONTAL);
        timePicker2.set24HourView(true);
        timePicker3.set24HourView(true);
        timePicker1.setEditor(earlytimeStartFormattedTextField);
        timePicker2.setEditor(earlytimeEndFormattedTextField1);
        timePicker3.setEditor(latetimeFormattedTextField);
        registerFormattedTextField.setEnabled(false);
        codeTextField.setText(generateRandomCode());

        checkRecent(recent);
        loadGender();
        loadRole();
        loadTheme();
        disableAll(recent);
    }

    private void loadGender() {

        try {
            ResultSet resultSet = DatabaseConnection.executeSearch("SELECT * FROM `gender`");

            Vector vg = new Vector();

            while (resultSet.next()) {
                vg.add(resultSet.getString("name"));
                genderMap.put(resultSet.getString("name"), resultSet.getString("id"));
            }

            DefaultComboBoxModel dcbm1 = new DefaultComboBoxModel(vg);
            genderComboBox.setModel(dcbm1);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loadRole() {

        try {
            ResultSet resultSet = DatabaseConnection.executeSearch("SELECT * FROM `role`");

            Vector vr = new Vector();

            while (resultSet.next()) {
                vr.add(resultSet.getString("name"));
                roleMap.put(resultSet.getString("name"), resultSet.getString("id"));
            }

            DefaultComboBoxModel dcbm2 = new DefaultComboBoxModel(vr);
            roleComboBox.setModel(dcbm2);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loadTheme() {

        try {
            ResultSet resultSet = DatabaseConnection.executeSearch("SELECT * FROM `theme`");

            Vector vr = new Vector();

            while (resultSet.next()) {
                vr.add(resultSet.getString("theme_name"));
                themeMap.put(resultSet.getString("theme_name"), resultSet.getString("id"));
            }

            DefaultComboBoxModel dcbm3 = new DefaultComboBoxModel(vr);
            themeComboBox.setModel(dcbm3);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public JComboBox roleBox() {
        return roleComboBox;
    }

    public JComboBox genderBox() {
        return genderComboBox;
    }

    public JComboBox statusBox() {
        return statusComboBox;
    }

    public JComboBox salaryBox() {
        return salaryTypeComboBox;
    }

    public JComboBox themeBox() {
        return themeComboBox;
    }

    public JTextField fName() {
        return fnameTextField;
    }

    public JTextField lName() {
        return lnameTextField;
    }

    public JTextField codeField() {
        return codeTextField;
    }

    public JTextField usernameField() {
        return usernameTextField;
    }

    public JTextField emailField() {
        return emailTextField;
    }

    public JTextField workingHoursField() {
        return whoursTextField;
    }

    public JTextField salaryField() {
        return salaryTextField;
    }

    public JTextField savingMoneyField() {
        return saveMoneyTextField;
    }

    public JFormattedTextField earlytimeStartField() {
        return earlytimeStartFormattedTextField;
    }

    public JFormattedTextField earlytimeEndField() {
        return earlytimeEndFormattedTextField1;
    }

    public JTextField bonusField() {
        return bonusTextField;
    }

    public JFormattedTextField latetimeField() {
        return latetimeFormattedTextField;
    }

    public JTextField whatsappField() {
        return whatsappTextField;
    }

    public JTextField mobileField() {
        return mobileTextField;
    }

    public JTextField nicField() {
        return nicTextField;
    }

    public JFormattedTextField registerField() {
        return registerFormattedTextField;
    }

    public JDateChooser bdayChooser() {
        return bdayDateChooser;
    }

    public JLabel titleLabel() {
        return TitleLabel;
    }

    public JLabel profileJLabel() {
        return profilepicLabel;
    }

    public JLabel signatureJLabel() {
        return signatureLabel;
    }

    public JLabel profileMiniJLabel() {
        return profileMiniLabel;
    }

    public JLabel signatureMiniJLabel() {
        return signatureMiniLabel;
    }

    private JFileChooser profileFileChooser;
    private File profileSelectedFile;

    private JFileChooser signatureFileChooser;
    private File signatureSelectedFile;

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        timePicker1 = new raven.datetime.component.time.TimePicker();
        timePicker2 = new raven.datetime.component.time.TimePicker();
        timePicker3 = new raven.datetime.component.time.TimePicker();
        TitleLabel = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        updateButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        addButton = new javax.swing.JButton();
        fnameTextField = new javax.swing.JTextField();
        addmoreButton = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        lnameTextField = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        codeTextField = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        usernameTextField = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        emailTextField = new javax.swing.JTextField();
        roleComboBox = new javax.swing.JComboBox<>();
        jLabel14 = new javax.swing.JLabel();
        mobileTextField = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        whatsappTextField = new javax.swing.JTextField();
        jPanel21 = new javax.swing.JPanel();
        signatureLabel = new javax.swing.JLabel();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jLabel44 = new javax.swing.JLabel();
        signatureMiniLabel = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        genderComboBox = new javax.swing.JComboBox<>();
        jLabel17 = new javax.swing.JLabel();
        statusComboBox = new javax.swing.JComboBox<>();
        jPanel22 = new javax.swing.JPanel();
        profilepicLabel = new javax.swing.JLabel();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jLabel45 = new javax.swing.JLabel();
        profileMiniLabel = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel3 = new javax.swing.JLabel();
        salaryTypeComboBox = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        whoursTextField = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        salaryTextField = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        saveMoneyTextField = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        earlytimeStartFormattedTextField = new javax.swing.JFormattedTextField();
        jLabel18 = new javax.swing.JLabel();
        earlytimeEndFormattedTextField1 = new javax.swing.JFormattedTextField();
        jLabel19 = new javax.swing.JLabel();
        latetimeFormattedTextField = new javax.swing.JFormattedTextField();
        jLabel20 = new javax.swing.JLabel();
        themeComboBox = new javax.swing.JComboBox<>();
        jLabel21 = new javax.swing.JLabel();
        bonusTextField = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        nicTextField = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        registerFormattedTextField = new javax.swing.JFormattedTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        bdayDateChooser = new com.toedter.calendar.JDateChooser();
        jLabel35 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        TitleLabel.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        TitleLabel.setForeground(new java.awt.Color(0, 153, 0));
        TitleLabel.setText("View User Details");

        jLabel2.setForeground(new java.awt.Color(0, 51, 0));
        jLabel2.setText("Here you can see the detailed information of the selected user.");

        updateButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        updateButton.setForeground(new java.awt.Color(204, 204, 0));
        updateButton.setText("Update");
        updateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateButtonActionPerformed(evt);
            }
        });

        cancelButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        cancelButton.setForeground(new java.awt.Color(255, 0, 0));
        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        jLabel5.setText("First Name");

        addButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        addButton.setForeground(new java.awt.Color(0, 204, 0));
        addButton.setText("Save & Exit");
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        addmoreButton.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        addmoreButton.setForeground(new java.awt.Color(0, 204, 204));
        addmoreButton.setText("Save & Add More");
        addmoreButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addmoreButtonActionPerformed(evt);
            }
        });

        jLabel9.setText("Last Name");

        jLabel10.setText("Code");

        jLabel11.setText("Username");

        jLabel12.setText("Role");

        jLabel13.setText("Email");

        roleComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel14.setText("Contact Number");

        jLabel15.setText("Whatsapp Number");

        jPanel21.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204), 2));

        signatureLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        signatureLabel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jButton9.setText("Select Signature");
        jButton9.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton10.setText("Default");
        jButton10.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jLabel44.setText("Set/Change Signature");

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37))
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel44))
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addGap(67, 67, 67)
                        .addComponent(signatureLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(signatureMiniLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addComponent(jLabel44)
                        .addGap(12, 12, 12)
                        .addComponent(signatureLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(signatureMiniLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jLabel16.setText("Gender");

        genderComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel17.setText("Status");

        statusComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Active", "Deactivate" }));

        jPanel22.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204), 2));

        profilepicLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        profilepicLabel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jButton11.setText("Select Profile Pic");
        jButton11.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jButton12.setText("Default");
        jButton12.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jLabel45.setText("Set/Change Profile Picture");

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel22Layout.createSequentialGroup()
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButton11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel22Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel45))
                            .addGroup(jPanel22Layout.createSequentialGroup()
                                .addGap(67, 67, 67)
                                .addComponent(profilepicLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addComponent(profileMiniLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(37, 37, 37))
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addComponent(jLabel45)
                        .addGap(14, 14, 14)
                        .addComponent(profilepicLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(profileMiniLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel3.setText("Basic Salary Type");

        salaryTypeComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Monthly", "Daily", "Weekly" }));

        jLabel4.setText("Working Hours");

        whoursTextField.setText("00.00");

        jLabel6.setText("Basic Salary");

        salaryTextField.setText("00.00");

        jLabel7.setText("Saving Money");

        saveMoneyTextField.setText("00.00");

        jLabel8.setText("Early Hours Time Start");

        earlytimeStartFormattedTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(java.text.DateFormat.getTimeInstance())));
        earlytimeStartFormattedTextField.setText("00:00:00");

        jLabel18.setText("Early Hours Time End");

        earlytimeEndFormattedTextField1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(java.text.DateFormat.getTimeInstance())));
        earlytimeEndFormattedTextField1.setText("00:00:00");

        jLabel19.setText("Late Out Time");

        latetimeFormattedTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("h:mm:ss"))));
        latetimeFormattedTextField.setText("00:00:00");
        latetimeFormattedTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                latetimeFormattedTextFieldKeyPressed(evt);
            }
        });

        jLabel20.setText("Theme");

        themeComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel21.setText("Early Hours Bonus");

        bonusTextField.setText("0");

        jLabel22.setText("NIC");

        jLabel23.setText("Register Date");

        registerFormattedTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("y MMM d"))));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 0, 0));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("*");

        jLabel24.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(255, 0, 0));
        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel24.setText("*");

        jLabel25.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(255, 0, 0));
        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel25.setText("*");

        jLabel26.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(255, 0, 0));
        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel26.setText("*");

        jLabel27.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(255, 0, 0));
        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel27.setText("*");

        jLabel28.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(255, 0, 0));
        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel28.setText("*");

        jLabel29.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(255, 0, 0));
        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel29.setText("*");

        jLabel30.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(255, 0, 0));
        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel30.setText("*");

        jLabel31.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(255, 0, 0));
        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel31.setText("*");

        jLabel32.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(255, 0, 0));
        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel32.setText("*");

        jLabel33.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(255, 0, 0));
        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel33.setText("*");

        jLabel34.setText("Birthday");

        bdayDateChooser.setDateFormatString("yyyy-MM-dd");

        jLabel35.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(255, 0, 0));
        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel35.setText("*");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(TitleLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel5)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabel1))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel10)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabel24))
                                .addComponent(codeTextField)
                                .addComponent(jLabel12)
                                .addComponent(roleComboBox, 0, 245, Short.MAX_VALUE)
                                .addComponent(fnameTextField))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(320, 320, 320)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                            .addComponent(jLabel9)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jLabel25))
                                        .addComponent(lnameTextField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                            .addComponent(jLabel11)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jLabel28))
                                        .addComponent(usernameTextField, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                            .addComponent(jLabel13)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jLabel27))
                                        .addComponent(emailTextField, javax.swing.GroupLayout.Alignment.LEADING)))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel16)
                                        .addComponent(genderComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 77, Short.MAX_VALUE)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(statusComboBox, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jPanel21, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                        .addGap(20, 20, 20)
                        .addComponent(jSeparator1)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(20, 20, 20)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel3)
                                            .addComponent(salaryTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)
                                            .addComponent(salaryTypeComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel6)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel31)))
                                .addGap(75, 75, 75)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel29))
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(saveMoneyTextField, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(whoursTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel7)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel30))))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel21)
                                        .addGap(227, 227, 227)
                                        .addComponent(jLabel19)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel32))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(bonusTextField, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(earlytimeStartFormattedTextField)
                                            .addComponent(jLabel15)
                                            .addComponent(whatsappTextField)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel8)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel33))
                                            .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(themeComboBox, 0, 247, Short.MAX_VALUE)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel34)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jLabel35))
                                            .addComponent(bdayDateChooser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGap(75, 75, 75)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel14)
                                            .addComponent(earlytimeEndFormattedTextField1, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel18)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel26))
                                            .addComponent(mobileTextField)
                                            .addComponent(latetimeFormattedTextField)
                                            .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(nicTextField)
                                            .addComponent(registerFormattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                        .addGap(20, 20, 20)))
                .addGap(13, 13, 13))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(addButton, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(addmoreButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(updateButton, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(TitleLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jSeparator1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel29))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(salaryTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(whoursTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7)
                            .addComponent(jLabel30)
                            .addComponent(jLabel31))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(salaryTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(saveMoneyTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel18)
                                    .addComponent(jLabel26)
                                    .addComponent(jLabel33))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(earlytimeStartFormattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addComponent(earlytimeEndFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel19)
                                .addComponent(jLabel32))
                            .addComponent(jLabel21))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(latetimeFormattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bonusTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel15)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(whatsappTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(mobileTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel20)
                            .addComponent(jLabel22))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(themeComboBox)
                            .addComponent(nicTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel23)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(registerFormattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel34)
                                    .addComponent(jLabel35))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(bdayDateChooser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(fnameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel10)
                                    .addComponent(jLabel24))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(codeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(roleComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(64, 64, 64)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel11)
                                    .addComponent(jLabel28))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(usernameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel13)
                                    .addComponent(jLabel27))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(emailTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel25))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lnameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel16)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(genderComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel17)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(statusComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addButton, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(updateButton, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addmoreButton, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void updateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateButtonActionPerformed
        addOrUpdateUser("");
    }//GEN-LAST:event_updateButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        this.dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        addOrUpdateUser("delete");
    }//GEN-LAST:event_addButtonActionPerformed

    private void addmoreButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addmoreButtonActionPerformed
        addOrUpdateUser("");
    }//GEN-LAST:event_addmoreButtonActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        signatureFileChooser = new JFileChooser();
        signatureFileChooser.setFileFilter(new FileNameExtensionFilter("Image files", "jpg", "jpeg", "png"));
        int returnValue = signatureFileChooser.showOpenDialog(this);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            signatureSelectedFile = signatureFileChooser.getSelectedFile();
            String filePath = signatureSelectedFile.getPath();

            if (filePath.endsWith(".jpg") || filePath.endsWith(".jpeg") || filePath.endsWith(".png")) {
                ImageIcon icon = new ImageIcon(filePath);
                Image img = icon.getImage();
                Image resizedImg = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                ImageIcon resizedIcon = new ImageIcon(resizedImg);
                signatureLabel.setIcon(resizedIcon);
                signatureLabel.grabFocus();
            } else {
                JOptionPane.showMessageDialog(this, "Please select a valid image file (.png, .jpg, .jpeg).");
            }
        }
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed

        signatureSelectedFile = null;
        ImageIcon icon = new ImageIcon(getClass().getResource("/icon/nullsignature.png"));
        signatureLabel.setIcon(icon);
        signatureLabel.grabFocus();

    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed

        profileFileChooser = new JFileChooser();
        profileFileChooser.setFileFilter(new FileNameExtensionFilter("Image files", "jpg", "jpeg", "png"));
        int returnValue = profileFileChooser.showOpenDialog(this);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            profileSelectedFile = profileFileChooser.getSelectedFile();
            String filePath = profileSelectedFile.getPath();

            if (filePath.endsWith(".jpg") || filePath.endsWith(".jpeg") || filePath.endsWith(".png")) {
                ImageIcon icon = new ImageIcon(filePath);
                Image img = icon.getImage();
                Image resizedImg = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                ImageIcon resizedIcon = new ImageIcon(resizedImg);
                profilepicLabel.setIcon(resizedIcon);
                profilepicLabel.grabFocus();
            } else {
                JOptionPane.showMessageDialog(this, "Please select a valid image file (.png, .jpg, .jpeg).");
            }
        }

    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed

        profileSelectedFile = null;
        ImageIcon icon = new ImageIcon(getClass().getResource("/icon/man.png"));
        profilepicLabel.setIcon(icon);
        profilepicLabel.grabFocus();

    }//GEN-LAST:event_jButton12ActionPerformed

    private void latetimeFormattedTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_latetimeFormattedTextFieldKeyPressed

        if (evt.getKeyCode() == evt.VK_ENTER) {

            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

            String inputTime = latetimeFormattedTextField.getText() + ":00";

            try {
                LocalTime time = LocalTime.parse(inputTime, dateTimeFormatter);
                System.out.println(dateTimeFormatter.format(time));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }//GEN-LAST:event_latetimeFormattedTextFieldKeyPressed

    private void checkRecent(String recent) {

        if (recent.equals("update")) {
            addButton.setVisible(false);
            addmoreButton.setVisible(false);
            updateButton.setVisible(true);
            cancelButton.setVisible(true);

        } else if (recent.equals("view")) {
            addButton.setVisible(false);
            addmoreButton.setVisible(false);
            updateButton.setVisible(false);
            cancelButton.setVisible(true);

        } else {
            addButton.setVisible(true);
            addmoreButton.setVisible(true);
            updateButton.setVisible(false);
            cancelButton.setVisible(true);
        }

    }

    private void disableAll(String recent) {

        if (recent.equals("view")) {
            jButton9.setEnabled(false);
            jButton10.setEnabled(false);
            jButton11.setEnabled(false);
            jButton12.setEnabled(false);

            roleBox().setEditable(false);
            genderBox().setEditable(false);
            statusBox().setEditable(false);
            salaryBox().setEditable(false);
            themeBox().setEditable(false);

            fName().setEditable(false);
            lName().setEditable(false);
            codeField().setEditable(false);
            usernameField().setEditable(false);
            emailField().setEditable(false);
            workingHoursField().setEditable(false);
            salaryField().setEditable(false);
            savingMoneyField().setEditable(false);
            earlytimeStartField().setEditable(false);
            earlytimeEndField().setEditable(false);
            bonusField().setEditable(false);
            latetimeField().setEditable(false);
            whatsappField().setEditable(false);
            mobileField().setEditable(false);
            nicField().setEditable(false);
            bdayChooser().setEnabled(false);
            registerField().setEditable(false);

        } else if (recent.equals("add")) {
            jButton9.setEnabled(true);
            jButton10.setEnabled(true);
            jButton11.setEnabled(true);
            jButton12.setEnabled(true);

            roleBox().setEnabled(true);
            genderBox().setEnabled(true);
            statusBox().setEnabled(true);
            salaryBox().setEnabled(true);
            themeBox().setEnabled(true);

            fName().setEditable(true);
            lName().setEditable(true);
            codeField().setEditable(false);
            usernameField().setEditable(true);
            emailField().setEditable(true);
            workingHoursField().setEditable(true);
            salaryField().setEditable(true);
            savingMoneyField().setEditable(true);
            earlytimeStartField().setEditable(true);
            earlytimeEndField().setEditable(true);
            bonusField().setEditable(true);
            latetimeField().setEditable(true);
            whatsappField().setEditable(true);
            mobileField().setEditable(true);
            nicField().setEditable(true);
            bdayChooser().setEnabled(true);
            registerField().setEditable(true);

            jLabel23.setVisible(false);
            registerFormattedTextField.setVisible(false);

        } else if (recent.equals("update")) {
            jButton9.setEnabled(true);
            jButton10.setEnabled(true);
            jButton11.setEnabled(true);
            jButton12.setEnabled(true);

            roleBox().setEnabled(true);
            genderBox().setEnabled(true);
            statusBox().setEnabled(true);
            salaryBox().setEnabled(true);
            themeBox().setEnabled(true);

            fName().setEditable(true);
            lName().setEditable(true);
            codeField().setEditable(false);
            usernameField().setEditable(true);
            emailField().setEditable(false);
            workingHoursField().setEditable(true);
            salaryField().setEditable(true);
            savingMoneyField().setEditable(true);
            earlytimeStartField().setEditable(true);
            earlytimeEndField().setEditable(true);
            bonusField().setEditable(true);
            latetimeField().setEditable(true);
            whatsappField().setEditable(true);
            mobileField().setEditable(true);
            nicField().setEditable(true);
            bdayChooser().setEnabled(true);
            registerField().setEditable(true);
        }

    }

    private void addOrUpdateUser(String delete) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try {
            // Get user input
            String firstName = fnameTextField.getText().trim();
            String lastName = lnameTextField.getText().trim();
            String code = codeTextField.getText().trim();
            String username = usernameTextField.getText().trim();
            String email = emailTextField.getText().trim();
            String nic = nicTextField.getText().trim();
            String whatsappNumber = whatsappTextField.getText().trim();
            String mobile = mobileTextField.getText().trim();
            String workingHours = whoursTextField.getText().trim();
            String basicSalary = salaryTextField.getText().trim();
            String savingMoney = saveMoneyTextField.getText().trim();
            String earlyHoursBonus = bonusTextField.getText().trim();
            String earlyHoursStart = earlytimeStartFormattedTextField.getText().trim();
            String earlyHoursEnd = earlytimeEndFormattedTextField1.getText().trim();
            String lateOutTime = latetimeFormattedTextField.getText().trim();
            String role = String.valueOf(roleComboBox.getSelectedItem());
            String gender = String.valueOf(genderComboBox.getSelectedItem());
            String status = String.valueOf(statusComboBox.getSelectedItem());
            String basicSalaryType = String.valueOf(salaryTypeComboBox.getSelectedItem());
            String theme = String.valueOf(themeComboBox.getSelectedItem());

            // Capitalize first letter of first and last names
            firstName = capitalizeFirstLetter(firstName);
            lastName = capitalizeFirstLetter(lastName);

            // Validate required fields (including birthday)
            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || code.isEmpty() || earlyHoursStart.equals("--:--") || earlyHoursEnd.equals("--:--") || lateOutTime.equals("--:--") || bdayDateChooser.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Please fill in all required fields, including a valid birthday.");
                return;
            }

            // Validate birthday date format (yyyy-MM-dd)
            try {
                String bday = sdf.format(bdayDateChooser.getDate());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Invalid birthday format. Please select a valid date.");
                return;
            }

            // Calculate the age based on birthday
            LocalDate birthDate = bdayDateChooser.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate today = LocalDate.now();
            int age = Period.between(birthDate, today).getYears();

            // Email format validation
            if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                JOptionPane.showMessageDialog(this, "Please enter a valid email address.");
                return;
            }

            // Validate numeric fields
            if (!isNumeric(workingHours) || !isNumeric(basicSalary) || !isNumeric(savingMoney) || !isNumeric(earlyHoursBonus)) {
                JOptionPane.showMessageDialog(this, "Working hours, basic salary, saving money, and early hours bonus must be numeric.");
                return;
            }

            // Validate mobile number
            if (mobile.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Mobile number is required.");
                return;
            } else if (!mobile.matches("\\+?[0-9\\s\\-]+") || mobile.length() < 7 || mobile.length() > 15) {
                JOptionPane.showMessageDialog(this, "Invalid mobile number. It must contain only digits, spaces, hyphens, and may start with +.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Validate WhatsApp number if provided
            if (!whatsappNumber.isEmpty()) {
                if (!whatsappNumber.matches("\\+?[0-9\\s\\-]+") || whatsappNumber.length() < 7 || whatsappNumber.length() > 15) {
                    JOptionPane.showMessageDialog(this, "Invalid WhatsApp number. It must contain only digits, spaces, hyphens, and may start with +.", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            ResultSet userResultSet;

            // Check if email, code, mobile, nic, or whatsapp number already exists
            String query = "SELECT * FROM `employee` WHERE `email` = '" + email + "' OR `code` = '" + code + "' OR `mobile` = '" + mobile + "' OR `username` = '" + username + "'";
            if (!nic.isEmpty()) {
                query += " OR `nic` = '" + nic + "'";
            }
            if (!whatsappNumber.isEmpty()) {
                query += " OR `whatsapp_number` = '" + whatsappNumber + "'";
            }

            userResultSet = DatabaseConnection.executeSearch(query);
            boolean userExists = userResultSet.next(); // Move cursor to the first row

            // Process profile picture and signature
            String profilePicturePath = processProfilePicture(userResultSet, userExists);
            String signaturePath = processSignature(userResultSet, userExists);

            // If user doesn't exist, insert new user
            if (!userExists) {
                DatabaseConnection.executeIUD("INSERT INTO `employee` (`email`, `password`, `first_name`, `last_name`, `code`, `username`, `role_id`, `nic`, `status`, `basic_salary_type`, `working_hours`, `date_registered`, `basic_salary`, `saving_money`, `early_hours_start`, `early_hours_end`, `late_out_time`, `early_hours_bonus`, `whatsapp_number`, `mobile`, `gender_id`, `theme_id`, `profile_picture`, `signature`, `bday`, `age`) VALUES ('"
                        + email + "', '" + generatePassword(firstName) + "', '" + firstName + "', '" + lastName + "', '" + code + "', '" + username + "', '" + roleMap.get(role) + "', '" + nic + "', '" + status + "', '" + basicSalaryType + "', '"
                        + workingHours + "', CURDATE(), '" + basicSalary + "', '" + savingMoney + "', '" + earlyHoursStart + "', '" + earlyHoursEnd + "', '" + lateOutTime + "', '" + earlyHoursBonus + "', '" + whatsappNumber + "', '" + mobile + "', '"
                        + genderMap.get(gender) + "', '" + themeMap.get(theme) + "', '" + profilePicturePath + "', '" + signaturePath + "', '" + sdf.format(bdayDateChooser.getDate()) + "', '" + age + "')");
                parentFrame.loadUserData("","");
                JOptionPane.showMessageDialog(this, "User added successfully.");
                this.dispose();
                parentFrame.addUserProcess();
            } else {
                // Update existing user
                handleUserUpdate(userResultSet, firstName, lastName, code, username, nic, status, basicSalaryType, workingHours, basicSalary, savingMoney, earlyHoursStart, earlyHoursEnd, lateOutTime, earlyHoursBonus, whatsappNumber, mobile, role, gender, theme, profilePicturePath, signaturePath, sdf.format(bdayDateChooser.getDate()), age);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

// Helper method to capitalize the first letter
    private String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return Character.toUpperCase(input.charAt(0)) + input.substring(1).toLowerCase();
    }

// Update method
    private void handleUserUpdate(ResultSet userResultSet, String firstName, String lastName, String code, String username, String nic, String status, String basicSalaryType, String workingHours, String basicSalary, String savingMoney, String earlyHoursStart, String earlyHoursEnd, String lateOutTime, String earlyHoursBonus, String whatsappNumber, String mobile, String role, String gender, String theme, String profilePicturePath, String signaturePath, String bday, int age) {
        int response = JOptionPane.showConfirmDialog(this,
                "User already exists. Do you want to update the user?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION);

        if (response == JOptionPane.YES_OPTION) {
            if (recentUser.equals("update")) {
                try {
                    // Update the existing user
                    DatabaseConnection.executeIUD("UPDATE `employee` SET `first_name` = '" + firstName + "', `last_name` = '" + lastName + "', `code` = '" + code + "', `username` = '" + username + "', `nic` = '" + nic + "', `status` = '" + status + "', `basic_salary_type` = '" + basicSalaryType + "', `working_hours` = '" + workingHours + "', `basic_salary` = '" + basicSalary + "', `saving_money` = '" + savingMoney + "', `early_hours_start` = '" + earlyHoursStart + "', `early_hours_end` = '" + earlyHoursEnd + "', `late_out_time` = '" + lateOutTime + "', `early_hours_bonus` = '" + earlyHoursBonus + "', `whatsapp_number` = '" + whatsappNumber + "', `mobile` = '" + mobile + "', `role_id` = '" + roleMap.get(role) + "', `gender_id` = '" + genderMap.get(gender) + "', `theme_id` = '" + themeMap.get(theme) + "', `profile_picture` = '" + profilePicturePath + "', `signature` = '" + signaturePath + "', `bday` = '" + bday + "', `age` = '" + age + "' WHERE `email` = '" + userResultSet.getString("email") + "'");

                    // Refresh the data in parentFrame and show success message
                    parentFrame.loadUserData("","");
                    JOptionPane.showMessageDialog(this, "User updated successfully.", "Update Successful", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "An error occurred while updating the user: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else if (recentUser.equals("add")) {
                // Handle case where update is requested for non-existent user
                JOptionPane.showMessageDialog(this, "You cannot update a user that doesn't exist. Please add the user first.", "Update Failed", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "User update cancelled.", "Cancelled", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    
    // Helper methods for profile picture and signature
    private String processProfilePicture(ResultSet userResultSet, boolean userExists) {
        String profilePicturePath = null;
        if (profileSelectedFile != null || profilepicLabel.getIcon() != null || profileMiniLabel.getIcon() != null) {
            if (profileSelectedFile != null || profilepicLabel.getIcon() != null && profileMiniLabel.getIcon() == null) {
                profilePicturePath = saveFile(profileSelectedFile, "profileImages", "profile_picture");
            } else if (userExists && profilepicLabel.getIcon() == null && profileMiniLabel.getIcon() != null) {
                try {
                    String path = userResultSet.getString("profile_picture");
                    String correctedPath = path.replace("\\", "\\\\");
                    profilePicturePath = correctedPath;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                profilePicturePath = "no";
            }
        } else {
            JOptionPane.showMessageDialog(this, "No profile picture selected.");
            profilePicturePath = "no";
        }
        return profilePicturePath;
    }

    private String processSignature(ResultSet userResultSet, boolean userExists) {
        String signaturePath = null;
        if (signatureSelectedFile != null || signatureLabel.getIcon() != null || signatureMiniLabel.getIcon() != null) {
            if (signatureSelectedFile != null || signatureLabel.getIcon() != null && signatureMiniLabel.getIcon() == null) {
                signaturePath = saveFile(signatureSelectedFile, "signature", "signature");
            } else if (userExists && signatureLabel.getIcon() == null && signatureMiniLabel.getIcon() != null) {
                try {
                    String path = userResultSet.getString("signature");
                    String correctedPath = path.replace("\\", "\\\\");
                    signaturePath = correctedPath;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                signaturePath = "no";
            }
        } else {
            JOptionPane.showMessageDialog(this, "No signature selected.");
            signaturePath = "no";
        }
        return signaturePath;
    }

// Helper method to save files (profile picture or signature)
    private String saveFile(File selectedFile, String targetDir, String fieldName) {

        if (selectedFile == null) {
            return null;
        }

        try {
            Random random = new Random();
            int randomNumber = random.nextInt(100000);
            String originalFileName = selectedFile.getName();
            String newFileName = randomNumber + "_" + originalFileName;

            File dir = new File(targetDir);

            // Create directory if it does not exist
            if (!dir.exists()) {
                dir.mkdir();
            }

            File targetFile = new File(dir, newFileName);
            Files.copy(selectedFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            JOptionPane.showMessageDialog(this, fieldName + " saved successfully to " + targetFile.getAbsolutePath());

            // Return the path to store in the database
            return targetFile.getAbsolutePath().replace("\\", "\\\\");
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving " + fieldName + ".");
            return null;
        }

    }

// Helper method to check if a string is numeric
    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

// Method to generate password
    private String generatePassword(String firstName) {
        Random random = new Random();
        int randomNumber = 100 + random.nextInt(900);
        return firstName + randomNumber + "bookiya";
    }

    private String generateRandomCode() {
        Random random = new Random();
        int randomNumber = 1000 + random.nextInt(9000); // Generates a random 4-digit number
        return "W" + randomNumber;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        FlatMacLightLaf.setup();

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                AddOrUpdateUsers dialog = new AddOrUpdateUsers(new javax.swing.JFrame(), true, "");
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel TitleLabel;
    private javax.swing.JButton addButton;
    private javax.swing.JButton addmoreButton;
    private com.toedter.calendar.JDateChooser bdayDateChooser;
    private javax.swing.JTextField bonusTextField;
    private javax.swing.JButton cancelButton;
    private javax.swing.JTextField codeTextField;
    private javax.swing.JFormattedTextField earlytimeEndFormattedTextField1;
    private javax.swing.JFormattedTextField earlytimeStartFormattedTextField;
    private javax.swing.JTextField emailTextField;
    private javax.swing.JTextField fnameTextField;
    private javax.swing.JComboBox<String> genderComboBox;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
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
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JFormattedTextField latetimeFormattedTextField;
    private javax.swing.JTextField lnameTextField;
    private javax.swing.JTextField mobileTextField;
    private javax.swing.JTextField nicTextField;
    private javax.swing.JLabel profileMiniLabel;
    private javax.swing.JLabel profilepicLabel;
    private javax.swing.JFormattedTextField registerFormattedTextField;
    private javax.swing.JComboBox<String> roleComboBox;
    private javax.swing.JTextField salaryTextField;
    private javax.swing.JComboBox<String> salaryTypeComboBox;
    private javax.swing.JTextField saveMoneyTextField;
    private javax.swing.JLabel signatureLabel;
    private javax.swing.JLabel signatureMiniLabel;
    private javax.swing.JComboBox<String> statusComboBox;
    private javax.swing.JComboBox<String> themeComboBox;
    private raven.datetime.component.time.TimePicker timePicker1;
    private raven.datetime.component.time.TimePicker timePicker2;
    private raven.datetime.component.time.TimePicker timePicker3;
    private javax.swing.JButton updateButton;
    private javax.swing.JTextField usernameTextField;
    private javax.swing.JTextField whatsappTextField;
    private javax.swing.JTextField whoursTextField;
    // End of variables declaration//GEN-END:variables
}
