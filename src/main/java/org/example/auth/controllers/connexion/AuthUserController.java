package org.example.auth.controllers.connexion;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.auth.models.User;
import org.example.auth.services.UserService;

import java.io.IOException;

public class AuthUserController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    private UserService userService = new UserService();

    // Static variable to store the logged-in user's ID
    private static int loggedInUserId = -1;

    // Public static method to get the logged-in user's ID
    public static int getLoggedInUserId() {
        return loggedInUserId;
    }

    // Public static method to set the logged-in user's ID
    public static void setLoggedInUserId(int userId) {
        loggedInUserId = userId;
    }

    // Method to handle login button action
    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        // Retrieve the user by email
        User user = userService.getUserByEmail(email);

        if (user != null && user.getPassword().equals(password)) {
            // Store the logged-in user's ID
            setLoggedInUserId(user.getId());

            // Show success message
            showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome, " + user.getName() + "!");

            // Print logged-in user ID in the terminal
            System.out.println("Logged-in User ID: " + getLoggedInUserId());

            // Navigate to the user dashboard
            navigateToUserDash();
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

    // Method to navigate to the user dashboard
    private void navigateToUserDash() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/auth/connexionview/userdash.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Unable to load the user dashboard.");
        }
    }
}
