package org.example.demo.service;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseService {
    private static final String URL = "jdbc:mysql://localhost:3306/conge_absence_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    // Get a connection to the database
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}