package org.example.auth.controllers.JobAndApplicantion;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.example.auth.models.Job;
import org.example.auth.services.JobService;

import java.io.IOException;

public class JobDetailController {
    @FXML
    private Label titleLabel;
    @FXML
    private Label descriptionLabel;
    @FXML
    private Label companyLabel;
    @FXML
    private Label locationLabel;
    @FXML
    private Label postedDateLabel;
    @FXML
    private Button applyButton;
    @FXML
    private Button backButton;

    private int currentJobId;
    private JobService jobService = new JobService();

    public void loadJobDetails(int jobId) {
        Job job = jobService.getJobById(jobId);
        if (job != null) {
            currentJobId = jobId; // Store jobId
            titleLabel.setText("Title: " + job.getTitle());
            descriptionLabel.setText("Description: " + job.getDescription());
            companyLabel.setText("Company: " + job.getCompanyId());
            locationLabel.setText("Location: " + job.getLocation());
            postedDateLabel.setText("Posted Date: " + job.getPostedDate().toString());
        } else {
            titleLabel.setText("Job not found");
        }
    }
    @FXML
    public void handleBackButton(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/auth/connexionview/userdash.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }
    @FXML
    private void handleApply() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/auth/JobAndApplication/apply_job.fxml"));
            Parent root = loader.load();

            // Pass jobId to ApplyJobController
            ApplyJobController controller = loader.getController();
            controller.setJobId(currentJobId);

            Stage stage = new Stage();
            stage.setTitle("Apply for Job");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
