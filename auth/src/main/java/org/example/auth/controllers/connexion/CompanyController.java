package org.example.auth.controllers.connexion;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import org.example.auth.models.Company;
import org.example.auth.services.CompanyService;
import org.example.auth.services.TranslationService;

public class CompanyController {

    private final CompanyService companyService;

    @FXML
    private TextField companyNameField;
    @FXML
    private TextField locationField;
    @FXML
    private TextField secteurField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private ComboBox<String> languageComboBox;

    @FXML
    private Label registrationTitle;
    @FXML
    private Button registerButton;

    private TranslationService translationService;

    public CompanyController() {
        this.companyService = new CompanyService();  // Initialize the service
    }

    @FXML
    public void handleRegister(ActionEvent event) {
        // Retrieve data from input fields
        String companyName = companyNameField.getText();
        String location = locationField.getText();
        String secteur = secteurField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        // Create a new company object
        Company newCompany = new Company(companyName, location, secteur, email, password);

        // Try to register the company
        boolean isRegistered = companyService.registerCompany(newCompany);

        // Show a message based on whether the registration was successful
        if (isRegistered) {
            showAlert("Registration Successful", "Company registered successfully.", AlertType.INFORMATION);
        } else {
            showAlert("Registration Failed", "Email already exists.", AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
        registrationTitle.setText(translationService.getTranslation("company.registration.title", "Company Registration"));
        companyNameField.setPromptText(translationService.getTranslation("company.registration.companyName", "Enter Company Name"));
        locationField.setPromptText(translationService.getTranslation("company.registration.location", "Enter Location"));
        secteurField.setPromptText(translationService.getTranslation("company.registration.sector", "Enter Sector"));
        emailField.setPromptText(translationService.getTranslation("company.registration.email", "Enter Email"));
        passwordField.setPromptText(translationService.getTranslation("company.registration.password", "Enter Password"));
        registerButton.setText(translationService.getTranslation("company.registration.registerButton", "Register"));
    }
}