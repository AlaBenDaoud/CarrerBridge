package org.example.auth.controllers.connexion;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.auth.models.Admin;
import org.example.auth.services.AdminService;

import java.io.IOException;

public class AdminLoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    private AdminService adminService = new AdminService();

    // Static variable to store the logged-in admin's ID (token)
    private static int loggedInAdminId = -1;

    // Method to get the logged-in admin's ID
    public static int getLoggedInAdminId() {
        return loggedInAdminId;
    }

    // Method to handle login button action
    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        // Retrieve the admin by email
        Admin admin = adminService.getAdminByEmail(email);

        if (admin != null && admin.getPassword().equals(password)) {
            // Store the logged-in admin's ID
            loggedInAdminId = admin.getId();

            // Show success message
            showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome, " + admin.getName() + "!");

            // Print logged-in admin ID in the terminal
            System.out.println("Logged-in Admin ID: " + loggedInAdminId);

            // After successful login, load the Admin Dashboard
            loadAdminDashboard();
        } else {
            // Show error message
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid email or password.");
        }
    }

    // Helper method to show alerts
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Method to load the Admin Dashboard
    private void loadAdminDashboard() {
        try {
            // Load the Admin Dashboard FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/auth/connexionview/AdminDashboard.fxml"));
            AnchorPane dashboard = loader.load();

            // Create a new scene with the dashboard layout
            Scene dashboardScene = new Scene(dashboard);

            // Get the current stage (window) and set the new scene
            Stage currentStage = (Stage) emailField.getScene().getWindow();
            currentStage.setScene(dashboardScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load Admin Dashboard.");
        }
    }
}
