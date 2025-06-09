package com.lexso.inventory.stock;

import com.formdev.flatlaf.themes.FlatMacLightLaf;
import com.lexso.connection.DatabaseConnection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import javax.swing.JOptionPane;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author Anton Shane Delima
 */
public class UpdateStock extends javax.swing.JFrame {

    String query;
    Date selectedWarningDate = null;
    Date exp;
    Date mfd;
    Date expWarning;
    double buyingPrice;
    double sellingPrice;
    int qtydb;
    String buyingPrice1;
    String sellingPrice1;
    String qty1;
    String lowStock1;
    private String buyingPriceBeforeEvent;
    private Date expWarningbeforeEvent;
    private Date mfdbeforeEvent;
    private String sellingPriceBeforeEvent;
    private String qtyPriceBeforeEvent;
    private String lowStockBeforeEvent;
    private Date nullDate = new Date(0);
    private String itemDiscount;
    private String itemDiscountBeforeEvent;
    private String discountType;
    private String discountTypeBeforeEvent;
    private Boolean discountCheckBox;
    private Boolean expWarningCheckBox;
    private Boolean lowStockCheckBox;
    private ResultSet resultSet;
    private Date expInDBDate;
    private Date mfdInDBDate;
    private Date expWarningDate;
    private Double buyingPriceDB;
    private Double sellingPriceDB;
    private String qtyInDB;
    private String lowStockInDB;
    private Double discountInDB;
    private String discountTypeInDB;
    private Boolean byingPriceIsEqual;
    private Boolean sellingPriceIsEqual;
    private Boolean qtyIsEqual;
    private Boolean expWarningIsEqual;
    private Boolean lowStockIsEqual;
    private Boolean discountIsEqual;
    private Boolean discountTypeIsEqual;
    private String barcode;
    private Date expWarningInDBDate;
    Double nullDiscount = 0.0;
    Date disStartDateInDB;
    Date disStartDate;
    Date disEndDate;
    Date disEndDateInDB;
    private boolean initializing = true;
    int expWarningNew;

