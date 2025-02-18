package com.example.testfx;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthService {

    public static boolean authenticateUser(String email, String password) {
        String query = "SELECT password FROM admin WHERE email = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, email);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                String storedPassword = resultSet.getString("password");
                return storedPassword.equals(password); // Comparaison des mots de passe
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


}
