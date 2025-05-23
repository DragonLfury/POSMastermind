package com.lexso.dashboard.component;

import com.lexso.dashboard.event.EventMenu;
import com.lexso.dashboard.event.EventMenuSelected;
import com.lexso.dashboard.event.EventShowPopupMenu;
import com.lexso.dashboard.model.ModelMenu;
import com.lexso.dashboard.swing.MenuAnimation;
import com.lexso.dashboard.swing.MenuItem;
import com.lexso.dashboard.swing.scrollbar.ScrollBarCustom;
import com.lexso.util.CurrentUser;
import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;
import net.miginfocom.swing.MigLayout;

public class Menu extends javax.swing.JPanel {

    public boolean isShowMenu() {
        return showMenu;
    }

    public void addEvent(EventMenuSelected event) {
        this.event = event;
    }

    public void setEnableMenu(boolean enableMenu) {
        this.enableMenu = enableMenu;
    }

    public void setShowMenu(boolean showMenu) {
        this.showMenu = showMenu;
    }

    public void addEventShowPopup(EventShowPopupMenu eventShowPopup) {
        this.eventShowPopup = eventShowPopup;
    }

    private final MigLayout layout;
    private EventMenuSelected event;
    private EventShowPopupMenu eventShowPopup;
    private boolean enableMenu = true;
    private boolean showMenu = true;
    private String currentUserRole;

    public Menu() {
        initComponents();
        setOpaque(false);
        sp.getViewport().setOpaque(false);
        sp.setVerticalScrollBar(new ScrollBarCustom());
        layout = new MigLayout("wrap, fillx, insets 0", "[fill]", "[]0[]");
        panel.setLayout(layout);
    }

    public void initMenuItem() {
        // Get current user role (you'll need to pass this to the Menu class)
        String userRole = CurrentUser.getRole(); // Or get it from constructor

        // Dashboard - visible to all roles
        addMenu(new ModelMenu(new ImageIcon(getClass().getResource("/com/lexso/dashboard/icon/1.png")),
                "Dashboard",
                new String[]{"Administrator", "Manager", "Cashier", "Stock Manager", "Accountant"}, // All roles
                null, // No submenu restrictions
                "Dashboard")); // Single submenu item

        // User Management - Admin only with some restricted submenus
        Map<String, Boolean> userMgmtPerms = new HashMap<>();
        userMgmtPerms.put("User Registration", false); // Disabled by default
        userMgmtPerms.put("Audit Logs", false); // Disabled by default
        addMenu(new ModelMenu(new ImageIcon(getClass().getResource("/com/lexso/dashboard/icon/4.png")),
                "User Management",
                new String[]{"Administrator"}, // Only Admin
                userMgmtPerms,
                "Login/Logout", "User Registration", "User Profile", "User List & Roles", "Change Password", "Audit Logs"));

        // Bank Management - Admin and Accountant
        addMenu(new ModelMenu(new ImageIcon(getClass().getResource("/com/lexso/dashboard/icon/8.png")),
                "Bank Management",
                new String[]{"Administrator", "Accountant"},
                null, // All submenus enabled for these roles
                "Banks & Branches", "Staff Bank Details"));

        // Product & Inventory - Admin, Manager, Stock Manager
        addMenu(new ModelMenu(new ImageIcon(getClass().getResource("/com/lexso/dashboard/icon/2.png")),
                "Product & Inventory",
                new String[]{"Administrator", "Manager", "Stock Manager"},
                null,
                "Add New Product", "Product List", "Categories & Brands",
                "Brand to Category", "Stock Adjustment", "Supplier Management"));

        // Sales & Billing - Admin, Manager, Cashier
        Map<String, Boolean> salesPerms = new HashMap<>();
        salesPerms.put("Discounts & Offers", false); // Only Manager+ can configure discounts
        salesPerms.put("Returns & Refunds", false); // Only Manager+ can process returns
        addMenu(new ModelMenu(new ImageIcon(getClass().getResource("/com/lexso/dashboard/icon/7.png")),
                "Sales & Billing",
                new String[]{"Administrator", "Manager", "Cashier"},
                salesPerms,
                "Point of Sale (POS)", "Discounts & Offers", "Invoice Generator", "Returns & Refunds", "Customer Management"));

        // Purchasing & Receiving - Admin, Manager, Stock Manager
        addMenu(new ModelMenu(new ImageIcon(getClass().getResource("/com/lexso/dashboard/icon/6.png")),
                "Purchasing & Receiving",
                new String[]{"Administrator", "Manager", "Stock Manager"},
                null,
                "GRN", "GRN Records"));

        // Reports & Analytics - Admin, Manager, Accountant
        Map<String, Boolean> reportsPerms = new HashMap<>();
        reportsPerms.put("User-wise Sales Report", false); // Only Admin
        reportsPerms.put("Product Movement Report", false); // Only Admin/Manager
        addMenu(new ModelMenu(new ImageIcon(getClass().getResource("/com/lexso/dashboard/icon/5.png")),
                "Reports & Analytics",
                new String[]{"Administrator", "Manager", "Accountant"},
                reportsPerms,
                "Daily Sales Report", "Monthly Sales Report", "Inventory Report", "User-wise Sales Report",
                "Shift-wise Sales Report", "Product Movement Report"));

        // Attendance & Shifts - Admin, Manager
        addMenu(new ModelMenu(new ImageIcon(getClass().getResource("/com/lexso/dashboard/icon/9.png")),
                "Attendance & Shifts",
                new String[]{"Administrator", "Manager"},
                null,
                "Check-in/Check-out", "Shift Schedule", "Attendance List", "Leave Management"));

        // Finance & Salary - Admin, Accountant
        addMenu(new ModelMenu(new ImageIcon(getClass().getResource("/com/lexso/dashboard/icon/6.png")),
                "Finance & Salary",
                new String[]{"Administrator", "Accountant"},
                null,
                "Salary Management", "Backup Logs Viewer"));

        // Geographic Data - Admin only
        addMenu(new ModelMenu(new ImageIcon(getClass().getResource("/com/lexso/dashboard/icon/10.png")),
                "Geographic Data",
                new String[]{"Administrator"},
                null,
                "Country Management", "Province Management", "District Management", "City Management"));

        // System Settings - Admin only
        addMenu(new ModelMenu(new ImageIcon(getClass().getResource("/com/lexso/dashboard/icon/12.png")),
                "System Settings",
                new String[]{"Administrator"},
                null,
                "Editable Receipt", "Database Backup", "Feedbacks", "Contact Developers"));
    }

