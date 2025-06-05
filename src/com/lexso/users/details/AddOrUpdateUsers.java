/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package com.lexso.users.details;

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
import com.lexso.whatsapp.WhatsAppService;
import java.awt.MediaTracker;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author User
 */
public class AddOrUpdateUsers extends javax.swing.JDialog {

    private static HashMap<String, String> roleMap = new HashMap<>();
    private static HashMap<String, String> genderMap = new HashMap<>();
    private static HashMap<String, String> themeMap = new HashMap<>();
    private static final HashMap<String, String> ROLE_PREFIX_MAP = new HashMap<String, String>() {
        {
            put("0", "ADM");
            put("1", "MNG");
            put("2", "CHR");
            put("3", "STK");
            put("4", "ACC");
        }
    };

    private AllUsersWindow parentFrame;
    private static String recentUser;

    public AddOrUpdateUsers(java.awt.Frame parent, boolean modal, String recent,
            String profilePicPath, String signaturePath) {
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
        registerFormattedTextField.setEnabled(false);
        generateRandomCode();

        checkRecent(recent);
        loadGender();
        loadRole();
        loadTheme();
        disableAll(recent);

        // Load images if paths are provided
        if (profilePicPath != null) {
            loadImageFromPath(profilePicPath, profilepicLabel, 100, 100);
            loadImageFromPath(profilePicPath, profileMiniLabel, 37, 37);
        }
        if (signaturePath != null) {
            loadImageFromPath(signaturePath, signatureLabel, 100, 100);
            loadImageFromPath(signaturePath, signatureMiniLabel, 37, 37);
        }
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

    private void sendRegistrationNotification(String email, String firstName, String lastName,
            String username, String password, String role) {
        sendRegistrationEmail(email, firstName, lastName, username, password, role);
        sendRegistrationWhatsApp(whatsappTextField.getText(), firstName, lastName, username, password, role);
    }

    private void sendRegistrationEmail(String email, String firstName, String lastName,
            String username, String password, String role) {
        try {
            String fromEmail = "geekhirusha@gmail.com";
            String emailPassword = "sltqhvxghjmpztlt";

            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");

            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(fromEmail, emailPassword);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("🌟 Welcome to LexSo POS System - Your Account Details");

            String htmlContent = "<html>"
                    + "<body style='font-family: Arial, sans-serif; line-height: 1.6;'>"
                    + "<div style='max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #e0e0e0; border-radius: 8px;'>"
                    + "<h2 style='color: #2c3e50; text-align: center;'>Welcome " + firstName + " " + lastName + "!</h2>"
                    + "<p style='font-size: 16px;'>Your account has been successfully created with the following details:</p>"
                    + "<div style='background-color: #f8f9fa; padding: 15px; border-radius: 5px; margin: 15px 0;'>"
                    + "<p><strong>Full Name:</strong> " + firstName + " " + lastName + "</p>"
                    + "<p><strong>Username:</strong> " + username + "</p>"
                    + "<p><strong>Temporary Password:</strong> " + password + "</p>"
                    + "<p><strong>Role:</strong> " + role + "</p>"
                    + "</div>"
                    + "<p style='color: #e74c3c; font-weight: bold;'>For security reasons, please change your password after first login.</p>"
                    + "<p>Best regards,<br>System Administration Team</p>"
                    + "</div>"
                    + "</body>"
                    + "</html>";

            message.setContent(htmlContent, "text/html");

            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to send welcome email: " + e.getMessage(),
                    "Email Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void sendRegistrationWhatsApp(String mobileNumber, String firstName, String lastName,
            String username, String password, String role) {
        try {
            String formattedNumber = mobileNumber.startsWith("0") ? "94" + mobileNumber.substring(1) : mobileNumber;

            String message = "🌟 *Welcome Aboard!* 🌟\n\n"
                    + "Dear " + firstName + " " + lastName + ",\n\n"
                    + "Your account has been successfully created! 🎉\n\n"
                    + "📋 *Account Details:*\n"
                    + "▸ *Username:* `" + username + "`\n"
                    + "▸ *Password:* `" + password + "`\n"
                    + "▸ *Role:* " + role + "\n\n"
                    + "🔐 *Security Notice:*\n"
                    + "Please change your password after first login for security.\n\n"
                    + "We're excited to have you on board! 🚀\n\n"
                    + "Best regards,\n"
                    + "LexSo POS Team";

            WhatsAppService whatsappService = new WhatsAppService();
            whatsappService.sendMediaMessage(formattedNumber, message, WhatsAppService.MediaType.TEXT, null);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to send WhatsApp notification: " + e.getMessage(),
                    "WhatsApp Error", JOptionPane.WARNING_MESSAGE);
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
        jLabel20 = new javax.swing.JLabel();
        themeComboBox = new javax.swing.JComboBox<>();
        jLabel22 = new javax.swing.JLabel();
        nicTextField = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        registerFormattedTextField = new javax.swing.JFormattedTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
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
        roleComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                roleComboBoxItemStateChanged(evt);
            }
        });

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

        statusComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Active", "Inactive" }));

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

        jLabel20.setText("Theme");

        themeComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

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

        jLabel27.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(255, 0, 0));
        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel27.setText("*");

        jLabel28.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(255, 0, 0));
        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel28.setText("*");

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
                                    .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(75, 75, 75)
                                    .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(20, 20, 20)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(genderComboBox, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(whatsappTextField, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel20, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(themeComboBox, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                    .addComponent(jLabel34)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jLabel35))
                                .addComponent(bdayDateChooser, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel16))
                        .addGap(75, 75, 75)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14)
                            .addComponent(mobileTextField)
                            .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(nicTextField)
                            .addComponent(registerFormattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(statusComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(TitleLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(25, Short.MAX_VALUE))
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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
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
                                    .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
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
                                        .addComponent(bdayDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel16)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(genderComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel17)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(statusComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(47, 47, 47)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(addButton, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(updateButton, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(addmoreButton, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 414, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void deleteOldFile(String filePath) {
        try {
            if (filePath.startsWith("/com/lexso/")) {
                // For resource paths, we need to convert to actual file path
                File file = new File("src" + filePath);
                if (file.exists()) {
                    file.delete();
                }
            } else {
                // For absolute paths
                File file = new File(filePath);
                if (file.exists()) {
                    file.delete();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void updateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateButtonActionPerformed
        // Get current image paths before updating
        String currentProfilePicPath = null;
        String currentSignaturePath = null;

        try {
            String email = emailTextField.getText().trim();
            ResultSet rs = DatabaseConnection.executeSearch("SELECT profile_picture, signature FROM user WHERE email = '" + email + "'");
            if (rs.next()) {
                currentProfilePicPath = rs.getString("profile_picture");
                currentSignaturePath = rs.getString("signature");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Perform the update
        addOrUpdateUser("");

        // If images were changed, delete old files if they're not default images
        if (profileSelectedFile != null && currentProfilePicPath != null
                && !currentProfilePicPath.equals("/com/lexso/users/profilepics/man.png")) {
            deleteOldFile(currentProfilePicPath);
        }
        if (signatureSelectedFile != null && currentSignaturePath != null
                && !currentSignaturePath.equals("/com/lexso/users/signatures/nullsignature.png")) {
            deleteOldFile(currentSignaturePath);
        }
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
                Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                signatureLabel.setIcon(new ImageIcon(img));

                // Also update the mini label
                Image miniImg = icon.getImage().getScaledInstance(37, 37, Image.SCALE_SMOOTH);
                signatureMiniLabel.setIcon(new ImageIcon(miniImg));
            } else {
                JOptionPane.showMessageDialog(this, "Please select a valid image file (.png, .jpg, .jpeg).");
            }
        }
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        signatureSelectedFile = null;
        ImageIcon icon = new ImageIcon(getClass().getResource("/com/lexso/users/signatures/nullsignature.png"));

        if (recentUser.equals("view")) {
            // View mode - set default in main label
            Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            signatureLabel.setIcon(new ImageIcon(img));
        } else {
            // Add/Update mode - set default in both labels
            Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            signatureLabel.setIcon(new ImageIcon(img));
            Image miniImg = icon.getImage().getScaledInstance(37, 37, Image.SCALE_SMOOTH);
            signatureMiniLabel.setIcon(new ImageIcon(miniImg));
        }
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
                Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                profilepicLabel.setIcon(new ImageIcon(img));

                // Also update the mini label
                Image miniImg = icon.getImage().getScaledInstance(37, 37, Image.SCALE_SMOOTH);
                profileMiniLabel.setIcon(new ImageIcon(miniImg));
            } else {
                JOptionPane.showMessageDialog(this, "Please select a valid image file (.png, .jpg, .jpeg).");
            }
        }
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        profileSelectedFile = null;
        ImageIcon icon = new ImageIcon(getClass().getResource("/com/lexso/users/profilepics/man.png"));

        if (recentUser.equals("view")) {
            Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            profilepicLabel.setIcon(new ImageIcon(img));
        } else {
            Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            profilepicLabel.setIcon(new ImageIcon(img));
            Image miniImg = icon.getImage().getScaledInstance(37, 37, Image.SCALE_SMOOTH);
            profileMiniLabel.setIcon(new ImageIcon(miniImg));
        }
    }//GEN-LAST:event_jButton12ActionPerformed

    private void roleComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_roleComboBoxItemStateChanged
        generateRandomCode();
    }//GEN-LAST:event_roleComboBoxItemStateChanged

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
            profileMiniLabel.setVisible(false);
            signatureMiniLabel.setVisible(false);

            roleBox().setEditable(false);
            genderBox().setEditable(false);
            statusBox().setEditable(false);
            themeBox().setEditable(false);

            fName().setEditable(false);
            lName().setEditable(false);
            codeField().setEditable(false);
            usernameField().setEditable(false);
            emailField().setEditable(false);
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
            themeBox().setEnabled(true);

            fName().setEditable(true);
            lName().setEditable(true);
            codeField().setEditable(false);
            usernameField().setEditable(true);
            emailField().setEditable(true);
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
            themeBox().setEnabled(true);

            fName().setEditable(true);
            lName().setEditable(true);
            codeField().setEditable(false);
            usernameField().setEditable(true);
            emailField().setEditable(false);
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
            String firstName = fnameTextField.getText().trim();
            String lastName = lnameTextField.getText().trim();
            String code = codeTextField.getText().trim();
            String username = usernameTextField.getText().trim();
            String email = emailTextField.getText().trim();
            String nic = nicTextField.getText().trim();
            String whatsappNumber = whatsappTextField.getText().trim();
            String mobile = mobileTextField.getText().trim();
            String role = String.valueOf(roleComboBox.getSelectedItem());
            String gender = String.valueOf(genderComboBox.getSelectedItem());
            String status = String.valueOf(statusComboBox.getSelectedItem());

            firstName = capitalizeFirstLetter(firstName);
            lastName = capitalizeFirstLetter(lastName);

            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || code.isEmpty()
                    || bdayDateChooser.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Please fill in all required fields, including a valid birthday.");
                return;
            }

            LocalDate birthDate = bdayDateChooser.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate today = LocalDate.now();
            int age = Period.between(birthDate, today).getYears();

            if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                JOptionPane.showMessageDialog(this, "Please enter a valid email address.");
                return;
            }

            if (mobile.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Mobile number is required.");
                return;
            } else if (!mobile.matches("\\+?[0-9\\s\\-]+") || mobile.length() < 7 || mobile.length() > 15) {
                JOptionPane.showMessageDialog(this, "Invalid mobile number.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!whatsappNumber.isEmpty()) {
                if (!whatsappNumber.matches("\\+?[0-9\\s\\-]+") || whatsappNumber.length() < 7 || whatsappNumber.length() > 15) {
                    JOptionPane.showMessageDialog(this, "Invalid WhatsApp number.", "Warning", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            ResultSet userResultSet;
            String query = "SELECT * FROM `user` WHERE `email` = '" + email + "' OR `code` = '" + code + "' OR `mobile` = '" + mobile + "' OR `username` = '" + username + "'";
            if (!nic.isEmpty()) {
                query += " OR `nic` = '" + nic + "'";
            }
            if (!whatsappNumber.isEmpty()) {
                query += " OR `whatsapp_number` = '" + whatsappNumber + "'";
            }

            userResultSet = DatabaseConnection.executeSearch(query);
            boolean userExists = userResultSet.next();

            String profilePicturePath = processProfilePicture(userResultSet, userExists);
            String signaturePath = processSignature(userResultSet, userExists);
            String password = generatePassword(firstName);
            if (!userExists) {
                DatabaseConnection.executeIUD("INSERT INTO `user` (`email`, `password`, `first_name`, `last_name`, `code`, `username`, `role_id`, `nic`, `status`, `date_registered`, `whatsapp_number`, `mobile`, `birthday`, `age`, `gender_id`, `profile_picture`, `signature`) VALUES ('"
                        + email + "', '" + password + "', '" + firstName + "', '" + lastName + "', '" + code + "', '" + username + "', '" + roleMap.get(role) + "', '" + nic + "', '" + status + "', CURDATE(), '"
                        + whatsappNumber + "', '" + mobile + "', '" + sdf.format(bdayDateChooser.getDate()) + "', '" + age + "', '" + genderMap.get(gender) + "', '" + profilePicturePath + "', '" + signaturePath + "')");
                parentFrame.loadUserData("", "");
                sendRegistrationNotification(email, firstName, lastName, username, password, role);
                JOptionPane.showMessageDialog(this, "User added successfully.");
                this.dispose();
                parentFrame.addUserProcess();
            } else {
                handleUserUpdate(userResultSet, firstName, lastName, code, username, nic, status, whatsappNumber, mobile, role, gender, profilePicturePath, signaturePath, sdf.format(bdayDateChooser.getDate()), age);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

// Update method
    private void handleUserUpdate(ResultSet userResultSet, String firstName, String lastName, String code,
            String username, String nic, String status, String whatsappNumber, String mobile,
            String role, String gender, String profilePicturePath, String signaturePath,
            String bday, int age) {
        int response = JOptionPane.showConfirmDialog(this,
                "User already exists. Do you want to update the user?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION);

        if (response == JOptionPane.YES_OPTION) {
            try {
                // Get current values from database
                String currentEmail = userResultSet.getString("email");
                String currentProfilePic = userResultSet.getString("profile_picture");
                String currentSignature = userResultSet.getString("signature");

                // Only update profile picture if it was changed
                if (profilePicturePath == null) {
                    profilePicturePath = currentProfilePic;
                }

                // Only update signature if it was changed
                if (signaturePath == null) {
                    signaturePath = currentSignature;
                }

                DatabaseConnection.executeIUD("UPDATE `user` SET "
                        + "`first_name` = '" + firstName + "', "
                        + "`last_name` = '" + lastName + "', "
                        + "`code` = '" + code + "', "
                        + "`username` = '" + username + "', "
                        + "`nic` = '" + nic + "', "
                        + "`status` = '" + status + "', "
                        + "`whatsapp_number` = '" + whatsappNumber + "', "
                        + "`mobile` = '" + mobile + "', "
                        + "`role_id` = '" + roleMap.get(role) + "', "
                        + "`gender_id` = '" + genderMap.get(gender) + "', "
                        + "`profile_picture` = '" + profilePicturePath + "', "
                        + "`signature` = '" + signaturePath + "', "
                        + "`birthday` = '" + bday + "', "
                        + "`age` = '" + age + "' "
                        + "WHERE `email` = '" + currentEmail + "'");

                parentFrame.loadUserData("", "");
                JOptionPane.showMessageDialog(this, "User updated successfully.", "Update Successful", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "An error occurred while updating the user: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "User update cancelled.", "Cancelled", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private String processProfilePicture(ResultSet userResultSet, boolean userExists) {
        try {
            // Get current profile picture path from database if user exists
            String currentProfilePicPath = userExists ? userResultSet.getString("profile_picture") : null;

            // Check if default image is set
            ImageIcon currentIcon = (ImageIcon) profilepicLabel.getIcon();
            ImageIcon defaultIcon = new ImageIcon(getClass().getResource("/com/lexso/users/profilepics/man.png"));

            if (profileSelectedFile != null) {
                // User selected a new profile picture
                // Delete old profile picture if exists and it's not the default
                if (currentProfilePicPath != null && !currentProfilePicPath.equals("/com/lexso/users/profilepics/man.png")) {
                    File oldFile = new File("src" + currentProfilePicPath);
                    if (oldFile.exists()) {
                        oldFile.delete();
                    }
                }
                // Save the new file
                return saveFile(profileSelectedFile, "profile_picture");
            } // Check if default image is displayed
            else if (currentIcon != null && currentIcon.getImage().equals(defaultIcon.getImage())) {
                return "/com/lexso/users/profilepics/man.png";
            } // Keep existing profile picture if not changed
            else if (userExists) {
                return currentProfilePicPath;
            } // Use default for new user
            else {
                return "/com/lexso/users/profilepics/man.png";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "/com/lexso/users/profilepics/man.png";
        }
    }

    private void loadImageFromPath(String imagePath, JLabel label, int width, int height) {
        try {
            ImageIcon icon;
            // Check if the path is a resource path (starts with /com/lexso/)
            if (imagePath.startsWith("/com/lexso/")) {
                icon = new ImageIcon(getClass().getResource(imagePath));
                if (icon.getImageLoadStatus() != MediaTracker.COMPLETE) {
                    throw new Exception("Image not found in resources: " + imagePath);
                }
            } else {
                // Handle absolute file paths
                File imageFile = new File(imagePath);
                if (imageFile.exists()) {
                    icon = new ImageIcon(imagePath);
                } else {
                    // Try to find in resources even if path doesn't start with /com/lexso/
                    icon = new ImageIcon(getClass().getResource(imagePath));
                    if (icon.getImageLoadStatus() != MediaTracker.COMPLETE) {
                        throw new Exception("Image file not found: " + imagePath);
                    }
                }
            }

            Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            label.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            e.printStackTrace();
            // Load default image based on label type
            String defaultPath;
            if (label == profilepicLabel || label == profileMiniLabel) {
                defaultPath = "/com/lexso/users/profilepics/man.png";
            } else {
                defaultPath = "/com/lexso/users/signatures/nullsignature.png";
            }
            ImageIcon defaultIcon = new ImageIcon(getClass().getResource(defaultPath));
            Image img = defaultIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            label.setIcon(new ImageIcon(img));
        }
    }

    private String processSignature(ResultSet userResultSet, boolean userExists) {
        try {
            // Get current signature path from database if user exists
            String currentSignaturePath = userExists ? userResultSet.getString("signature") : null;

            // Check if default image is set
            ImageIcon currentIcon = (ImageIcon) signatureLabel.getIcon();
            ImageIcon defaultIcon = new ImageIcon(getClass().getResource("/com/lexso/users/signatures/nullsignature.png"));

            if (signatureSelectedFile != null) {
                // User selected a new signature
                // Delete old signature if exists and it's not the default
                if (currentSignaturePath != null && !currentSignaturePath.equals("/com/lexso/users/signatures/nullsignature.png")) {
                    File oldFile = new File("src" + currentSignaturePath);
                    if (oldFile.exists()) {
                        oldFile.delete();
                    }
                }
                // Save the new file
                return saveFile(signatureSelectedFile, "signature");
            } // Check if default image is displayed
            else if (currentIcon != null && currentIcon.getImage().equals(defaultIcon.getImage())) {
                return "/com/lexso/users/signatures/nullsignature.png";
            } // Keep existing signature if not changed
            else if (userExists) {
                return currentSignaturePath;
            } // Use default for new user
            else {
                return "/com/lexso/users/signatures/nullsignature.png";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "/com/lexso/users/signatures/nullsignature.png";
        }
    }

    private String saveFile(File selectedFile, String fieldName) {
        if (selectedFile == null) {
            // Return default path based on field type
            return fieldName.equals("profile_picture")
                    ? "/com/lexso/users/profilepics/man.png"
                    : "/com/lexso/users/signatures/nullsignature.png";
        }

        try {
            // Generate a random filename
            Random random = new Random();
            int randomNumber = random.nextInt(100000);
            String originalFileName = selectedFile.getName();
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            String newFileName = randomNumber + fileExtension;

            // Determine the target directory based on field type
            String targetDirPath = fieldName.equals("profile_picture")
                    ? "src/com/lexso/users/profilepics"
                    : "src/com/lexso/users/signatures";

            // Create directory if it doesn't exist
            File targetDir = new File(targetDirPath);
            if (!targetDir.exists()) {
                targetDir.mkdirs();
            }

            // Copy the file
            File targetFile = new File(targetDir, newFileName);
            Files.copy(selectedFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            // Return the relative path for database storage
            return fieldName.equals("profile_picture")
                    ? "/com/lexso/users/profilepics/" + newFileName
                    : "/com/lexso/users/signatures/" + newFileName;
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving " + fieldName + ".");
            return fieldName.equals("profile_picture")
                    ? "/com/lexso/users/profilepics/man.png"
                    : "/com/lexso/users/signatures/nullsignature.png";
        }
    }

    private void generateRandomCode() {
        int role = roleComboBox.getSelectedIndex();
        String prefix;
        Random random = new Random();
        int randomNumber = 1000 + random.nextInt(9000);

        if (role == 0) {
            prefix = "AMD";
        } else if (role == 1) {
            prefix = "MNG";
        } else if (role == 2) {
            prefix = "CHR";
        } else if (role == 3) {
            prefix = "STK";
        } else if (role == 4) {
            prefix = "ACC";
        } else {
            prefix = "USR";
        }

        String randomCode = prefix + randomNumber;
        codeTextField.setText(randomCode);
    }

    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private String generatePassword(String firstName) {
        Random random = new Random();
        int randomNumber = 100 + random.nextInt(900);
        return firstName + randomNumber + "bookiya";
    }

    private String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return Character.toUpperCase(input.charAt(0)) + input.substring(1).toLowerCase();
    }

    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        FlatMacLightLaf.setup();
//
//        /* Create and display the dialog */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                AddOrUpdateUsers dialog = new AddOrUpdateUsers(new javax.swing.JFrame(), true, "", null, null);
//                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
//                    @Override
//                    public void windowClosing(java.awt.event.WindowEvent e) {
//                        System.exit(0);
//                    }
//                });
//                dialog.setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel TitleLabel;
    private javax.swing.JButton addButton;
    private javax.swing.JButton addmoreButton;
    private com.toedter.calendar.JDateChooser bdayDateChooser;
    private javax.swing.JButton cancelButton;
    private javax.swing.JTextField codeTextField;
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
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField lnameTextField;
    private javax.swing.JTextField mobileTextField;
    private javax.swing.JTextField nicTextField;
    private javax.swing.JLabel profileMiniLabel;
    private javax.swing.JLabel profilepicLabel;
    private javax.swing.JFormattedTextField registerFormattedTextField;
    private javax.swing.JComboBox<String> roleComboBox;
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
    // End of variables declaration//GEN-END:variables
}
