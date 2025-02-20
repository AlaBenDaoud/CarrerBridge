package org.example.auth.controllers.connexion;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.example.auth.services.EmployeeService;

import java.io.IOException;

public class UserDashController {

    @FXML
    private Button searchJobButton;

    @FXML
    private Button viewApplicationsButton;

    @FXML
    private Button editProfileButton;

    @FXML
    private Button consultWorkButton;

    @FXML
    private Button createPostButton;


    private EmployeeService employeeService;

    public UserDashController() {
        this.employeeService = new EmployeeService(); // Initialize the EmployeeService
    }

    public void initialize() {
        // Get the current user ID (replace with your logic to get the user ID)
        int userId = getCurrentUserId();

        // Check if the user is an employee using EmployeeService
        boolean isEmployee = employeeService.isUserEmployee(userId);

        // Set the visibility of the "Consult My Work" button
        consultWorkButton.setVisible(isEmployee);
    }

    @FXML
    private void handleSearchJob() {
        showAlert(Alert.AlertType.INFORMATION, "Search for Job", "Search for Job button clicked.");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/auth/JobAndApplication/view_jobs.fxml"));
            Scene jobDetailScene = new Scene(loader.load());
            Stage currentStage = (Stage) searchJobButton.getScene().getWindow();
            currentStage.setScene(jobDetailScene);
            currentStage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load job details.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleViewApplications() {
        showAlert(Alert.AlertType.INFORMATION, "View My Applications", "View My Applications button clicked.");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/auth/JobAndApplication/view_applicant_by_id.fxml"));
            Scene applicantViewScene = new Scene(loader.load());
            Stage currentStage = (Stage) viewApplicationsButton.getScene().getWindow();
            currentStage.setScene(applicantViewScene);
            currentStage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load applications view.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEditProfile() {
        showAlert(Alert.AlertType.INFORMATION, "Edit My Profile", "Edit My Profile button clicked.");
        // Add logic to handle editing the profile
    }

    @FXML
    private void handleConsultWork() {
        showAlert(Alert.AlertType.INFORMATION, "Consult My Work", "Consult My Work button clicked.");
        // Add logic to handle consulting work
    }

    @FXML
    private void handleCreatePost() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/auth/JobAndApplication/post_form.fxml"));
            Stage postStage = new Stage();
            postStage.setScene(new Scene(loader.load()));
            postStage.setTitle("Create Post");
            postStage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load Create Post window.");
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private int getCurrentUserId() {
        // Replace this with your logic to get the current user ID
        // For example, you might retrieve it from a session or login context
        return 1; // Example user ID
    }
}