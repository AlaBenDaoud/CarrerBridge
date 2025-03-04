package org.example.auth.controllers.connexion;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.auth.services.AdminService;
import org.example.auth.services.TranslationService;

import java.io.IOException;

public class AdminDashboardController {
    @FXML
    private ComboBox<String> languageComboBox;

    @FXML
    private Label dashboardTitle;

    @FXML
    private AnchorPane dashboardPane;

    @FXML
    private Button viewReclamationButton;

    @FXML
    private Button viewCompanyEmployeeButton;

    @FXML
    private Button viewUserButton;
    @FXML
    private Button viewStatisticsButton;

    private TranslationService translationService;

    private AdminService adminService = new AdminService();


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
        dashboardTitle.setText(translationService.getTranslation("dashboard.title", "Admin Dashboard"));
        viewReclamationButton.setText(translationService.getTranslation("dashboard.viewReclamations", "View Reclamations"));
        viewCompanyEmployeeButton.setText(translationService.getTranslation("dashboard.viewCompanyEmployee", "View Company et Employee"));
        viewUserButton.setText(translationService.getTranslation("dashboard.viewUsers", "View Users"));
        viewStatisticsButton.setText(translationService.getTranslation("dashboard.viewStatistics", "View Statistics"));
    }

    // Handle view reclamation button click
    @FXML
    private void handleViewReclamation() {
        // Code to switch to the Reclamation view
        System.out.println("Viewing Reclamations...");
        // You can load a new FXML here or change the scene
    }

    // Handle view company and employee button click
    @FXML
    private void handleViewCompanyEmployee() {
        try {
            // Load the Company List page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/auth/connexionview/CompanyListPage.fxml"));
            Scene scene = new Scene(loader.load());

            // Get the stage and set the new scene
            Stage stage = (Stage) viewCompanyEmployeeButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Show an error message if the page cannot be loaded
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Failed to load Company List page.");
            alert.show();
        }
    }

    // Handle view user button click
    // Handle the "View Users" button click
    @FXML
    private void handleViewUser() {
        try {
            // Load the User List page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/auth/connexionview/UserListPage.fxml"));
            Scene scene = new Scene(loader.load());

            // Get the stage and set the new scene
            Stage stage = (Stage) viewUserButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Show an error message if the page cannot be loaded
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Failed to load User List page.");
            alert.show();
        }
    }

    @FXML
    private void handleViewStatistics() {
        try {
            // Charger le fichier FXML de la page des statistiques
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/auth/connexionview/StatisticsView.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Récupérer la fenêtre actuelle
            Stage currentStage = (Stage) viewReclamationButton.getScene().getWindow();

            // Définir la scène sur la fenêtre actuelle
            currentStage.setScene(scene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement de StatisticsView.fxml");
        }


    }






}
