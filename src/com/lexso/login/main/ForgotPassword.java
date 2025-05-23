package com.lexso.login.main;

import com.lexso.connection.DatabaseConnection;
import com.lexso.dashboard.main.Dashboard;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import java.io.IOException;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.lexso.login.service.ForgotMailService;
import com.lexso.login.service.ForgotWhatsappService;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class ForgotPassword extends javax.swing.JFrame {

    public static final Logger LOGGER = Logger.getLogger(ForgotPassword.class.getName());
    private String generatedOTP;
    private String userEmail;
    private Timer otpTimer;
    private int remainingSeconds = 300; // 5 minutes timeout
    private boolean otpVerified = false;

    public ForgotPassword() {
        try {
            // Configure logger
            FileHandler fileHandler = new FileHandler("forgot_password.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fileHandler);
            LOGGER.setLevel(Level.ALL);

            LOGGER.info("Starting LexSo POS application Password Reset.");
            gradientBackground();
            initComponents();
            loadSVGIcons();
            setPanelTransparency(jPanel2, 0.7f); // jPanel2: 70% opacity
            setPanelTransparency(jPanel1, 0.9f); // jPanel1: 90% opacity
            jPasswordField3.setEchoChar((char) 0);
            jPasswordField2.setEchoChar((char) 0);
            jTextField3.setForeground(new java.awt.Color(150, 150, 150));
            jTextField2.setForeground(new java.awt.Color(150, 150, 150));
            jPasswordField3.setForeground(new java.awt.Color(150, 150, 150));
            jPasswordField2.setForeground(new java.awt.Color(150, 150, 150));
            jButton1.grabFocus();
            jTextField3.setVisible(false);
            jPasswordField3.setVisible(false);
            jPasswordField2.setVisible(false);
            jLabel4.setVisible(false); // Timer label initially hidden

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error initializing logger", e);
            JOptionPane.showMessageDialog(this, "Error initializing system. Please check logs.",
                    "System Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void gradientBackground() {
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0)); // Keep JFrame background transparent for shaping
        setOpacity(1.0f); // Ensure JFrame is fully opaque

        // Add custom panel with gradient and glass effect
        GradientPanel gradientPanel = new GradientPanel();
        setContentPane(gradientPanel);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 80, 80));
            }
        });
    }

    private class GradientPanel extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            int width = getWidth();
            int height = getHeight();

            // Define the start and end points for the gradient
            Point2D start = new Point2D.Float(0, height); // Bottom of the panel
            Point2D end = new Point2D.Float(0, 0); // Top of the panel

            // Define the gradient stops
            float[] fractions = {0.0f, 1.0f}; // Start and end of the gradient
            Color[] colors = {
                new Color(89, 74, 66), // #d1fdff
                new Color(113, 101, 87) // #fddb92
            };

            // Define the linear gradient
            LinearGradientPaint gradient = new LinearGradientPaint(
                    start, // Start point of the gradient
                    end, // End point of the gradient
                    fractions, // Color stops
                    colors // Colors at the gradient stops
            );

            // Fill the panel with the linear gradient
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, width, height);

            // Optional: Drawing white corners
            g2d.setColor(Color.WHITE);
            int cornerRadius = 80;

            // Set the stroke to make the border thicker
            g2d.setStroke(new BasicStroke(5)); // Adjust the number for thickness
            g2d.drawRoundRect(0, 0, width - 1, height - 1, cornerRadius, cornerRadius); // Apply the white border to the rounded corners
        }
    }

    private void loadSVGIcons() {
        // Existing code
        Random rand = new Random();
        int randomIndex = rand.nextInt(2); // 0 or 1
        if (randomIndex == 0) {
            jLabel1.setIcon(new FlatSVGIcon("com/lexso/login/icon/man.svg", jLabel1.getWidth(), jLabel1.getHeight()));
        } else {
            FlatSVGIcon viewIcon = new FlatSVGIcon("com/lexso/login/icon/woman.svg", jLabel1.getWidth(), jLabel1.getHeight());
            jLabel1.setIcon(viewIcon);
        }
    }

    // Method to set the transparency of a given JPanel
    private void setPanelTransparency(JPanel panel, float opacity) {
        Color backgroundColor = panel.getBackground();
        Color transparentColor = new Color(backgroundColor.getRed(), backgroundColor.getGreen(), backgroundColor.getBlue(), (int) (opacity * 255));
        panel.setBackground(transparentColor);
        panel.setOpaque(false); // Important: Make the panel non-opaque
    }

    private void generateAndSendOTP(String email) {
        try {
            // Check if email exists in database
            ResultSet rs = DatabaseConnection.executeSearch("SELECT email, mobile, first_name, last_name FROM user WHERE email = '" + email + "'");

            if (!rs.next()) {
                LOGGER.warning("Password reset attempt for non-existent email: " + email);
                JOptionPane.showMessageDialog(this, "Email not found in our system.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            userEmail = email;
            String mobile = rs.getString("mobile");
            String firstName = rs.getString("first_name");
            String lastName = rs.getString("last_name");

            // Generate 6-digit OTP
            Random random = new Random();
            generatedOTP = String.format("%06d", random.nextInt(999999));
            remainingSeconds = 300; // Reset timer to 5 minutes

            // Start OTP timer
            startOtpTimer();

            // Send OTP via email and WhatsApp
            ForgotMailService.sendOtpEmail(email, firstName, lastName, generatedOTP);
            ForgotWhatsappService.sendOtpWhatsApp(mobile, firstName, lastName, generatedOTP);

            // Show OTP input field
            jTextField3.setVisible(true);
            jLabel4.setVisible(true);
            jButton1.setText("Verify OTP");

            LOGGER.info("OTP generated and sent for email: " + email);
            JOptionPane.showMessageDialog(this, "OTP has been sent to your email and WhatsApp.",
                    "OTP Sent", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error during OTP generation", e);
            JOptionPane.showMessageDialog(this, "Database error. Please try again later.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            Logger.getLogger(ForgotPassword.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void startOtpTimer() {
        // Cancel existing timer if any
        if (otpTimer != null) {
            otpTimer.cancel();
        }

        otpTimer = new Timer();
        otpTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                remainingSeconds--;
                updateTimerLabel();

                if (remainingSeconds <= 0) {
                    otpTimer.cancel();
                    generatedOTP = null; // Invalidate OTP
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(ForgotPassword.this,
                                "OTP has expired. Please request a new one.",
                                "OTP Expired", JOptionPane.WARNING_MESSAGE);
                        resetOtpFields();
                    });
                }
            }
        }, 1000, 1000); // Update every second
    }

    private void updateTimerLabel() {
        SwingUtilities.invokeLater(() -> {
            int minutes = remainingSeconds / 60;
            int seconds = remainingSeconds % 60;
            jLabel4.setText(String.format("OTP expires in: %02d:%02d", minutes, seconds));
        });
    }

    private void verifyOTP(String enteredOTP) {
        if (generatedOTP == null) {
            JOptionPane.showMessageDialog(this, "No OTP generated or OTP has expired.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (enteredOTP.equals(generatedOTP)) {
            otpVerified = true;
            otpTimer.cancel();
            jLabel4.setText("OTP verified successfully!");
            jTextField3.setEnabled(false);
            jButton1.setText("Reset Password");

            // Show password fields
            jPasswordField2.setVisible(true);
            jPasswordField3.setVisible(true);

            LOGGER.info("OTP verified successfully for email: " + userEmail);
        } else {
            LOGGER.warning("Invalid OTP attempt for email: " + userEmail);
            JOptionPane.showMessageDialog(this, "Invalid OTP. Please try again.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetPassword() {
        String newPassword = new String(jPasswordField2.getPassword());
        String confirmPassword = new String(jPasswordField3.getPassword());

        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter and confirm your new password.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (newPassword.length() < 8) {
            JOptionPane.showMessageDialog(this, "Password must be at least 8 characters long.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Update password in database
            DatabaseConnection.executeIUD("UPDATE user SET password = '" + newPassword
                    + "' WHERE email = '" + userEmail + "'");

            LOGGER.info("Password reset successfully for email: " + userEmail);
            JOptionPane.showMessageDialog(this, "Password reset successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);

            // Send confirmation notifications
            ForgotMailService.sendPasswordResetConfirmation(userEmail);

            this.dispose();
            
            java.awt.EventQueue.invokeLater(() -> {
                new Main().setVisible(true);
            });
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error resetting password", e);
            JOptionPane.showMessageDialog(this, "Error resetting password. Please try again later.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

   
    private void resetOtpFields() {
        generatedOTP = null;
        userEmail = null;
        otpVerified = false;
        if (otpTimer != null) {
            otpTimer.cancel();
        }

        jTextField2.setText("Email");
        jTextField2.setForeground(new Color(150, 150, 150));
        jTextField3.setText("OTP Code");
        jTextField3.setForeground(new Color(150, 150, 150));
        jTextField3.setVisible(false);
        jPasswordField2.setText("New Password");
        jPasswordField2.setForeground(new Color(150, 150, 150));
        jPasswordField2.setVisible(false);
        jPasswordField3.setText("Confirm Password");
        jPasswordField3.setForeground(new Color(150, 150, 150));
        jPasswordField3.setVisible(false);
        jLabel4.setVisible(false);
        jButton1.setText("Send OTP");
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
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jTextField2 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jPasswordField3 = new javax.swing.JPasswordField();
        jPasswordField2 = new javax.swing.JPasswordField();
        jTextField3 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Forgot Password");
        setAlwaysOnTop(true);

        jPanel2.setBackground(new java.awt.Color(0, 203, 255));
        jPanel2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabel2.setFont(new java.awt.Font("Nirmala UI Semilight", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("LexSo POS Password Reset.");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 0, 0));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("X");
        jLabel5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel5MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(80, 80, 80)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addGap(15, 15, 15))
        );

        jPanel3.setBackground(new java.awt.Color(0, 204, 255));

        jTextField2.setForeground(new java.awt.Color(150, 150, 150));
        jTextField2.setText("Email");
        jTextField2.setToolTipText("Enter Your Email");
        jTextField2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField2FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField2FocusLost(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Stencil", 1, 24)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("forgot password");

        jPanel4.setBackground(new java.awt.Color(0, 204, 255));

        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton1.setText("Send OTP");
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("jLabel4");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(70, 70, 70)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(73, 73, 73))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24))
        );

        jPasswordField3.setForeground(new java.awt.Color(150, 150, 150));
        jPasswordField3.setText("Confirm Password");
        jPasswordField3.setToolTipText("Enter Confirm Password");
        jPasswordField3.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jPasswordField3FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jPasswordField3FocusLost(evt);
            }
        });

        jPasswordField2.setForeground(new java.awt.Color(150, 150, 150));
        jPasswordField2.setText("New Password");
        jPasswordField2.setToolTipText("Enter New Password");
        jPasswordField2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jPasswordField2FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jPasswordField2FocusLost(evt);
            }
        });

        jTextField3.setForeground(new java.awt.Color(150, 150, 150));
        jTextField3.setText("OTP Code");
        jTextField3.setToolTipText("Enter OTP Code");
        jTextField3.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField3FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField3FocusLost(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(67, 67, 67)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, 252, Short.MAX_VALUE)
                        .addComponent(jPasswordField3, javax.swing.GroupLayout.DEFAULT_SIZE, 252, Short.MAX_VALUE)
                        .addComponent(jPasswordField2, javax.swing.GroupLayout.DEFAULT_SIZE, 252, Short.MAX_VALUE)
                        .addComponent(jTextField3, javax.swing.GroupLayout.DEFAULT_SIZE, 252, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(jLabel3)
                .addGap(27, 27, 27)
                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPasswordField2, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPasswordField3, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTextField2.getAccessibleContext().setAccessibleName("");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseClicked
        this.dispose();
    }//GEN-LAST:event_jLabel5MouseClicked

    private void jTextField2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField2FocusGained
        if (jTextField2.getText().equals("Email")) {
            jTextField2.setText("");
            jTextField2.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_jTextField2FocusGained

    private void jTextField2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField2FocusLost
        if (jTextField2.getText().isEmpty()) {
            jTextField2.setText("Email");
            jTextField2.setForeground(new Color(150, 150, 150)); // Placeholder color
        }
    }//GEN-LAST:event_jTextField2FocusLost

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (!otpVerified && jTextField3.isVisible()) {
            // Verify OTP phase
            String enteredOTP = jTextField3.getText().trim();
            if (enteredOTP.isEmpty() || enteredOTP.equals("OTP Code")) {
                JOptionPane.showMessageDialog(this, "Please enter the OTP code.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            verifyOTP(enteredOTP);
        } else if (otpVerified) {
            // Reset password phase
            resetPassword();
        } else {
            // Initial phase - send OTP
            String email = jTextField2.getText().trim();
            if (email.isEmpty() || email.equals("Email")) {
                JOptionPane.showMessageDialog(this, "Please enter your email address.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                JOptionPane.showMessageDialog(this, "Please enter a valid email address.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            generateAndSendOTP(email);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jPasswordField3FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jPasswordField3FocusGained
        jPasswordField3.setEchoChar('•'); // Show password character
        if (new String(jPasswordField3.getPassword()).equals("Confirm Password")) {
            jPasswordField3.setText("");
            jPasswordField3.setForeground(Color.BLACK); // Normal text color
        }
    }//GEN-LAST:event_jPasswordField3FocusGained

    private void jPasswordField3FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jPasswordField3FocusLost
        if (new String(jPasswordField3.getPassword()).isEmpty()) {
            jPasswordField3.setEchoChar((char) 0); // Hide echo character
            jPasswordField3.setText("Confirm Password");
            jPasswordField3.setForeground(new Color(150, 150, 150)); // Placeholder color
        }
    }//GEN-LAST:event_jPasswordField3FocusLost

    private void jPasswordField2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jPasswordField2FocusGained
        jPasswordField2.setEchoChar('•'); // Show password character
        if (new String(jPasswordField2.getPassword()).equals("New Password")) {
            jPasswordField2.setText("");
            jPasswordField2.setForeground(Color.BLACK); // Normal text color
        }
    }//GEN-LAST:event_jPasswordField2FocusGained

    private void jPasswordField2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jPasswordField2FocusLost
        if (new String(jPasswordField2.getPassword()).isEmpty()) {
            jPasswordField2.setEchoChar((char) 0); // Hide echo character
            jPasswordField2.setText("New Password");
            jPasswordField2.setForeground(new Color(150, 150, 150)); // Placeholder color
        }
    }//GEN-LAST:event_jPasswordField2FocusLost

    private void jTextField3FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField3FocusGained
        if (jTextField3.getText().equals("OTP Code")) {
            jTextField3.setText("");
            jTextField3.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_jTextField3FocusGained

    private void jTextField3FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField3FocusLost
        if (jTextField3.getText().isEmpty()) {
            jTextField3.setText("OTP Code");
            jTextField3.setForeground(new Color(150, 150, 150)); // Placeholder color
        }
    }//GEN-LAST:event_jTextField3FocusLost


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPasswordField jPasswordField2;
    private javax.swing.JPasswordField jPasswordField3;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    // End of variables declaration//GEN-END:variables
}
