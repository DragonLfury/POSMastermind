package com.lexso.inventory.stock;

import com.lexso.connection.ShaneConnection;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;

public class SingleGrnViewEdit extends javax.swing.JDialog {

    private String id;

    public SingleGrnViewEdit(java.awt.Frame parent, boolean modal, String id) {
        super(parent, modal);
        initComponents();
        this.id = id;
        loadInventory(this.id);
        grnTotal();
        paidTotal();
    }

    private void grnTotal() {

        double total = 0.0;

        for (int row = 0; row < jTable1.getRowCount(); row++) {
            Object value = jTable1.getValueAt(row, 10); // Column index 10 (11th column)

            if (value != null) {
                try {
                    double number = Double.parseDouble(value.toString());
                    total += number;
                } catch (NumberFormatException e) {
                    // Optional: Handle non-numeric data
                    System.out.println("Invalid number at row " + row);
                }
            }
        }

       jLabel5.setText(String.valueOf(total));

    }
    
     private void paidTotal() {

        double total = 0.0;

        for (int row = 0; row < jTable1.getRowCount(); row++) {
            Object value = jTable1.getValueAt(row, 11); // Column index 10 (11th column)

            if (value != null) {
                try {
                    double number = Double.parseDouble(value.toString());
                    total += number;
                } catch (NumberFormatException e) {
                    // Optional: Handle non-numeric data
                    System.out.println("Invalid number at row " + row);
                }
            }
        }

       jLabel7.setText(String.valueOf(total));

    }


    private void loadInventory(String id) {
        try {
            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
            dtm.setRowCount(0);

            String query
                    = "SELECT DISTINCT "
                    + "p.id AS product_id, "
                    + "s.id AS stock_id, "
                    + "p.name, "
                    + "s.barcode, "
                    + "c.category_name, "
                    + "s.selling_price, "
                    + "s.mft, "
                    + "s.exp, "
                    + "s.exp_warning_days, "
                    + "s.buying_price, "
                    + "s.qty, "
                    + "s.low_stock_warning, "
                    + "s.discount_type, "
                    + "s.discount_value, "
                    + "s.discount_start_date, "
                    + "s.discount_end_date, "
                    + "(s.qty * s.selling_price) AS item_total, "
                    + "COALESCE(SUM(gi.price * gi.qty), 0) AS paid_amount "
                    + "FROM product p "
                    + "INNER JOIN stock s ON p.id = s.product_id "
                    + "INNER JOIN category_has_brand chb ON p.category = chb.category_has_brand_id "
                    + "INNER JOIN category c ON chb.category_category_id = c.id "
                    + "INNER JOIN grn_item gi ON s.id = gi.stock_id "
                    + "INNER JOIN grn g ON gi.grn_id = g.id "
                    + "WHERE g.id = '" + id + "' "
                    + "GROUP BY p.id, s.id, p.name, s.barcode, c.category_name, s.selling_price, s.mft, s.exp, s.exp_warning_days, s.buying_price, s.qty, s.low_stock_warning, s.discount_type, s.discount_value, s.discount_start_date, s.discount_end_date";

            ResultSet resultSet = ShaneConnection.executeSearch(query);
            while (resultSet.next()) {
                Vector vector = new Vector();
                vector.add(resultSet.getString("name"));               // Product name
                vector.add(resultSet.getString("stock_id"));           // Stock ID
                vector.add(resultSet.getString("mft"));                // Manufacture date
                vector.add(resultSet.getString("exp"));                // Expiry date
                vector.add(resultSet.getString("exp_warning_days"));   // EXP warning days (allows null)
                vector.add(resultSet.getString("qty"));                // Quantity
                vector.add(resultSet.getString("low_stock_warning"));  // Low stock warning
                // Format discount based on type
                String discountType = resultSet.getString("discount_type");
                String discountValue = resultSet.getString("discount_value");
                String formattedDiscount = discountValue;
                if (discountType != null && discountValue != null) {
                    if (discountType.equals("percentage")) {
                        formattedDiscount = discountValue + "%";
                    } else if (discountType.equals("fixed")) {
                        formattedDiscount = "LKR " + discountValue;
                    }
                }
                vector.add(formattedDiscount);                         // Discount (formatted)
                vector.add(resultSet.getString("discount_start_date")); // Discount start date
                vector.add(resultSet.getString("discount_end_date"));   // Discount end date
                vector.add(resultSet.getString("item_total"));          // Item total (qty * selling_price)
                vector.add(resultSet.getString("paid_amount"));         // Paid amount (from GRN items)

                dtm.addRow(vector);
            }
            resultSet.close();
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

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Product Name", "Stock ID", "MFD Date", "EXP Date", "EXP Warning", "QTY", "Low Stock Warning", "Discount", "Discount Start", "Discount End", "Item total", "Paid Amount"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false
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

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setText("Single GRN View");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setText("GRN Total");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setText("...");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setText("Total Paid");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel7.setText("...");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 881, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(2, 2, 2)))
                .addContainerGap(46, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 440, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7))
                .addContainerGap(192, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked

    }//GEN-LAST:event_jTable1MouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SingleGrnViewEdit.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SingleGrnViewEdit.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SingleGrnViewEdit.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SingleGrnViewEdit.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                SingleGrnViewEdit dialog = new SingleGrnViewEdit(new javax.swing.JFrame(), true, "");
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
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
