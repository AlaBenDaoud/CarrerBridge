package org.example.auth.controllers.JobAndApplicantion;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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
    private void handleViewWork() {
        showAlert(Alert.AlertType.INFORMATION, "View Work", "View Work button clicked.");
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
        showAlert(Alert.AlertType.INFORMATION, "Edit Work", "Edit Work button clicked.");
        // Add logic to handle editing work
    }

    @FXML
    private void handleDeleteWork() {
        showAlert(Alert.AlertType.INFORMATION, "Delete Work", "Delete Work button clicked.");
        // Add logic to handle deleting work
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}