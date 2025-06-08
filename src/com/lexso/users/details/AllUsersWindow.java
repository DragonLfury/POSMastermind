/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.lexso.users.details;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import com.lexso.connection.DatabaseConnection;
import javax.swing.JFrame;

/**
 *
 * @author User
 */
public class AllUsersWindow extends javax.swing.JFrame {

    public AllUsersWindow() {
        initComponents();
        int newRowHeight = 35;
        jTable1.setRowHeight(newRowHeight);
        searchTextField.setForeground(new java.awt.Color(150, 150, 150));
        loadUserData("", "yes");
        loadSVGIcons();

        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        jLabel1.setText("<html>"
                + "<div style='font-family: Arial, sans-serif; color: #333; padding: 10px;'>"
                + "<div style='background-color: #f0f0f0; border-radius: 8px; padding: 15px;'>"
                + "<h2 style='color: #2c3e50; margin-bottom: 10px;'>Keyboard Shortcuts:</h2>"
                + "<div style='text-align: center;'>"
                + "<span style='color: #2980b9; font-weight: bold;'>Ctrl + N : <span style='color: #7f8c8d;'>Add User.</span></span>&nbsp;&nbsp;&nbsp;&nbsp;"
                + "<span style='margin-left: 40px; color: #e74c3c; font-weight: bold;'>Del : <span style='color: #7f8c8d;'>Remove User.</span></span>&nbsp;&nbsp;&nbsp;&nbsp;"
                + "<span style='margin-left: 40px; color: #16a085; font-weight: bold;'>Enter : <span style='color: #7f8c8d;'>Show User Details.</span></span>&nbsp;&nbsp;&nbsp;&nbsp;"
                + "<span style='margin-left: 40px; color: #f39c12; font-weight: bold;'>F2 : <span style='color: #7f8c8d;'>Update User.</span></span>&nbsp;&nbsp;&nbsp;&nbsp;"
                + "<span style='margin-left: 40px; color: #8e44ad; font-weight: bold;'>/ : <span style='color: #7f8c8d;'>Focus Search Field.</span></span>"
                + "</div>"
                + "</div>"
                + "</div>"
                + "</html>");
    }

    private void loadSVGIcons() {

        FlatSVGIcon viewIcon = new FlatSVGIcon("com/lexso/users/icon/view.svg", jButton3.getWidth() - 110, jButton3.getHeight() - 20);
        jButton3.setIcon(viewIcon);

        FlatSVGIcon addIcon = new FlatSVGIcon("com/lexso/users/icon/add.svg", jButton4.getWidth() - 90, jButton4.getHeight() - 20);
        jButton4.setIcon(addIcon);

        FlatSVGIcon updateIcon = new FlatSVGIcon("com/lexso/users/icon/update.svg", jButton1.getWidth() - 90, jButton1.getHeight() - 20);
        jButton1.setIcon(updateIcon);

        FlatSVGIcon deleteIcon = new FlatSVGIcon("com/lexso/users/icon/delete.svg", jButton2.getWidth() - 90, jButton2.getHeight() - 20);
        jButton2.setIcon(deleteIcon);
    }

