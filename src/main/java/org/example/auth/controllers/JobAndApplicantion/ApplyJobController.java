package org.example.auth.controllers.JobAndApplicantion;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.auth.models.Applicant;
import org.example.auth.models.Job;
import org.example.auth.services.ApplicantService;
import org.example.auth.services.JobService;
import org.example.auth.controllers.connexion.AuthUserController;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class ApplyJobController {

    @FXML
    private TextField commentField;  // Changed 'name' to 'comment' for the field
    @FXML
    private TextField additionalFileField;  // Changed 'email' to 'additionalFile' for the field
    @FXML
    private Button submitButton;

    private int jobId; // Store job ID
    private ApplicantService applicantService = new ApplicantService();
    private JobService jobService = new JobService(); // To fetch company_id from job

    // Setter for jobId
    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    @FXML
    private void handleSubmit() {
        String comment = commentField.getText();
        String additionalFile = additionalFileField.getText();

        if (comment.isEmpty() || additionalFile.isEmpty()) {
            System.out.println("All fields are required!");
            return;
        }

        // Fetch the job to get the company_id
        Job job = jobService.getJobById(jobId);
        if (job == null) {
            System.err.println("Job not found for ID: " + jobId);
            return;
        }

        // Access the logged-in user ID directly from AuthUserController's static method
        int loggedInUserId = AuthUserController.getLoggedInUserId();

        if (loggedInUserId == -1) {
            System.err.println("User not logged in!");
            return;
        }

        // Create Applicant object
        Applicant applicant = new Applicant();
        applicant.setJobId(jobId);
        applicant.setCompanyId(job.getCompanyId()); // Set company_id from the job
        applicant.setComment(comment);
        applicant.setAdditionalFile(additionalFile);
        applicant.setAppliedDate(Timestamp.valueOf(LocalDateTime.now()));
        applicant.setUserId(loggedInUserId);  // Ensure you're saving the user ID

        // Save to database
        boolean success = applicantService.addApplicant(applicant);
        if (success) {
            System.out.println("Application submitted successfully!");
        } else {
            System.err.println("Failed to submit application.");
        }

        // Close form
        Stage stage = (Stage) submitButton.getScene().getWindow();
        stage.close();
    }
}
