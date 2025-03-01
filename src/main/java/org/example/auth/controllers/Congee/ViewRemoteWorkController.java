package org.example.auth.controllers.Congee;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.auth.models.OnlineJob;
import org.example.auth.services.OnlineJobService;

import java.util.List;

public class ViewRemoteWorkController {

    @FXML
    private VBox remoteWorkContainer;

    private OnlineJobService onlineJobService = new OnlineJobService();

    @FXML
    public void initialize() {
        // Automatically load the remote work applications
        loadRemoteWorkData();
    }

    private void loadRemoteWorkData() {
        List<OnlineJob> onlineJobs = onlineJobService.getAllOnlineJobs();
        remoteWorkContainer.getChildren().clear(); // Clear existing cards

        if (!onlineJobs.isEmpty()) {
            for (OnlineJob onlineJob : onlineJobs) {
                remoteWorkContainer.getChildren().add(createRemoteWorkCard(onlineJob));
            }
        } else {
            Label noRequestsLabel = new Label("No remote work applications found.");
            noRequestsLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 14px; -fx-text-fill: #94a3b8;");
            remoteWorkContainer.getChildren().add(noRequestsLabel);
        }
    }

    private VBox createRemoteWorkCard(OnlineJob onlineJob) {
        VBox card = new VBox();
        card.setSpacing(10);
        card.setStyle("-fx-padding: 15; -fx-background-color: rgba(30, 41, 59, 0.7); -fx-border-radius: 10; -fx-border-color: rgba(59, 130, 246, 0.2); -fx-background-radius: 10;");
        card.setPrefWidth(760);

        // ID
        Label idLabel = new Label("ID: " + onlineJob.getId());
        idLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 14px; -fx-text-fill: #f8fafc;");

        // Leave Request ID
        Label leaveRequestIdLabel = new Label("Leave Request ID: " + onlineJob.getLeaveRequestId());
        leaveRequestIdLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 14px; -fx-text-fill: #94a3b8;");

        // Title
        Label titleLabel = new Label("Title: " + onlineJob.getTitle());
        titleLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 14px; -fx-text-fill: #94a3b8;");

        // Description
        Label descriptionLabel = new Label("Description: " + onlineJob.getPost());
        descriptionLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 14px; -fx-text-fill: #94a3b8;");

        // Start Date
        Label startDateLabel = new Label("Start Date: " + onlineJob.getStartDate());
        startDateLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 14px; -fx-text-fill: #94a3b8;");

        // End Date
        Label endDateLabel = new Label("End Date: " + onlineJob.getEndDate());
        endDateLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 14px; -fx-text-fill: #94a3b8;");

        // Confirmation Status
        Label confirmedLabel = new Label("Confirmed: " + (onlineJob.isConfirmed() ? "Yes" : "No"));
        confirmedLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 14px; -fx-text-fill: #94a3b8;");

        // Action Buttons
        HBox buttonBox = new HBox(10);
        Button confirmButton = new Button(onlineJob.isConfirmed() ? "Not Confirm" : "Confirm");

        confirmButton.setStyle("-fx-text-fill: white; -fx-background-color: #28a745; -fx-font-family: 'Segoe UI'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-padding: 8px 16px; -fx-background-radius: 20px; -fx-cursor: hand;");

        confirmButton.setOnAction(event -> {
            onlineJob.setConfirmed(!onlineJob.isConfirmed()); // Toggle the confirmation status
            onlineJobService.updateOnlineJob(onlineJob);
            loadRemoteWorkData(); // Refresh the cards
        });

        buttonBox.getChildren().add(confirmButton);

        // Add all elements to the card
        card.getChildren().addAll(idLabel, leaveRequestIdLabel, titleLabel, descriptionLabel, startDateLabel, endDateLabel, confirmedLabel, buttonBox);
        return card;
    }

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/auth/congee/leave_request.fxml"));
            VBox root = loader.load();

            Stage stage = (Stage) remoteWorkContainer.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}