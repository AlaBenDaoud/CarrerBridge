package org.example.auth.controllers.JobAndApplicantion;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.example.auth.models.Job;
import org.example.auth.services.JobService;
import org.example.auth.utils.AlertUtils;

import java.io.IOException;
import java.util.List;

public class ViewJobsController {

    @FXML
    private VBox jobsContainer;

    @FXML
    private Button backButton;

    private final JobService jobService = new JobService();

    @FXML
    public void initialize() {
        loadJobs(); // Load jobs into the card layout
    }

    @FXML
    private Button bestMatchButton;


    /**
     * Loads jobs from the database and displays them as cards.
     */
    private void loadJobs() {
        try {
            List<Job> jobs = jobService.getAllJobs(); // Fetch jobs from the database
            for (Job job : jobs) {
                jobsContainer.getChildren().add(createJobCard(job));
            }
        } catch (Exception e) {
            AlertUtils.showError("Failed to load jobs. Please try again.");
            e.printStackTrace();
        }
    }

    /**
     * Creates a job card for the given job.
     *
     * @param job The job to display.
     * @return A VBox representing the job card.
     */
    private VBox createJobCard(Job job) {
        VBox card = new VBox();
        card.setSpacing(10);
        card.setStyle("-fx-padding: 15; -fx-background-color: rgba(30, 41, 59, 0.7); -fx-border-radius: 10; -fx-border-color: rgba(59, 130, 246, 0.2); -fx-background-radius: 10;");
        card.setPrefWidth(760);

        // Job Title
        Label titleLabel = new Label(job.getTitle());
        titleLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #f8fafc;");

        // Job Position
        Label positionLabel = new Label("Position: " + job.getPosition());
        positionLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 14px; -fx-text-fill: #94a3b8;");

        // Job Location
        Label locationLabel = new Label("Location: " + job.getLocation());
        locationLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 14px; -fx-text-fill: #94a3b8;");

        // Posted Date
        Label postedDateLabel = new Label("Posted on: " + job.getPostedDate());
        postedDateLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 12px; -fx-text-fill: #94a3b8;");

        // View Details Button
        Button viewButton = new Button("View Details");
        viewButton.setStyle("-fx-text-fill: white; -fx-background-color: #3b82f6; -fx-font-family: 'Segoe UI'; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 8px 16px; -fx-background-radius: 20px; -fx-cursor: hand;");
        viewButton.setOnAction(event -> openJobDetails(job.getId(), event));

        // Add all elements to the card
        card.getChildren().addAll(titleLabel, positionLabel, locationLabel, postedDateLabel, viewButton);
        return card;
    }

    /**
     * Opens the job details view for the selected job.
     *
     * @param jobId  The ID of the job to view.
     * @param event  The action event.
     */
    private void openJobDetails(int jobId, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/auth/JobAndApplication/job_detail.fxml"));
            Parent root = loader.load();

            // Pass job ID to the JobDetailController
            JobDetailController controller = loader.getController();
            controller.loadJobDetails(jobId);

            // Load new scene
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.show();
        } catch (IOException e) {
            AlertUtils.showError("Failed to open job details. Please try again.");
            e.printStackTrace();
        }
    }

    /**
     * Handles the "Back" button click event.
     *
     * @param event The action event.
     */
    @FXML
    public void handleBackButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/auth/connexionview/userdash.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.show();
        } catch (IOException e) {
            AlertUtils.showError("Failed to navigate back. Please try again.");
            e.printStackTrace();
        }
    }

    @FXML
    public void handleBestMatchButton(ActionEvent event) {
        try {
            // Load the pop-up FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/auth/JobAndApplication/best_match_popup.fxml"));
            Parent root = loader.load();

            // Load the best match job details
            BestMatchPopupController controller = loader.getController();
            controller.loadBestMatchJobs();

            // Create a new stage for the pop-up
            Stage popupStage = new Stage();
            popupStage.setTitle("Best Match Job");
            popupStage.setScene(new Scene(root, 400, 300));
            popupStage.show();
        } catch (IOException e) {
            AlertUtils.showError("Failed to open best match pop-up. Please try again.");
            e.printStackTrace();
        }
    }
}