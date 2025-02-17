package org.example.demo.jobboardapp.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.demo.jobboardapp.Models.Applicant;
import org.example.demo.jobboardapp.Models.Job;
import org.example.demo.jobboardapp.Services.ApplicantService;
import org.example.demo.jobboardapp.Services.JobService;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class ApplyJobController {
    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField resumeField;
    @FXML
    private Button submitButton;

    private int jobId; // Store job ID
    private ApplicantService applicantService = new ApplicantService();
    private JobService jobService = new JobService(); // To fetch company_id from job

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    @FXML
    private void handleSubmit() {
        String name = nameField.getText();
        String email = emailField.getText();
        String resume = resumeField.getText();

        if (name.isEmpty() || email.isEmpty() || resume.isEmpty()) {
            System.out.println("All fields are required!");
            return;
        }

        // Fetch the job to get the company_id
        Job job = jobService.getJobById(jobId);
        if (job == null) {
            System.err.println("Job not found for ID: " + jobId);
            return;
        }

        // Create Applicant object
        Applicant applicant = new Applicant();
        applicant.setJobId(jobId);
        applicant.setCompanyId(job.getCompanyId()); // Set company_id from the job
        applicant.setName(name);
        applicant.setEmail(email);
        applicant.setResume(resume);
        applicant.setAppliedDate(Timestamp.valueOf(LocalDateTime.now()));

        // Save to database
        applicantService.addApplicant(applicant);
        System.out.println("Application submitted successfully!");

        // Close form
        Stage stage = (Stage) submitButton.getScene().getWindow();
        stage.close();
    }
}