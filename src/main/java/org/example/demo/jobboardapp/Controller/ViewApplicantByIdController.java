package org.example.demo.jobboardapp.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.example.demo.jobboardapp.Models.Applicant;
import org.example.demo.jobboardapp.Services.ApplicantService;

public class ViewApplicantByIdController {

    @FXML
    private TextField applicantIdField;

    @FXML
    private VBox applicantDetails;

    @FXML
    private TextField jobIdField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField resumeField;

    @FXML
    private TextField appliedDateField;

    @FXML
    private Button saveButton;

    private ApplicantService applicantService = new ApplicantService();

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
                jobIdField.setText(String.valueOf(applicant.getJobId()));
                nameField.setText(applicant.getName());
                emailField.setText(applicant.getEmail());
                resumeField.setText(applicant.getResume());
                appliedDateField.setText(applicant.getAppliedDate().toString());

                // Show details
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

    @FXML
    private void handleEditApplicant() {
        // Enable editing of fields
        jobIdField.setEditable(true);
        nameField.setEditable(true);
        emailField.setEditable(true);
        resumeField.setEditable(true);
        appliedDateField.setEditable(true);

        // Show save button
        saveButton.setVisible(true);
    }

    @FXML
    private void handleSaveApplicant() {
        if (currentApplicantId != 0) {
            // Create an updated applicant object
            Applicant updatedApplicant = new Applicant();
            updatedApplicant.setId(currentApplicantId);
            updatedApplicant.setJobId(Integer.parseInt(jobIdField.getText()));
            updatedApplicant.setName(nameField.getText());
            updatedApplicant.setEmail(emailField.getText());
            updatedApplicant.setResume(resumeField.getText());
            updatedApplicant.setAppliedDate(java.sql.Timestamp.valueOf(appliedDateField.getText()));

            // Update in the database
            boolean success = applicantService.updateApplicant(updatedApplicant);
            if (success) {
                System.out.println("Applicant updated successfully.");
                saveButton.setVisible(false);

                // Disable editing
                jobIdField.setEditable(false);
                nameField.setEditable(false);
                emailField.setEditable(false);
                resumeField.setEditable(false);
                appliedDateField.setEditable(false);
            } else {
                System.out.println("Failed to update applicant.");
            }
        }
    }
}