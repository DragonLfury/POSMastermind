/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.lexso.sales;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import com.lexso.connection.DatabaseConnection;
import com.lexso.customers.Customer;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import com.lexso.util.InvoiceItem;
//import lexso.Signin;
import com.lexso.util.CurrentUser;
import static com.lexso.util.IDGenerator.generateID;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author User6
 */
public class Main extends javax.swing.JFrame {

    HashMap<String, InvoiceItem> invoiceItemMap = new HashMap<>();
    private HashMap<String, Object> paymentMap = new HashMap<>();
    private double totalPayments = 0;
    private String selectedDiscountId = null;
    private boolean paymentMade = false;
    private double pointsToAdd = 0;
    private double totalItemDiscounts = 0;
    private static double discountedValue;

    private String loggedInUsername = CurrentUser.getUsername();
    private String loggedInEmail = CurrentUser.getEmail();

    /**
     * Creates new form Main
     */
    public Main() {
        initComponents();
        setExtendedState(MAXIMIZED_BOTH);
        stock_id.grabFocus();
        loadSVG();
        invoiceNumberField.setText(generateID("invoice", "id", "I"));
        loadInvoiceItems();
        jButton3.setEnabled(false);
        employeLabel.setText(loggedInUsername);

        // Initialize payment buttons as disabled
        jButton5.setEnabled(false);
        jButton4.setEnabled(false);
        jButton33.setEnabled(false);
        jButton6.setEnabled(false);
        // Initialize for unknown customer by default
        setCustomerMobile("0000000000");
        setCustomerName("Unknown Customer");
        setAvailablePoints("0");
        jTabbedPane1.setEnabledAt(jTabbedPane1.indexOfTab("Loyality Points"), false); // Disable loyalty points tab by default
    }

    private void loadSVG() {

        FlatSVGIcon houseIcon = new FlatSVGIcon("com/lexso/sales/icon/home.svg", homeIcon.getWidth(), homeIcon.getHeight());
        homeIcon.setIcon(houseIcon);
    }

    public void loadInvoiceItems() {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);
        total = 0;
        totalItemDiscounts = 0; // Reset item discounts when reloading

        for (InvoiceItem invoiceItem : invoiceItemMap.values()) {
            Vector<String> vector = new Vector<>();
            vector.add(invoiceItem.getStockID());
            vector.add(invoiceItem.getCategoryName());
            vector.add(invoiceItem.getName());
            vector.add(invoiceItem.getQty());
            vector.add(invoiceItem.getSellingPrice());
            vector.add(invoiceItem.getDiscount());

            double qty = Double.parseDouble(invoiceItem.getQty());
            double pricePerUnit = Double.parseDouble(invoiceItem.getSellingPrice());
            double itemTotal = qty * pricePerUnit;
            double itemDiscount = calculateItemDiscount(invoiceItem.getDiscount(), itemTotal, qty);
            totalItemDiscounts += itemDiscount; // Accumulate item discounts

            double finalItemTotal = itemTotal - itemDiscount;
            total += finalItemTotal;
            vector.add(String.valueOf(finalItemTotal));
            model.addRow(vector);
        }
        totalField.setText(String.valueOf(total));
        calculate(); // Recalculate invoice-level discounts

