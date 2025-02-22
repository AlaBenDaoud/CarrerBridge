package org.example.auth.controllers.JobAndApplicantion;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.auth.models.Applicant;
import org.example.auth.services.ApplicantService;
import org.example.auth.controllers.connexion.AuthUserController;

import java.io.IOException;

public class ViewApplicantByIdController {

    @FXML
    private VBox applicationsContainer;

    private final ApplicantService applicantService = new ApplicantService();

    @FXML
    public void initialize() {
        // Automatically fetch and display applications for the logged-in user
        loadApplications();
    }

    /**
     * Loads applications for the logged-in user and displays them as cards.
     */
    private void loadApplications() {
        applicationsContainer.getChildren().clear(); // Clear existing cards

        int loggedInUserId = AuthUserController.getLoggedInUserId();
        ObservableList<Applicant> applications = FXCollections.observableArrayList(
                applicantService.getApplicationsByApplicantId(loggedInUserId)
        );

        if (!applications.isEmpty()) {
            for (Applicant application : applications) {
                applicationsContainer.getChildren().add(createApplicationCard(application));
            }
        } else {
            Label noApplicationsLabel = new Label("No applications found.");
            noApplicationsLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 14px; -fx-text-fill: #94a3b8;");
            applicationsContainer.getChildren().add(noApplicationsLabel);
        }
    }

    /**
     * Creates a card for the given application.
     *
     * @param application The application to display.
     * @return A VBox representing the application card.
     */
    private VBox createApplicationCard(Applicant application) {
        VBox card = new VBox();
        card.setSpacing(10);
        card.setStyle("-fx-padding: 15; -fx-background-color: rgba(30, 41, 59, 0.7); -fx-border-radius: 10; -fx-border-color: rgba(59, 130, 246, 0.2); -fx-background-radius: 10;");
        card.setPrefWidth(760);

        // Application ID
        Label idLabel = new Label("Application ID: " + application.getId());
        idLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 14px; -fx-text-fill: #f8fafc;");

        // Job ID
        Label jobIdLabel = new Label("Job ID: " + application.getJobId());
        jobIdLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 14px; -fx-text-fill: #94a3b8;");

        // Company ID
        Label companyIdLabel = new Label("Company ID: " + application.getCompanyId());
        companyIdLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 14px; -fx-text-fill: #94a3b8;");

        // Comment
        Label commentLabel = new Label("Comment: " + application.getComment());
        commentLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 14px; -fx-text-fill: #94a3b8;");

        // Additional File
        Label additionalFileLabel = new Label("Additional File: " + application.getAdditionalFile());
        additionalFileLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 14px; -fx-text-fill: #94a3b8;");

        // Status
        Label statusLabel = new Label("Status: " + application.getStatus());
        statusLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 14px; -fx-text-fill: #94a3b8;");

        // Applied Date
        Label appliedDateLabel = new Label("Applied Date: " + application.getAppliedDate());
        appliedDateLabel.setStyle("-fx-font-family: 'Segoe UI'; -fx-font-size: 14px; -fx-text-fill: #94a3b8;");

        // Action Buttons (Edit and Delete)
        HBox buttonBox = new HBox(10);
        Button editButton = new Button("Edit");
        Button deleteButton = new Button("Delete");

        editButton.setStyle("-fx-text-fill: white; -fx-background-color: #3b82f6; -fx-font-family: 'Segoe UI'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-padding: 8px 16px; -fx-background-radius: 20px; -fx-cursor: hand;");
        deleteButton.setStyle("-fx-text-fill: white; -fx-background-color: #ef4444; -fx-font-family: 'Segoe UI'; -fx-font-size: 12px; -fx-font-weight: bold; -fx-padding: 8px 16px; -fx-background-radius: 20px; -fx-cursor: hand;");

        editButton.setOnAction(event -> handleEditApplication(application));
        deleteButton.setOnAction(event -> handleDeleteApplication(application));

        buttonBox.getChildren().addAll(editButton, deleteButton);

        // Add all elements to the card
        card.getChildren().addAll(idLabel, jobIdLabel, companyIdLabel, commentLabel, additionalFileLabel, statusLabel, appliedDateLabel, buttonBox);
        return card;
    }

    /**
     * Handles editing an application.
     *
     * @param application The application to edit.
     */
    private void handleEditApplication(Applicant application) {
        // Implement edit functionality here
        System.out.println("Editing application with ID: " + application.getId());
    }

    /**
     * Handles deleting an application.
     *
     * @param application The application to delete.
     */
    private void handleDeleteApplication(Applicant application) {
        boolean success = applicantService.deleteApplicantById(application.getId());
        if (success) {
            System.out.println("Application deleted successfully.");
            loadApplications(); // Refresh the cards
        } else {
            System.out.println("Failed to delete application.");
        }
    }

    /**
     * Handles the "Back to Dashboard" button click event.
     */
    @FXML
    private void handleBackButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/auth/connexionview/userdash.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) applicationsContainer.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.show();
        } catch (IOException e) {
            System.err.println("Failed to navigate back to the dashboard: " + e.getMessage());
        }
    }
}