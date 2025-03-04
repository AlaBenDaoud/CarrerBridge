package org.example.auth.controllers.connexion;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.example.auth.models.Company;
import org.example.auth.services.CompanyService;

import java.io.IOException;
import java.util.List;

public class CompanyListController {

    @FXML
    private ListView<String> companyListView;

    private CompanyService companyService;

    public CompanyListController() {
        companyService = new CompanyService();
    }

    // Method to populate the ListView with company names and emails
    public void initialize() {
        List<Company> companies = companyService.getAllCompanies();
        if (companies != null && !companies.isEmpty()) {
            for (Company company : companies) {
                companyListView.getItems().add(company.getCompanyName() + " (" + company.getEmail() + ")");
            }
        } else {
            companyListView.getItems().add("No companies available.");
        }
    }

    // Handle the back button action to go back to the Admin Dashboard
    @FXML
    private void handleBackButton() {
        try {
            // Récupérer la fenêtre actuelle
            Stage currentStage = (Stage) companyListView.getScene().getWindow();

            // Charger le fichier FXML du tableau de bord
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/auth/connexionview/AdminDashboard.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Définir la scène sur la fenêtre actuelle
            currentStage.setScene(scene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erreur lors du chargement de AdminDashboard.fxml");
        }
    }
}
