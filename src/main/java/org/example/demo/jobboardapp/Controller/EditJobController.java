package org.example.demo.jobboardapp.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.demo.jobboardapp.Models.Job;
import org.example.demo.jobboardapp.Services.JobService;
import org.example.demo.jobboardapp.Utils.AlertUtils;

public class EditJobController {

    @FXML
    private TextField idField;

    @FXML
    private TextField titleField;

    @FXML
    private TextArea descriptionField;

    @FXML
    private TextField companyNameField;

    @FXML
    private TextField locationField;

    private Job job;
    private JobService jobService = new JobService();

    /**
     * Initializes the form with the job details.
     *
     * @param job The job to edit.
     */
    public void initializeData(Job job) {
        this.job = job;
        idField.setText(String.valueOf(job.getId()));
        titleField.setText(job.getTitle());
        descriptionField.setText(job.getDescription());
        companyNameField.setText(job.getCompanyName());
        locationField.setText(job.getLocation());
    }

    /**
     * Handles the "Save Changes" button click event.
     */
    @FXML
    private void handleSaveChanges() {
        // Update the job object with the new values
        job.setTitle(titleField.getText());
        job.setDescription(descriptionField.getText());
        job.setCompanyName(companyNameField.getText());
        job.setLocation(locationField.getText());

        // Save the updated job to the database
        boolean isUpdated = jobService.updateJob(job);
        if (isUpdated) {
            AlertUtils.showSuccess("Job updated successfully!");
            closeWindow();
        } else {
            AlertUtils.showError("Failed to update job. Please try again.");
        }
    }

    /**
     * Handles the "Cancel" button click event.
     */
    @FXML
    private void handleCancel() {
        closeWindow();
    }

    /**
     * Closes the current window.
     */
    private void closeWindow() {
        Stage stage = (Stage) idField.getScene().getWindow();
        stage.close();
    }
}