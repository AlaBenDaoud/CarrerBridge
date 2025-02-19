package org.example.auth.services;

import org.example.auth.models.Admin;
import org.example.auth.utils.DatabaseService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminService {

    private DatabaseService databaseService;

    public AdminService() {
        this.databaseService = new DatabaseService();
    }

    // Retrieve an admin by email
    public Admin getAdminByEmail(String email) {
        String sql = "SELECT * FROM admin WHERE email = ?";

        try (Connection connection = databaseService.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    // Map the row to an Admin object
                    Admin admin = new Admin();
                    admin.setId(resultSet.getInt("id"));
                    admin.setName(resultSet.getString("name"));
                    admin.setEmail(resultSet.getString("email"));
                    admin.setPassword(resultSet.getString("password"));
                    return admin;
                }
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving admin by email: " + e.getMessage());
        }

        return null; // Return null if no admin is found
    }
}
