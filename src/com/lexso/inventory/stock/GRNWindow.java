package com.lexso.inventory.stock;

import com.formdev.flatlaf.themes.FlatMacLightLaf;
import com.lexso.connection.DatabaseConnection;
import com.lexso.connection.ShaneConnection;
import java.awt.event.ItemEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class GRNWindow extends javax.swing.JFrame {

    public class GRNItem {

        /**
         * @return the lowStockThreshold
         */
        public Integer getLowStockThreshold() {
            return lowStockThreshold;
        }

        /**
         * @return the discountType
         */
        public String getDiscountType() {
            return discountType;
        }
        // Supplier Information

        private String supplierMobile;
        private String supplierId;

        // Product Information
        private String productId;
        private String productName;

        // Stock Information
        private String stockId;
        private boolean fromStockReorder;

        // Dates
        private Date mfdDate;
        private Date expDate;
        private Date expWarningDate;

        // Pricing
        private double buyingPrice;
        private double sellingPrice;
        private int quantity;

        // Low Stock Warning
        private Integer lowStockThreshold;

        // Discount Information
        private String discountType = "no_discount";
        private Double discountValue;
        private Date discountStartDate;
        private Date discountEndDate;

        // Transaction Details
        private double itemTotal;
        private double paidAmount;

        /**
         * @return the supplierMobile
         */
        public String getSupplierMobile() {
            return supplierMobile;
        }

        /**
         * @param supplierMobile the supplierMobile to set
         */
        public void setSupplierMobile(String supplierMobile) {
            this.supplierMobile = supplierMobile;
        }

        /**
         * @return the supplierId
         */
        public String getSupplierId() {
            return supplierId;
        }

        /**
         * @param supplierId the supplierId to set
         */
        public void setSupplierId(String supplierId) {
            this.supplierId = supplierId;
        }

        /**
         * @return the productId
         */
        public String getProductId() {
            return productId;
        }

        /**
         * @param productId the productId to set
         */
        public void setProductId(String productId) {
            this.productId = productId;
        }

        /**
         * @return the productName
         */
        public String getProductName() {
            return productName;
        }

        /**
         * @param productName the productName to set
         */
        public void setProductName(String productName) {
            this.productName = productName;
        }

        /**
         * @return the stockId
         */
        public String getStockId() {
            return stockId;
        }

        /**
         * @param stockId the stockId to set
         */
        public void setStockId(String stockId) {
            this.stockId = stockId;
        }

        /**
         * @return the fromStockReorder
         */
        public boolean isFromStockReorder() {
            return fromStockReorder;
        }

        /**
         * @param fromStockReorder the fromStockReorder to set
         */
        public void setFromStockReorder(boolean fromStockReorder) {
            this.fromStockReorder = fromStockReorder;
        }

        /**
         * @return the mfdDate
         */
        public Date getMfdDate() {
            return mfdDate;
        }

        /**
         * @param mfdDate the mfdDate to set
         */
        public void setMfdDate(Date mfdDate) {
            this.mfdDate = mfdDate;
        }

        /**
         * @return the expDate
         */
        public Date getExpDate() {
            return expDate;
        }

        /**
         * @param expDate the expDate to set
         */
        public void setExpDate(Date expDate) {
            this.expDate = expDate;
        }

        /**
         * @return the expWarningDate
         */
        public Date getExpWarningDate() {
            return expWarningDate;
        }

        /**
         * @param expWarningDate the expWarningDate to set
         */
        public void setExpWarningDate(Date expWarningDate) {
            this.expWarningDate = expWarningDate;
        }

        /**
         * @return the buyingPrice
         */
        public double getBuyingPrice() {
            return buyingPrice;
        }

        /**
         * @param buyingPrice the buyingPrice to set
         */
        public void setBuyingPrice(double buyingPrice) {
            this.buyingPrice = buyingPrice;
        }

        /**
         * @return the sellingPrice
         */
        public double getSellingPrice() {
            return sellingPrice;
        }

        /**
         * @param sellingPrice the sellingPrice to set
         */
        public void setSellingPrice(double sellingPrice) {
            this.sellingPrice = sellingPrice;
        }

        /**
         * @return the quantity
         */
        public int getQuantity() {
            return quantity;
        }

        /**
         * @param quantity the quantity to set
         */
        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        /**
         * @param lowStockThreshold the lowStockThreshold to set
         */
        public void setLowStockThreshold(Integer lowStockThreshold) {
            this.lowStockThreshold = lowStockThreshold;
        }

        /**
         * @param discountType the discountType to set
         */
        public void setDiscountType(String discountType) {
            this.discountType = discountType;
        }

        /**
         * @return the discountValue
         */
        public Double getDiscountValue() {
            return discountValue;
        }

        /**
         * @param discountValue the discountValue to set
         */
        public void setDiscountValue(Double discountValue) {
            this.discountValue = discountValue;
        }

        /**
         * @return the discountStartDate
         */
        public Date getDiscountStartDate() {
            return discountStartDate;
        }

        /**
         * @param discountStartDate the discountStartDate to set
         */
        public void setDiscountStartDate(Date discountStartDate) {
            this.discountStartDate = discountStartDate;
        }

        /**
         * @return the discountEndDate
         */
        public Date getDiscountEndDate() {
            return discountEndDate;
        }

        /**
         * @param discountEndDate the discountEndDate to set
         */
        public void setDiscountEndDate(Date discountEndDate) {
            this.discountEndDate = discountEndDate;
        }

        /**
         * @return the itemTotal
         */
        public double getItemTotal() {
            return itemTotal;
        }

        /**
         * @param itemTotal the itemTotal to set
         */
        public void setItemTotal(double itemTotal) {
            this.itemTotal = itemTotal;
        }

        /**
         * @return the paidAmount
         */
        public double getPaidAmount() {
            return paidAmount;
        }

        /**
         * @param paidAmount the paidAmount to set
         */
        public void setPaidAmount(double paidAmount) {
            this.paidAmount = paidAmount;
        }

    }

    public interface ProductSelectionListener {

        void onProductSelected(int selectedProductId);
    }

    private String selectedProductId;
    public String supplierMobile;
    suplierManagement supSelect;
    selectProductFrame selectProduct;
    String supFName;
    String supLName;
    String supCompany;
    String productName;
    String productBrand;
    private Date nullDate = new Date(0);
    private Boolean discountCheckBox;
    private Boolean expWarningCheckBox;
    private Boolean lowStockCheckBox;
    private boolean initializing = true;
    String reorderID;
    ReorderItemSelection reorderSelect;
    int StockID;
    int GRNItems = 0;
    int grnID;
    private ArrayList<GRNItem> grnItemsList = new ArrayList<>();

    public GRNWindow() {
        initComponents();
        initializeCheckBoxes();
        loadGRNID();
        loadStockID();
    }

    public void setSupMobile(String supMobile) {
        supplierMobile = supMobile;
    }

    public void loadGRNID() {
        try {
            String query = "SELECT `id` FROM `grn` ORDER BY id DESC LIMIT 1";
            ResultSet rs = ShaneConnection.executeSearch(query);
            if (rs.next()) {
                grnID = rs.getInt("id") + 1;
                jLabel9.setText("000" + String.valueOf(grnID) + "");
            } else {
                grnID = 1;
                jLabel9.setText("000" + String.valueOf(grnID) + "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadStockID() {
        try {
            String query = "SELECT `id` FROM `stock` ORDER BY id DESC LIMIT 1";
            ResultSet rs = ShaneConnection.executeSearch(query);
            if (rs.next()) {
                StockID = rs.getInt("id") + 1;
                jLabel7.setText("000" + String.valueOf(StockID) + "");
            } else {
                grnID = 1;
                jLabel7.setText("000" + String.valueOf(StockID) + "");
            }
        } catch (Exception e) {
        }
    }

    private void clearAll() {
        jTextField1.setText("");
        jTextField3.setText("");
        jTextField4.setText("");
        jTextField6.setText("");
        jTextField7.setText("");
        jTextField8.setText("");
        jTextField9.setText("");

        // Reset labels
        jLabel5.setText("");
        jLabel6.setText("");

        // Reset date choosers
        jDateChooser1.setDate(null);
        jDateChooser2.setDate(null);
        jDateChooser3.setDate(null);
        jDateChooser4.setDate(null);
        jDateChooser5.setDate(null);
        jTextField7.setEditable(false);
        jDateChooser3.setEnabled(false);
        jComboBox3.setEnabled(false);
        jTextField8.setEditable(false);
        jDateChooser4.setEnabled(false);
        jDateChooser5.setEnabled(false);

        // Reset checkboxes
        jCheckBox1.setSelected(false);
        jCheckBox2.setSelected(false);
        jCheckBox3.setSelected(false);
        jCheckBox4.setSelected(false);
        lowStockCheckBox = false;
        expWarningCheckBox = false;
        discountCheckBox = false;
        // Reset combo box
        jComboBox3.setSelectedIndex(0);

        // Reset buttons
        jButton4.setEnabled(false);
        jButton5.setEnabled(false);
        jButton8.setEnabled(true);

        // Reset declared variables
    }

    private void initializeCheckBoxes() {
        // Set initial states
        jCheckBox1.setSelected(false);
        jCheckBox2.setSelected(false);
        jCheckBox3.setSelected(false);

        // Disable dependent fields initially
        jTextField7.setEditable(false);
        jDateChooser3.setEnabled(false);
        jComboBox3.setEnabled(false);
        jTextField8.setEditable(false);
        jDateChooser4.setEnabled(false);
        jDateChooser5.setEnabled(false);

        initializing = false;
    }

    private boolean validateForm() {
        // Validate Supplier (jLabel5 must not be empty)
        if (jLabel5.getText().trim().isEmpty() || jLabel5.getText().equals("No supplier selected")) {
            JOptionPane.showMessageDialog(this, "Please select a supplier first.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validate Product (jLabel6 must not be empty)
        if (jLabel6.getText().trim().isEmpty() || jLabel6.getText().equals("No product selected")) {
            JOptionPane.showMessageDialog(this, "Please select a product first.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validate MFD date (must not be empty and must be before today)
        Date mfdDate = jDateChooser1.getDate();
        if (mfdDate == null) {
            JOptionPane.showMessageDialog(this, "Please select a manufacture (MFD) date.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!mfdDate.before(new Date())) {
            JOptionPane.showMessageDialog(this, "Manufacture (MFD) date must be before today.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validate EXP date (must not be empty and must be after today)
        Date expDate = jDateChooser2.getDate();
        if (expDate == null) {
            JOptionPane.showMessageDialog(this, "Please select an expiry (EXP) date.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!expDate.after(new Date())) {
            JOptionPane.showMessageDialog(this, "Expiry (EXP) date must be after today.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (jCheckBox2.isSelected()) {
            // First ensure EXP date is selected
            if (jDateChooser2.getDate() == null) {
                JOptionPane.showMessageDialog(this,
                        "Please set the EXP date before setting an EXP warning date.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

            Date warningDate = jDateChooser3.getDate();
            if (warningDate == null) {
                JOptionPane.showMessageDialog(this,
                        "Please select an EXP warning date.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // Warning date must be in the future (after today)
            if (!warningDate.after(new Date())) {
                JOptionPane.showMessageDialog(this,
                        "EXP warning date must be after today.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // Warning date must be before the EXP date
            if (!warningDate.before(jDateChooser2.getDate())) {
                JOptionPane.showMessageDialog(this,
                        "EXP warning date must be before the EXP date.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        try {
            if (jTextField1.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Buying price cannot be empty.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

            double buyingPrice = Double.parseDouble(jTextField1.getText());
            if (buyingPrice <= 0) {
                JOptionPane.showMessageDialog(this,
                        "Buying price must be a positive value.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Invalid buying price format. Please enter a valid number.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validate Selling Price (jTextField3)
        try {
            if (jTextField3.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Selling price cannot be empty.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

            double sellingPrice = Double.parseDouble(jTextField3.getText());
            if (sellingPrice <= 0) {
                JOptionPane.showMessageDialog(this,
                        "Selling price must be a positive value.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // Compare with buying price
            double buyingPrice = Double.parseDouble(jTextField1.getText());
            if (sellingPrice <= buyingPrice) {
                JOptionPane.showMessageDialog(this,
                        "Selling price must be higher than buying price.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Invalid selling price format. Please enter a valid number.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        try {
            if (jTextField1.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Buying price cannot be empty.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

            double buyingPrice = Double.parseDouble(jTextField1.getText());
            if (buyingPrice <= 0) {
                JOptionPane.showMessageDialog(this,
                        "Buying price must be a positive value.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Invalid buying price format. Please enter a valid number.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validate Selling Price (jTextField3)
        try {
            if (jTextField3.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Selling price cannot be empty.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

            double sellingPrice = Double.parseDouble(jTextField3.getText());
            if (sellingPrice <= 0) {
                JOptionPane.showMessageDialog(this,
                        "Selling price must be a positive value.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // Compare with buying price
            double buyingPrice = Double.parseDouble(jTextField1.getText());
            if (sellingPrice <= buyingPrice) {
                JOptionPane.showMessageDialog(this,
                        "Selling price must be higher than buying price.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Invalid selling price format. Please enter a valid number.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        try {
            if (jTextField4.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Quantity cannot be empty.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

            int quantity = Integer.parseInt(jTextField4.getText());
            if (quantity <= 0) {
                JOptionPane.showMessageDialog(this,
                        "Quantity must be a positive whole number.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Invalid quantity format. Please enter a whole number.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validate Low Stock Warning (jTextField7) only if checkbox is checked
        if (jCheckBox1.isSelected()) {
            try {
                if (jTextField7.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "Low stock warning threshold cannot be empty when enabled.",
                            "Validation Error",
                            JOptionPane.ERROR_MESSAGE);
                    return false;
                }

                int lowStockThreshold = Integer.parseInt(jTextField7.getText());
                if (lowStockThreshold <= 0) {
                    JOptionPane.showMessageDialog(this,
                            "Low stock warning threshold must be a positive whole number.",
                            "Validation Error",
                            JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                        "Invalid low stock threshold format. Please enter a whole number.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        // Validate Discount Fields only if checkbox is checked
        if (jCheckBox3.isSelected()) {
            // Validate Discount Type (jComboBox3)
            if (jComboBox3.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this,
                        "Please select a valid discount type (percentage or fixed).",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // Validate Discount Value (jTextField8)
            try {
                if (jTextField8.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "Discount value cannot be empty when discount is enabled.",
                            "Validation Error",
                            JOptionPane.ERROR_MESSAGE);
                    return false;
                }

                double discountValue = Double.parseDouble(jTextField8.getText());
                if (discountValue <= 0) {
                    JOptionPane.showMessageDialog(this,
                            "Discount value must be a positive number.",
                            "Validation Error",
                            JOptionPane.ERROR_MESSAGE);
                    return false;
                }

                // Additional validation for percentage discount
                if (jComboBox3.getSelectedIndex() == 1 && discountValue > 100) {
                    JOptionPane.showMessageDialog(this,
                            "Percentage discount cannot exceed 100%.",
                            "Validation Error",
                            JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                        "Invalid discount value format. Please enter a valid number.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // Validate Discount Dates
            if (jDateChooser4.getDate() == null || jDateChooser5.getDate() == null) {
                JOptionPane.showMessageDialog(this,
                        "Both discount start and end dates must be set when discount is enabled.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // Validate Discount Date Range
            if (!jDateChooser5.getDate().after(jDateChooser4.getDate())) {
                JOptionPane.showMessageDialog(this,
                        "Discount end date must be after the start date.",
                        "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } else {
            // If checkbox is not selected, clear/reset discount fields (optional)
            jComboBox3.setSelectedIndex(0);
            jTextField8.setText("");
            jDateChooser4.setDate(null);
            jDateChooser5.setDate(null);
        }

        // All validations passed
        return true;
    }

    public GRNItem validateAndCollectData() {
        if (!validateForm()) {
            return null; // Validation failed
        }

        GRNItem item = new GRNItem();

        // Supplier Information
        item.setSupplierMobile(this.supplierMobile); // Set when supplier is selected
        // Implement this method

        // Product Information
        item.setProductId(this.selectedProductId);   // Set when product is selected
        item.setProductName(this.productName);       // Set when product is selected

        // Stock Information
        item.setStockId(jLabel7.getText());
        item.setFromStockReorder(this.reorderID != null);

        // Dates
        item.setMfdDate(jDateChooser1.getDate());
        item.setExpDate(jDateChooser2.getDate());
        if (jCheckBox2.isSelected()) {
            item.setExpWarningDate(jDateChooser3.getDate());
        }

        // Pricing and Quantity
        item.setBuyingPrice(Double.parseDouble(jTextField1.getText()));
        item.setSellingPrice(Double.parseDouble(jTextField3.getText()));
        item.setQuantity(Integer.parseInt(jTextField4.getText()));

        // Low Stock Warning
        if (jCheckBox1.isSelected()) {
            item.setLowStockThreshold(Integer.parseInt(jTextField7.getText()));
        }

        // Discount Information
        if (jCheckBox3.isSelected()) {
            item.setDiscountType(jComboBox3.getSelectedItem().toString());
            item.setDiscountValue(Double.parseDouble(jTextField8.getText()));
            item.setDiscountStartDate(jDateChooser4.getDate());
            item.setDiscountEndDate(jDateChooser5.getDate());
        }

        // Transaction Details
        item.setItemTotal(Double.parseDouble(jTextField6.getText()));
        item.setPaidAmount(Double.parseDouble(jTextField9.getText()));

        return item;
    }

    private void saveGRNToDatabase(ArrayList<GRNItem> items) {
        try {
            if (items == null || items.isEmpty()) {
                throw new Exception("GRN item list is empty.");
            }

            String supplierMobile = items.get(0).getSupplierMobile();
            if (supplierMobile == null || supplierMobile.trim().isEmpty()) {
                throw new Exception("Supplier mobile is missing.");
            }

            double fullAmount = 0;
            double paidAmount = 0;
            for (GRNItem item : items) {
                fullAmount += item.getItemTotal();
                paidAmount += item.getPaidAmount();
            }

            // Insert into GRN table
            String insertGRNSQL = String.format(
                    "INSERT INTO grn (supplier_mobile, date, user_email, paid_amount, full_amount, created_at) "
                    + "VALUES ('%s', '%s', '%s', %.2f, %.2f, NOW())",
                    supplierMobile,
                    java.time.LocalDate.now().toString(),
                    "lexsoadmin@lexsomail.com",
                    paidAmount,
                    fullAmount
            );
            ShaneConnection.executeIUD(insertGRNSQL);

            // Get last GRN ID
            ResultSet rsGRN = ShaneConnection.executeSearch("SELECT LAST_INSERT_ID() AS last_id");
            int grnId = -1;
            if (rsGRN.next()) {
                grnId = rsGRN.getInt("last_id");
            } else {
                throw new Exception("Unable to retrieve GRN ID");
            }

            // Get last barcode
            ResultSet rsBarcode = ShaneConnection.executeSearch(
                    "SELECT barcode FROM stock WHERE barcode REGEXP '^[0-9]+$' ORDER BY CAST(barcode AS UNSIGNED) DESC LIMIT 1"
            );
            long nextBarcode = 1000000000000L;
            if (rsBarcode.next()) {
                try {
                    nextBarcode = Long.parseLong(rsBarcode.getString("barcode")) + 1;
                } catch (NumberFormatException e) {
                    nextBarcode++;
                }
            }

            // Insert stock and GRN items
            for (GRNItem item : items) {
                int stockId;

                if (item.isFromStockReorder()) {
                    String stockIdStr = item.getStockId();
                    if (stockIdStr == null || stockIdStr.trim().isEmpty()) {
                        throw new Exception("Stock ID is missing for reorder item.");
                    }
                    try {
                        stockId = Integer.parseInt(stockIdStr.trim());
                    } catch (NumberFormatException e) {
                        throw new Exception("Invalid stock ID format: " + stockIdStr);
                    }
                } else {
                    String newBarcode = Long.toString(nextBarcode++);
                    String discountStart = item.getDiscountStartDate() != null
                            ? "'" + new java.sql.Date(item.getDiscountStartDate().getTime()).toString() + "'"
                            : "NULL";

                    String discountEnd = item.getDiscountEndDate() != null
                            ? "'" + new java.sql.Date(item.getDiscountEndDate().getTime()).toString() + "'"
                            : "NULL";

                    String insertStockSQL = String.format(
                            "INSERT INTO stock (product_id, barcode, qty, buying_price, selling_price, mft, exp,  low_stock_warning, exp_warning_days, discount_type, discount_value, discount_start_date, discount_end_date, user_email, created_at) "
                            + "VALUES ('%s', '%s', %d, %.2f, %.2f, '%s', '%s', '%s', %d, %d, '%s', %.2f, '%s', '%s', '%s', NOW())",
                            item.getProductId(),
                            newBarcode,
                            item.getQuantity(),
                            item.getBuyingPrice(),
                            item.getSellingPrice(),
                            new java.sql.Date(item.getMfdDate().getTime()),
                            new java.sql.Date(item.getExpDate().getTime()),
                            item.getLowStockThreshold() != null ? item.getLowStockThreshold() : 0,
                            item.getExpWarningDate() != null
                            ? (int) ((item.getExpDate().getTime() - item.getExpWarningDate().getTime()) / (1000 * 60 * 60 * 24)) : 0,
                            item.getDiscountType() != null ? item.getDiscountType() : "no_discount",
                            item.getDiscountValue() != null ? item.getDiscountValue() : 0.0,
                            item.getDiscountStartDate() != null ? new java.sql.Date(item.getDiscountStartDate().getTime()).toString() : "0000-00-00",
                            item.getDiscountEndDate() != null ? new java.sql.Date(item.getDiscountEndDate().getTime()).toString() : "0000-00-00",
                            "lexoadmin@lexomail.com"
                    );

                    ShaneConnection.executeIUD(insertStockSQL);

                    ResultSet rsStock = ShaneConnection.executeSearch("SELECT LAST_INSERT_ID() AS last_stock_id");
                    if (rsStock.next()) {
                        stockId = rsStock.getInt("last_stock_id");
                    } else {
                        throw new Exception("Unable to retrieve stock ID");
                    }
                }

                String insertGRNItemSQL = String.format(
                        "INSERT INTO grn_item (grn_id, qty, price, stock_id, created_at) VALUES (%d, %d, %.2f, %d, NOW())",
                        grnId,
                        item.getQuantity(),
                        item.getBuyingPrice(),
                        stockId
                );

                ShaneConnection.executeIUD(insertGRNItemSQL);
            }

            javax.swing.JOptionPane.showMessageDialog(this, "GRN saved successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this, "Error saving GRN: " + e.getMessage());
        }
    }

    /**
     * Checks if a stock record exists in the database
     */
    private boolean stockExists(java.sql.Connection conn, String stockId) throws Exception {
        final String query = "SELECT 1 FROM stock WHERE id = ? LIMIT 1";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, stockId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // Returns true if stock exists
            }
        }
    }

// Helper method to get current user email (implement according to your auth system)
    private String getCurrentUserEmail() {
        // Return the logged in user's email
        return "stock@lexso.com"; // Example - replace with actual implementation
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton6 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jLabel13 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox<>();
        jLabel15 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jDateChooser4 = new com.toedter.calendar.JDateChooser();
        jTextField4 = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jLabel18 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jTextField1 = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButton7 = new javax.swing.JButton();
        jCheckBox2 = new javax.swing.JCheckBox();
        jCheckBox3 = new javax.swing.JCheckBox();
        jCheckBox1 = new javax.swing.JCheckBox();
        jLabel28 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jTextField7 = new javax.swing.JTextField();
        jTextField8 = new javax.swing.JTextField();
        jDateChooser3 = new com.toedter.calendar.JDateChooser();
        jLabel27 = new javax.swing.JLabel();
        jDateChooser5 = new com.toedter.calendar.JDateChooser();
        jLabel29 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jCheckBox4 = new javax.swing.JCheckBox();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel9.setText("GRN ID here");

        jLabel24.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel24.setText("Low Stock Warning:");
        jLabel24.setPreferredSize(new java.awt.Dimension(30, 16));

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel16.setText("EXP Date Warning:");
        jLabel16.setPreferredSize(new java.awt.Dimension(30, 16));

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

        jButton6.setBackground(new java.awt.Color(51, 51, 255));
        jButton6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton6.setText("Add Item");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton5.setBackground(new java.awt.Color(51, 51, 255));
        jButton5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton5.setText("Select");
        jButton5.setEnabled(false);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel17.setText("MFD Date:");

        jDateChooser2.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser2PropertyChange(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel13.setText("Selling Price:");

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "no_discount", "percentage", "fixed" }));
        jComboBox3.setEnabled(false);

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel15.setText("Item Total :");

        jLabel4.setForeground(new java.awt.Color(84, 84, 84));
        jLabel4.setText("View and manage your GR notes easily here");

        jDateChooser4.setEnabled(false);
        jDateChooser4.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser4PropertyChange(evt);
            }
        });

        jTextField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField4ActionPerformed(evt);
            }
        });
        jTextField4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField4KeyReleased(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel14.setText("QTY :");

        jLabel25.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel25.setText("Discount Start Date:");
        jLabel25.setPreferredSize(new java.awt.Dimension(30, 16));

        jTextField6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField6ActionPerformed(evt);
            }
        });
        jTextField6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField6KeyReleased(evt);
            }
        });

        jLabel7.setText("Stock ID here");

        jDateChooser1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser1PropertyChange(evt);
            }
        });

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel18.setText("EXP Date:");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setText("Goods Recived note");

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel19.setText("Stock id:");

        jLabel23.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel23.setText("Select Supplier:");

        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel21.setText("GRN ID");

        jTextField3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField3KeyReleased(evt);
            }
        });

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });

        jLabel26.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel26.setText("Discount End Date");
        jLabel26.setPreferredSize(new java.awt.Dimension(30, 16));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("Select item from Reorders:");

        jButton7.setBackground(new java.awt.Color(51, 51, 255));
        jButton7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton7.setText("Save GRN");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jCheckBox2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox2.setText("EXP Date Warning");
        jCheckBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox2ActionPerformed(evt);
            }
        });

        jCheckBox3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox3.setText("Item Discount");
        jCheckBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox3ActionPerformed(evt);
            }
        });

        jCheckBox1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox1.setText("Low Stock Warning");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jLabel28.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel28.setText("Discount Type:");
        jLabel28.setPreferredSize(new java.awt.Dimension(30, 16));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("Select Product");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel12.setText("Buying Price:");

        jButton4.setBackground(new java.awt.Color(51, 51, 255));
        jButton4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton4.setText("Select");
        jButton4.setEnabled(false);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jTextField7.setEnabled(false);
        jTextField7.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField7KeyReleased(evt);
            }
        });

        jTextField8.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField8KeyReleased(evt);
            }
        });

        jDateChooser3.setEnabled(false);
        jDateChooser3.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser3PropertyChange(evt);
            }
        });

        jLabel27.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel27.setText("Item Discount:");
        jLabel27.setPreferredSize(new java.awt.Dimension(30, 16));

        jDateChooser5.setEnabled(false);
        jDateChooser5.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser5PropertyChange(evt);
            }
        });

        jLabel29.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel29.setText("Paid amount:");

        jTextField9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField9ActionPerformed(evt);
            }
        });
        jTextField9.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField9KeyReleased(evt);
            }
        });

        jButton8.setBackground(new java.awt.Color(51, 51, 255));
        jButton8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton8.setText("Select");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton9.setBackground(new java.awt.Color(255, 0, 0));
        jButton9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton9.setText("Clear");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jCheckBox4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jCheckBox4.setText("Select Form Reorder");
        jCheckBox4.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBox4ItemStateChanged(evt);
            }
        });
        jCheckBox4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox4ActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel8.setText("GRN Total");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel10.setText("0.00");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel11.setText("0.00");

        jLabel20.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel20.setText("Paid Total");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(118, 118, 118)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGap(110, 110, 110))
                                    .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jDateChooser5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                    .addComponent(jLabel25, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jLabel24, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jDateChooser4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jLabel19)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jLabel12)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jDateChooser3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jLabel17)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jTextField4)
                                                    .addComponent(jTextField3))))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                            .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE)
                                            .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jButton8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jCheckBox2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jCheckBox3))
                                    .addComponent(jCheckBox1)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jCheckBox4))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane1))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 309, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 1021, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel21)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGap(110, 110, 110))
                                            .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(366, 366, 366)))))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(27, 27, 27)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 757, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jButton4))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jButton5))
                        .addGap(22, 22, 22)
                        .addComponent(jCheckBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel17)
                            .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel18)
                            .addComponent(jDateChooser2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jDateChooser3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14)
                            .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jDateChooser4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(4, 4, 4)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jDateChooser5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(8, 8, 8)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel15)
                            .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel29)
                            .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jCheckBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jCheckBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel20)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(17, 17, 17))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        GRNItem item = validateAndCollectData();
        if (item != null) {
            grnItemsList.add(item);

            // Update the table display
            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.addRow(new Object[]{
                item.getProductName(),
                item.getStockId(),
                new SimpleDateFormat("yyyy-MM-dd").format(item.getMfdDate()),
                new SimpleDateFormat("yyyy-MM-dd").format(item.getExpDate()),
                item.getExpWarningDate() != null ? "Yes" : "No",
                item.getQuantity(),
                item.getLowStockThreshold() != null ? item.getLowStockThreshold() : "N/A",
                item.getDiscountValue() != null ? item.getDiscountValue() + " (" + item.getDiscountType() + ")" : "N/A",
                item.getDiscountStartDate() != null ? new SimpleDateFormat("yyyy-MM-dd").format(item.getDiscountStartDate()) : "N/A",
                item.getDiscountEndDate() != null ? new SimpleDateFormat("yyyy-MM-dd").format(item.getDiscountEndDate()) : "N/A",
                item.getItemTotal(),
                item.getPaidAmount()
            });

            // Clear form for next item
            clearAll();
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        reorderSelect = new ReorderItemSelection();
        reorderSelect.setVisible(true);
        reorderSelect.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent evt) {
                reorderID = reorderSelect.getReorderID();
                if (reorderID != null) {
                    try {
                        String query = "SELECT `supplier`.`first_name`, `supplier`.`last_name`, `company`.`name`, `brand`.`name`, `product`.`name`, `stock_reorder`.`order_amount`, `stock_reorder`.`order_qty`, `stock`.`selling_price`, `stock`.`buying_price` "
                                + "FROM `stock_reorder` INNER JOIN `supplier` "
                                + "ON `supplier`.`mobile` = `stock_reorder`.`supplier_mobile` INNER JOIN `company` "
                                + "ON `supplier`.`company_id` = `company`.`id` INNER JOIN `stock` "
                                + "ON `stock`.`id` = `stock_reorder`.`stock_id` INNER JOIN `product` "
                                + "ON `stock`.`product_id` = `product`.`id` INNER JOIN `category_has_brand` "
                                + "ON `product`.`category` = `category_has_brand`.`category_has_brand_id` INNER JOIN `brand` "
                                + "ON `category_has_brand`.`brand_id` = `brand`.`id` WHERE `stock_reorder`.`id` = '" + reorderID + "'";
                        ResultSet rs = ShaneConnection.executeSearch(query);

                        while (rs.next()) {
                            supFName = rs.getString("supplier.first_name");
                            supLName = rs.getString("supplier.last_name");
                            supCompany = rs.getString("company.name");
                            jLabel5.setText("" + supFName + "  " + supLName + "  from  " + supCompany + "");
                            productName = rs.getString("product.name");
                            productBrand = rs.getString("brand.name");
                            jLabel6.setText("" + productBrand + " 's " + productName + "  Selected  ");
                            jButton8.setEnabled(false);
                            jTextField1.setText(rs.getString("buying_price"));
                            jTextField3.setText(rs.getString("selling_price"));
                            jTextField4.setText(rs.getString("stock_reorder.order_qty"));
                            jTextField6.setText(rs.getString("stock_reorder.order_amount"));
                        }

                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jDateChooser2PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser2PropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_jDateChooser2PropertyChange

    private void jDateChooser4PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser4PropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_jDateChooser4PropertyChange

    private void jTextField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField4ActionPerformed

    private void jTextField4KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField4KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField4KeyReleased

    private void jTextField6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField6ActionPerformed

    private void jTextField6KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField6KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField6KeyReleased

    private void jDateChooser1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser1PropertyChange

    }//GEN-LAST:event_jDateChooser1PropertyChange

    private void jTextField3KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField3KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3KeyReleased

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyReleased

    }//GEN-LAST:event_jTextField1KeyReleased

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        
        JOptionPane.showMessageDialog(this, "GRN Created successfully!");

        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);

