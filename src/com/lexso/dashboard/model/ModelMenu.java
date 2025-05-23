package com.lexso.dashboard.model;

import java.util.Arrays;
import java.util.Map;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public class ModelMenu {

    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String[] getSubMenu() {
        return subMenu;
    }

    public void setSubMenu(String[] subMenu) {
        this.subMenu = subMenu;
    }

    public ModelMenu(ImageIcon icon, String menuName, String[] allowedRoles, Map<String, Boolean> subMenuPermissions, String... subMenu) {
        this.icon = icon;
        this.menuName = menuName;
        this.subMenu = subMenu;
        this.allowedRoles = allowedRoles;
        this.subMenuPermissions = subMenuPermissions;
    }

    public boolean isRoleAllowed(String role) {
        return Arrays.asList(allowedRoles).contains(role);
    }

    public boolean isSubMenuAllowed(String subMenuItem, String role) {
        // Check if this specific submenu item is allowed for the role
        return subMenuPermissions == null || 
               subMenuPermissions.getOrDefault(subMenuItem, true) && 
               isRoleAllowed(role);
    }
    
    public ModelMenu() {
    }

    private String menuName;
    private ImageIcon icon;
    private String[] subMenu;
    private String[] allowedRoles;
    private Map<String, Boolean> subMenuPermissions; 
}