    public void loadUserData(String searchTerm, String bdayAlert) {
        String query = "SELECT "
                + "u.email, "
                + "u.first_name, "
                + "u.last_name, "
                + "u.username, "
                + "u.code, "
                + "u.nic, "
                + "u.birthday, "
                + "u.age, "
                + "u.status, "
                + "u.date_registered, "
                + "u.whatsapp_number, "
                + "u.mobile, "
                + "g.name AS gender_name, "
                + "r.name AS role_name "
                + "FROM "
                + "user u "
                + "INNER JOIN "
                + "role r ON u.role_id = r.id "
                + "INNER JOIN "
                + "gender g ON u.gender_id = g.id "
                + "WHERE "
                + "u.email LIKE '%" + searchTerm + "%' OR "
                + "u.first_name LIKE '%" + searchTerm + "%' OR "
                + "u.last_name LIKE '%" + searchTerm + "%' OR "
                + "u.username LIKE '%" + searchTerm + "%' OR "
                + "u.code LIKE '%" + searchTerm + "%' OR "
                + "u.nic LIKE '%" + searchTerm + "%' OR "
                + "u.status LIKE '%" + searchTerm + "%' OR "
                + "u.whatsapp_number LIKE '%" + searchTerm + "%' OR "
                + "u.mobile LIKE '%" + searchTerm + "%' OR "
                + "g.name LIKE '%" + searchTerm + "%' OR "
                + "r.name LIKE '%" + searchTerm + "%'";

        String sort = String.valueOf(jComboBox1.getSelectedItem());
        query += " ORDER BY ";

        if (sort.equals("Full Name (A to Z)")) {
            query += "u.first_name ASC, u.last_name ASC";
        } else if (sort.equals("Full Name (Z to A)")) {
            query += "u.first_name DESC, u.last_name DESC";
        } else if (sort.equals("Username (A to Z)")) {
            query += "u.username ASC";
        } else if (sort.equals("Username (Z to A)")) {
            query += "u.username DESC";
        } else if (sort.equals("Email (A to Z)")) {
            query += "u.email ASC";
        } else if (sort.equals("Email (Z to A)")) {
            query += "u.email DESC";
        } else if (sort.equals("Mobile Number (Lowest to Highest)")) {
            query += "u.mobile ASC";
        } else if (sort.equals("Mobile Number (Highest to Lowest)")) {
            query += "u.mobile DESC";
        } else if (sort.equals("Role (A to Z)")) {
            query += "r.name ASC";
        } else if (sort.equals("Role (Z to A)")) {
            query += "r.name DESC";
        } else if (sort.equals("Date of Birth (Oldest to Newest)")) {
            query += "u.birthday ASC";
        } else if (sort.equals("Date of Birth (Newest to Oldest)")) {
            query += "u.birthday DESC";
        } else if (sort.equals("Registration Date (Oldest to Newest)")) {
            query += "u.date_registered ASC";
        } else if (sort.equals("Registration Date (Newest to Oldest)")) {
            query += "u.date_registered DESC";
        } else if (sort.equals("Status (A to Z)")) {
            query += "u.status ASC";
        } else if (sort.equals("Status (Z to A)")) {
            query += "u.status DESC";
        }

        try {
            ResultSet resultSet = DatabaseConnection.executeSearch(query);
            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.setRowCount(0);

            LocalDate today = LocalDate.now();
            LocalDate tomorrow = today.plusDays(1);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            StringBuilder todayBirthdays = new StringBuilder();
            StringBuilder tomorrowBirthdays = new StringBuilder();

            while (resultSet.next()) {
                Vector<String> v = new Vector<>();
                v.add(resultSet.getString("first_name") + " " + resultSet.getString("last_name"));
                v.add(resultSet.getString("code"));
                v.add(resultSet.getString("username"));
                v.add(resultSet.getString("email"));
                v.add(resultSet.getString("mobile"));
                v.add(resultSet.getString("role_name"));
                v.add(resultSet.getString("birthday"));
                v.add(resultSet.getString("date_registered"));
                v.add(resultSet.getString("status"));
                model.addRow(v);

                String birthdateStr = resultSet.getString("birthday");
                LocalDate birthdate = LocalDate.parse(birthdateStr, formatter);
                LocalDate thisYearBday = LocalDate.of(today.getYear(), birthdate.getMonth(), birthdate.getDayOfMonth());

                String age = resultSet.getString("age");

                if (thisYearBday.equals(today)) {
                    todayBirthdays.append("<b>").append(resultSet.getString("first_name")).append(" ")
                            .append(resultSet.getString("last_name")).append("</b> (Age: ")
                            .append(age).append(") - ").append(resultSet.getString("code")).append("<br>");
                } else if (thisYearBday.equals(tomorrow)) {
                    tomorrowBirthdays.append("<b>").append(resultSet.getString("first_name")).append(" ")
                            .append(resultSet.getString("last_name")).append("</b> (Age: ")
                            .append(age).append(") - ").append(resultSet.getString("code")).append("<br>");
                }
            }

            jTable1.setModel(model);

            if (bdayAlert.equalsIgnoreCase("yes")) {
                StringBuilder message = new StringBuilder();
                message.append("<html><body style='font-family:sans-serif;'>");

                if (todayBirthdays.length() > 0) {
                    message.append("<h2 style='color:#2196F3;'>Today's Birthdays 🎉</h2>")
                            .append(todayBirthdays)
                            .append("<br>");
                }
                if (tomorrowBirthdays.length() > 0) {
                    message.append("<h2 style='color:#FF9800;'>Tomorrow's Birthdays 🎂</h2>")
                            .append(tomorrowBirthdays);
                }

                message.append("</body></html>");

                if (todayBirthdays.length() > 0 || tomorrowBirthdays.length() > 0) {
                    JPanel panel = new JPanel();
                    JLabel label = new JLabel(message.toString());
                    label.setFont(new Font("Arial", Font.PLAIN, 14));
                    label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                    panel.setLayout(new BorderLayout());
                    panel.setBackground(Color.WHITE);
                    panel.add(label, BorderLayout.CENTER);

                    JOptionPane.showMessageDialog(this, panel, "Birthday Alerts", JOptionPane.PLAIN_MESSAGE);
                }
            }

            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

            for (int i = 0; i < jTable1.getColumnCount(); i++) {
                jTable1.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }

            TableCellRenderer statusRenderer = new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    JLabel label = new JLabel(value.toString());
                    label.setOpaque(true);
                    label.setHorizontalAlignment(SwingConstants.CENTER);

                    if ("Active".equalsIgnoreCase(value.toString())) {
                        label.setBackground(new Color(0, 204, 0));
                        label.setForeground(Color.WHITE);
                    } else if ("Inactive".equalsIgnoreCase(value.toString())) {
                        label.setBackground(new Color(255, 0, 0));
                        label.setForeground(Color.WHITE);
                    } else {
                        label.setBackground(Color.WHITE);
                        label.setForeground(Color.BLACK);
                    }

                    return label;
                }
            };

