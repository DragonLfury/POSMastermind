package com.lexso.inventory.stock;

import java.awt.Image;
import java.net.URL;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.Vector;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.plaf.metal.MetalBorders;
import javax.swing.table.DefaultTableModel;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;
import javax.swing.ImageIcon;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.lexso.connection.DatabaseConnection;
import com.lexso.files.FileUploadService;
import com.lexso.util.CurrentUser;
import java.io.File;
import java.nio.file.Paths;
import com.lexso.whatsapp.WhatsAppService;

/**
 *
 * @author champ
 */
public class ReorderStock extends javax.swing.JFrame {

    /**
     * Creates new form ReorderStock
     */
    String productName;
    String barcode;
    String supplier;
    String qty;
    String arrivalDate;

    public String query;
    public String query1;
    private static HashMap<String, String> supplierMap = new HashMap<>();
    public String query2;
    public String query3;
    public String query4;
    public String query5;
    
    
    private String loggedInUsername = CurrentUser.getUsername();

    public boolean isValidint(String number) {
        try {
            Integer.parseInt(number);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean isValidDouble(String dNumber) {
        try {
            Double.parseDouble(dNumber);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void loadReorderHistory() {
        query3 = "SELECT * FROM stock_reorder";
        try {
            ResultSet resultSet = DatabaseConnection.executeSearch(query3);
            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
            dtm.setRowCount(0);

            while (resultSet.next()) {

                Vector vector = new Vector();
                vector.add(resultSet.getString("stock_id"));
                vector.add(resultSet.getString("id"));
                vector.add(resultSet.getString("reorder_date"));
                vector.add(resultSet.getString("arrival_date"));
                vector.add(resultSet.getString("order_amount"));
                vector.add(resultSet.getString("status"));
                dtm.addRow(vector);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadReorderItem(String barcode) {

        try {
            query = "SELECT s.qty,p.name,p.product_image FROM "
                    + "stock s INNER JOIN product p ON s.product_id = p.id WHERE s.barcode = '" + barcode + "'";

            ResultSet resultSet = DatabaseConnection.executeSearch(query);

            if (resultSet.next()) {
                jLabel7.setText(resultSet.getString("name"));
                jLabel9.setText(barcode);
                jLabel11.setText(resultSet.getString("qty"));
                String productImage = resultSet.getString("product_image");
                setProductImage(productImage);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setProductImage(String imageName) {
        String basePath = "src/product-image/";
        String[] extensions = {".png", ".jpg", ".jpeg", ".svg"};

        File imageFile = null;
        String resolvedName = imageName;

        // Auto-resolve extension if missing
        boolean found = false;
        if (!imageName.contains(".")) {
            for (String ext : extensions) {
                File testFile = new File(basePath, imageName + ext);
                if (testFile.exists()) {
                    resolvedName = imageName + ext;
                    imageFile = testFile;
                    found = true;
                    break;
                }
            }
        } else {
            imageFile = new File(basePath, imageName);
            found = imageFile.exists();
        }

        if (!found) {
            System.out.println("Image not found: " + new File(basePath, imageName).getPath() + ". Trying default image...");
            imageFile = new File(basePath, "default-product.png");
            resolvedName = "default-product.png";
            if (!imageFile.exists()) {
                System.out.println("Default image also missing: " + imageFile.getPath());
                jLabel2.setIcon(null);
                return;
            }
        }

        try {
            if (resolvedName.toLowerCase().endsWith(".svg")) {
                String resourcePath = "/" + basePath + "/" + resolvedName;
                FlatSVGIcon svgIcon = new FlatSVGIcon(resourcePath, jLabel2.getWidth(), jLabel2.getHeight());
                jLabel2.setIcon(svgIcon);
            } else {
                ImageIcon icon = new ImageIcon(imageFile.getAbsolutePath());
                Image img = icon.getImage().getScaledInstance(jLabel2.getWidth(), jLabel2.getHeight(), Image.SCALE_SMOOTH);
                jLabel2.setIcon(new ImageIcon(img));
            }
        } catch (Exception e) {
            e.printStackTrace();
            jLabel2.setIcon(null);
        }
    }

    private void loadSuppliers(String barcode) {
        try {
            System.out.println(barcode);
            query1 = "SELECT sup.first_name, sup.last_name, com.name, sup.mobile "
                    + "FROM supplier sup INNER JOIN "
                    + "company com ON sup.company_id = com.id "
                    + "INNER JOIN grn ON sup.mobile = grn.supplier_mobile "
                    + "INNER JOIN grn_item gi ON gi.grn_id = grn.id "
                    + "INNER JOIN stock s ON s.id = gi.stock_id "
                    + "WHERE s.barcode = '" + barcode + "'";

            ResultSet resultSet1 = DatabaseConnection.executeSearch(query1);
            Vector<String> vector = new Vector<>();
            vector.add("Select");

            while (resultSet1.next()) {
                String ddOption = (resultSet1.getString("first_name") != null ? resultSet1.getString("first_name") : "") + " "
                        + (resultSet1.getString("last_name") != null ? resultSet1.getString("last_name") : "") + "-"
                        + (resultSet1.getString("name") != null ? resultSet1.getString("name") : "");
                vector.add(ddOption);

                supplierMap.put(ddOption, resultSet1.getString("mobile"));
            }
            DefaultComboBoxModel dcbm = new DefaultComboBoxModel(vector);
            jComboBox1.setModel(dcbm);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ReorderStock(String productName, String barcode, String category, String expDate, String price, String qty) {

        initComponents();
        loadReorderItem(barcode);
        loadSuppliers(barcode);
        loadReorderHistory();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel18 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jTextField2 = new javax.swing.JTextField();
        jComboBox2 = new javax.swing.JComboBox<>();
        jComboBox3 = new javax.swing.JComboBox<>();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jDateChooser3 = new com.toedter.calendar.JDateChooser();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jCheckBox3 = new javax.swing.JCheckBox();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jComboBox4 = new javax.swing.JComboBox<>();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jLabel26 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel3.setText("Stock Reordering");

        jLabel4.setForeground(new java.awt.Color(84, 84, 84));
        jLabel4.setText("Low On Stock ?  Reorder right now.");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("Reorder Item");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setText("Search:");

        jLabel7.setText("Product Name");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel8.setText("Search on:");

        jLabel9.setText("Barcode");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel10.setText("Stock Remaining:");

        jLabel11.setText("Remaining Stock");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel12.setText("Reorder Amount:");

        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel13.setText("Buying Price:");

        jLabel14.setText("Please Select Supplier");

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel15.setText("Total Amount:");

        jLabel16.setText("Please Select Supplier");

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel17.setText("Stock Arrival Before");

        jDateChooser1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser1PropertyChange(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(255, 51, 51));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton1.setText("Clear All");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(255, 153, 0));
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton2.setText("Cancel");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(51, 0, 255));
        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton3.setText("Reorder");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel6.setText("Order History");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Stock ID", "Order ID", "Order Date", "Stock Arrival Date", "Order Amount", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
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

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel18.setText("Select Supplier:");

        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });

        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField2KeyReleased(evt);
            }
        });

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Stock ID", "Order ID", "Order Amount" }));
        jComboBox2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox2ItemStateChanged(evt);
            }
        });

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Stock ID Earliest", "Stock ID Latest", "Order ID Earliest", "Order ID Latest", "Order Date Earliest", "Order Date Latest", "Stock Arrival Date Latest", "Stock Arrival Date Earliest", "Order Net Amount Highest", "Order Net Amount Lowest" }));
        jComboBox3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox3ItemStateChanged(evt);
            }
        });

