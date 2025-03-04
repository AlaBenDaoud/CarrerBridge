package org.example.auth.controllers.connexion;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import org.example.auth.services.TranslationService;

public class CompanyDashController {
    @FXML
    private ComboBox<String> languageComboBox;

    @FXML
    private Button postOfferButton;

    @FXML
    private Button viewPostsButton;

    @FXML
    private Button viewApplicantsButton;

    @FXML
    private Button viewEmployeesButton;

    private TranslationService translationService;

    @FXML
    private void handlePostOffer() {
        showAlert(Alert.AlertType.INFORMATION, "Post an Offer", "Post an Offer button clicked.");
        // Add logic to handle posting an offer
    }

    @FXML
    private void handleViewPosts() {
        showAlert(Alert.AlertType.INFORMATION, "View My Posts", "View My Posts button clicked.");
        // Add logic to handle viewing posts
    }

    @FXML
    private void handleViewApplicants() {
        showAlert(Alert.AlertType.INFORMATION, "View Applicants", "View Applicants button clicked.");
        // Add logic to handle viewing applicants
    }

    @FXML
    private void handleViewEmployees() {
        showAlert(Alert.AlertType.INFORMATION, "View Employees", "View Employees button clicked.");
        // Add logic to handle viewing employees
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
        postOfferButton.setText(translationService.getTranslation("company.dashboard.postOffer", "Post an Offer"));
        viewPostsButton.setText(translationService.getTranslation("company.dashboard.viewPosts", "View My Posts"));
        viewApplicantsButton.setText(translationService.getTranslation("company.dashboard.viewApplicants", "View Applicants"));
        viewEmployeesButton.setText(translationService.getTranslation("company.dashboard.viewEmployees", "View Employees"));
    }
}