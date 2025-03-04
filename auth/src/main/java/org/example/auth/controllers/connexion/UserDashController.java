package org.example.auth.controllers.connexion;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import org.example.auth.services.TranslationService;
import org.w3c.dom.Text;

public class UserDashController {
    @FXML
    private ComboBox<String> languageComboBox;

    @FXML
    private Button searchJobButton;

    @FXML
    private Button viewApplicationsButton;

    @FXML
    private Button editProfileButton;
    @FXML
    private Label welcomeTitle;

    @FXML
    private Label welcomeSubtitle;

    private TranslationService translationService;

    @FXML
    private void handleSearchJob() {
        showAlert(Alert.AlertType.INFORMATION, "Search for Job", "Search for Job button clicked.");
        // Add logic to handle searching for jobs
    }

    @FXML
    private void handleViewApplications() {
        showAlert(Alert.AlertType.INFORMATION, "View My Applications", "View My Applications button clicked.");
        // Add logic to handle viewing applications
    }

    @FXML
    private void handleEditProfile() {
        showAlert(Alert.AlertType.INFORMATION, "Edit My Profile", "Edit My Profile button clicked.");
        // Add logic to handle editing the profile
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
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
        searchJobButton.setText(translationService.getTranslation("user.dashboard.searchJob", "Search for Job"));
        viewApplicationsButton.setText(translationService.getTranslation("user.dashboard.viewApplications", "View My Applications"));
        editProfileButton.setText(translationService.getTranslation("user.dashboard.editProfile", "Edit My Profile"));
        welcomeTitle.setText(translationService.getTranslation("user.dashboard.welcomeTitle", "Welcome to Your Dashboard"));
        welcomeSubtitle.setText(translationService.getTranslation("user.dashboard.welcomeSubtitle", "Please use the navigation bar above to explore your options."));
    }
}