//        saveGRNToDatabase(grnItemsList);
//        clearAll();
//        loadGRNID(); // Refresh GRN ID for next entry
//        grnItemsList.clear(); // Clear the list for new GRN
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox2ActionPerformed
        if (jCheckBox2.isSelected()) {
            jDateChooser3.setEnabled(true);
            expWarningCheckBox = true;
        } else {
            jDateChooser3.setDate(null);
            jDateChooser3.setEnabled(false);
            expWarningCheckBox = false;
        }
    }//GEN-LAST:event_jCheckBox2ActionPerformed

    private void jCheckBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox3ActionPerformed
        if (jCheckBox3.isSelected()) {
            jComboBox3.setEnabled(true);
            jTextField8.setEditable(true);
            jDateChooser4.setEnabled(true);
            jDateChooser5.setEnabled(true);
            discountCheckBox = true;
        } else {
            jComboBox3.setSelectedIndex(0);
            jTextField8.setText("0");
            jDateChooser4.setDate(null);
            jDateChooser5.setDate(null);
            jComboBox3.setEnabled(false);
            jTextField8.setEditable(false);
            jDateChooser4.setEnabled(false);
            jDateChooser5.setEnabled(false);
            discountCheckBox = false;
        }
    }//GEN-LAST:event_jCheckBox3ActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed

        if (jCheckBox1.isSelected()) {
            jTextField7.setEditable(true);
            jTextField7.setEnabled(true);
            lowStockCheckBox = true;
        } else {
            jTextField7.setText("0");
            jTextField7.setEditable(false);
            jTextField7.setEnabled(false);
            lowStockCheckBox = false;
        }
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        selectProduct = new selectProductFrame();
        selectProduct.setVisible(true);
        selectProduct.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent evt) {
                selectedProductId = selectProduct.returnSelectedPID();
                if (selectedProductId != null) {
                    try {
                        String query = "SELECT `product`.`name`, `brand`.`name` FROM `product`  "
                                + " JOIN `category_has_brand` ON `product`.`category`= `category_has_brand`.`category_has_brand_id` "
                                + " JOIN `brand` ON `category_has_brand`.`brand_id` = `brand`.`id` "
                                + "where `product`.`id` = '" + selectedProductId + "'";
                        ResultSet rs = ShaneConnection.executeSearch(query);

                        while (rs.next()) {
                            productName = rs.getString("product.name");
                            productBrand = rs.getString("brand.name");
                            jLabel6.setText("" + productBrand + " 's " + productName + "  Selected  ");
                        }

                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
    }//GEN-LAST:event_jButton4ActionPerformed


    private void jTextField7KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField7KeyReleased

    }//GEN-LAST:event_jTextField7KeyReleased

    private void jTextField8KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField8KeyReleased

    }//GEN-LAST:event_jTextField8KeyReleased

    private void jDateChooser3PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser3PropertyChange

    }//GEN-LAST:event_jDateChooser3PropertyChange

    private void jDateChooser5PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser5PropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_jDateChooser5PropertyChange

    private void jTextField9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField9ActionPerformed

    private void jTextField9KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField9KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField9KeyReleased

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        supSelect = new suplierManagement(this);
        supSelect.setVisible(true);
        supSelect.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                // Get the selected mobile directly from the dialog
                String selectedMobile = supSelect.getSelectedMobile();

                if (selectedMobile == null || selectedMobile.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(GRNWindow.this,
                            "No supplier was selected",
                            "Selection Error",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Only proceed if we got a valid mobile
                supplierMobile = selectedMobile;

                try {
                    String query = "SELECT s.first_name, s.last_name, c.name "
                            + "FROM supplier s JOIN company c "
                            + "ON s.company_id = c.id "
                            + "WHERE s.mobile = ?";

                    try (PreparedStatement pstmt = ShaneConnection.getConnection().prepareStatement(query)) {
                        pstmt.setString(1, supplierMobile);

                        try (ResultSet rs = pstmt.executeQuery()) {
                            if (rs.next()) {
                                supFName = rs.getString("first_name");
                                supLName = rs.getString("last_name");
                                supCompany = rs.getString("name");
                                jLabel5.setText(supFName + " " + supLName + " from " + supCompany);
                                jButton4.setEnabled(true); // Enable product selection
                            } else {
                                supplierMobile = null; // Reset if not found
                                JOptionPane.showMessageDialog(GRNWindow.this,
                                        "Supplier not found in database",
                                        "Error",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                } catch (Exception e1) {
                    supplierMobile = null; // Reset on error
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(GRNWindow.this,
                            "Error loading supplier details",
                            "Database Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked

    }//GEN-LAST:event_jTable1MouseClicked

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        clearAll();
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jCheckBox4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox4ActionPerformed

    }//GEN-LAST:event_jCheckBox4ActionPerformed

    private void jCheckBox4ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox4ItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            jButton8.setEnabled(false);
            jButton4.setEnabled(false);
            jButton5.setEnabled(true);
        } else {
            jButton8.setEnabled(true);
            jButton4.setEnabled(false);
            jButton5.setEnabled(false);
        }
    }//GEN-LAST:event_jCheckBox4ItemStateChanged

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        FlatMacLightLaf.setup();

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GRNWindow().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JComboBox<String> jComboBox3;
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
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
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
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    // End of variables declaration//GEN-END:variables
}
