package org.example.auth.controllers.connexion;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.auth.models.Company;
import org.example.auth.services.CompanyService;
import org.example.auth.services.TranslationService;

import java.io.IOException;

public class AuthCompanyController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;
    @FXML
    private ComboBox<String> languageComboBox;

    @FXML
    private Label emailLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private Button loginButton;

    private TranslationService translationService;

    private CompanyService companyService = new CompanyService();

    // Static variable to store the logged-in company's ID (token)
    private static int loggedInCompanyId = -1;

    // Method to get the logged-in company's ID
    public static int getLoggedInCompanyId() {
        return loggedInCompanyId;
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
            loggedInCompanyId = company.getId();

            // Show success message
            showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome, " + company.getCompanyName() + "!");

            // Print logged-in company ID in the terminal
            System.out.println("Logged-in Company ID: " + loggedInCompanyId);

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
    @FXML
    public void initialize() {
        // Initialiser la ComboBox avec les langues disponibles
        ObservableList<String> languages = FXCollections.observableArrayList("English", "Français");
        languageComboBox.setItems(languages);
        languageComboBox.setValue("English"); // Langue par défaut

        // Initialiser le service de traduction
        translationService = new TranslationService("en");

        // Mettre à jour l'interface avec la langue par défaut
        updateUI();

        // Gérer le changement de langue
        languageComboBox.setOnAction(event -> {
            String selectedLanguage = languageComboBox.getValue();
            String languageCode = selectedLanguage.equals("Français") ? "fr" : "en";
            translationService = new TranslationService(languageCode);
            updateUI();
        });
    }

    private void updateUI() {
        emailLabel.setText(translationService.getTranslation("login.email", "Email:")); // Valeur par défaut
        passwordLabel.setText(translationService.getTranslation("login.password", "Password:")); // Valeur par défaut
        loginButton.setText(translationService.getTranslation("login.button", "Login")); // Valeur par défaut
    }
}