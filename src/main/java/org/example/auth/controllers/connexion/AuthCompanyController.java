package org.example.auth.controllers.connexion;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.auth.models.Company;
import org.example.auth.services.CompanyService;

import java.io.IOException;

public class AuthCompanyController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    private CompanyService companyService = new CompanyService();

    // Private static variable to store the logged-in company's ID (token)
    private static int loggedInCompanyId = -1;

    // Public static method to get the logged-in company ID
    public static int getLoggedInCompanyId() {
        return loggedInCompanyId;
    }

    // Public static method to set the logged-in company ID
    public static void setLoggedInCompanyId(int companyId) {
        loggedInCompanyId = companyId;
    }

    // Method to handle login button action
    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        // Retrieve the company by email
        Company company = companyService.getCompanyByEmail(email);

        if (company != null && company.getPassword().equals(password)) {
            // Store the logged-in company's ID
            setLoggedInCompanyId(company.getId());

            // Show success message
            showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome, " + company.getCompanyName() + "!");

            // Print logged-in company ID in the terminal
            System.out.println("Logged-in Company ID: " + getLoggedInCompanyId());

            // Navigate to the company dashboard
            navigateToCompanyDash();
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

    // Method to navigate to the company dashboard
    private void navigateToCompanyDash() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/auth/connexionview/companydash.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Unable to load the company dashboard.");
        }
    }
}