    private void addMenu(ModelMenu menu) {
        panel.add(new MenuItem(menu, getEventMenu(), event, panel.getComponentCount()), "h 40!");
    }

    private EventMenu getEventMenu() {
        return new EventMenu() {
            @Override
            public boolean menuPressed(Component com, boolean open) {
                if (enableMenu) {
                    if (isShowMenu()) {
                        if (open) {
                            new MenuAnimation(layout, com).openMenu();
                        } else {
                            new MenuAnimation(layout, com).closeMenu();
                        }
                        return true;
                    } else {
                        eventShowPopup.showPopup(com);
                    }
                }
                return false;
            }
        };
    }

    public void hideallMenu() {
        for (Component com : panel.getComponents()) {
            MenuItem item = (MenuItem) com;
            if (item.isOpen()) {
                new MenuAnimation(layout, com, 500).closeMenu();
                item.setOpen(false);
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        sp = new javax.swing.JScrollPane();
        panel = new javax.swing.JPanel();
        profile1 = new com.lexso.dashboard.component.Profile();

        sp.setBorder(null);
        sp.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        sp.setViewportBorder(null);

        panel.setOpaque(false);

        javax.swing.GroupLayout panelLayout = new javax.swing.GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 312, Short.MAX_VALUE)
        );
        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 523, Short.MAX_VALUE)
        );

        sp.setViewportView(panel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(sp, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
            .addComponent(profile1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(profile1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sp, javax.swing.GroupLayout.DEFAULT_SIZE, 523, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    @Override
    protected void paintComponent(Graphics grphcs) {
        Graphics2D g2 = (Graphics2D) grphcs;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color solidColor = new Color(55, 57, 66); // #373942
        g2.setPaint(solidColor);
        g2.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(grphcs);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel panel;
    private com.lexso.dashboard.component.Profile profile1;
    private javax.swing.JScrollPane sp;
    // End of variables declaration//GEN-END:variables
}