    public boolean isValidDouble(String number) {
        try {
            Double.parseDouble(number);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean isValidInteger(String number) {
        try {
            Integer.parseInt(number);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static int daysBetween(Date start, Date end) {
        LocalDate startDate = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endDate = end.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return (int) ChronoUnit.DAYS.between(startDate, endDate);
    }

    private void loadStock(String productId, String barcode, String category, String expDate, String price, String qty) {

        query = "SELECT p.name, s.qty, s.buying_price, s.selling_price, s.mft, s.exp, s.low_stock_warning, "
                + "b.name, cat.category_name, s.discount_type, s.discount_value, s.id, s.exp_warning_days, "
                + "s.low_stock_warning, s.discount_start_date, s.discount_end_date "
                + "FROM stock s "
                + "INNER JOIN product p ON s.product_id = p.id "
                + "INNER JOIN category cat ON p.category = cat.id "
                + "LEFT JOIN category_has_brand chb ON chb.category_category_id = cat.id "
                + "LEFT JOIN brand b ON chb.brand_id = b.id "
                + "WHERE s.barcode = '" + barcode + "'";

        try {
            resultSet = DatabaseConnection.executeSearch(query);

            while (resultSet.next()) {

                String productName = resultSet.getString("p.name");
                String stockId = resultSet.getString("s.id");
                qtydb = resultSet.getInt("s.qty");
                buyingPrice = resultSet.getDouble("s.buying_price");
                sellingPrice = resultSet.getDouble("s.selling_price");
                mfd = resultSet.getDate("s.mft");
                exp = resultSet.getDate("s.exp");
                int expWarning = resultSet.getInt("s.exp_warning_days");
                disStartDate = resultSet.getDate("s.discount_start_date");
                disEndDate = resultSet.getDate("s.discount_end_date");

                java.util.Date mfdDate = null;
                java.util.Date expDate1 = null;
                java.util.Date expWarningDate = null;
                java.util.Date discountStartDate = null;
                java.util.Date discountEndDate = null;
                if (disStartDate != null) {
                    discountStartDate = new java.util.Date(disStartDate.getTime());
                }
                if (disEndDate != null) {
                    discountEndDate = new java.util.Date(disEndDate.getTime());

                }

                if (mfd != null) {
                    mfdDate = new java.util.Date(mfd.getTime());
                }

                if (exp != null) {
                    expDate1 = new java.util.Date(exp.getTime());
                }

                if (expWarning != 0) {
                    jDateChooser3.setEnabled(true);
                    jCheckBox2.setSelected(true);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(expDate1);
                    cal.add(Calendar.DAY_OF_MONTH, -expWarning);
                    expWarningDate = cal.getTime();
                    jDateChooser3.setDate(expWarningDate);
                } else {
                    jDateChooser3.setEnabled(false);
                    jCheckBox2.setSelected(false);
                }

                int lowStockWarning = resultSet.getInt("s.low_stock_warning");
                String brandName = resultSet.getString("b.name");
                String categoryName = resultSet.getString("cat.category_name");
                Double discount = resultSet.getDouble("s.discount_value");
                String discountType = resultSet.getString("s.discount_type");

                jLabel3.setText(productName);
                jLabel5.setText(barcode);
                jLabel6.setText(brandName);
                jLabel11.setText(categoryName);
                jLabel9.setText(stockId);
                jDateChooser2.setDate(mfdDate);
                jDateChooser1.setDate(expDate1);
                jTextField1.setText(String.valueOf(buyingPrice));
                mfdbeforeEvent = mfd;
                buyingPriceBeforeEvent = jTextField1.getText();
                jTextField2.setText(String.valueOf(sellingPrice));
                jTextField3.setText(String.valueOf(qtydb));
                qtyPriceBeforeEvent = String.valueOf(qty);
                jTextField5.setText(String.valueOf(discount));
                if (discountType.equals("percentage")) {
                    jDateChooser4.setDate(discountStartDate);
                    jDateChooser5.setDate(discountEndDate);
                    jComboBox1.setSelectedIndex(1);
                    jCheckBox3.setSelected(true);
                } else if (discountType.equals("fixed")) {
                    jDateChooser4.setDate(discountStartDate);
                    jDateChooser5.setDate(discountEndDate);
                    jComboBox1.setSelectedIndex(2);
                    jCheckBox3.setSelected(true);
                } else if (discountType.equals("no discount")) {
                    jComboBox1.setSelectedIndex(3);
                    jCheckBox3.setSelected(false);
                }

                if (lowStockWarning != 0) {
                    jTextField6.setText(String.valueOf(lowStockWarning));
                    jTextField6.setEditable(true);
                    jCheckBox1.setSelected(true);
                    lowStockBeforeEvent = String.valueOf(lowStockWarning);
                    lowStockCheckBox = false;

                } else {
                    jCheckBox1.setSelected(false);
                    jTextField6.setEditable(false);
                    jTextField6.setText("0");
                    lowStockCheckBox = true;

                }
                if (expWarningDate != null) {
                    expWarningbeforeEvent = new java.util.Date(expWarningDate.getTime());
                }
                discountInDB = resultSet.getDouble("discount_value");

                sellingPriceBeforeEvent = String.valueOf(sellingPrice);
                itemDiscountBeforeEvent = String.valueOf(jTextField5.getText());
                discountTypeBeforeEvent = String.valueOf(jComboBox1.getSelectedItem());

                if (discountInDB != 0) {

                } else {
                    jComboBox1.setEnabled(false);
                    jCheckBox3.setSelected(false);
                    jDateChooser4.setDate(null);
                    jDateChooser5.setDate(null);
                    jDateChooser4.setEnabled(false);
                    jDateChooser5.setEnabled(false);
                    jTextField5.setEditable(false);
                    jComboBox1.setEditable(false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public UpdateStock(String productName, String barcodeFromInventory, String category, String expDate, String price, String qty) {
        initComponents();
        initializing = true;
        barcode = barcodeFromInventory;
        loadStock(productName, barcode, category, expDate, price, qty);
        initializing = false;

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jLabel13 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jLabel16 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jButton3 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jTextField6 = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jCheckBox2 = new javax.swing.JCheckBox();
        jDateChooser3 = new com.toedter.calendar.JDateChooser();
        jCheckBox3 = new javax.swing.JCheckBox();
        jLabel21 = new javax.swing.JLabel();
        jDateChooser4 = new com.toedter.calendar.JDateChooser();
        jLabel22 = new javax.swing.JLabel();
        jDateChooser5 = new com.toedter.calendar.JDateChooser();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Update Stock");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("Product Name:");
        jLabel2.setPreferredSize(new java.awt.Dimension(30, 16));

        jLabel3.setText("product");
        jLabel3.setPreferredSize(new java.awt.Dimension(30, 16));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setText("Barcode no:");
        jLabel4.setPreferredSize(new java.awt.Dimension(30, 16));

        jLabel5.setText("barcode");
        jLabel5.setPreferredSize(new java.awt.Dimension(30, 16));

        jLabel6.setText("Brand Name");
        jLabel6.setPreferredSize(new java.awt.Dimension(30, 16));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel7.setText("Brand Name:");
        jLabel7.setPreferredSize(new java.awt.Dimension(30, 16));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel8.setText("Product Category:");
        jLabel8.setPreferredSize(new java.awt.Dimension(30, 16));

        jLabel9.setText("Stock ID");
        jLabel9.setPreferredSize(new java.awt.Dimension(30, 16));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel10.setText("Stock ID:");
        jLabel10.setPreferredSize(new java.awt.Dimension(30, 16));

        jLabel11.setText("Category Name");
        jLabel11.setPreferredSize(new java.awt.Dimension(30, 16));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel12.setText("EXP Date:");
        jLabel12.setPreferredSize(new java.awt.Dimension(30, 16));

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel13.setText("Buying Price:");
        jLabel13.setPreferredSize(new java.awt.Dimension(30, 16));

        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel14.setText("Selling Price:");
        jLabel14.setPreferredSize(new java.awt.Dimension(30, 16));

        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField2KeyReleased(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel15.setText("EXP Date Warning:");
        jLabel15.setPreferredSize(new java.awt.Dimension(30, 16));

        jDateChooser2.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser2PropertyChange(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel16.setText("Item QTY:");
        jLabel16.setPreferredSize(new java.awt.Dimension(30, 16));

        jTextField3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField3KeyReleased(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel17.setText("MFD Date:");
        jLabel17.setPreferredSize(new java.awt.Dimension(30, 16));

        jCheckBox1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox1.setText("Low Stock Warning");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(255, 51, 51));
        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton3.setText("Clear All");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(204, 153, 0));
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton2.setText("Cancel");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(51, 51, 255));
        jButton4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton4.setText("update");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel18.setText("Item Discount:");
        jLabel18.setPreferredSize(new java.awt.Dimension(30, 16));

        jTextField5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField5KeyReleased(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel19.setText("Discount Type:");
        jLabel19.setPreferredSize(new java.awt.Dimension(30, 16));

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select", "percentage", "fixed", "no_discount" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jTextField6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField6KeyReleased(evt);
            }
        });

        jLabel20.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel20.setText("Low Stock Warning:");
        jLabel20.setPreferredSize(new java.awt.Dimension(30, 16));

        jCheckBox2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox2.setText("EXP Date Warning");
        jCheckBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox2ActionPerformed(evt);
            }
        });

        jDateChooser3.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser3PropertyChange(evt);
            }
        });

        jCheckBox3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox3.setText("Item Discount");
        jCheckBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox3ActionPerformed(evt);
            }
        });

        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel21.setText("Discount Start Date:");
        jLabel21.setPreferredSize(new java.awt.Dimension(30, 16));

        jDateChooser4.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser4PropertyChange(evt);
            }
        });

        jLabel22.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel22.setText("Discount End Date");
        jLabel22.setPreferredSize(new java.awt.Dimension(30, 16));

        jDateChooser5.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser5PropertyChange(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
                    .addComponent(jLabel19, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBox3, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jDateChooser5, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addComponent(jCheckBox2)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jCheckBox1))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)
                                .addComponent(jTextField1)
                                .addComponent(jTextField2)
                                .addComponent(jTextField3)
                                .addComponent(jTextField6)
                                .addComponent(jDateChooser3, javax.swing.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE))
                            .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jComboBox1, javax.swing.GroupLayout.Alignment.LEADING, 0, 254, Short.MAX_VALUE)
                                .addComponent(jTextField5, javax.swing.GroupLayout.Alignment.LEADING))))
                    .addComponent(jDateChooser4, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jDateChooser3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jDateChooser4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jDateChooser5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed

        if (jCheckBox1.isSelected()) {
            if (lowStockBeforeEvent != null) {
                jTextField6.setText(lowStockBeforeEvent);
                jTextField6.setEditable(true);
            } else {
                jTextField6.setText("");
                jTextField6.setEditable(true);
            }
            lowStockCheckBox = true;
            jTextField6.setEditable(true);
        } else {
            jTextField6.setText("0");
            jTextField6.setEditable(false);
            lowStockCheckBox = false;
        }
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        try {
            // Validate dates
            exp = jDateChooser1.getDate();
            if (exp == null) {
                JOptionPane.showMessageDialog(this, "Please select an expiration date.");
                return;
            }

            mfd = jDateChooser2.getDate();
            if (mfd == null) {
                JOptionPane.showMessageDialog(this, "Please select a manufacture date.");
                return;
            }

            if (exp.before(mfd)) {
                JOptionPane.showMessageDialog(this, "Expiry date cannot be before manufacture date.");
                return;
            }

            if (jCheckBox2.isSelected()) {
                expWarning = jDateChooser3.getDate();
                if (expWarning == null) {
                    JOptionPane.showMessageDialog(this, "Please select an expiry warning date.");
                    return;
                }
            } else {
                expWarning = nullDate;
            }

            // Validate buying price
            try {
                buyingPrice = Double.parseDouble(jTextField1.getText().trim());
                if (buyingPrice <= 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter a valid positive Buying Price.");
                return;
            }

            // Validate selling price
            try {
                sellingPrice = Double.parseDouble(jTextField2.getText().trim());
                if (sellingPrice <= 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter a valid positive Selling Price.");
                return;
            }

            // Validate quantity
            try {
                qty1 = jTextField3.getText().trim();
                if (Integer.parseInt(qty1) < 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter a valid positive Quantity.");
                return;
            }

            // Validate low stock warning
            try {
                lowStock1 = jTextField6.getText().trim();
                if (Integer.parseInt(lowStock1) < 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter a valid positive Low Stock Warning.");
                return;
            }

            // Validate discount
            if (jCheckBox3.isSelected()) {
                discountType = String.valueOf(jComboBox1.getSelectedItem());

                if ("Select".equals(discountType)) {
                    JOptionPane.showMessageDialog(this, "Please select a valid Discount Type.");
                    return;
                }

                try {
                    itemDiscount = jTextField5.getText().trim();

                    double discount = Double.parseDouble(itemDiscount);
                    if (discount <= 0) {
                        throw new NumberFormatException();
                    }

                    if ("percentage".equals(discountType) && discount >= 100) {
                        JOptionPane.showMessageDialog(this, "Percentage discount must be less than 100.");
                        return;
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Please enter a valid positive Discount value.");
                    return;
                }

            } else {
                discountType = "no_discount";
                itemDiscount = "0";
                disStartDate = nullDate;
                disEndDate = nullDate;
            }
            try {
                ResultSet resultSet1 = DatabaseConnection.executeSearch(query);

                while (resultSet1.next()) {
                    buyingPriceDB = resultSet1.getDouble("buying_price");
                    sellingPriceDB = resultSet1.getDouble("selling_price");
                    qtyInDB = resultSet1.getString("qty");
                    lowStockInDB = resultSet1.getString("low_stock_warning");
                    discountInDB = resultSet1.getDouble("discount_value");
                    discountTypeInDB = resultSet1.getString("discount_type");

                    Date expInDB = resultSet1.getDate("exp");
                    if (expInDB != null) {
                        expInDBDate = new java.util.Date(expInDB.getTime());
                    }
                    Date mfdInDB = resultSet1.getDate("mft");
                    if (mfdInDB != null) {
                        mfdInDBDate = new java.util.Date(mfdInDB.getTime());
                    }
                    int expWarningInDB = resultSet1.getInt("exp_warning_days");
                    if (expWarningInDB != 0) {
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(expInDBDate);
                        cal.add(Calendar.DAY_OF_MONTH, -expWarningInDB);
                        expWarningDate = cal.getTime();
                    }
                    Date disStartDate1 = resultSet1.getDate("s.discount_start_date");
                    if (!discountTypeInDB.equals("no_discount")) {
                        disStartDateInDB = new java.util.Date(disStartDate1.getTime());
                    }
                    Date disEndDate1 = resultSet1.getDate("s.discount_end_date");
                    if (!discountTypeInDB.equals("no_discount")) {
                        disEndDateInDB = new java.util.Date(disEndDate1.getTime());
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            boolean allMatch
                    = expInDBDate.equals(exp)
                    && Objects.equals(expWarningDate, expWarning)
                    && mfdInDBDate.equals(mfd)
                    && buyingPriceDB.equals(buyingPrice)
                    && sellingPriceDB.equals(sellingPrice)
                    && qtyInDB.equals(String.valueOf(qty1))
                    && disStartDate.equals(disStartDateInDB)
                    && disEndDate.equals(disEndDateInDB)
                    && lowStockInDB.equals(String.valueOf(lowStock1))
                    && discountInDB.equals(Double.valueOf(itemDiscount))
                    && discountTypeInDB.equals(discountType);

            if (allMatch) {
                JOptionPane.showMessageDialog(this, "Please change al least one field to update");
            } else {
                try {
                    SimpleDateFormat mysqlDateFormat = new SimpleDateFormat("yyyy-MM-dd");

                    String expFormatted = (exp != null) ? mysqlDateFormat.format(exp) : null;
                    String mfdFormatted = (mfd != null) ? mysqlDateFormat.format(mfd) : null;
                    if (!expInDBDate.equals(exp)) {
                        expWarningNew = daysBetween(expWarning, exp);
                    }

//                    System.out.println(discountType);  
                    String query3 = "UPDATE stock s "
                            + "SET "
                            + "    s.exp = '" + expFormatted + "', "
                            + "    s.exp_warning_days = '" + expWarningNew + "',  "
                            + "    s.mft = '" + mfdFormatted + "',  "
                            + "    s.buying_price = '" + buyingPrice + "',  "
                            + "    s.selling_price = '" + sellingPrice + "',  "
                            + "    s.qty = '" + qty1 + "',  "
                            + "    s.low_stock_warning = '" + lowStock1 + "', "
                            + "    s.discount_value = '" + itemDiscount + "', "
                            + "    s.discount_type = '" + discountType + "' "
                            + "WHERE s.barcode = '" + barcode + "'; ";
                    DatabaseConnection.executeIUD(query3);
                    this.dispose();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "An unexpected error occurred.");
        }


    }//GEN-LAST:event_jButton4ActionPerformed

    private void jCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox2ActionPerformed

        if (jCheckBox2.isSelected()) {
            if (selectedWarningDate != null) {
                jDateChooser3.setDate(selectedWarningDate);
            } else if (expWarningbeforeEvent != null) {
                jDateChooser3.setDate(expWarningbeforeEvent);
            } else {
                selectedWarningDate = jDateChooser3.getDate();
            }
            jDateChooser3.setEnabled(true);
            expWarningCheckBox = true;
        } else {
            selectedWarningDate = jDateChooser3.getDate();
            jDateChooser3.setDate(null);
            jDateChooser3.setEnabled(false);
            expWarningCheckBox = false;
        }

    }//GEN-LAST:event_jCheckBox2ActionPerformed


    private void jDateChooser2PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser2PropertyChange
        exp = jDateChooser1.getDate();
        mfd = jDateChooser2.getDate();

        if (mfd != null) {
            if (exp != null) {
                if (mfd.before(exp)) {
                } else {
                    JOptionPane.showMessageDialog(this, "Please select a date before the expiration date");
                    jDateChooser2.setDate(mfdbeforeEvent);
                }
            }

        }

    }//GEN-LAST:event_jDateChooser2PropertyChange

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased
        buyingPrice1 = jTextField1.getText();
        if (isValidDouble(buyingPrice1)) {

        } else {
            if (buyingPrice1.isEmpty()) {
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a valid buying price");
                jTextField1.setText(buyingPriceBeforeEvent);
            }

        }
    }//GEN-LAST:event_jTextField1KeyReleased

    private void jTextField2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyReleased
        sellingPrice1 = jTextField2.getText();
        if (isValidDouble(sellingPrice1)) {

        } else {
            if (sellingPrice1.isEmpty()) {
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a valid selling price");
                jTextField2.setText(sellingPriceBeforeEvent);
            }

        }
    }//GEN-LAST:event_jTextField2KeyReleased

    private void jTextField3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField3KeyReleased
        qty1 = jTextField3.getText();
        if (isValidDouble(qty1)) {

        } else {
            if (qty1.isEmpty()) {
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a valid qty");
                jTextField3.setText(qtyPriceBeforeEvent);
            }
        }
    }//GEN-LAST:event_jTextField3KeyReleased

    private void jTextField6KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField6KeyReleased
        lowStock1 = jTextField6.getText();
        if (isValidInteger(lowStock1)) {

        } else {
            if (lowStock1.isEmpty()) {
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a valid qty");
                jTextField6.setText(lowStockBeforeEvent);
            }
        }
    }//GEN-LAST:event_jTextField6KeyReleased

    private void jTextField5KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField5KeyReleased
        itemDiscount = jTextField5.getText();
        if (isValidDouble(itemDiscount)) {

        } else {
            if (itemDiscount.isEmpty()) {
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a valid discount");
                jTextField5.setText(itemDiscountBeforeEvent);
            }
        }
    }//GEN-LAST:event_jTextField5KeyReleased

    private void jCheckBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox3ActionPerformed
        if (jCheckBox3.isSelected()) {
            jTextField5.setText(itemDiscountBeforeEvent);
            jComboBox1.setEnabled(true);
            discountCheckBox = true;
            jDateChooser4.setEnabled(true);
            jDateChooser5.setEnabled(true);
            jDateChooser4.setDate(disStartDate);
            jDateChooser5.setDate(disEndDate);
            jTextField5.setEditable(true);
            jComboBox1.setEditable(true);
            jComboBox1.setSelectedItem(discountTypeBeforeEvent);
        } else {
            jTextField5.setText("0");
            jComboBox1.setSelectedItem("Select");
            jComboBox1.setEnabled(false);
            discountCheckBox = false;
            jDateChooser4.setDate(null);
            jDateChooser5.setDate(null);
            jDateChooser4.setEnabled(false);
            jDateChooser5.setEnabled(false);
            jTextField5.setEditable(false);
            jComboBox1.setEditable(false);
        }
    }//GEN-LAST:event_jCheckBox3ActionPerformed

    private void jDateChooser4PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser4PropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_jDateChooser4PropertyChange

    private void jDateChooser5PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser5PropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_jDateChooser5PropertyChange

    private void jDateChooser3PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser3PropertyChange
        if (initializing) {
            return;
        }
        exp = jDateChooser1.getDate();
        selectedWarningDate = jDateChooser3.getDate();

        if (selectedWarningDate != null) {
            if (exp != null) {
                if (selectedWarningDate.before(exp)) {
                } else if (selectedWarningDate.after(exp)) {
                    JOptionPane.showMessageDialog(this, "Please select a date before the expiration date");
                    jDateChooser3.setDate(expWarningbeforeEvent);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Expiration date is not set");
            }
        }
    }//GEN-LAST:event_jDateChooser3PropertyChange

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JComboBox<String> jComboBox1;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private com.toedter.calendar.JDateChooser jDateChooser3;
    private com.toedter.calendar.JDateChooser jDateChooser4;
    private com.toedter.calendar.JDateChooser jDateChooser5;
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
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    // End of variables declaration//GEN-END:variables
}
