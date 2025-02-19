package org.example.auth.controllers.connexion;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class CompanyDashController {

    @FXML
    private Button postOfferButton;

    @FXML
    private Button viewPostsButton;

    @FXML
    private Button viewApplicantsButton;

    @FXML
    private Button viewEmployeesButton;

    @FXML
    private void handlePostOffer() {
        try {
            // Load the post_job.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/auth/JobAndApplication/post_job.fxml"));
            Parent root = loader.load();

            // Create a new scene with the loaded FXML
            Scene scene = new Scene(root);

            // Get the current stage (window)
            Stage stage = (Stage) postOfferButton.getScene().getWindow();

            // Set the new scene on the current stage
            stage.setScene(scene);
            stage.setTitle("Post Job");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Unable to load the post job page.");
        }
    }

    @FXML
    private void handleViewPosts() {
        try {
            // Load the ViewJobs.fxml file (this is the page where the company can view the posted jobs)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/auth/JobAndApplication/rhviewdetails.fxml"));
            Parent root = loader.load();

            // Create a new scene with the loaded FXML
            Scene scene = new Scene(root);

            // Get the current stage (window)
            Stage stage = (Stage) viewPostsButton.getScene().getWindow();

            // Set the new scene on the current stage
            stage.setScene(scene);
            stage.setTitle("View My Posts");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Unable to load the View Jobs page.");
        }
    }

    @FXML
    private void handleViewApplicants() {
        try {
            // Load the view_applications.fxml file (this is the page where the company can view applicants)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/auth/JobAndApplication/view_applications.fxml"));
            Parent root = loader.load();

            // Create a new scene with the loaded FXML
            Scene scene = new Scene(root);

            // Get the current stage (window)
            Stage stage = (Stage) viewApplicantsButton.getScene().getWindow();

            // Set the new scene on the current stage
            stage.setScene(scene);
            stage.setTitle("View Applicants");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Unable to load the View Applicants page.");
        }
    }

    @FXML
    private void handleViewEmployees() {
        try {
            // Load the ViewEmployees.fxml file (this is the page where the company can view employees)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/auth/JobAndApplication/ViewEmployees.fxml"));
            Parent root = loader.load();

            // Create a new scene with the loaded FXML
            Scene scene = new Scene(root);

            // Get the current stage (window)
            Stage stage = (Stage) viewEmployeesButton.getScene().getWindow();

            // Set the new scene on the current stage
            stage.setScene(scene);
            stage.setTitle("View Employees");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Unable to load the View Employees page.");
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
