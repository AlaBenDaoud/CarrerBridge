package org.example.auth.services;

import org.example.auth.models.User;
import org.example.auth.utils.DatabaseService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserService {

    private DatabaseService databaseService;

    public UserService() {
        this.databaseService = new DatabaseService();
    }

    // Register a new user in the database
    public boolean registerUser(User user) {
        String checkEmailQuery = "SELECT * FROM users WHERE email = ?";
        String insertUserQuery = "INSERT INTO users (name, email, password, cv) VALUES (?, ?, ?, ?)";

        try (Connection connection = databaseService.getConnection()) {
            // Check if email already exists
            PreparedStatement checkEmailStmt = connection.prepareStatement(checkEmailQuery);
            checkEmailStmt.setString(1, user.getEmail());
            ResultSet resultSet = checkEmailStmt.executeQuery();

            if (resultSet.next()) {
                return false; // Email already exists
            }

            // Insert new user into the database
            PreparedStatement insertUserStmt = connection.prepareStatement(insertUserQuery);
            insertUserStmt.setString(1, user.getName());
            insertUserStmt.setString(2, user.getEmail());
            insertUserStmt.setString(3, user.getPassword());
            insertUserStmt.setString(4, user.getCv());
            insertUserStmt.executeUpdate();

            return true; // User registered successfully
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Registration failed
        }
    }

    // Retrieve a user by ID
    public User getUserById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";

        try (Connection connection = databaseService.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    // Map the row to a User object
                    return new User(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("email"),
                            resultSet.getString("password"),
                            resultSet.getString("cv")
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving user by ID: " + e.getMessage());
        }

        return null; // Return null if no user is found
    }

    // Retrieve a user by email
    public User getUserByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";

        try (Connection connection = databaseService.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    // Map the row to a User object
                    return new User(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("email"),
                            resultSet.getString("password"),
                            resultSet.getString("cv")
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving user by email: " + e.getMessage());
        }

        return null; // Return null if no user is found
    }

    // Method to retrieve all users
    public List<User> getAllUsers() {
        String sql = "SELECT * FROM users";
        List<User> users = new ArrayList<>();

        try (Connection connection = databaseService.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                User user = new User(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        resultSet.getString("cv")
                );
                users.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users; // Return the list of users
    }
}
