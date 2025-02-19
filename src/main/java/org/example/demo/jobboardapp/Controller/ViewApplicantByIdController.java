package org.example.demo.jobboardapp.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.example.demo.jobboardapp.Models.Applicant;
import org.example.demo.jobboardapp.Models.Job;
import org.example.demo.jobboardapp.Services.ApplicantService;
import org.example.demo.jobboardapp.Services.JobService;

public class ViewApplicantByIdController {

    @FXML
    private TextField applicantIdField;

    @FXML
    private VBox applicantDetails;

    @FXML
    private Label idLabel;

    @FXML
    private Label jobIdLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label resumeLabel;

    @FXML
    private Label appliedDateLabel;

    @FXML
    private Label jobTitleLabel;

    @FXML
    private Label companyNameLabel;

    @FXML
    private Label jobDescriptionLabel;

    private ApplicantService applicantService = new ApplicantService();
    private JobService jobService = new JobService();

    private int currentApplicantId; // To track the currently displayed applicant

    @FXML
    private void handleFetchApplicant() {
        String applicantId = applicantIdField.getText();
        if (applicantId != null && !applicantId.isEmpty()) {
            int id = Integer.parseInt(applicantId);
            Applicant applicant = applicantService.getApplicantById(id);

            if (applicant != null) {
                // Display applicant details
                currentApplicantId = applicant.getId();
                idLabel.setText("ID: " + applicant.getId());
                jobIdLabel.setText("Job ID: " + applicant.getJobId());
                nameLabel.setText("Name: " + applicant.getName());
                emailLabel.setText("Email: " + applicant.getEmail());
                resumeLabel.setText("Resume: " + applicant.getResume());
                appliedDateLabel.setText("Applied Date: " + applicant.getAppliedDate());

                // Fetch and display job details
                Job job = jobService.getJobById(applicant.getJobId());
                if (job != null) {
                    jobTitleLabel.setText("Job Title: " + job.getTitle());
                    companyNameLabel.setText("Company: " + job.getCompanyName());
                    jobDescriptionLabel.setText("Description: " + job.getDescription());
                } else {
                    jobTitleLabel.setText("Job Title: Not Found");
                    companyNameLabel.setText("Company: Not Found");
                    jobDescriptionLabel.setText("Description: Not Found");
                }

                applicantDetails.setVisible(true);
            } else {
                applicantDetails.setVisible(false);
                System.out.println("Applicant not found for ID: " + id);
            }
        }
    }

    @FXML
    private void handleDeleteApplicant() {
        if (currentApplicantId != 0) {
            boolean success = applicantService.deleteApplicantById(currentApplicantId);
            if (success) {
                System.out.println("Applicant deleted successfully.");
                applicantDetails.setVisible(false);
            } else {
                System.out.println("Failed to delete applicant.");
            }
        }
    }
}