package com.lexso.login.service;

import com.lexso.connection.DatabaseConnection;
import com.lexso.login.model.ModelLogin;
import com.lexso.login.model.ModelUser;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Random;

public class ServiceUser {

    private final Connection con;

    public ServiceUser() {
        con = DatabaseConnection.getInstance().getConnection();
    }

    public ModelUser login(ModelLogin login) throws SQLException {
        ModelUser data = null;
        String query = "SELECT `code`, `username`, `email` FROM `user` WHERE BINARY(`email`)=? AND BINARY(`password`)=? AND `status`='Active' LIMIT 1";
        
        try (PreparedStatement p = con.prepareStatement(query)) {
            p.setString(1, login.getEmail());
            p.setString(2, login.getPassword()); // Consider hashing the password

            try (ResultSet r = p.executeQuery()) {
                if (r.next()) {
                    int userID = r.getInt(1);
                    String userName = r.getString(2);
                    String email = r.getString(3);
                    data = new ModelUser(userID, userName, email, "");
                }
            }
        }
        return data;
    }

    public void insertUser(ModelUser user) throws SQLException {
        String query = "INSERT INTO `user` (userName, email, `password`, VerifyCode) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement p = con.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            String code = generateVerifyCode();
            p.setString(1, user.getUserName());
            p.setString(2, user.getEmail());
            p.setString(3, user.getPassword()); // Consider hashing the password
            p.setString(4, code);
            p.executeUpdate();

            try (ResultSet r = p.getGeneratedKeys()) {
                if (r.next()) {
                    int userID = r.getInt(1);
                    user.setUserID(userID);
                    user.setVerifyCode(code);
                }
            }
        }
    }

    private String generateVerifyCode() throws SQLException {
        DecimalFormat df = new DecimalFormat("000000");
        Random ran = new Random();
        String code;

        do {
            code = df.format(ran.nextInt(1000000));
        } while (checkDuplicateCode(code));

        return code;
    }

    private boolean checkDuplicateCode(String code) throws SQLException {
        String query = "SELECT `UserID` FROM `user` WHERE `VerifyCode`=? LIMIT 1";
        try (PreparedStatement p = con.prepareStatement(query)) {
            p.setString(1, code);
            try (ResultSet r = p.executeQuery()) {
                return r.next();
            }
        }
    }

    public boolean checkDuplicateUser(String user) throws SQLException {
        String query = "SELECT UserID FROM `user` WHERE UserName=? AND `Status`='Verified' LIMIT 1";
        try (PreparedStatement p = con.prepareStatement(query)) {
            p.setString(1, user);
            try (ResultSet r = p.executeQuery()) {
                return r.next();
            }
        }
    }

    public boolean checkDuplicateEmail(String email) throws SQLException {
        String query = "SELECT UserID FROM `user` WHERE Email=? AND `Status`='Verified' LIMIT 1";
        try (PreparedStatement p = con.prepareStatement(query)) {
            p.setString(1, email);
            try (ResultSet r = p.executeQuery()) {
                return r.next();
            }
        }
    }

    public void doneVerify(int userID) throws SQLException {
        String query = "UPDATE `user` SET VerifyCode='', `Status`='Verified' WHERE UserID=? LIMIT 1";
        try (PreparedStatement p = con.prepareStatement(query)) {
            p.setInt(1, userID);
            p.executeUpdate();
        }
    }

    public boolean verifyCodeWithUser(int userID, String code) throws SQLException {
        String query = "SELECT UserID FROM `user` WHERE UserID=? AND VerifyCode=? LIMIT 1";
        try (PreparedStatement p = con.prepareStatement(query)) {
            p.setInt(1, userID);
            p.setString(2, code);
            try (ResultSet r = p.executeQuery()) {
                return r.next();
            }
        }
    }
}