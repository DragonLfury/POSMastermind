package com.lexso.backup;

import com.formdev.flatlaf.themes.FlatMacLightLaf;
import com.lexso.connection.DatabaseConnection;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;

public class BackupWindow extends javax.swing.JFrame {

    public BackupWindow() {
        initComponents();
        loadBackupLogsToTableByDate(null); // Changed to null for initial load
    }

    private void reset() {
        jTable1.clearSelection();
        jDateChooser4.setDate(null);
        loadBackupLogsToTableByDate(null); // Changed to null for initial load
    }

    // Inside com.lexso.backup.BackupWindow class
    private void confirmAndRestoreBackup(String fileName) {
        int choice = JOptionPane.showConfirmDialog(null,
                "Do you want to restore the backup:\n" + fileName + " ?\n This will delete all current data.",
                "Restore Backup", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (choice == JOptionPane.YES_OPTION) {
            java.sql.Connection conn = null;
            java.sql.Statement stmt = null;
            java.sql.ResultSet rs = null;

            try {
                // First, close the connection in DatabaseConnection if it's open,
                // to ensure a fresh connection is made for the restore sequence
                // and later for reloading backup logs.
                if (DatabaseConnection.connection != null && !DatabaseConnection.connection.isClosed()) {
                    DatabaseConnection.connection.close();
                }
                com.lexso.connection.DatabaseConnection.createConnection(); // Re-establish a clean connection
                conn = com.lexso.connection.DatabaseConnection.connection; // Access the static connection directly

                stmt = conn.createStatement();
                stmt.execute("SET FOREIGN_KEY_CHECKS=0;");
                stmt.close();

                // 2. Clear current database data by truncating tables, EXCLUDING 'backup_logs'
                stmt = conn.createStatement();
                rs = stmt.executeQuery("SHOW TABLES;");

                java.util.List<String> tableNamesToTruncate = new java.util.ArrayList<>();
                while (rs.next()) {
                    String tableName = rs.getString(1);
                    // EXCLUDE the backup_logs table from truncation
                    if (!tableName.equalsIgnoreCase("backup_logs")) {
                        tableNamesToTruncate.add(tableName);
                    }
                }
                rs.close();
                stmt.close();

                for (String tableName : tableNamesToTruncate) {
                    stmt = conn.createStatement();
                    stmt.execute("TRUNCATE TABLE `" + tableName + "`;");
                    stmt.close();
                }

                // 3. Restore from the selected backup file using mysql.exe
                String restoreFile = "backup/" + fileName;
                String mysqlExePath = "C:\\Program Files\\MySQL\\MySQL Server 8.0\\bin\\mysql.exe";
                String databaseName = "u821149722_lexso";
                String username = "root";
                String password = "password";

                ProcessBuilder pb = new ProcessBuilder(
                        mysqlExePath,
                        "-u", username,
                        "-p" + password,
                        databaseName
                );
                pb.redirectInput(new File(restoreFile));
                pb.redirectErrorStream(true);

                Process process = pb.start();
                int processComplete = process.waitFor();

                StringBuilder output = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        output.append(line).append("\n");
                    }
                }

                if (processComplete == 0) {
                    JOptionPane.showMessageDialog(null, "Database restored successfully from:\n" + fileName + " 🎉");

                    // IMPORTANT: Re-establish connection *before* reloading backup logs
                    // This ensures loadBackupLogsToTableByDate gets a fresh connection
                    // and accurately reads the *current* state of the backup_logs table.
                    if (DatabaseConnection.connection != null && !DatabaseConnection.connection.isClosed()) {
                        DatabaseConnection.connection.close();
                    }
                    DatabaseConnection.createConnection(); // Force a fresh connection for loadBackupLogsToTableByDate

                    loadBackupLogsToTableByDate(null); // Now this should correctly load the *actual* current logs
                    reset(); // This also calls loadBackupLogsToTableByDate(null), but the above ensures connection is fresh
                } else {
                    JOptionPane.showMessageDialog(null, "Restore failed. ❌\nError details:\n" + output.toString(), "Restore Error", JOptionPane.ERROR_MESSAGE);
                }

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error during restore: " + e.getMessage() + " 🐛", "Error", JOptionPane.ERROR_MESSAGE);
            } finally {
                try {
                    if (conn != null && !conn.isClosed()) {
                        stmt = conn.createStatement();
                        stmt.execute("SET FOREIGN_KEY_CHECKS=1;");
                        stmt.close();
                    }
                } catch (java.sql.SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Could not re-enable foreign key checks. Manual check needed. ⚠️", "Warning", JOptionPane.WARNING_MESSAGE);
                } finally {
                    if (conn != null) {
                        try {
                            conn.close();
                        } catch (java.sql.SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    private void loadBackupLogsToTableByDate(String date) {
        try {
            String query;
            if (date == null || date.isEmpty()) {
                query = "SELECT * FROM `backup_logs` ORDER BY `backup_id` DESC";
            } else {
                query = "SELECT * FROM `backup_logs` WHERE DATE(`backup_time`) = '" + date + "' ORDER BY `backup_id` DESC";
            }
            ResultSet rs = DatabaseConnection.executeSearch(query);

            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.setRowCount(0);

            while (rs.next()) {
                Vector<String> row = new Vector<>();
                row.add(rs.getString("backup_id"));
                row.add(rs.getString("backup_time"));
                row.add(rs.getString("backup_file"));
                model.addRow(row);
            }
            if (rs != null) {
                rs.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading backups by date. 🐞");
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jDateChooser4 = new com.toedter.calendar.JDateChooser();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel6.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabel6.setText("Backup Database");

        jButton4.setBackground(new java.awt.Color(0, 51, 255));
        jButton4.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("Backup");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Poppins", 1, 18)); // NOI18N
        jLabel7.setText("Restore Data");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Backup Id", "Backup Time", "Backupfile"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
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

        jDateChooser4.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser4PropertyChange(evt);
            }
        });

        jButton5.setBackground(new java.awt.Color(0, 51, 255));
        jButton5.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        jButton5.setForeground(new java.awt.Color(255, 255, 255));
        jButton5.setText("Clear");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setBackground(new java.awt.Color(0, 51, 255));
        jButton6.setFont(new java.awt.Font("Poppins", 1, 14)); // NOI18N
        jButton6.setForeground(new java.awt.Color(255, 255, 255));
        jButton6.setText("Backup");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 801, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jDateChooser4, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(35, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(58, 58, 58)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jDateChooser4, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton5)
                            .addComponent(jButton6))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 13, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        try {
            File backupFolder = new File("backup");
            if (!backupFolder.exists()) {
                backupFolder.mkdir();
            }

            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String backupFileName = "lexso_" + timestamp + ".sql";
            String backupFilePath = "backup/" + backupFileName;

            String dumpCommand = "\"C:\\Program Files\\MySQL\\MySQL Server 8.0\\bin\\mysqldump.exe\" -u root -ppassword u821149722_lexso -r \"" + backupFilePath + "\"";

            Process process = Runtime.getRuntime().exec(dumpCommand);
            int processComplete = process.waitFor();

            if (processComplete == 0) {
                String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                String insertQuery = "INSERT INTO backup_logs (backup_time, backup_file) VALUES ('" + currentTime + "', '" + backupFileName + "')";

                try {
                    DatabaseConnection.executeIUD(insertQuery);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Backup created, but failed to log in DB.\n" + ex.getMessage() + " ⚠️");
                    return;
                }

                JOptionPane.showMessageDialog(null, "Backup created and logged:\n" + backupFilePath + " ✅");
                loadBackupLogsToTableByDate(null);
                reset();
            } else {
                JOptionPane.showMessageDialog(null, "Backup failed. ❌");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error during backup: " + e.getMessage() + " 🐛");
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        if (evt.getClickCount() == 2) {
            int row = jTable1.getSelectedRow();
            if (row != -1) {
                String fileName = jTable1.getValueAt(row, 2).toString();
                confirmAndRestoreBackup(fileName);
            }
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jDateChooser4PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser4PropertyChange
        if ("date".equals(evt.getPropertyName()) && jDateChooser4.getDate() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String selectedDate = sdf.format(jDateChooser4.getDate());
            loadBackupLogsToTableByDate(selectedDate);
        } else if ("date".equals(evt.getPropertyName()) && jDateChooser4.getDate() == null) {
            loadBackupLogsToTableByDate(null);
        }
    }//GEN-LAST:event_jDateChooser4PropertyChange

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        reset();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        int row = jTable1.getSelectedRow();
        if (row != -1) {
            String fileName = jTable1.getValueAt(row, 2).toString();
            confirmAndRestoreBackup(fileName);
        } else {
            JOptionPane.showMessageDialog(null, "Please select a backup file to restore! 👆", "Info", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton6ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private com.toedter.calendar.JDateChooser jDateChooser4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
