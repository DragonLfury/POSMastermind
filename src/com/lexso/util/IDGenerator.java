/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lexso.util;

import com.lexso.connection.DatabaseConnection;
import java.sql.ResultSet;

/**
 *
 * @author NGD NISSHANKA
 */
public class IDGenerator {
    
    public static String generateID(String tableName, String columnName, String prefix) {
        String newID = null;
        try {
            String lastIDQuery = "SELECT " + columnName + " FROM " + tableName
                    + " WHERE " + columnName + " LIKE '" + prefix + "%' ORDER BY " + columnName + " DESC LIMIT 1";
            ResultSet resultSet = DatabaseConnection.executeSearch(lastIDQuery);

            if (resultSet.next()) {
                String lastID = resultSet.getString(columnName);
                int lastIDNumber = Integer.parseInt(lastID.substring(prefix.length()));
                int newIDNumber = lastIDNumber + 1;
                newID = prefix + String.format("%05d", newIDNumber);
            } else {
                newID = prefix + "00001";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return newID;
    }
}
