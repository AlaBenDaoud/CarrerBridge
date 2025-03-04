package org.example.auth.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import org.example.auth.services.TranslationService;

import java.io.IOException;
import java.net.URL;

public class MainController {

    @FXML
    private ComboBox<String> languageComboBox;

    @FXML
    private Label welcomeTextSmall;

    @FXML
    private Label welcomeTextLarge;

    @FXML
    private Label welcomeDescription;

    @FXML
    private Label adminCardTitle;

    @FXML
    private Label adminCardDescription;

    @FXML
    private Label companyCardTitle;

    @FXML
    private Label companyCardDescription;

    @FXML
    private Label userCardTitle;

    @FXML
    private Label userCardDescription;

    @FXML
    private Button ctaButton;

    private TranslationService translationService;

    private Stage stage;
    private Scene scene;
    private Parent root;

    // Navigate to Admin Login
    @FXML
    private void handleAdminLogin(ActionEvent event) throws IOException {
        navigateToPage(event, "/org/example/auth/connexionview/AdminLogin.fxml");
    }



    // Navigate to Company Login
    @FXML
    private void handleCompanyLogin(ActionEvent event) throws IOException {
        navigateToPage(event, "/org/example/auth/connexionview/loginCompany.fxml");
    }

    // Navigate to Company Register
    @FXML
    private void handleCompanyRegister(ActionEvent event) throws IOException {
        navigateToPage(event, "/org/example/auth/connexionview/company_register.fxml");
    }

    // Navigate to User Login
    @FXML
    private void handleUserLogin(ActionEvent event) throws IOException {
        navigateToPage(event, "/org/example/auth/connexionview/UserLoginView.fxml");
    }

    // Navigate to User Register
    @FXML
    private void handleUserRegister(ActionEvent event) throws IOException {
        navigateToPage(event, "/org/example/auth/connexionview/register.fxml");
    }

    // Helper method to load the FXML file and navigate to a new page
    private void navigateToPage(ActionEvent event, String fxmlPath) throws IOException {
        URL fxmlUrl = getClass().getResource(fxmlPath);
        if (fxmlUrl == null) {
            System.out.println("FXML file not found at: " + fxmlPath);
            throw new IOException("FXML file not found: " + fxmlPath);
        }
        root = FXMLLoader.load(fxmlUrl);

        // Correctly retrieve the stage from the MenuItem
        MenuItem menuItem = (MenuItem) event.getSource();
        Scene scene = ((Stage) menuItem.getParentPopup().getOwnerWindow()).getScene();
        Stage stage = (Stage) scene.getWindow();

        // Set the new scene
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

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
        welcomeTextSmall.setText(translationService.getTranslation("welcome.small", "Welcome to")); // Valeur par défaut
        welcomeTextLarge.setText(translationService.getTranslation("welcome.large", "Business Management System")); // Valeur par défaut
        welcomeDescription.setText(translationService.getTranslation("welcome.description", "Streamline your business operations with our comprehensive solution")); // Valeur par défaut
        adminCardTitle.setText(translationService.getTranslation("admin.card.title", "For Administrators")); // Valeur par défaut
        adminCardDescription.setText(translationService.getTranslation("admin.card.description", "Manage system settings and user permissions")); // Valeur par défaut
        companyCardTitle.setText(translationService.getTranslation("company.card.title", "For Companies")); // Valeur par défaut
        companyCardDescription.setText(translationService.getTranslation("company.card.description", "Track resources and manage operations")); // Valeur par défaut
        userCardTitle.setText(translationService.getTranslation("user.card.title", "For Users")); // Valeur par défaut
        userCardDescription.setText(translationService.getTranslation("user.card.description", "Access services and manage personal accounts")); // Valeur par défaut
        ctaButton.setText(translationService.getTranslation("cta.button", "Get Started")); // Valeur par défaut
    }
}