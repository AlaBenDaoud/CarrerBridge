package org.example.auth.controllers.JobAndApplicantion;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class ConsultWorkController {

    @FXML
    private Button viewWorkButton;

    @FXML
    private Button editWorkButton;

    @FXML
    private Button deleteWorkButton;

    @FXML
    private Button viewReclamationButton; // New button

    @FXML
    private void handleViewWork() {
        // Add logic to navigate to a new FXML page
        try {
            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/auth/congee/EmployeeList.fxml"));
            Parent root = loader.load();

            // Get the current stage
            Stage stage = (Stage) viewWorkButton.getScene().getWindow();

            // Create a new scene with the loaded FXML
            Scene scene = new Scene(root);

            // Set the new scene to the current stage
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load the new page.");
        }
    }

    @FXML
    private void handleEditWork() {
        // Add logic to navigate to a new FXML page
        try {
            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/auth/congee/view_my_requests_popup.fxml"));
            Parent root = loader.load();

            // Get the current stage
            Stage stage = (Stage) viewWorkButton.getScene().getWindow();

            // Create a new scene with the loaded FXML
            Scene scene = new Scene(root);

            // Set the new scene to the current stage
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load the new page.");
        }
    }

    @FXML
    private void handleDeleteWork() {
        showAlert(Alert.AlertType.INFORMATION, "Delete Work", "Delete Work button clicked.");
        // Add logic to handle deleting work
    }

    @FXML
    private void handleViewReclamation() {
        // Add logic to navigate to the reclamation view
        try {
            // Load the new FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/auth/reclamation/UserViewRecalamation.fxml"));
            Parent root = loader.load();

            // Get the current stage
            Stage stage = (Stage) viewReclamationButton.getScene().getWindow();

            // Create a new scene with the loaded FXML
            Scene scene = new Scene(root);

            // Set the new scene to the current stage
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load the reclamation page.");
        }
    }

    @FXML
    private void handleBackButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/auth/connexionview/userdash.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) viewWorkButton.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.show();
        } catch (IOException e) {
            System.err.println("Failed to navigate back to the dashboard: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}