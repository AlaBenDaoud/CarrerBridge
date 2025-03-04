package org.example.auth.controllers.connexion;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.auth.models.User;
import org.example.auth.services.TranslationService;
import org.example.auth.services.UserService;

import java.io.IOException;

public class AuthUserController {

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

    private UserService userService = new UserService();

    // Static variable to store the logged-in user's ID (token)
    private static int loggedInUserId = -1;

    // Method to get the logged-in user's ID
    public static int getLoggedInUserId() {
        return loggedInUserId;
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
            loggedInUserId = user.getId();

            // Show success message
            showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome, " + user.getName() + "!");

            // Print logged-in user ID in the terminal
            System.out.println("Logged-in User ID: " + loggedInUserId);

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