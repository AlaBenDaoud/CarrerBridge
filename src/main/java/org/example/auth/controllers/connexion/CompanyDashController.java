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
    private Button viewLeaveRequestsButton;

    @FXML
    private Button viewReclamationsButton;

    @FXML
    private Button viewOnlineJobsButton; // New button for viewing online jobs

    @FXML
    private Button viewEmployeeRankingsButton; // Add this line to declare the button

    @FXML
    private void handlePostOffer() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/auth/JobAndApplication/post_job.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) postOfferButton.getScene().getWindow();
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/auth/JobAndApplication/rhviewdetails.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) viewPostsButton.getScene().getWindow();
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/auth/JobAndApplication/view_applications.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) viewApplicantsButton.getScene().getWindow();
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/auth/JobAndApplication/ViewEmployees.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) viewEmployeesButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("View Employees");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Unable to load the View Employees page.");
        }
    }

    @FXML
    private void handleViewLeaveRequests() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/auth/congee/view_leave_requests.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) viewLeaveRequestsButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("View Leave Requests");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Unable to load the View Leave Requests page.");
        }
    }

    @FXML
    private void handleViewReclamations() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/auth/reclamation/ViewAllReclamations.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) viewReclamationsButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("View Reclamations");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Unable to load the View Reclamations page.");
        }
    }

    @FXML
    private void handleViewOnlineJobs() {
        try {
            // Load the view_online_jobs.fxml file (this is the page where the company can view online jobs)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/auth/congee/view_remote_work.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) viewOnlineJobsButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("View Online Jobs");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Unable to load the View Online Jobs page.");
        }
    }

    @FXML
    private void handleViewEmployeeRankings() {
        try {
            // Load the FXML file for the employee rankings page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/auth/Congee/employee_rankings.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) viewEmployeeRankingsButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Employee Rankings");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Unable to load the Employee Rankings page.");
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