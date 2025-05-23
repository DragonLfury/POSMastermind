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
import java.util.prefs.Preferences;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import com.lexso.dashboard.main.DashboardWindow;
import com.lexso.util.CurrentUser;
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
import javax.swing.JPanel;

/**
 *
 * @author User
 */
public class Main extends javax.swing.JFrame {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
    private static final Preferences PREFS = Preferences.userNodeForPackage(Main.class);
    private static final String REMEMBER_ME_KEY = "rememberMe";
    private static final String USERNAME_KEY = "rememberedUsername";
    private static final String EMAIL_KEY = "rememberedEmail";
    private static final String ROLE_KEY = "rememberedRole";
    private static final String PASSWORD_KEY = "rememberedPassword";
    private static final String PROFILEPIC_KEY = "rememberedProfilePic";

    static {
        // Configure logger to write to a file
        try {
            FileHandler fileHandler = new FileHandler("lexso_pos_%g.log", 1024 * 1024, 10, true); // 1MB limit, 10 files
            fileHandler.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fileHandler);
            LOGGER.setLevel(Level.ALL);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to initialize file handler for logging.", e);
        }
    }

    public Main() {
        // Log application startup
        LOGGER.info("Starting LexSo POS application.");
        gradientBackground();
        initComponents();
        loadSVGIcons();
        setPanelTransparency(jPanel2, 0.7f); // jPanel2: 70% opacity
        setPanelTransparency(jPanel1, 0.9f); // jPanel1: 90% opacity
        jPasswordField1.setEchoChar((char) 0);
        jTextField2.setForeground(new java.awt.Color(150, 150, 150));
        jPasswordField1.setForeground(new java.awt.Color(150, 150, 150));
    }

    // Validate remembered user
    private boolean validateRememberedUser(String username, String password) {
        try {
            String query = "SELECT * FROM user WHERE username = '" + username + "' AND password = '" + password + "' AND status = 'Active'";
            ResultSet rs = DatabaseConnection.executeSearch(query);
            boolean exists = rs.next();
            if (exists) {
                LOGGER.info("Validated remembered user: " + username + " with password: " + password);
            } else {
                LOGGER.warning("Remembered user not found in database: " + username + " with password: " + password);
            }
            return exists;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error validating remembered user: " + username + " with password: " + password, e);
            return false;
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
        jCheckBox1 = new javax.swing.JCheckBox();
        jLabel4 = new javax.swing.JLabel();
        jPasswordField1 = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setAlwaysOnTop(true);
        setResizable(false);

        jPanel2.setBackground(new java.awt.Color(0, 203, 255));
        jPanel2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabel2.setFont(new java.awt.Font("Nirmala UI Semilight", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Welcome to LexSo POS");

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

        jLabel1.getAccessibleContext().setAccessibleDescription("");

        jPanel3.setBackground(new java.awt.Color(51, 204, 255));

        jTextField2.setForeground(new java.awt.Color(150, 150, 150));
        jTextField2.setText("Username");
        jTextField2.setToolTipText("Enter Your Username");
        jTextField2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextField2FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField2FocusLost(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Stencil", 1, 36)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Login");

        jPanel4.setBackground(new java.awt.Color(51, 204, 255));

        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton1.setText("Login");
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jCheckBox1.setForeground(new java.awt.Color(102, 102, 102));
        jCheckBox1.setText("Remember Me");
        jCheckBox1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jLabel4.setForeground(new java.awt.Color(102, 102, 102));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Forgot Password");
        jLabel4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel4MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addComponent(jCheckBox1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(55, 55, 55))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(116, 116, 116)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox1)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );

        jPasswordField1.setForeground(new java.awt.Color(150, 150, 150));
        jPasswordField1.setText("Password");
        jPasswordField1.setToolTipText("Enter Your Password");
        jPasswordField1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jPasswordField1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jPasswordField1FocusLost(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPasswordField1, javax.swing.GroupLayout.DEFAULT_SIZE, 252, Short.MAX_VALUE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.DEFAULT_SIZE, 252, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(50, 50, 50))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(jLabel3)
                .addGap(29, 29, 29)
                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

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
        if (jTextField2.getText().equals("Username")) {
            jTextField2.setText("");
            jTextField2.setForeground(Color.BLACK);
        }
    }//GEN-LAST:event_jTextField2FocusGained

    private void jTextField2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextField2FocusLost
        if (jTextField2.getText().isEmpty()) {
            jTextField2.setText("Username");
            jTextField2.setForeground(new Color(150, 150, 150)); // Placeholder color
        }
    }//GEN-LAST:event_jTextField2FocusLost

    private void jPasswordField1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jPasswordField1FocusGained
        jPasswordField1.setEchoChar('•'); // Show password character
        if (new String(jPasswordField1.getPassword()).equals("Password")) {
            jPasswordField1.setText("");
            jPasswordField1.setForeground(Color.BLACK); // Normal text color
        }
    }//GEN-LAST:event_jPasswordField1FocusGained

    private void jPasswordField1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jPasswordField1FocusLost
        if (new String(jPasswordField1.getPassword()).isEmpty()) {
            jPasswordField1.setEchoChar((char) 0); // Hide echo character
            jPasswordField1.setText("Password");
            jPasswordField1.setForeground(new Color(150, 150, 150)); // Placeholder color
        }
    }//GEN-LAST:event_jPasswordField1FocusLost

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String username = jTextField2.getText().trim();
        String password = new String(jPasswordField1.getPassword()).trim();

        LOGGER.info("Login button clicked. Attempting login for username: " + username);

        // Validation checks
        if (username.isEmpty() || username.equals("Username")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid username.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            LOGGER.warning("Login failed: Username field is empty or still default.");
            return;
        }
        if (password.isEmpty() || password.equals("Password")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid password.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            LOGGER.warning("Login failed: Password field is empty or still default.");
            return;
        }

        try {
            String query = "SELECT * FROM user INNER JOIN `role` ON `role`.`id` = `user`.`role_id` WHERE username = '" + username + "' AND password = '" + password + "' AND status = 'Active'";
            ResultSet rs = DatabaseConnection.executeSearch(query);

            if (rs.next()) {
                String authenticatedEmail = rs.getString("email");
                String userRole = rs.getString("role.name");
                String userProfilePic = rs.getString("profile_picture");
                CurrentUser.setLoggedInUser(username, authenticatedEmail, userRole, userProfilePic);
                LOGGER.info("Login successful for user: " + username + " (Email: " + authenticatedEmail + ")");
                // Handle "Remember Me" preference
                if (jCheckBox1.isSelected()) {
                    PREFS.putBoolean(REMEMBER_ME_KEY, true);
                    PREFS.put(USERNAME_KEY, username);
                    PREFS.put(PASSWORD_KEY, password);
                    PREFS.put(EMAIL_KEY, authenticatedEmail);
                    PREFS.put(ROLE_KEY, userRole);
                    LOGGER.info("Remember Me checked. Preference saved for user: " + username);
                } else {
                    PREFS.putBoolean(REMEMBER_ME_KEY, false);
                    PREFS.remove(USERNAME_KEY); // New: Clear username if "Remember Me" is unchecked
                    PREFS.remove(PASSWORD_KEY);
                    PREFS.remove(EMAIL_KEY);
                    PREFS.remove(ROLE_KEY);
                    LOGGER.info("Remember Me unchecked. Preference cleared.");
                }

                // Redirect to Dashboard
                this.dispose();
                new DashboardWindow().setVisible(true);

                LOGGER.info("Redirected to Dashboard. Login window disposed.");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                LOGGER.warning("Login failed for user: " + username + ". Invalid credentials.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            LOGGER.log(Level.SEVERE, "SQL error during login attempt for user: " + username, e);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Unexpected error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            LOGGER.log(Level.SEVERE, "Unexpected error during login attempt for user: " + username, e);
        }

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jLabel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseClicked
        ForgotPassword forgotPassword = new ForgotPassword();
        forgotPassword.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jLabel4MouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        FlatMacLightLaf.setup();

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Preferences prefs = Preferences.userNodeForPackage(Main.class);
                boolean rememberMe = prefs.getBoolean(REMEMBER_ME_KEY, false);

                if (rememberMe) {
                    String rememberedUsername = prefs.get(USERNAME_KEY, null);
                    String rememberedPassword = prefs.get(PASSWORD_KEY, null);

                    if (rememberedUsername != null && rememberedPassword != null) {
                        try {
                            String query = "SELECT * FROM user INNER JOIN `role` ON `role`.`id` = `user`.`role_id` WHERE username = '" + rememberedUsername + "' AND password = '" + rememberedPassword + "' AND status = 'Active'";
                            ResultSet rs = DatabaseConnection.executeSearch(query);

                            if (rs.next()) {
                                String authenticatedEmail = rs.getString("email");
                                String userRole = rs.getString("role.name");
                                String userProfilePic = rs.getString("profile_picture");
                                CurrentUser.setLoggedInUser(rememberedUsername, authenticatedEmail, userRole, userProfilePic);
                                LOGGER.info("Auto-login successful for remembered user: " + rememberedUsername);
                                new DashboardWindow().setVisible(true);
                                return;
                            } else {
                                LOGGER.warning("Remembered user credentials invalid or not found. Clearing preference.");
                                prefs.putBoolean(REMEMBER_ME_KEY, false);
                                prefs.remove(USERNAME_KEY);
                                prefs.remove(PASSWORD_KEY);
                                prefs.remove(EMAIL_KEY);
                                prefs.remove(ROLE_KEY);
                                prefs.remove(PROFILEPIC_KEY);
                            }
                        } catch (SQLException e) {
                            LOGGER.log(Level.SEVERE, "SQL error during auto-login validation.", e);
                            JOptionPane.showMessageDialog(null, "Database error during auto-login. Please try manual login.", "Error", JOptionPane.ERROR_MESSAGE);
                        } catch (Exception e) {
                            LOGGER.log(Level.SEVERE, "Unexpected error during auto-login.", e);
                            JOptionPane.showMessageDialog(null, "An unexpected error occurred during auto-login. Please try manual login.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        LOGGER.warning("Incomplete remembered credentials. Proceeding to manual login.");
                        prefs.putBoolean(REMEMBER_ME_KEY, false);
                        prefs.remove(USERNAME_KEY);
                        prefs.remove(PASSWORD_KEY);
                        prefs.remove(EMAIL_KEY);
                        prefs.remove(ROLE_KEY);
                        prefs.remove(PROFILEPIC_KEY);
                    }
                }

                new Main().setVisible(true);
            }
        });

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables
}