        // Update payment buttons state
        updatePaymentButtonsState();
    }

    private double calculateItemDiscount(String discountString, double itemTotal, double quantity) {
        if (discountString == null || discountString.trim().isEmpty()) {
            return 0.0;
        }

        discountString = discountString.trim();

        try {
            if (discountString.endsWith("%")) {
                // Percentage discount, applied per unit and scaled by quantity
                double percent = Double.parseDouble(discountString.replace("%", "").trim());
                double discountPerUnit = (Double.parseDouble(invoiceItemMap.get(stock_id.getText()).getSellingPrice()) * (percent / 100));
                return discountPerUnit * quantity;
            } else {
                // Fixed amount discount, applied per unit and scaled by quantity
                double fixedDiscount = Double.parseDouble(discountString.replaceAll("[^0-9.]", ""));
                return fixedDiscount * quantity;
            }
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    public void dataLoadToTable() {

        if (paymentMade) {
            JOptionPane.showMessageDialog(this, "Please generate a new Invoice", "Payment Already Made", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Validate required fields
        if (stock_id.getText().isEmpty() || jFormattedTextField2.getText().isEmpty() || jLabel8.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a product to add", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String stockID = stock_id.getText();
        String CategoryName = jLabel21.getText();
        String productID = jLabel13.getText();
        String discount = jLabel30.getText();
        String productName = jLabel15.getText();
        String sellingPrice = jFormattedTextField2.getText();
        String qty = jFormattedTextField1.getText();
        String mfd = jLabel17.getText();
        String exp = jLabel19.getText();
        String availableQtyText = jLabel8.getText();

        // Validate quantity field
        if (qty.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter quantity", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validate quantity value
        try {
            double quantity = Double.parseDouble(qty);
            double availableQty = Double.parseDouble(availableQtyText);

            if (quantity <= 0) {
                JOptionPane.showMessageDialog(this, "Quantity must be greater than 0", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (quantity > availableQty) {
                JOptionPane.showMessageDialog(this, "Not enough stock available", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid quantity value", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        InvoiceItem invoiceItem = new InvoiceItem();
        invoiceItem.setCategoryName(CategoryName);
        invoiceItem.setProductID(productID);
        invoiceItem.setDiscount(discount);
        invoiceItem.setExp(exp);
        invoiceItem.setMfd(mfd);
        invoiceItem.setName(productName);
        invoiceItem.setQty(qty);
        invoiceItem.setSellingPrice(sellingPrice);
        invoiceItem.setStockID(stockID);

        if (invoiceItemMap.get(stockID) == null) {
            invoiceItemMap.put(stockID, invoiceItem);
        } else {
            InvoiceItem found = invoiceItemMap.get(stockID);
            int option = JOptionPane.showConfirmDialog(this, "Do you wanna update the Quantity of Product : " + productName + "", "Message", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

            if (option == JOptionPane.YES_OPTION) {
                found.setQty(String.valueOf(Double.parseDouble(found.getQty()) + Double.parseDouble(qty)));
            }
        }
        loadInvoiceItems();
        jFormattedTextField1.setText("");
    }

    private String customerMobile;
    private String customerName;
    private String CategoryName;
    private String productID;
    private String AvailablePoints;
    private String stockID;
    private String productName;
    private String productPrice;
    private String productAvailableQTY;
    private String productmfd;
    private String productexp;

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
        getCustomerMobile().setText(customerMobile);
        getCustomerMobile().revalidate();
        getCustomerMobile().repaint();
        System.out.println("Customer Mobile: " + customerMobile);

        // Enable or disable loyalty points tab based on customer status
        if (customerMobile.equals("0000000000")) {
            jTabbedPane1.setEnabledAt(jTabbedPane1.indexOfTab("Loyality Points"), false);
            jFormattedTextField7.setText("0");
            jFormattedTextField8.setText("");
            jButton6.setEnabled(false);
        } else {
            jTabbedPane1.setEnabledAt(jTabbedPane1.indexOfTab("Loyality Points"), true);
        }
    }

    public void setAvailablePoints(String AvailablePoints) {
        this.AvailablePoints = AvailablePoints;
        getAvailablePoints().setText(AvailablePoints);
        jFormattedTextField7.setText(AvailablePoints);
        jFormattedTextField7.setEditable(false);
        getAvailablePoints().revalidate();
        getAvailablePoints().repaint();
        System.out.println("Available Points: " + AvailablePoints);
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
        jTextField1.setText(customerName);
        getCustomerName().setText(customerName);
        getCustomerName().revalidate();
        getCustomerName().repaint();
        System.out.println("Customer Name: " + customerName);
    }

    public JTextField getAvailablePoints() {
        return jTextField2;
    }

    public JTextField getCustomerMobile() {
        return jTextField3;
    }

    public JTextField getCustomerName() {
        return jTextField1;
    }

    public void setStockID(String stockID) {
        stock_id.setText(stockID);
    }

    public void setCategoryName(String CategoryName) {
        System.out.println("Category Name: " + CategoryName);
        jLabel21.setText(CategoryName);
    }

    public void setProductID(String productID) {
        System.out.println("productID " + productID);
        jLabel13.setText(productID);
    }

    public void setDiscount(String discount) {
        System.out.println("Discount " + discount);
        jLabel30.setText(discount);
    }

    public void setProductName(String productName) {
        jLabel15.setText(productName);
    }

    public void setProductPrice(String productPrice) {
        jFormattedTextField2.setText(productPrice);
    }

    public void setProductAvailableQTY(String productAvailableQTY) {
        jLabel8.setText(productAvailableQTY);
    }

    public void setProductMFD(String productmfd) {
        jLabel17.setText(productmfd);
    }

    public void setProductEXP(String productexp) {
        jLabel19.setText(productexp);
    }

    public JFormattedTextField getProductQTY() {
        return jFormattedTextField1;
    }

    private double total = 0;
    private double invoiceDiscount = 0; // Changed from static to instance variable and renamed
    private double payment = 0;
    private double balance = 0;
    private double newPoints = 0; // Changed from static to instance variable
    private boolean withdrawPoints = false;
    private String discountMethod = "Select";

    private void calculate() {
        try {
            // Clear the discount combobox
            discountField.removeAllItems();

            // Step 1: Load all available discounts
            String query = "SELECT id, discount_name, invoice_subtotal FROM invoice_discount WHERE NOW() BETWEEN start_date AND end_date";
            ResultSet rs = DatabaseConnection.executeSearch(query);

            boolean discountAvailable = false;
            while (rs.next()) {
                selectedDiscountId = rs.getString("id");
                String discountName = rs.getString("discount_name");
                discountField.addItem(discountName);
                discountAvailable = true;
            }

            if (!discountAvailable) {
                discountField.addItem("No Discount");
                discountMethod = "No Discount";
                discountField.setSelectedItem("No Discount");
            }

            if (totalField.getText().isEmpty()) {
                payment = 0;
                balance = 0;
                balanceField.setText("0");
                return;
            }

            total = Double.parseDouble(totalField.getText());

            if (total < 0) {
                JOptionPane.showMessageDialog(this, "Total cannot be negative.");
                total = 0;
                balance = 0;
                balanceField.setText("0");
                return;
            }

            // Step 2: Filter discounts to only include the one with the closest invoice_subtotal <= total
            query = "SELECT id, discount_name, discount_type, discount_value, invoice_subtotal "
                    + "FROM invoice_discount "
                    + "WHERE NOW() BETWEEN start_date AND end_date AND invoice_subtotal <= " + total + " "
                    + "ORDER BY ABS(invoice_subtotal - " + total + ") ASC LIMIT 1";
            rs = DatabaseConnection.executeSearch(query);

            if (rs.next()) {
                selectedDiscountId = rs.getString("id");
                String closestDiscountName = rs.getString("discount_name");
                String discountType = rs.getString("discount_type");
                double discountValue = rs.getDouble("discount_value");

                discountField.setSelectedItem(closestDiscountName);
                discountMethod = closestDiscountName;

                // Apply the discount
                if (discountType.equalsIgnoreCase("percentage")) {
                    invoiceDiscount = (total * discountValue) / 100;
                } else if (discountType.equalsIgnoreCase("fixed")) {
                    invoiceDiscount = discountValue;
                }

                if (invoiceDiscount > total) {
                    invoiceDiscount = total;
                }

                total -= invoiceDiscount;
            } else {
                selectedDiscountId = null;
                invoiceDiscount = 0;
                discountField.setSelectedItem("No Discount");
                discountMethod = "No Discount";
            }

            balance = -total;
            totalField.setText(String.valueOf(total));
            balanceField.setText(String.valueOf(balance));

            // Update payment buttons state at the end
            updatePaymentButtonsState();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error applying discount: " + e.getMessage());
        }
    }

    private void addCashPayment() {
        if (paymentField1.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the cash amount.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            payment = Double.parseDouble(paymentField1.getText());

            if (payment <= 0) {
                JOptionPane.showMessageDialog(this, "Cash amount must be greater than 0", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Accumulate cash payments
            double currentCashTotal = 0;
            if (paymentMap.containsKey("Cash")) {
                currentCashTotal = (Double) paymentMap.get("Cash");
            }
            currentCashTotal += payment;
            paymentMap.put("Cash", currentCashTotal); // Store the accumulated total

            totalPayments += payment;
            cashField.setText(String.valueOf(totalPayments));
            balance += payment;
            balanceField.setText(String.valueOf(balance));

            paymentField1.setText("");
            paymentMade = true;

            updateBalance();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid cash amount", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    }

    private void addGiftCardPayment() {
        if (voucherField.getText().isEmpty()) { // Only check for voucher code initially
            JOptionPane.showMessageDialog(this, "Please enter the voucher code.");
            return;
        }
        try {
            String giftCardCode = voucherField.getText().trim();
            // Look up the amount from the database using the gift card code
            double giftCardAmount = getGiftCardAmount(giftCardCode);

            if (giftCardAmount <= 0) {
                JOptionPane.showMessageDialog(this, "Invalid or inactive gift card code, or zero balance.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Optional: If you want to allow partial redemption, you'd need another input field
            // and logic to ensure the entered amount doesn't exceed giftCardAmount.
            // For simplicity, this assumes the whole balance is used.
            // If totalVoucherField is for *user input* of amount, then keep it and validate.
            // For instance, you could take the input from totalVoucherField, but max it at giftCardAmount.
            double amountToRedeem = Double.parseDouble(totalVoucherField.getText()); // Still use this if user specifies amount
            if (amountToRedeem <= 0 || amountToRedeem > giftCardAmount) {
                JOptionPane.showMessageDialog(this, "Invalid redemption amount. Must be positive and not exceed available balance of " + giftCardAmount, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            giftCardAmount = amountToRedeem; // Use the user-specified amount

            // ... rest of your existing logic for paymentMap ...
            HashMap<String, Double> giftCardsUsed;
            if (!paymentMap.containsKey("Gift Cards")) {
                giftCardsUsed = new HashMap<>();
                paymentMap.put("Gift Cards", giftCardsUsed);
            } else {
                giftCardsUsed = (HashMap<String, Double>) paymentMap.get("Gift Cards");
            }

            double currentCardAmount = giftCardsUsed.getOrDefault(giftCardCode, 0.0);
            currentCardAmount += giftCardAmount;
            giftCardsUsed.put(giftCardCode, currentCardAmount);

            payment = giftCardAmount;
            totalPayments += payment;
            cashField.setText(String.valueOf(totalPayments));
            balance += payment;
            balanceField.setText(String.valueOf(balance));

            // Update gift card status or balance in the database
            // If a card is only partially redeemed, you'd update its remaining balance.
            // If fully redeemed, set to 'Inactive'.
            // This query assumes full redemption:
            DatabaseConnection.executeIUD("UPDATE `gift_cards` SET `status` = 'Inactive' WHERE `card_code` = '" + giftCardCode + "'");
            // Or if partial redemption:
            // DatabaseConnection.executeIUD("UPDATE `gift_cards` SET `amount` = amount - " + giftCardAmount + " WHERE `card_code` = '" + giftCardCode + "'");

            paymentMade = true;
            JOptionPane.showMessageDialog(this, "Gift card payment applied successfully for " + giftCardCode + ".");

            voucherField.setText("");
            totalVoucherField.setText("");
            jLabel5.setText("Status"); // Update status label if you have one showing card status

            updateBalance();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid amount entered.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error processing gift card payment: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void addCardPayment() {
        if (jFormattedTextField3.getText().isEmpty() || jTextField4.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both card number and amount.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String cardNumber = jTextField4.getText().trim(); // Card number is the key
            double cardAmount = Double.parseDouble(jFormattedTextField3.getText());

            if (cardNumber.isEmpty() || cardAmount <= 0) {
                JOptionPane.showMessageDialog(this, "Invalid card number or amount.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // We need a nested HashMap to store total amount for each unique card number
            HashMap<String, Double> cardsUsed;
            if (!paymentMap.containsKey("Cards")) {
                cardsUsed = new HashMap<>();
                paymentMap.put("Cards", cardsUsed);
            } else {
                cardsUsed = (HashMap<String, Double>) paymentMap.get("Cards");
            }

            // Accumulate amount for this specific card number
            double currentCardTotal = cardsUsed.getOrDefault(cardNumber, 0.0);
            currentCardTotal += cardAmount;
            cardsUsed.put(cardNumber, currentCardTotal); // Store accumulated total for this card number

            payment = cardAmount; // This 'payment' variable is for current transaction balance update
            totalPayments += payment;
            cashField.setText(String.valueOf(totalPayments));
            balance += payment;
            balanceField.setText(String.valueOf(balance));

            jFormattedTextField3.setText("");
            jTextField4.setText(""); // Clear card number field after adding
            paymentMade = true;

            updateBalance();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid card payment amount or number.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error processing card payment: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void addLoyaltyPointsPayment() {
        if (jFormattedTextField8.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter loyalty points amount.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double pointsBalance = Double.parseDouble(jFormattedTextField7.getText());
            payment = Double.parseDouble(jFormattedTextField8.getText());

            if (payment <= 0) {
                JOptionPane.showMessageDialog(this, "Loyalty points must be greater than 0", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (payment > pointsBalance) {
                JOptionPane.showMessageDialog(this, "Insufficient loyalty points balance.", "Error", JOptionPane.ERROR_MESSAGE); // Changed to ERROR_MESSAGE
                return;
            }

            // Accumulate loyalty points payments
            double currentLoyaltyTotal = 0;
            if (paymentMap.containsKey("loyalty_points")) {
                currentLoyaltyTotal = (Double) paymentMap.get("loyalty_points");
            }
            currentLoyaltyTotal += payment;
            paymentMap.put("loyalty_points", currentLoyaltyTotal); // Store the accumulated total

            totalPayments += payment;
            cashField.setText(String.valueOf(totalPayments));

            balance += payment;
            balanceField.setText(String.valueOf(balance));

            newPoints = pointsBalance - payment;
            // Update the points balance display
            jFormattedTextField7.setText(String.valueOf(newPoints));

            // Clear the payment amount field
            jFormattedTextField8.setText("");

            paymentMade = true;

            updateBalance();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid loyalty points amount", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    }

    
    private void updateBalance() {
        if (balance == 0) {
            balanceField.setText("0");
            jButton3.setEnabled(true);
            JOptionPane.showMessageDialog(this, "Payment complete. You can finalize the sale.");
        } else if (balance > 0) {
            double remainingBalance = balance;
            pointsToAdd = 0;

            // Check if customer is "UNKNOWN"
            if (customerMobile.equals("0000000000")) {
                // For unknown customers, return the entire balance as cash
                balanceField.setText(String.valueOf(remainingBalance));
                JOptionPane.showMessageDialog(this,
                        "Customer balance: " + remainingBalance + "\n"
                        + "Please return this amount to the customer.");
            } else {
                // For registered customers, check if there's any amount that can be converted to points
                double fractionalPart = balance; // For amounts < 10, consider the whole amount

                if (balance >= 10) {
                    fractionalPart = balance % 10; // For amounts ≥ 10, consider only the fractional part
                }

                if (fractionalPart > 0) {
                    int option = JOptionPane.showConfirmDialog(this,
                            "Would you like to add " + fractionalPart + " to customer's loyalty points?\n"
                            + "The remaining " + (balance - fractionalPart) + " will be given as cash balance.",
                            "Convert Balance to Points",
                            JOptionPane.YES_NO_OPTION);

                    if (option == JOptionPane.YES_OPTION) {
                        pointsToAdd = fractionalPart;
                        remainingBalance = balance - fractionalPart;

                        // Show confirmation but don't update points yet
                        JOptionPane.showMessageDialog(this,
                                fractionalPart + " points will be added to customer's account when invoice is finalized.\n"
                                + remainingBalance + " will be given as cash balance.");
                    }
                }

                // Update the balance field with the remaining balance to be given to customer
                balanceField.setText(String.valueOf(remainingBalance));

                // Show the remaining balance to be given to customer
                if (remainingBalance > 0) {
                    JOptionPane.showMessageDialog(this,
                            "Customer balance: " + remainingBalance + "\n"
                            + "Please return this amount to the customer.");
                }
            }

            jButton3.setEnabled(true);
        } else {
            balanceField.setText(String.valueOf(balance));
            jButton3.setEnabled(false);
        }
    }

    private void completeSale() {
        if (balance >= 0) {

            resetFields();
        } else {
            JOptionPane.showMessageDialog(this, "Please complete the payment before finalizing the sale. ⚠️");
        }
    }

    private void updatePaymentButtonsState() {
        boolean enableButtons = !totalField.getText().equals("0") && !totalField.getText().isEmpty();

        jButton5.setEnabled(enableButtons);  // Cash payment
        jButton4.setEnabled(enableButtons); // Gift Card payment
        jButton33.setEnabled(enableButtons); // Card payment
        jButton6.setEnabled(enableButtons); // Loyalty Points payment
    }

    private void resetFields() {
        //Validate part
        paymentMade = false;
        //customerpanel
        jTextField1.setText("Unknown Customer");
        jTextField3.setText("0000000000");
        jTextField2.setText("0");
        //stockpanel
        stock_id.setText("");
        jFormattedTextField1.setText("");
        jLabel8.setText("QTY");
        jFormattedTextField2.setText("");
        //product details
        jLabel21.setText("CATEGORY NAME HERE");
        jLabel13.setText("PRODUCT ID HERE");
        jLabel15.setText("PRODUCT NAME HERE");
        jLabel17.setText("PRODUCT MFD HERE");
        jLabel19.setText("PRODUCT EXP HERE");
        jLabel30.setText("PRODUCT DISCOUNT");
        //TABLE
        jTable1.clearSelection();
        paymentMap.clear();
        invoiceItemMap.clear();
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);
        //CASH
        paymentField1.setText("0");
        //GIFT CARD
        voucherField.setText("");
        totalVoucherField.setText("");
        jLabel5.setText("Status");
        //CARD
        jFormattedTextField3.setText("");
        //POINTS
        jFormattedTextField7.setText("0");
        jFormattedTextField8.setText("");
        //total and balance
        totalField.setText("0");
        balanceField.setText("0");
        //print invoice btn
        jButton3.setEnabled(false);
        invoiceNumberField.setText(generateID("invoice", "id", "I"));
        // Reset total payments
        totalPayments = 0;
        cashField.setText("0");
        // Reset discount ID
        selectedDiscountId = null;
        // Disable payment buttons and loyalty points tab
        updatePaymentButtonsState();
        jTabbedPane1.setEnabledAt(jTabbedPane1.indexOfTab("Loyality Points"), false);

    }

    private void savePaymentsToDatabase(String invoiceID) {
        try {
            // Iterate over the HashMap to save consolidated payments
            for (String paymentMethodKey : paymentMap.keySet()) {
                Object value = paymentMap.get(paymentMethodKey);
                String transactionReference = null;
                String paymentTypeForDb = paymentMethodKey.toLowerCase().replace(" ", "_"); // Standardize name for DB

                if (paymentMethodKey.equals("Cash")) {
                    double amount = (Double) value;
                    transactionReference = "Cash Payment";
                    String query = "INSERT INTO `payment_method` (invoice_id, payment_type, amount, transaction_reference) "
                            + "VALUES ('" + invoiceID + "', '" + paymentTypeForDb + "', " + amount + ", '" + transactionReference + "')";
                    DatabaseConnection.executeIUD(query);

                } else if (paymentMethodKey.equals("loyalty_points")) {
                    double amount = (Double) value;
                    transactionReference = "Loyalty Points Redemption";
                    String query = "INSERT INTO `payment_method` (invoice_id, payment_type, amount, transaction_reference) "
                            + "VALUES ('" + invoiceID + "', '" + paymentTypeForDb + "', " + amount + ", '" + transactionReference + "')";
                    DatabaseConnection.executeIUD(query);

                } else if (paymentMethodKey.equals("Gift Cards")) {
                    // This value is a HashMap<String, Double> where key is gift card code and value is its amount
                    if (value instanceof HashMap) {
                        HashMap<String, Double> giftCardsUsed = (HashMap<String, Double>) value;
                        for (java.util.Map.Entry<String, Double> entry : giftCardsUsed.entrySet()) {
                            String giftCardCode = entry.getKey();
                            double amount = entry.getValue();
                            // Each gift card is a separate row
                            String query = "INSERT INTO `payment_method` (invoice_id, payment_type, amount, transaction_reference) "
                                    + "VALUES ('" + invoiceID + "', 'gift_card', " + amount + ", '" + giftCardCode + "')";
                            DatabaseConnection.executeIUD(query);
                        }
                    }

                } else if (paymentMethodKey.equals("Cards")) {
                    // This value is a HashMap<String, Double> where key is card number and value is its accumulated amount
                    if (value instanceof HashMap) {
                        HashMap<String, Double> cardsUsed = (HashMap<String, Double>) value;
                        for (java.util.Map.Entry<String, Double> entry : cardsUsed.entrySet()) {
                            String cardNumber = entry.getKey();
                            double amount = entry.getValue();
                            // Each distinct card number is a separate row
                            String query = "INSERT INTO `payment_method` (invoice_id, payment_type, amount, transaction_reference) "
                                    + "VALUES ('" + invoiceID + "', 'card', " + amount + ", '" + cardNumber + "')";
                            DatabaseConnection.executeIUD(query);
                        }
                    }
                }
            }

            JOptionPane.showMessageDialog(this, "All payments saved successfully!");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error saving payments: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private double getGiftCardAmount(String giftCardCode) {
        double amount = 0.0;

        try {
            String query = "SELECT amount FROM `gift_cards` WHERE card_code = '" + giftCardCode + "'";
            ResultSet rs = DatabaseConnection.executeSearch(query);
            if (rs.next()) {
                amount = rs.getDouble("amount");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return amount;
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
        homeIcon = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        employeLabel = new javax.swing.JLabel();
        stock_id = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jFormattedTextField1 = new javax.swing.JFormattedTextField();
        jLabel23 = new javax.swing.JLabel();
        jFormattedTextField2 = new javax.swing.JFormattedTextField();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel85 = new javax.swing.JLabel();
        totalField = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        balanceField = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel26 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel11 = new javax.swing.JPanel();
        jLabel33 = new javax.swing.JLabel();
        paymentField1 = new javax.swing.JFormattedTextField();
        jSeparator6 = new javax.swing.JSeparator();
        jButton5 = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        voucherField = new javax.swing.JFormattedTextField();
        jSeparator5 = new javax.swing.JSeparator();
        totalVoucherField = new javax.swing.JFormattedTextField();
        jLabel32 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        jFormattedTextField3 = new javax.swing.JFormattedTextField();
        jLabel29 = new javax.swing.JLabel();
        jButton33 = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JSeparator();
        jTextField4 = new javax.swing.JTextField();
        jPanel12 = new javax.swing.JPanel();
        jLabel34 = new javax.swing.JLabel();
        jFormattedTextField7 = new javax.swing.JFormattedTextField();
        jSeparator7 = new javax.swing.JSeparator();
        jFormattedTextField8 = new javax.swing.JFormattedTextField();
        jSeparator8 = new javax.swing.JSeparator();
        jLabel35 = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
        discountField = new javax.swing.JComboBox<>();
        jLabel36 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jSeparator9 = new javax.swing.JSeparator();
        jPanel14 = new javax.swing.JPanel();
        jLabel37 = new javax.swing.JLabel();
        cashField = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        invoiceNumberField = new javax.swing.JTextField();
        jLabel39 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jLabel40 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 153, 255));
        jLabel1.setText("Sales Transaction ");

        jLabel2.setForeground(new java.awt.Color(0, 102, 102));
        jLabel2.setText("Streamlined process for managing customer purchases and payments.");

        homeIcon.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        homeIcon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                homeIconMouseClicked(evt);
            }
        });

        jLabel41.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel41.setText("Employee :");

        employeLabel.setText("Employee email here");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(employeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(homeIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(homeIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel41)
                                .addComponent(employeLabel))
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(16, 16, 16))
        );

        stock_id.setEditable(false);
        stock_id.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stock_idActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(72, 158, 231));
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Select");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel10.setText("Quantity : ");

        jFormattedTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jFormattedTextField1KeyReleased(evt);
            }
        });

        jLabel23.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel23.setText("Price : ");

        jFormattedTextField2.setEditable(false);

        jLabel85.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel85.setForeground(new java.awt.Color(102, 102, 102));
        jLabel85.setText("Total");

        totalField.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        totalField.setForeground(new java.awt.Color(111, 214, 75));
        totalField.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        totalField.setText("0");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(totalField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(63, Short.MAX_VALUE)
                .addComponent(jLabel85)
                .addGap(41, 41, 41))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel85)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(totalField)
                .addContainerGap(30, Short.MAX_VALUE))
        );

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(102, 102, 102));
        jLabel11.setText("Balance");

        balanceField.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        balanceField.setForeground(new java.awt.Color(255, 158, 40));
        balanceField.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        balanceField.setText("0");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jLabel11)
                .addContainerGap(19, Short.MAX_VALUE))
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(balanceField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(balanceField)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel26.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel26.setText("Add Payment ");

        jLabel33.setText("Amount");

        paymentField1.setBorder(null);
        paymentField1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        paymentField1.setText("0");
        paymentField1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N

        jButton5.setBackground(new java.awt.Color(72, 158, 231));
        jButton5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton5.setForeground(new java.awt.Color(255, 255, 255));
        jButton5.setText("Add Payment");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(paymentField1)
                        .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 402, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 401, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(71, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel33)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(paymentField1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(158, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Cash", jPanel11);

        jLabel31.setText("Gift Card Number");

        voucherField.setBorder(null);
        voucherField.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        voucherField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                voucherFieldKeyReleased(evt);
            }
        });

        totalVoucherField.setEditable(false);
        totalVoucherField.setBorder(null);
        totalVoucherField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        totalVoucherField.setText("0");
        totalVoucherField.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N

        jLabel32.setText("Amount");

        jButton4.setBackground(new java.awt.Color(72, 158, 231));
        jButton4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("Add Payment");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 153, 255));
        jLabel5.setText("Status");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 401, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 402, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel31)
                        .addComponent(totalVoucherField)
                        .addGroup(jPanel8Layout.createSequentialGroup()
                            .addComponent(voucherField, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(36, 36, 36)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(74, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel31)
                .addGap(9, 9, 9)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(voucherField, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel32)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(totalVoucherField, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(82, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Gift Card", jPanel8);

        jLabel27.setText("Card Information");

        jFormattedTextField3.setBorder(null);
        jFormattedTextField3.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jFormattedTextField3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N

        jLabel29.setText("Amount");

        jButton33.setBackground(new java.awt.Color(72, 158, 231));
        jButton33.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton33.setForeground(new java.awt.Color(255, 255, 255));
        jButton33.setText("Add Payment");
        jButton33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton33ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap(48, Short.MAX_VALUE)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel27)
                    .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jButton33, javax.swing.GroupLayout.PREFERRED_SIZE, 401, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jFormattedTextField3, javax.swing.GroupLayout.DEFAULT_SIZE, 402, Short.MAX_VALUE)
                        .addComponent(jSeparator4))
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 401, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(47, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(jLabel29)
                .addGap(9, 9, 9)
                .addComponent(jFormattedTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel27)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton33, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Credit / Debit Card", jPanel10);

        jLabel34.setText("Loyality Points Balance");

        jFormattedTextField7.setEditable(false);
        jFormattedTextField7.setBorder(null);
        jFormattedTextField7.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        jFormattedTextField7.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N

        jFormattedTextField8.setBorder(null);
        jFormattedTextField8.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jFormattedTextField8.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N

        jLabel35.setText("Amount");

        jButton6.setBackground(new java.awt.Color(72, 158, 231));
        jButton6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton6.setForeground(new java.awt.Color(255, 255, 255));
        jButton6.setText("Add Payment");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 401, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 402, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jFormattedTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 402, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel34)
                        .addComponent(jFormattedTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 402, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, 402, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel35)))
                .addContainerGap(77, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel34)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jFormattedTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel35)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jFormattedTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(84, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Loyality Points", jPanel12);

        discountField.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel36.setText("Special Discount : ");

        jButton3.setBackground(new java.awt.Color(204, 255, 204));
        jButton3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jButton3.setForeground(new java.awt.Color(0, 153, 51));
        jButton3.setText(" Complete Sale");
        jButton3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(111, 214, 75), 3));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jSeparator9.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel37.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(102, 102, 102));
        jLabel37.setText("Cash");

        cashField.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        cashField.setForeground(new java.awt.Color(51, 153, 255));
        cashField.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        cashField.setText("0");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(63, 63, 63)
                .addComponent(jLabel37)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                .addContainerGap(26, Short.MAX_VALUE)
                .addComponent(cashField, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel37)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cashField)
                .addContainerGap(31, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane1)
                    .addComponent(jSeparator2)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator3)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jLabel36)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(discountField, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(34, 34, 34)
                                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jSeparator9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel26))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 432, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(discountField, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jSeparator9, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel14, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel26)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                .addGap(26, 26, 26))
        );

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel12.setText("Product ID");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel13.setText("PRODUCT ID HERE");

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel14.setText("Name");

        jLabel15.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel15.setText("PRODUCT NAME HERE");

        jLabel20.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel20.setText("Category");

        jLabel21.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel21.setText("CATEGORY NAME HERE");

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel16.setText("MFD");

        jLabel17.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel17.setText("PRODUCT MFD HERE");

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel18.setText("EXP");

        jLabel19.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel19.setText("PRODUCT EXP HERE");

        jLabel25.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel25.setText("Discounts");

        jLabel30.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel30.setText("PRODUCT DISCOUNT");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel20)
                    .addComponent(jLabel12)
                    .addComponent(jLabel14))
                .addGap(18, 18, 18)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(10, 10, 10)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel16))
                        .addGap(49, 49, 49)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel17)))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel25)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel30)))
                .addGap(16, 16, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addGap(10, 10, 10))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel17, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel20)
                                .addComponent(jLabel21)
                                .addComponent(jLabel16)))
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel12)
                                    .addComponent(jLabel13)))
                            .addGroup(jPanel9Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel19)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)))
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel25)
                        .addComponent(jLabel30))
                    .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel14)
                        .addComponent(jLabel15)))
                .addContainerGap(10, Short.MAX_VALUE))
        );

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setText("Customer Details ");

        jTextField1.setEditable(false);
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setText("Points : ");

        jTextField3.setEditable(false);
        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });

        jButton1.setBackground(new java.awt.Color(72, 158, 231));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Select");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        invoiceNumberField.setEditable(false);
        invoiceNumberField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                invoiceNumberFieldActionPerformed(evt);
            }
        });

        jLabel39.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel39.setText("Invoice No :");

        jTextField2.setEditable(false);

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setText("Tel No:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(invoiceNumberField, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap(10, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(invoiceNumberField, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 6, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4)
                        .addComponent(jLabel7))
                    .addComponent(jTextField2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel9.setText("Products");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Stock_ID", "Category", "Name", "Quantity", "Price", "Discount", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(jTable1);

        jButton8.setBackground(new java.awt.Color(51, 204, 0));
        jButton8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton8.setForeground(new java.awt.Color(255, 255, 255));
        jButton8.setText("Add");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton9.setBackground(new java.awt.Color(51, 102, 255));
        jButton9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton9.setForeground(new java.awt.Color(255, 255, 255));
        jButton9.setText("Refresh");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton10.setBackground(new java.awt.Color(255, 0, 51));
        jButton10.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton10.setForeground(new java.awt.Color(255, 255, 255));
        jButton10.setText("Clear item");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addContainerGap(30, Short.MAX_VALUE)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(281, 281, 281)
                        .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(82, 82, 82)
                        .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 865, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(jLabel9))
                    .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jLabel40.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel40.setText("Stock ID : ");

        jLabel8.setText("QTY");
        jLabel8.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(31, 31, 31)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel23)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jFormattedTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel10)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel40)
                                .addGap(18, 18, 18)
                                .addComponent(stock_id, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addComponent(jButton2)
                        .addGap(37, 37, 37)
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(51, 51, 51)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(15, 15, 15))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(11, 11, 11)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(stock_id, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel40))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jFormattedTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel10)
                                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jFormattedTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel23)))
                                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(7, 7, 7))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)))
                        .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(93, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3ActionPerformed

    private void jFormattedTextField1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextField1KeyReleased
        // Qty Key Released and Enter Press *****
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
//            loadInvoiceItems();
            dataLoadToTable();
        }
    }//GEN-LAST:event_jFormattedTextField1KeyReleased

    private void stock_idActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stock_idActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_stock_idActionPerformed

    private void homeIconMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_homeIconMouseClicked
        this.dispose();
    }//GEN-LAST:event_homeIconMouseClicked

    private void invoiceNumberFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_invoiceNumberFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_invoiceNumberFieldActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // Customer panel open
        if (paymentMade) {
            JOptionPane.showMessageDialog(this,
                    "Once a payment is made you cannot change the Customer",
                    "Customer Change Not Allowed",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        System.out.println("Button Clicked");
        Customer customerPanel = new Customer();
        customerPanel.setInvoice(this);
        JDialog frame = new JDialog(this, true);
        frame.add(customerPanel);
        frame.setSize(1030, 766);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // If no customer is selected (e.g., dialog closed without selection), reset to unknown
        if (customerMobile == null || customerMobile.isEmpty()) {
            setCustomerMobile("0000000000");
            setCustomerName("Unknown Customer");
            setAvailablePoints("0");
        }

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // products open button
        System.out.println("Button Clicked");
        InventoryManagement productPanel = new InventoryManagement();
        productPanel.setInvoice(this);
        JDialog frame = new JDialog(this, true);
        frame.add(productPanel);
        frame.setSize(1030, 766);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // Print Invoice button
        try {
            String invoiceID = invoiceNumberField.getText();
            String customerMobile = jTextField3.getText();
            String employeeEmail = loggedInEmail;
            String dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            String paidAmount = totalField.getText();
            String recievedAmount = cashField.getText();

            // Calculate final total discount (item discounts + invoice discount)
            double finalTotalDiscount = totalItemDiscounts + invoiceDiscount;
            System.out.println("Total Item Discounts: " + totalItemDiscounts);
            System.out.println("Invoice Discount: " + invoiceDiscount);
            System.out.println("Final Total Discount: " + finalTotalDiscount);

            // Insert to Invoice with reference to invoice_discount
            String invoiceQuery = "INSERT INTO `invoice` (`id`, `customer_mobile`, `date`, `user_email`, `paid_amount`, `discount`, `invoice_discount_id`, `payment_status`) "
                    + "VALUES ('" + invoiceID + "','" + customerMobile + "', '" + dateTime + "', '" + employeeEmail + "', '" + paidAmount + "', '" + finalTotalDiscount + "', "
                    + (selectedDiscountId != null ? "'" + selectedDiscountId + "'" : "NULL") + ", 'Success')";
            DatabaseConnection.executeIUD(invoiceQuery);

            // Insert into Invoice Items
            for (InvoiceItem invoiceItem : invoiceItemMap.values()) {
                DatabaseConnection.executeIUD("INSERT INTO `invoice_item` (`stock_id`, `qty`, `invoice_id`) "
                        + "VALUES ('" + invoiceItem.getStockID() + "', '" + invoiceItem.getQty() + "', '" + invoiceID + "')");

                // Stock Update
                DatabaseConnection.executeIUD("UPDATE `stock` SET `qty` = `qty` - '" + invoiceItem.getQty() + "' WHERE `id` = '" + invoiceItem.getStockID() + "'");
            }

            // LexFury Point System - Update points
            if (withdrawPoints) {
                DatabaseConnection.executeIUD("UPDATE `customer` SET `point` = '" + newPoints + "' WHERE `mobile` = '" + customerMobile + "'");
            }

            // Add the points from balance conversion (only if customer agreed)
            if (pointsToAdd > 0) {
                DatabaseConnection.executeIUD("UPDATE `customer` SET `point` = `point` + '" + pointsToAdd + "' WHERE `mobile` = '" + customerMobile + "'");
            }

            // Calculate and update loyalty points (1% of paid amount)
            double percentage = 1.0;
            double updatedPoints = Double.parseDouble(totalField.getText()) * (percentage / 100);
            double roundedPoints = Math.round(updatedPoints);

            DatabaseConnection.executeIUD("UPDATE `customer` SET `point` = `point` + '" + roundedPoints + "' WHERE `mobile` = '" + customerMobile + "'");

            // Save the payment methods to the database
            savePaymentsToDatabase(invoiceID);

            // Fetch shop details from editable_receipt table
            ResultSet receiptDetails = DatabaseConnection.executeSearch("SELECT shop_name, thank_note, Address, mobile, email FROM editable_receipt WHERE id = 1");
            String shopName = "";
            String thankNote = "";
            String address = "";
            String shopMobile = "";
            String shopEmail = "";

            if (receiptDetails.next()) {
                shopName = receiptDetails.getString("shop_name");
                thankNote = receiptDetails.getString("thank_note");
                address = receiptDetails.getString("Address");
                shopMobile = receiptDetails.getString("mobile");
                shopEmail = receiptDetails.getString("email");
            }

            // Jasper Report
            InputStream s = this.getClass().getResourceAsStream("/com/lexso/sales/reports/InvoiceReport.jasper");
            if (s == null) {
                throw new FileNotFoundException("Jasper report file not found at /com/lexso/sales/reports/InvoiceReport.jasper");
            }

            HashMap<String, Object> params = new HashMap<>();
            params.put("Parameter1", shopName);
            params.put("Parameter2", thankNote);
            params.put("Parameter3", String.valueOf(invoiceID));
            params.put("Parameter4", String.valueOf(employeeEmail));
//            params.put("Parameter5", String.valueOf(customerMobile));
            params.put("Parameter6", String.valueOf(dateTime));
            params.put("Parameter7", String.valueOf(finalTotalDiscount));
            params.put("Parameter8", String.valueOf(paidAmount));
            params.put("Parameter9", String.valueOf(recievedAmount));
            params.put("Parameter10", String.valueOf(balanceField.getText()));
            params.put("Parameter11", address);
            params.put("Parameter12", shopMobile);
            params.put("Parameter13", shopEmail);

            JRTableModelDataSource dataSource = new JRTableModelDataSource(jTable1.getModel());
            JasperPrint jasperPrint = JasperFillManager.fillReport(s, params, dataSource);
            JasperViewer.viewReport(jasperPrint, false);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error generating invoice: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        completeSale();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // Add To Invoice BTn   ******
        dataLoadToTable();
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // cash add payment
        addCashPayment();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // voucher add payment
        // First check if status is Active
        if (jLabel5.getText().contains("Active")) {
            addGiftCardPayment();
        } else {
            JOptionPane.showMessageDialog(this, "Cannot add payment - gift card is not Valid");
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton33ActionPerformed
        // add card payment
        addCardPayment();
    }//GEN-LAST:event_jButton33ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // add loyalty points payment
        withdrawPoints = true;

        addLoyaltyPointsPayment();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void voucherFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_voucherFieldKeyReleased
        // voucher code no.
        String giftCardNumber = voucherField.getText().trim();

        // Clear fields if empty
        if (giftCardNumber.isEmpty()) {
            jLabel5.setText("Status");
            totalVoucherField.setText("");
            return;
        }

        try {
            // Query the database for gift card details
            ResultSet rs = DatabaseConnection.executeSearch("SELECT amount, status FROM `gift_cards` WHERE `card_code` = '" + giftCardNumber + "'");

            if (rs.next()) {
                String status = rs.getString("status");
                double giftCardAmount = rs.getDouble("amount");

                // Always update status label
                jLabel5.setText(status);

                // Only load amount if status is Active
                if (status.equalsIgnoreCase("Active")) {
                    totalVoucherField.setText(String.valueOf(giftCardAmount));
                } else {
                    totalVoucherField.setText("");
                    JOptionPane.showMessageDialog(this, "Gift card is not active. Status: " + status);
                }
            } else {
                jLabel5.setText("Not found");
                totalVoucherField.setText("");
//                JOptionPane.showMessageDialog(this, "Gift card not found.");
            }
        } catch (Exception ex) {
            jLabel5.setText("Error");
            totalVoucherField.setText("");
            JOptionPane.showMessageDialog(this, "Error searching gift card: " + ex.getMessage());
        }
    }//GEN-LAST:event_voucherFieldKeyReleased

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // clear item
        if (paymentMade) {
            JOptionPane.showMessageDialog(this, "Please generate a new Invoice", "Payment Already Made", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int selectedRow = jTable1.getSelectedRow();

        if (selectedRow >= 0) {
            String key = String.valueOf(jTable1.getValueAt(selectedRow, 0));
            invoiceItemMap.remove(key);
            ((DefaultTableModel) jTable1.getModel()).removeRow(selectedRow);

            // Recalculate totals and discounts after removal
            loadInvoiceItems();

            JOptionPane.showMessageDialog(this, "Item removed successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Please select an item to remove", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        //  refresh btn
        resetFields();
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        FlatMacLightLaf.setup();

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel balanceField;
    private javax.swing.JLabel cashField;
    private javax.swing.JComboBox<String> discountField;
    private javax.swing.JLabel employeLabel;
    private javax.swing.JLabel homeIcon;
    private javax.swing.JTextField invoiceNumberField;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton33;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JFormattedTextField jFormattedTextField1;
    private javax.swing.JFormattedTextField jFormattedTextField2;
    private javax.swing.JFormattedTextField jFormattedTextField3;
    private javax.swing.JFormattedTextField jFormattedTextField7;
    private javax.swing.JFormattedTextField jFormattedTextField8;
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
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel85;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JFormattedTextField paymentField1;
    private javax.swing.JTextField stock_id;
    private javax.swing.JLabel totalField;
    private javax.swing.JFormattedTextField totalVoucherField;
    private javax.swing.JFormattedTextField voucherField;
    // End of variables declaration//GEN-END:variables
}
