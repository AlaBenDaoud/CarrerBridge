package org.example.auth.controllers.connexion;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.example.auth.services.AdminService;

import java.io.IOException;

public class AdminDashboardController {

    @FXML
    private AnchorPane dashboardPane;

    @FXML
    private Button viewReclamationButton;

    @FXML
    private Button viewCompanyEmployeeButton;

    @FXML
    private Button viewUserButton;

    private AdminService adminService = new AdminService();

    // This method will initialize the Dashboard
    public void initialize() {
        // You can add logic here to load initial data for the dashboard if needed
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

    // Other methods to handle dashboard-specific actions (e.g., Logout)
}
