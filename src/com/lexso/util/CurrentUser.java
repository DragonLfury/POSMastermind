// File: com/lexso/util/CurrentUser.java
package com.lexso.util;


public class CurrentUser {

    private static String username;
    private static String email;
    private static String role;
    private static String profilePic;
    
    private CurrentUser() {
        
    }

    public static void setLoggedInUser(String username, String email,String role, String profilePic) {
        CurrentUser.username = username;
        CurrentUser.email = email;
        CurrentUser.role = role;
        CurrentUser.profilePic = profilePic;
        // Set other details if you add them
    }
    
    public static void clearCurrentUser() {
        CurrentUser.username = null;
        CurrentUser.email = null;
        CurrentUser.role = null;
        CurrentUser.profilePic = null;
    }

    public static String getUsername() {
        return username;
    }
    
    public static String getEmail() {
        return email;
    }
    
    public static String getRole() {
        return role;
    }
    
    public static String getProfilePic() {
        return profilePic;
    }

    public static boolean isLoggedIn() {
        return username != null;
    }
}