            jTable1.getColumnModel().getColumn(8).setCellRenderer(statusRenderer);

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
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        searchTextField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jButton1.setBackground(new java.awt.Color(76, 175, 80));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Update");
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(244, 67, 54));
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Inactive");
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(255, 193, 7));
        jButton4.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jButton4.setText("Add");
        jButton4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 11, Short.MAX_VALUE))
        );

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Name", "Code", "Username", "Email Address", "Contact Number ", "Role", "Date of Birth", "Date Registered", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.getTableHeader().setReorderingAllowed(false);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jTable1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTable1KeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        searchTextField.setText("Search anything");
        searchTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                searchTextFieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                searchTextFieldFocusLost(evt);
            }
        });
        searchTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchTextFieldKeyReleased(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 153, 255));
        jLabel2.setText("All Users");

        jLabel3.setForeground(new java.awt.Color(0, 153, 153));
        jLabel3.setText("View and manage all users registered in the system");

        jButton3.setBackground(new java.awt.Color(33, 150, 243));
        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Show More");
        jButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Full Name (A to Z)", "Full Name (Z to A)", "Username (A to Z)", "Username (Z to A)", "Email (A to Z)", "Email (Z to A)", "Mobile Number (Lowest to Highest)", "Mobile Number (Highest to Lowest)", "Role (A to Z)", "Role (Z to A)", "Date of Birth (Oldest to Newest)", "Date of Birth (Newest to Oldest)", "Registration Date (Oldest to Newest)", "Registration Date (Newest to Oldest)", "Status (A to Z)", "Status (Z to A)" }));
        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 100, Short.MAX_VALUE)
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(searchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(searchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 329, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        userUpdate();

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        userInactive();

    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed

        showUserDetails();

    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        addUserProcess();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void searchTextFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_searchTextFieldFocusGained

        if (searchTextField.getText().equals("Search anything")) {
            searchTextField.setText("");
            searchTextField.setForeground(new java.awt.Color(0, 0, 0));
        }
    }//GEN-LAST:event_searchTextFieldFocusGained

    private void searchTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_searchTextFieldFocusLost

        if (searchTextField.getText().equals("")) {
            searchTextField.setText("Search anything");
            searchTextField.setForeground(new java.awt.Color(150, 150, 150));
        }
    }//GEN-LAST:event_searchTextFieldFocusLost

    private void searchTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchTextFieldKeyReleased
        String search = searchTextField.getText();
        loadUserData(search, "");
    }//GEN-LAST:event_searchTextFieldKeyReleased

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        if (evt.getClickCount() == 2) {
            showUserDetails();
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jTable1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable1KeyPressed
        if (evt.getKeyCode() == evt.VK_DELETE) {
            userInactive();
        } else if (evt.getKeyCode() == evt.VK_ENTER) {
            showUserDetails();
        } else if (evt.getKeyCode() == evt.VK_F2) {
            userUpdate();
        } else if (evt.getKeyCode() == evt.VK_SLASH) {
            searchTextField.grabFocus();
        }
    }//GEN-LAST:event_jTable1KeyPressed

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged

        String search = searchTextField.getText();

        if (search.contains("Search anything")) {
            loadUserData("", "");
        } else {
            loadUserData(search, "");
        }
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void showUserDetails() {
        int selectedRow = jTable1.getSelectedRow();

        if (selectedRow != -1) {
            String email = String.valueOf(jTable1.getValueAt(selectedRow, 3));

            try {
                ResultSet resultSet = DatabaseConnection.executeSearch("SELECT * FROM `user` "
                        + "INNER JOIN `role` ON `user`.`role_id` = `role`.`id` "
                        + "INNER JOIN `gender` ON `user`.`gender_id` = `gender`.`id` "
                        + "WHERE `email` = '" + email + "'");

                if (resultSet.next()) {
                    String profilePicPath = resultSet.getString("profile_picture");
                    String signaturePath = resultSet.getString("signature");

                    AddOrUpdateUsers addOrUpdateUsers = new AddOrUpdateUsers(
                            this,
                            true,
                            "view",
                            profilePicPath,
                            signaturePath
                    );
                    addOrUpdateUsers.titleLabel().setText("View User Details");
                    addOrUpdateUsers.roleBox().setSelectedItem(resultSet.getString("role.name"));
                    addOrUpdateUsers.genderBox().setSelectedItem(resultSet.getString("gender.name"));
                    addOrUpdateUsers.statusBox().setSelectedItem(resultSet.getString("status"));
                    addOrUpdateUsers.fName().setText(resultSet.getString("first_name"));
                    addOrUpdateUsers.lName().setText(resultSet.getString("last_name"));
                    addOrUpdateUsers.codeField().setText(resultSet.getString("code"));
                    addOrUpdateUsers.usernameField().setText(resultSet.getString("username"));
                    addOrUpdateUsers.emailField().setText(email);
                    addOrUpdateUsers.whatsappField().setText(resultSet.getString("whatsapp_number"));
                    addOrUpdateUsers.mobileField().setText(resultSet.getString("mobile"));
                    addOrUpdateUsers.nicField().setText(resultSet.getString("nic"));

                    String bdayString = resultSet.getString("birthday");
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date bdayDate = dateFormat.parse(bdayString);
                    addOrUpdateUsers.bdayChooser().setDate(bdayDate);
                    addOrUpdateUsers.registerField().setText(resultSet.getString("date_registered"));

                    addOrUpdateUsers.setVisible(true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a row from the table.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void userInactive() {
        int selectedRow = jTable1.getSelectedRow();

        if (selectedRow != -1) {
            String userEmail = jTable1.getValueAt(selectedRow, 3).toString();

            try {
                ResultSet rs = DatabaseConnection.executeSearch("SELECT `status` FROM `user` WHERE `email` = '" + userEmail + "'");
                String currentStatus = "";
                if (rs.next()) {
                    currentStatus = rs.getString("status");
                }
                rs.close();

                if (currentStatus.equals("Inactive")) {
                    JOptionPane.showMessageDialog(this, "User with email " + userEmail + " is already inactive. 😔", "Status Update", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                int confirmation = JOptionPane.showConfirmDialog(this,
                        "Are you sure you want to *inactivate* the user with email " + userEmail + "? They will no longer be able to log in. 🚫",
                        "Confirm User Inactivation",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);

                if (confirmation == JOptionPane.YES_OPTION) {
                    DatabaseConnection.executeIUD("UPDATE `user` SET `status` = 'Inactive' WHERE `email` = '" + userEmail + "'"); // Corrected to 'Inactive'
                    JOptionPane.showMessageDialog(this, "User with email " + userEmail + " has been *inactivated* successfully. 🎉", "Inactivation Successful", JOptionPane.INFORMATION_MESSAGE);

                    String search = searchTextField.getText();
                    loadUserData(search.contains("Search anything") ? "" : search, "");
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "An error occurred while attempting to inactivate the user: " + e.getMessage() + " ❌", "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "An unexpected error occurred. ⚠️", "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a user from the list to inactivate. 👆", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void userUpdate() {
        int selectedRow = jTable1.getSelectedRow();

        if (selectedRow != -1) {
            String email = String.valueOf(jTable1.getValueAt(selectedRow, 3));

            try {
                ResultSet resultSet = DatabaseConnection.executeSearch("SELECT * FROM `user` "
                        + "INNER JOIN `role` ON `user`.`role_id` = `role`.`id` "
                        + "INNER JOIN `gender` ON `user`.`gender_id` = `gender`.`id` "
                        + "WHERE `email` = '" + email + "'");

                if (resultSet.next()) {

                    String profilePicPath = resultSet.getString("profile_picture");
                    String signaturePath = resultSet.getString("signature");

                    AddOrUpdateUsers addOrUpdateUsers = new AddOrUpdateUsers(
                            this,
                            true,
                            "update",
                            profilePicPath,
                            signaturePath
                    );
                    addOrUpdateUsers.titleLabel().setText("Update User Details");
                    addOrUpdateUsers.roleBox().setSelectedItem(resultSet.getString("role.name"));
                    addOrUpdateUsers.genderBox().setSelectedItem(resultSet.getString("gender.name"));
                    addOrUpdateUsers.statusBox().setSelectedItem(resultSet.getString("status"));
                    addOrUpdateUsers.fName().setText(resultSet.getString("first_name"));
                    addOrUpdateUsers.lName().setText(resultSet.getString("last_name"));
                    addOrUpdateUsers.codeField().setText(resultSet.getString("code"));
                    addOrUpdateUsers.usernameField().setText(resultSet.getString("username"));
                    addOrUpdateUsers.emailField().setText(email);
                    addOrUpdateUsers.whatsappField().setText(resultSet.getString("whatsapp_number"));
                    addOrUpdateUsers.mobileField().setText(resultSet.getString("mobile"));
                    addOrUpdateUsers.nicField().setText(resultSet.getString("nic"));

                    String bdayString = resultSet.getString("birthday");
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date bdayDate = dateFormat.parse(bdayString);
                    addOrUpdateUsers.bdayChooser().setDate(bdayDate);
                    addOrUpdateUsers.registerField().setText(resultSet.getString("date_registered"));

                    addOrUpdateUsers.setVisible(true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a row from the table.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }

    private ImageIcon scaleImage(String imagePath, JLabel label) {
        ImageIcon icon = new ImageIcon(imagePath);
        Image scaledImage = icon.getImage().getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    private ImageIcon scaleImage(URL imageUrl, JLabel label) {
        ImageIcon icon = new ImageIcon(imageUrl);
        Image scaledImage = icon.getImage().getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    public void addUserProcess() {
        String profilePicPath = null;
        String signaturePath = null;

        AddOrUpdateUsers addOrUpdateUsers = new AddOrUpdateUsers(
                this,
                true,
                "add",
                profilePicPath,
                signaturePath
        );
        addOrUpdateUsers.titleLabel().setText("Add User");
        addOrUpdateUsers.setVisible(true);
    }

    private void formKeyPressed(KeyEvent evt) {
        if (evt.isControlDown() && evt.getKeyCode() == KeyEvent.VK_N) {
            addUserProcess();
        } else if (evt.getKeyCode() == evt.VK_DELETE) {
            userInactive();
        } else if (evt.getKeyCode() == evt.VK_ENTER) {
            showUserDetails();
        } else if (evt.getKeyCode() == evt.VK_F2) {
            userUpdate();
        } else if (evt.getKeyCode() == evt.VK_SLASH) {
            searchTextField.grabFocus();
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField searchTextField;
    // End of variables declaration//GEN-END:variables
}
