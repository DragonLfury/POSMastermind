package com.lexso.connection;

import java.sql.*;

public class DatabaseConnection {
    
    private static Connection connection;
    
    public static void createConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String server = "localhost";
        String port = "3306";
        String database = "u821149722_lexso";
        String userName = "root";
        String password = "password";
        connection = DriverManager.getConnection("jdbc:mysql://" + server + ":" + port + "/" + database, userName, password);
    }
    
    public static ResultSet executeSearch(String query) throws Exception {
        createConnection();
        return connection.createStatement().executeQuery(query);
    }
    
    public static Integer executeIUD(String query) throws Exception {
        createConnection();
        return connection.createStatement().executeUpdate(query);
    }
    
}