        jDateChooser2.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser2PropertyChange(evt);
            }
        });

        jDateChooser3.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser3PropertyChange(evt);
            }
        });

        jCheckBox1.setText("recived");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jCheckBox2.setText("Pending");
        jCheckBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox2ActionPerformed(evt);
            }
        });

        jCheckBox3.setText("Canceled");
        jCheckBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox3ActionPerformed(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel19.setText("Product Name:");

        jLabel20.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel20.setText("Barcode:");

        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel21.setText("Sort By:");

        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Order Date", "Stock Arrival Date" }));
        jComboBox4.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox4ItemStateChanged(evt);
            }
        });

        jLabel22.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel22.setText("Search after:");

        jLabel23.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel23.setText("Search Before:");

        jLabel24.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel24.setText("Search On Dates of:");

        jLabel25.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel25.setText("Order Status Show:");

        jButton4.setBackground(new java.awt.Color(255, 51, 0));
        jButton4.setText("Clear All");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setBackground(new java.awt.Color(255, 51, 0));
        jButton5.setText("Cancel Order");
        jButton5.setEnabled(false);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jLabel26.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel26.setText("Cancel Selected Order:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(5, 5, 5)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(9, 9, 9)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 747, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(31, 31, 31)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel8)
                                                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(34, 34, 34)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(jLabel21)
                                                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                            .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jLabel22))
                                                .addGap(27, 27, 27)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jDateChooser3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jLabel23))
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel24)
                                                    .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel25)
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addComponent(jCheckBox1)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(jCheckBox2)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(jCheckBox3)))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 20, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addGap(8, 8, 8)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel19)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel20)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(jLabel11))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel18)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(jLabel14))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel15)
                            .addComponent(jLabel16))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jDateChooser1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(31, 31, 31)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel5)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel8)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel21)
                                .addGap(28, 28, 28)))
                        .addGap(17, 17, 17)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel22)
                                    .addComponent(jLabel23))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jDateChooser3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel24)
                                        .addGap(28, 28, 28))
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jCheckBox1)
                                        .addComponent(jCheckBox3)
                                        .addComponent(jCheckBox2)
                                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel25)
                                        .addGap(27, 27, 27)))
                                .addGap(1, 1, 1)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 439, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(16, 16, 16))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox2ActionPerformed
        search();
    }//GEN-LAST:event_jCheckBox2ActionPerformed

    private void sendEmail(String to, String subject, String body) {

        String fromEmail = "lexsopos@gmail.com";
        String password = "xhdv lyge baly ladh";
        String toEmail = to;

        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        // Session 
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {

            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(fromEmail));
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            msg.setSubject(subject);
            msg.setText("Product reorder for" + productName + "/order QTY = " + qty + "");

            Transport.send(msg);
            JOptionPane.showMessageDialog(null, "Email sent successfully!");

        } catch (MessagingException e) {
            JOptionPane.showMessageDialog(null, "Failed to send email: " + e.getMessage());
        }
    }

    private void sendCancelEmail(String to, String subject, String body, String productName, String qty, String orderDate) {

        String fromEmail = "geekhirusha@gmail.com";
        String password = "sltqhvxghjmpztlt";
        String toEmail = to;

        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        // Session 
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {

            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(fromEmail));
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            msg.setSubject(subject);
            msg.setText("Cancel Order for " + productName + "/n on  " + orderDate + "  with an order QTY of " + qty);

            Transport.send(msg);
            JOptionPane.showMessageDialog(null, "Email sent successfully!");

        } catch (MessagingException e) {
            JOptionPane.showMessageDialog(null, "Failed to send email: " + e.getMessage());
        }
    }

    public void calculateAmount() {
        String selectedSupplier = String.valueOf(jComboBox1.getSelectedItem());

        if (!selectedSupplier.equals("Select")) {

            try {

                String supplierMobile = supplierMap.get(selectedSupplier);
                query2 = "SELECT s.buying_price FROM stock s"
                        + " INNER JOIN grn_item gi ON gi.stock_id = s.id "
                        + "INNER JOIN grn ON grn.id = gi.grn_id "
                        + " WHERE supplier_mobile = '" + supplierMobile + "'";
                ResultSet resultSet = DatabaseConnection.executeSearch(query2);
                if (resultSet.next()) {
                    jLabel14.setText(resultSet.getString("buying_price"));

                    String stockQty = jTextField1.getText();
                    if (stockQty.isEmpty() && !jLabel14.equals("Please Select Supplier")) {
                        jLabel16.setText("Please Enter Required qty");
                    } else if (!stockQty.equals("Please Select Supplier") && isValidint(stockQty)) {
                        Double amount = Double.parseDouble(stockQty) * Double.parseDouble(resultSet.getString("buying_price"));
                        String amountString = amount.toString();
                        jLabel16.setText(amountString);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (selectedSupplier.equals("Select")) {
            jLabel14.setText("Please Select Supplier");
            jLabel16.setText("Please Select Supplier");
        }
    }

    private void search() {
        String searchTerm = jTextField2.getText();
        String searchSort = String.valueOf(jComboBox2.getSelectedItem());
        String sortBy = String.valueOf(jComboBox3.getSelectedItem());

        Date startDateObj = jDateChooser2.getDate();
        Date endDateObj = jDateChooser3.getDate();
        String startDate = (startDateObj != null) ? new SimpleDateFormat("yyyy-MM-dd").format(startDateObj) : null;
        String endDate = (endDateObj != null) ? new SimpleDateFormat("yyyy-MM-dd").format(endDateObj) : null;

        String sortDate = String.valueOf(jComboBox4.getSelectedItem());
        Boolean recived = jCheckBox1.isSelected();
        Boolean pending = jCheckBox2.isSelected();
        Boolean canceled = jCheckBox3.isSelected();

        String query = "SELECT * FROM stock_reorder WHERE 1=1";

//    search term and column filter
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            switch (searchSort) {
                case "Stock ID":
                    query += " AND stock_id LIKE '%" + searchTerm + "%'";
                    break;
                case "Order ID":
                    query += " AND id LIKE '%" + searchTerm + "%'";
                    break;
                case "Order Amount":
                    query += " AND order_amount LIKE '%" + searchTerm + "%'";
                    break;
            }
        }

        // status filters
        boolean hasStatusFilter = false;
        if (recived) {
            query += " AND status = 'received'";
            hasStatusFilter = true;
        }
        if (pending) {
            query += " AND status = 'pending'";
            hasStatusFilter = true;
        }
        if (canceled) {
            query += " AND status = 'canceled'";
            hasStatusFilter = true;
        }

        // date filters
        if (sortDate.equals("Order Date")) {
            if (startDate != null) {
                query += " AND reorder_date >= '" + startDate + "'";
            }
            if (endDate != null) {
                query += " AND reorder_date <= '" + endDate + "'";
            }
        } else if (sortDate.equals("Stock Arrival Date")) {
            if (startDate != null) {
                query += " AND arrival_date >= '" + startDate + "'";
            }
            if (endDate != null) {
                query += " AND arrival_date <= '" + endDate + "'";
            }
        }

        // Sorting
        switch (sortBy) {
            case "Stock ID Earliest":
                query += " ORDER BY stock_id ASC";
                break;
            case "Stock ID Latest":
                query += " ORDER BY stock_id DESC";
                break;
            case "Order ID Earliest":
                query += " ORDER BY id ASC";
                break;
            case "Order ID Latest":
                query += " ORDER BY id DESC";
                break;
            case "Order Date Earliest":
                query += " ORDER BY reorder_date ASC";
                break;
            case "Order Date Latest":
                query += " ORDER BY reorder_date DESC";
                break;
            case "Stock Arrival Date Latest":
                query += " ORDER BY arrival_date DESC";
                break;
            case "Stock Arrival Date Earliest":
                query += " ORDER BY arrival_date ASC";
                break;
            case "Order Net Amount Highest":
                query += " ORDER BY order_amount DESC";
                break;
            case "Order Net Amount Lowest":
                query += " ORDER BY order_amount ASC";
                break;
        }

        try {
            ResultSet resultSet = DatabaseConnection.executeSearch(query);
            DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
            dtm.setRowCount(0);

            while (resultSet.next()) {
                Vector<String> vector = new Vector<>();
                vector.add(resultSet.getString("stock_id"));
                vector.add(resultSet.getString("id"));
                vector.add(resultSet.getString("reorder_date"));
                vector.add(resultSet.getString("arrival_date"));
                vector.add(resultSet.getString("order_amount"));
                vector.add(resultSet.getString("status"));
                dtm.addRow(vector);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged

        calculateAmount();
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        calculateAmount();
    }//GEN-LAST:event_jTextField1KeyReleased

    private void jDateChooser1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser1PropertyChange

        Date selectedDate = jDateChooser1.getDate();
        Date currentDate = new Date();

        if (selectedDate != null && selectedDate.before(currentDate)) {
            JOptionPane.showMessageDialog(this, "Please select a future date!", "Invalid Date", JOptionPane.ERROR_MESSAGE);
            jDateChooser1.setDate(null);
        }

    }//GEN-LAST:event_jDateChooser1PropertyChange

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        barcode = jLabel9.getText();
        qty = jTextField1.getText();
        supplier = String.valueOf(jComboBox1.getSelectedItem());
        java.util.Date selectedDate = jDateChooser1.getDate();
        if (selectedDate != null) {
            arrivalDate = new java.sql.Date(selectedDate.getTime()).toString();
        } else {
            arrivalDate = ""; // Set to an empty string or handle the null case appropriately
        }

        String supplierMobile = supplierMap.get(String.valueOf(jComboBox1.getSelectedItem()));
        query5 = "SELECT sup.email, com.name, sup.mobile, cat.category_name, p.name, p.unit, s.id, p.product_image "
                + "FROM supplier sup "
                + "INNER JOIN company com ON sup.company_id = com.id "
                + "INNER JOIN grn ON sup.mobile = grn.supplier_mobile "
                + "INNER JOIN grn_item gi ON gi.grn_id = grn.id "
                + "INNER JOIN stock s ON s.id = gi.stock_id "
                + "INNER JOIN product p ON s.product_id = p.id "
                + "INNER JOIN category cat ON p.category = cat.id "
                + "WHERE s.barcode = '" + barcode + "' AND sup.mobile = '" + supplierMobile + "'";

        if (qty.isEmpty() || !qty.matches("\\d+") || Integer.parseInt(qty) <= 0) {
            JOptionPane.showMessageDialog(this, "QTY not Valid!", "Please enter required qty", JOptionPane.ERROR_MESSAGE);
        } else if (supplier.equals("Select")) {
            JOptionPane.showMessageDialog(this, "Supplier Not Selected!", "Please Select Supplier", JOptionPane.ERROR_MESSAGE);
        } else if (arrivalDate.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a future date!", "Please Select Stock Arrival Date", JOptionPane.ERROR_MESSAGE);
        } else {
            try {

                ResultSet resultSet = DatabaseConnection.executeSearch(query5);

                if (resultSet.next()) {
                    productName = resultSet.getString("p.name");
                    String supplierEmail = resultSet.getString("sup.email");
                    String stockId = resultSet.getString("s.id");
                    String filePath = resultSet.getString("product_image");
                    
                    String emailBody = "Dear Supplier Team,\n\n"
                            + "Hope you're having a productive week.\n\n"
                            + "This email is a reorder request for the following product to ensure we maintain optimal stock levels at LexSo:\n\n"
                            + "* Product Name: " + productName + "\n"
                            + "* Required Quantity: " + qty + " units\n\n"
                            + "We kindly request you to process this order as soon as possible to avoid any disruptions in our supply chain. We appreciate your prompt attention to this matter.\n\n"
                            + "Please confirm receipt of this order and provide an estimated delivery date at your earliest convenience. If there are any concerns regarding this request or stock availability, please don't hesitate to reach out.\n\n"
                            + "Thank you for your continued partnership and excellent service.\n\n"
                            + "Sincerely,\n\n"
                            + "The LexSo Team\n"
                            + "" + loggedInUsername + "\n"
                            + "0726733332\n"
                            + "contact@lexso.com\n"
                            + "www.lexfury.com";
                    
                    String correctedFilePath = filePath.replace("product-image///", "");
                    boolean emailSent = false;
                    boolean messageSent = false;

                    try {
                        sendEmail(supplierEmail, "Product Reorder", emailBody);
                        emailSent = true;
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(this, "Failed to send email: " + e.getMessage());
                    }

                    try {
                        WhatsAppService whatsAppService = new WhatsAppService();
                        FileUploadService fileUploadService = new FileUploadService();
                        String fullFilePath = Paths.get("src", "product-image/", filePath).toString();

                        String response = fileUploadService.uploadFile(fullFilePath);
                        System.out.println(response);

                        String whatsAppMessage = "_*🔔 Reorder Alert!*_\n\n" +
                         "*Product:* `" + productName + "`\n" +
                         "*Quantity Needed:* `" + qty + " units`\n\n" +
                         "Hey Supplier Team,\n" + // Or dynamically insert supplier name
                         "We need to restock _" + productName + "_! Kindly process a reorder for `" + qty + "` units.\n\n" +
                         "Your prompt action is appreciated! 🙏";
                        
                        whatsAppService.sendMediaMessage(
                                "94726733332",
                                whatsAppMessage,
                                WhatsAppService.MediaType.IMAGE,
                                response
                        );

                        messageSent = true;
                        JOptionPane.showMessageDialog(this, "WhatsApp message sent successfully!");
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(this, "Failed to send WhatsApp message: " + e.getMessage());
                    }

                    if (emailSent || messageSent) {

                        LocalDateTime currentDateTime = LocalDateTime.now();
                        String orderAmount = jLabel16.getText();

                        query = "INSERT INTO stock_reorder (reorder_date, arrival_date, order_amount, order_qty, status, stock_id, supplier_mobile) "
                                + " VALUES ('" + currentDateTime + "', '" + arrivalDate + "', '" + orderAmount + "', '" + qty + "', 'pending', '" + stockId + "', '" + supplierMobile + "')";

                        try {
                            DatabaseConnection.executeIUD(query);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        jTextField1.setText("");
                        jComboBox1.setSelectedIndex(0);
                        jLabel14.setText("Please Select Supplier");
                        jLabel16.setText("Please Select Supplier");
                        jDateChooser1.setDate(null);
                        loadReorderHistory();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        jTextField1.setText("");
        jComboBox1.setSelectedIndex(0);
        jLabel14.setText("Please Select Supplier");
        jLabel16.setText("Please Select Supplier");
        jDateChooser1.setDate(null);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextField2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyReleased
        search();
    }//GEN-LAST:event_jTextField2KeyReleased

    private void jComboBox2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox2ItemStateChanged
        search();
    }//GEN-LAST:event_jComboBox2ItemStateChanged

    private void jComboBox3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox3ItemStateChanged
        search();
    }//GEN-LAST:event_jComboBox3ItemStateChanged

    private void jDateChooser2PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser2PropertyChange
        search();
    }//GEN-LAST:event_jDateChooser2PropertyChange

    private void jDateChooser3PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser3PropertyChange
        search();
    }//GEN-LAST:event_jDateChooser3PropertyChange

    private void jComboBox4ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox4ItemStateChanged
        search();
    }//GEN-LAST:event_jComboBox4ItemStateChanged

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        search();
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jCheckBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox3ActionPerformed
        search();
    }//GEN-LAST:event_jCheckBox3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        jTextField2.setText("");
        jCheckBox1.setSelected(false);
        jCheckBox2.setSelected(false);
        jCheckBox3.setSelected(false);
        jDateChooser2.setDate(null);
        jDateChooser3.setDate(null);
        jComboBox2.setSelectedIndex(0);
        jComboBox3.setSelectedIndex(0);
        jComboBox4.setSelectedIndex(0);
        jTable1.clearSelection();
        loadReorderHistory();
        jButton5.setEnabled(false);

    }//GEN-LAST:event_jButton4ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        int selectedRow = jTable1.getSelectedRow();
        Boolean selectedRowStatus = false;

        if (selectedRow != -1) {
            String status = (String) jTable1.getValueAt(selectedRow, 5);
            selectedRowStatus = "pending".equals(status);
        } else {

            selectedRowStatus = false;
        }
        if (selectedRowStatus) {
            jButton5.setEnabled(true);
        } else {
            jButton5.setEnabled(false);
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed

        int selectedRow = jTable1.getSelectedRow();

        if (selectedRow != -1) {
            String orderId = (String) jTable1.getValueAt(selectedRow, 1);

            query = "UPDATE stock_reorder SET status = 'canceled' WHERE id = '" + orderId + "'";

            query1 = "SELECT sup.mobile, sup.email, sr.id, sr.reorder_date, sr.order_qty, p.name, p.product_image FROM "
                    + "stock_reorder sr INNER JOIN "
                    + "supplier sup ON sup.mobile = sr.supplier_mobile "
                    + "INNER JOIN stock s ON s.id = sr.stock_id "
                    + "INNER JOIN product p ON p.id = s.product_id "
                    + "WHERE sr.id = '" + orderId + "'";

            int response = JOptionPane.showConfirmDialog(null,
                    "Do you want to continue?",
                    "Confirm Action",
                    JOptionPane.YES_NO_OPTION);

            if (response == JOptionPane.YES_OPTION) {

                try {
                    DatabaseConnection.executeIUD(query);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    loadReorderHistory();
                    ResultSet resultSet = DatabaseConnection.executeSearch(query1);

                    while (resultSet.next()) {
                        String supplierMobile = resultSet.getString("sup.mobile");
                        String supplierEmail = resultSet.getString("sup.email");
                        String productName = resultSet.getString("p.name");
                        String qty = resultSet.getString("sr.order_qty");
                        String reorderDate = resultSet.getString("sr.reorder_date");
                        String filePath = resultSet.getString("product_image");
                        String correctedFilePath = filePath.replace("product-image//", "");
                        System.out.println(correctedFilePath);

                        String body = "Cancel order for " + productName + "/n on " + reorderDate + "/n with a qty of " + qty;

                        try {
                            WhatsAppService whatsAppService = new WhatsAppService();
                            whatsAppService.sendMediaMessage(
                                    "94776299240",
                                    "Generic image message",
                                    WhatsAppService.MediaType.IMAGE,
                                    "https://t4.ftcdn.net/jpg/00/53/45/31/360_F_53453175_hVgYVz0WmvOXPd9CNzaUcwcibiGao3CL.jpg"
                            );

//                            whatsAppService.sendMessage(supplierMobile, correctedFilePath, "Cancel order for " + productName + "/n on " + reorderDate + "/n with a qty of " + qty);
                            JOptionPane.showMessageDialog(this, "WhatsApp message sent successfully!");

                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(this, "Failed to send WhatsApp message: " + e.getMessage());
                        }

                        try {
                            sendCancelEmail(supplierEmail, "Cancel Order", body, productName, qty, reorderDate);

                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(this, "Failed to send email: " + e.getMessage());
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (response == JOptionPane.NO_OPTION) {

            } else {

            }

        } else {
            jButton5.setEnabled(false);
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private com.toedter.calendar.JDateChooser jDateChooser3;
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
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables

}
