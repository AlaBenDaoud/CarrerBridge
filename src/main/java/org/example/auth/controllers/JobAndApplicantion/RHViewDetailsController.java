package org.example.auth.controllers.JobAndApplicantion;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.example.auth.models.Job;
import org.example.auth.services.JobService;
import org.example.auth.utils.AlertUtils;
import org.example.auth.controllers.connexion.AuthCompanyController;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class RHViewDetailsController implements Initializable {

    @FXML
    private VBox jobsContainer;

    @FXML
    private TextField searchField;

    private JobService jobService = new JobService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Fetch jobs as soon as the view is loaded
        handleFetchJobs();
    }

    @FXML
    private void handleFetchJobs() {
        // Retrieve the logged-in company ID from the AuthCompanyController
        int companyId = AuthCompanyController.getLoggedInCompanyId();

        if (companyId != -1) {
            // Fetch jobs for the logged-in company
            List<Job> jobs = jobService.getJobsByCompanyId(companyId);
            displayJobs(jobs);
        } else {
            AlertUtils.showError("Company not logged in. Please log in first.");
        }
    }

    @FXML
    private void handleSearch() {
        String query = searchField.getText().toLowerCase();
        int companyId = AuthCompanyController.getLoggedInCompanyId();

        if (companyId != -1) {
            List<Job> jobs = jobService.getJobsByCompanyId(companyId);
            List<Job> filteredJobs = jobs.stream()
                    .filter(job -> job.getTitle().toLowerCase().contains(query) ||
                            job.getDescription().toLowerCase().contains(query) ||
                            job.getLocation().toLowerCase().contains(query))
                    .toList();
            displayJobs(filteredJobs);
        } else {
            AlertUtils.showError("Company not logged in. Please log in first.");
        }
    }

    private void displayJobs(List<Job> jobs) {
        jobsContainer.getChildren().clear(); // Clear existing cards

        for (Job job : jobs) {
            // Create a card for each job
            StackPane card = new StackPane();
            card.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 2);");

            // Card content
            VBox content = new VBox(10);
            content.setPadding(new Insets(20));

            // Job title
            Text title = new Text(job.getTitle());
            title.setFont(Font.font("Roboto", FontWeight.BOLD, 18));
            title.setFill(Color.web("#005bb5"));

            // Job description
            Text description = new Text(job.getDescription());
            description.setFont(Font.font("Roboto", 14));
            description.setFill(Color.web("#333333"));
            description.setWrappingWidth(600);

            // Job location and posted date
            HBox details = new HBox(10);
            Text location = new Text("Location: " + job.getLocation());
            location.setFont(Font.font("Roboto", 14));
            location.setFill(Color.web("#555555"));

            Text postedDate = new Text("Posted: " + job.getPostedDate());
            postedDate.setFont(Font.font("Roboto", 14));
            postedDate.setFill(Color.web("#555555"));

            details.getChildren().addAll(location, postedDate);

            // Buttons for modify and delete
            HBox buttons = new HBox(10);
            Button modifyButton = new Button("Modify");
            modifyButton.setStyle("-fx-background-color: #0078d7; -fx-text-fill: white; -fx-font-size: 14; -fx-padding: 10 20; -fx-background-radius: 5;");
            modifyButton.setOnAction(event -> handleModifyJob(job));

            Button deleteButton = new Button("Delete");
            deleteButton.setStyle("-fx-background-color: #ff4444; -fx-text-fill: white; -fx-font-size: 14; -fx-padding: 10 20; -fx-background-radius: 5;");
            deleteButton.setOnAction(event -> handleDeleteJob(job));

            buttons.getChildren().addAll(modifyButton, deleteButton);

            // Add all elements to the card
            content.getChildren().addAll(title, description, details, buttons);
            card.getChildren().add(content);

            // Add the card to the container
            jobsContainer.getChildren().add(card);
        }
    }

    private void handleModifyJob(Job job) {
        try {
            // Load the edit form
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/auth/JobAndApplication/edit_job.fxml"));
            Parent root = loader.load();

            // Get the controller and pass the job to it
            EditJobController controller = loader.getController();
            controller.initializeData(job);

            // Create a new stage for the edit form
            Stage stage = new Stage();
            stage.setScene(new Scene(root, 400, 500));
            stage.setTitle("Edit Job");
            stage.show();
        } catch (IOException e) {
            AlertUtils.showError("Failed to open edit form. Please try again.");
            e.printStackTrace();
        }
    }

    private void handleDeleteJob(Job job) {
        boolean isDeleted = jobService.deleteJob(job.getId());
        if (isDeleted) {
            handleFetchJobs(); // Refresh the job list
            System.out.println("Job deleted: " + job.getId());
        } else {
            System.out.println("Failed to delete job: " + job.getId());
        }
    }

    @FXML
    public void handleBackButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/auth/connexionview/companydash.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 600, 400));
            stage.show();
        } catch (IOException e) {
            AlertUtils.showError("Failed to navigate back. Please try again.");
            e.printStackTrace();
        }
    }
}