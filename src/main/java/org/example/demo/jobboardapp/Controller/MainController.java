package org.example.demo.jobboardapp.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    @FXML
    private void handlePostJobButton(ActionEvent event) {
        // Navigate to the "Post Job" page
        navigateTo(event, "/org/example/demo/jobboardapp/views/post_job.fxml", "Post a Job");
    }

    @FXML
    private void handleViewJobsButton(ActionEvent event) {
        // Navigate to the "View Jobs" page
        navigateTo(event, "/org/example/demo/jobboardapp/views/view_jobs.fxml", "View Jobs");
    }

    @FXML
    private void handleViewDetailsButton(ActionEvent event) {
        // Navigate to the "View Details" page
        navigateTo(event, "/org/example/demo/jobboardapp/views/rhviewdetails.fxml", "View Details");
    }

    @FXML
    private void handleViewApplicationsButton(ActionEvent event) {
        // Navigate to the "View Applications by Company ID" page
        navigateTo(event, "/org/example/demo/jobboardapp/views/view_applications.fxml", "View Applications by Company ID");
    }

    @FXML
    private void handleViewApplicantByIdButton(ActionEvent event) {
        // Navigate to the "View by Applicant ID" page
        navigateTo(event, "/org/example/demo/jobboardapp/views/view_applicant_by_id.fxml", "View by Applicant ID");
    }

    private void navigateTo(ActionEvent event, String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            // Create a new scene
            Scene scene = new Scene(root);

            // Get the current stage
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            // Set the new scene and title
            stage.setTitle(title);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}